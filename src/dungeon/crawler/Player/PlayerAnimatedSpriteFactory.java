package dungeon.crawler.Player;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dungeon.crawler.GameConstants;
import dungeon.crawler.Sprites.AnimationBuilder;

public class PlayerAnimatedSpriteFactory {

public PlayerAnimatedSprite createAnimation(
float viewPortWidth,
float viewPortHeight,
String initialState,
PlayerPositionHandler playerPositionHandler
) {
Map<String, Animation<TextureRegion>> animations = buildWalkAnim();
PlayerAnimatedSprite sprite = new PlayerAnimatedSprite(        
animations,
playerPositionHandler,
        initialState,
        viewPortWidth,
        viewPortHeight,
        GameConstants.SPRITE_WIDTH,
        GameConstants.SPRITE_HEIGHT
        );

return sprite;

}

    private Map<String, Animation<TextureRegion>> buildWalkAnim(){
    Texture fullSheet = new Texture(GameConstants.CURRENT_SPRITE);
    Map<String, Animation<TextureRegion>> animationMap = new HashMap<String, Animation<TextureRegion>>();

    animationMap.put(
    GameConstants.WALK_ANIMATIONS.get(PlayerDirection.DOWN),
    AnimationBuilder.createAnimationByRow(fullSheet, 3, 4, 1, GameConstants.FRAME_DURATION)
    );
    // TODO: Use Constants
    animationMap.put(
        GameConstants.WALK_ANIMATIONS.get(PlayerDirection.LEFT),
AnimationBuilder.createAnimationByRow(fullSheet, 3, 4, 2, GameConstants.FRAME_DURATION)
    );
    animationMap.put(
        GameConstants.WALK_ANIMATIONS.get(PlayerDirection.RIGHT),
    AnimationBuilder.createAnimationByRow(fullSheet, 3, 4, 3, GameConstants.FRAME_DURATION)
    );
    animationMap.put(
        GameConstants.WALK_ANIMATIONS.get(PlayerDirection.UP),
    AnimationBuilder.createAnimationByRow(fullSheet, 3, 4, 4, GameConstants.FRAME_DURATION)
    );
    return animationMap;

    }


}
