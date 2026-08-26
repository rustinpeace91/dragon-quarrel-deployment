package dungeon.crawler.GameSystem.Combat;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dungeon.crawler.Data.Enemies.EnemySpriteRegistry;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.Enemy;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Sprites.Enemy.EnemyAnimatedSprite;
import dungeon.crawler.Sprites.Enemy.EnemyAnimatedSpriteFactory;
import dungeon.crawler.Sprites.Explosion.ExplosionAnimationFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EnemyRenderer {

    private final CombatStateManager combatState;
    private float worldHeight;
    private float worldWidth;
    private GameState gameState;
    private Stage enemyStage;
    private EnemySpriteRegistry enemySpriteRegistry;
    private Map<Integer, EnemyAnimatedSprite> enemySpriteRoster;
    private EnemyAnimatedSpriteFactory enemySpriteFactory;
    private ExplosionAnimationFactory explosionAnimationFactory;
    private Animation<TextureRegion> explosionAnimation;
    public EnemyRenderer(
        GameState gameState,
        CombatStateManager combatState,
        Stage enemyStage
    ) {
        this.gameState = gameState;
        this.enemyStage = enemyStage;
        this.combatState = combatState;
        this.enemySpriteRegistry = new EnemySpriteRegistry();
        this.explosionAnimationFactory = new ExplosionAnimationFactory();
        this.explosionAnimation = explosionAnimationFactory.create();
        this.enemySpriteRoster = new HashMap<>();
        this.enemySpriteFactory = new EnemyAnimatedSpriteFactory();
        this.worldWidth = enemyStage.getViewport().getWorldWidth();
        this.worldHeight = enemyStage.getViewport().getWorldHeight();
    }


    public void intializeEnemySprites() {
        enemyStage.clear();
        float centerScreen = worldWidth / 2.0f;
        float spacer = 80f;
        float totalWidth = 0f;
        // spawn sprites, track width
        for (Map.Entry<Integer, EnemyCombatant> enemyEntry: combatState.getCurrentEnemyRoster().entrySet()){
            EnemyCombatant enemy = enemyEntry.getValue();
            EnemyAnimatedSprite newSprite = enemySpriteFactory.create(
                enemy.identifier,
                0f,
                0f
            );
            // if not first sprite, add spacer
            if(combatState.getCurrentEnemyRoster().containsKey(enemyEntry.getKey() - 1)){
                totalWidth = totalWidth + spacer;
            }
            totalWidth = totalWidth + newSprite.getSprite().getWidth();
            enemySpriteRoster.put(enemyEntry.getKey(), newSprite);
        }

        // reposition sprites so they appear centered equal width apart
        for (Map.Entry<Integer, EnemyAnimatedSprite> enemySprite: enemySpriteRoster.entrySet()){
            float xCoord = 0f;
            float yCoord = 0f;
            // not first sprite
            if(enemySpriteRoster.containsKey(enemySprite.getKey() - 1)){
                EnemyAnimatedSprite lastEnemy = enemySpriteRoster.get(enemySprite.getKey() - 1);
                // each subsequent enemy is one spacer away from last enemy
                xCoord = lastEnemy.getSprite().getX() + lastEnemy.getSprite().getWidth() + spacer;
                yCoord = worldHeight * 0.35f;
            } else{
                //first sprite
                // center the set of enemies by subtracting half of total width from the center of the screen
                // that's the X coord of the first sprite
                xCoord = centerScreen - (totalWidth / 2);
                yCoord = worldHeight * 0.35f;
            }
            enemySprite.getValue().getSprite().setPosition(xCoord, yCoord);
        }


    }


    public void draw(SpriteBatch batch, float delta){
        // batch.draw(animation.getFrame, actor.getX(), actor.getY)
        enemyStage.act(delta);
        // TODO: Make BOTH enemies nad Explosions actors and call enemyStage.draw(), remove batch from params
        // run outside of batch in calling code
        for (Actor actor : enemyStage.getActors()) {
            actor.draw(batch, 1.0f); // Calls explosionActor.draw directly!
        }
        for (Map.Entry<Integer, EnemyAnimatedSprite> enemyEntry: enemySpriteRoster.entrySet()){
            EnemyAnimatedSprite enemy = enemyEntry.getValue();
            enemy.update(delta);
            enemy.render(batch);
        }

    }

    public void updateEnemySprites(){
        // use returnAliveCombatants from EnemyRoster to check for sprites that do not have
        // an alive enemy and remove them. Trigger animation?
        Set<Integer> keys = enemySpriteRoster.keySet();
        Map<Integer, Combatant> aliveEnemies = CombatUtils.returnAliveCombatants(combatState.getCurrentEnemyRoster());

        Iterator<Map.Entry<Integer, EnemyAnimatedSprite>> it = enemySpriteRoster.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, EnemyAnimatedSprite> entry = it.next();

            if (!aliveEnemies.containsKey(entry.getKey())) {
                EnemyAnimatedSprite dyingSprite = entry.getValue();
                spawnQuickExplosion(enemyStage, dyingSprite.getSprite().getX(), dyingSprite.getSprite().getY());
                it.remove();
            }
        }

    }

    public void resize(int width, int height){
        enemyStage.getViewport().update(width, height, false);
        worldWidth = enemyStage.getViewport().getWorldWidth();
        worldHeight = enemyStage.getViewport().getWorldHeight();

        // TODO: reposition ALL enemies here :(
    }
    public void dispose(){
        // dispose of all assets here
        explosionAnimationFactory.dispose();
        enemySpriteFactory.dispose();
    }


    public void spawnQuickExplosion(Stage stage, final float x, final float y) {
        Actor explosionActor = new Actor() {
            private float stateTime = 0f;

            @Override
            public void act(float delta) {
                super.act(delta);
                stateTime += delta;

                if (explosionAnimation.isAnimationFinished(stateTime)) {
                    this.remove();
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                TextureRegion frame = explosionAnimation.getKeyFrame(stateTime);
                batch.draw(frame,
                    x - (frame.getRegionWidth() / 2f),
                    y - (frame.getRegionHeight() / 2f) + 40,
                    150f,
                    150f
                );
//                batch.draw(frame, 100, 100, 200f, 200f);
            }
        };

        stage.addActor(explosionActor);
    }
}
