package dungeon.crawler.Sprites.Enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import dungeon.crawler.Data.Enemies.EnemySpriteParams;
import dungeon.crawler.Data.Enemies.EnemySpriteRegistry;
import dungeon.crawler.GameConstants;

import java.util.HashMap;
import java.util.Map;

public class EnemyAnimatedSpriteFactory
{
    private EnemySpriteRegistry enemySpriteRegistry;
    public AssetManager assetManager;

    public EnemyAnimatedSpriteFactory(){
        this.assetManager = new AssetManager();
        this.enemySpriteRegistry = new EnemySpriteRegistry();
    }

    public EnemyAnimatedSpriteFactory(AssetManager assetManager){
        this.assetManager = assetManager;
        this.enemySpriteRegistry = new EnemySpriteRegistry();
    }

    public EnemyAnimatedSprite create(
        String enemyID,
        float x,
        float y
    ){
        EnemySpriteParams params = this.enemySpriteRegistry.getEnemySpriteMap().get(enemyID);
        Map<String, Animation<TextureRegion>> animations = new HashMap<>();
        String frame1 = params.getFrame1();
        String frame2 = params.getFrame2();

        if(!assetManager.isLoaded(frame1)){
            assetManager.load(frame1, Texture.class);
            assetManager.load(frame2, Texture.class);
            assetManager.finishLoading();
        }

        Texture f1 = assetManager.get(params.getFrame1(), Texture.class);
        Texture f2 = assetManager.get(params.getFrame2(), Texture.class);
        TextureRegion region1 = new TextureRegion(f1);
        TextureRegion region2 = new TextureRegion(f2);
        Animation<TextureRegion> animation = new Animation<>(GameConstants.ENEMY_ANIMATION_SPEED, region1, region2);
        animations.put("idle", animation);
        return new EnemyAnimatedSprite(
            enemyID,
            animations,
            "idle",
            x,
            y,
            params.getWidth(),
            params.getHeight()
        );
    }

public void dispose(){
    assetManager.dispose();
}

}
