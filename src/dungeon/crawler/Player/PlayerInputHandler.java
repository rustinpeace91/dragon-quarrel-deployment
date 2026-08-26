package dungeon.crawler.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.Controls.GameKey;

public class PlayerInputHandler {
    public PlayerDirection direction;
    private final GameInputHandler inputHandler;
    public boolean movementInputPressed = false;
    private GameKey controllerHeld = null;

    public PlayerInputHandler(
        PlayerDirection direction,
        GameInputHandler inputHandler
    ) {
    this.direction = direction;
    this.inputHandler = inputHandler;
    }
    // handle eventual key config here
    public void updateInput() {
        if (inputHandler.held(GameKey.LEFT)) {
            this.direction = PlayerDirection.LEFT;
            this.movementInputPressed = true;
        }
        else if (inputHandler.held(GameKey.RIGHT)) {
            this.direction = PlayerDirection.RIGHT;
            this.movementInputPressed = true;
        }
        else if (inputHandler.held(GameKey.UP)) {
            this.direction = PlayerDirection.UP;
            this.movementInputPressed = true;
        }
        else if (inputHandler.held(GameKey.DOWN)) {
            this.direction = PlayerDirection.DOWN;
            this.movementInputPressed = true;
        } else {
            this.movementInputPressed = false;
        }
    }
}
