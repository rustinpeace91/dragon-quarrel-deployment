package dungeon.crawler.Sprites.Enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dungeon.crawler.Data.Enemies.EnemySpriteParams;
import dungeon.crawler.Data.Enemies.EnemySpriteRegistry;
import dungeon.crawler.Sprites.AnimatedSprite;

import java.util.HashMap;
import java.util.Map;

public class EnemyAnimatedSprite extends AnimatedSprite {
    public String enemyID;
    public EnemyAnimatedSprite(
        String enemyID,
        Map<String, Animation<TextureRegion>> animations,
        String initialState,
        float x,
        float y,
        float sx,
        float sy
    ) {

       super(
           animations,
           "idle",
           x,
           y,
           sx,
           sy
       );
       this.enemyID = enemyID;
    }
}
