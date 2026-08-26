package dungeon.crawler.Player;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dungeon.crawler.GameConstants;
import dungeon.crawler.Observers.PlayerPositionObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Subject.GameSubject;

public class PlayerPositionHandler extends GameSubject<PlayerPositionObserver>{
    public float x;
    public float y;

    protected final Array<PlayerPositionObserver> observers = new Array<>();
    protected final Array<ScreenChangeObserver> screenObservers = new Array<>();

    // tile number
    public float tileX;
    public float tileY;

    // where the player intends to move based on input
    public float targetTileX;
    public float targetTileY;
    public float movementProgress;
    private Vector2 startVisualPos = new Vector2();


    // seems redundant from inPut handler but it won't be when tile based movement is implimented
    // becasue the player could be moving independant of what input is being pressed
    public PlayerDirection direction;
    public boolean isMoving;
    public boolean blocked;

    private PlayerInputHandler playerInputHandler;
    private float movementDuration;

    private float mapWidth;
    private float mapHeight;

    private TiledMap worldMap;
    private TiledMapTileLayer collisionLayer;
    TiledMapTileLayer groundLayer;
    TiledMapTileLayer transitionLayer;

    public PlayerPositionHandler(
        TiledMap worldMap,
        PlayerInputHandler input,
        float movementDuration,
        float initialX,
        float initialY
    ) {
        this.worldMap = worldMap;
        this.playerInputHandler = input;
        this.blocked = false;

        // where the player is on the screen. TODO: rename visualX
        this.x = initialX * GameConstants.TILE_WIDTH;
        this.y = initialY * GameConstants.TILE_WIDTH;
        // the  coord of the tile the player is on
        this.tileX = initialX;
        this.tileY = initialY;

        this.targetTileX = initialX;
        this.targetTileY = initialY;

        this.movementProgress = 0f;
        this.movementDuration = movementDuration;



    // Get map dimensions in pixels

        // move to util file
    int mapWidth = worldMap.getProperties().get("width", Integer.class);
    int mapHeight = worldMap.getProperties().get("height", Integer.class);


    this.groundLayer = (TiledMapTileLayer) worldMap.getLayers().get("Ground");
    this.transitionLayer = (TiledMapTileLayer) worldMap.getLayers().get("Transition");


    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;

    this.isMoving = false;
    this.direction = PlayerDirection.DOWN;


    }

    @Override
    public void addObserver(PlayerPositionObserver observer) {
        observers.add(observer);
    }

    public void addScreenChangeListener(ScreenChangeObserver observer) {
        screenObservers.add(observer);
    }

    @Override
    public void removeObserver(PlayerPositionObserver observer) {
    observers.removeValue(observer, true);
    }



    public void update(float delta){
        if(isMoving){
            updateMoveFrames(delta);
        } else {
            updateInput(delta);
        }
    }


    public void updateMoveFrames(float delta){
        movementProgress += delta / movementDuration;

        if (movementProgress >= 1f) {
            // Arrived at destination
            isMoving = false;
            tileX = targetTileX;
            tileY = targetTileY;
            x = tileX * GameConstants.TILE_WIDTH;
            y = tileY * GameConstants.TILE_HEIGHT;
            this.movementProgress = 0f;
            Cell tileCell = returnTileCell(tileX, tileY);
            onEnteredNewTile(tileCell);
        } else {
            // Apply smoothing: Interpolation.linear or Interpolation.smooth
            float alpha = Interpolation.linear.apply(movementProgress);
            // Lerp from the starting pixel position to the target pixel position
            float targetPixelX = targetTileX * GameConstants.TILE_WIDTH;
            float targetPixelY = targetTileY * GameConstants.TILE_HEIGHT;

            x = MathUtils.lerp(startVisualPos.x, targetPixelX, alpha);
            y = MathUtils.lerp(startVisualPos.y, targetPixelY, alpha);
        }
    }

