package dungeon.crawler.Player;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import dungeon.crawler.GameConstants;
import dungeon.crawler.Observers.PlayerPositionObserver;
import dungeon.crawler.Sprites.AnimatedSprite;

public class PlayerAnimatedSprite extends AnimatedSprite implements PlayerPositionObserver{
    private PlayerPositionHandler playerPosition;
    public PlayerAnimatedSprite(
        Map<String,
        Animation<TextureRegion>> animations,
        PlayerPositionHandler playerPosition,
        String initialState,
        float x,
        float y,
        float sx,
        float sy
    ) {
        super(animations, initialState, x, y, sx, sy);
        this.playerPosition = playerPosition;
    }

    @Override
    public void update(float delta) {
        Sprite sprite = super.getSprite();
        sprite.setPosition(playerPosition.x, playerPosition.y);
        if(playerPosition.isMoving) {
            super.update(delta);

        }
    }

    @Override
    public void onDirectionChange(PlayerDirection direction) {
        String animation = GameConstants.WALK_ANIMATIONS.get(direction);
        super.setState(animation);
    }

    @Override
    public void onEnteredNewTile(Cell tileCell){}

    @Override
    public void onTransition(int screenID){};

}
