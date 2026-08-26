package dungeon.crawler.Sprites.Explosion;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dungeon.crawler.Data.Enemies.EnemySpriteRegistry;
import dungeon.crawler.GameConstants;
import dungeon.crawler.Sprites.AnimationBuilder;

public class ExplosionAnimationFactory {
    private final int SPRITE_ROWS = 1;
    private final int SPRITE_COLUMNS = 16;

    public Texture sheet;

    public ExplosionAnimationFactory(){
        this.sheet = new Texture(GameConstants.EXPLOSION_SPRITES);
    }

    public Animation<TextureRegion> create(){

        Animation<TextureRegion> explosionAnimation = AnimationBuilder.createAnimation(
            sheet,
            SPRITE_COLUMNS,
            SPRITE_ROWS,
            0.10f
        );
        return explosionAnimation;
    }

    public void dispose(){
        sheet.dispose();
    }


}