    public void handleDirectionChange(PlayerDirection newDirection){
        if(newDirection != direction){
            // handle notify logic here
            this.direction = newDirection;

            onDirectionChange(newDirection);
        }
    }

    public void onDirectionChange(PlayerDirection newDirection) {
        for (PlayerPositionObserver obs : observers) {

            obs.onDirectionChange(newDirection);
        }
    }

    public void onEnteredNewTile(Cell tileCell){
        Cell actionCell = transitionLayer.getCell((int)tileX, (int)tileY);

        if (actionCell != null && actionCell.getTile() != null) {
            // 2. Check the properties of the "Yellow Square" tile
            MapProperties props = actionCell.getTile().getProperties();
            // Iterator<String> keys = props.getKeys();
            if (props.containsKey("world_screen")) {
                int screenId = props.get("world_screen", Integer.class);

                // 3. Notify your observers (MainGame, etc.)
                for (PlayerPositionObserver obs : observers) {
                    obs.onTransition(screenId);
                }
            }
            // if (props.containsKey("inn")) {
            //     System.out.println("MOOOOOOOOOOO");
            // }
            if (props.containsKey("inn")) {
                // 3. Notify your observers (MainGame, etc.)
                for (ScreenChangeObserver obs : screenObservers) {
                    obs.onScreenChange(GameConstants.GAME_SCREEN.INN);
                }

            }
            if (props.containsKey("general_shop")) {
                // 3. Notify your observers (MainGame, etc.)
                for (ScreenChangeObserver obs : screenObservers) {
                    obs.onScreenChange(GameConstants.GAME_SCREEN.SHOP_SCREEN);
                }

            }
            if (props.containsKey("church")) {
                // 3. Notify your observers (MainGame, etc.)
                for (ScreenChangeObserver obs : screenObservers) {
                    obs.onScreenChange(GameConstants.GAME_SCREEN.CHURCH_SCREEN);
                }

            }

        } else {
            for (PlayerPositionObserver obs : observers) {
                obs.onEnteredNewTile(tileCell);
            }
        }
    }

    public void updateInput(float delta){

        float tempTileX = tileX;
        float tempTileY = tileY;

        if(isMoving){
            return;
        }

        if(playerInputHandler.movementInputPressed == false){
            return;
        }

        if(playerInputHandler.direction == PlayerDirection.LEFT) {
            tempTileX = tileX - 1;
            tempTileY = tileY;
            handleDirectionChange(PlayerDirection.LEFT);
        }

        else if(playerInputHandler.direction == PlayerDirection.RIGHT) {
            tempTileX = tileX + 1;
            tempTileY = tileY;
            handleDirectionChange(PlayerDirection.RIGHT);
        }
        else if(playerInputHandler.direction == PlayerDirection.UP) {
            tempTileX = tileX;
            tempTileY = tileY + 1;
            handleDirectionChange(PlayerDirection.UP);

        }
        else if(playerInputHandler.direction == PlayerDirection.DOWN) {
            tempTileX = tileX;
            tempTileY = tileY -1;
            handleDirectionChange(PlayerDirection.DOWN);
        }

        if(!isTileBlocked(tempTileX, tempTileY)){
            targetTileX = tempTileX;
            targetTileY = tempTileY;
            startVisualPos.set(x, y);
            isMoving = true;
            isMoving = true;
            movementProgress = 0f;
        }

    }

    private boolean isTileBlocked(float tileX, float tileY) {

        if (tileX < 0 || tileY < 0 || tileX >= mapWidth || tileY >= mapHeight) {
            return true; // Treat "off-map" as blocked
        }
        TiledMapTileLayer.Cell cell = groundLayer.getCell((int) tileX, (int) tileY);

        // Check if cell exists and has the "blocked" property
        return cell != null && cell.getTile().getProperties().containsKey("blocked");
    }

    private Cell returnTileCell(float tileX, float tileY){
        TiledMapTileLayer.Cell cell = groundLayer.getCell((int) tileX, (int) tileY);
        return cell;
    }

}
