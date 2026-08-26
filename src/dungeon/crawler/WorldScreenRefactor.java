package dungeon.crawler;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.GameSystem.Combat.CombatStateManager;
import dungeon.crawler.GameSystem.Enemies.EnemySpawner;
import dungeon.crawler.Menu.InputHandlers.MenuInputHandler;
import dungeon.crawler.Menu.Overworld.OverworldMenu;
import dungeon.crawler.Menu.Overworld.PartyCharacterStatusMenu;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Observers.MenuInputObserver;
import dungeon.crawler.Observers.PlayerPositionObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Player.PlayerAnimatedSprite;
import dungeon.crawler.Player.PlayerAnimatedSpriteFactory;
import dungeon.crawler.Player.PlayerDirection;
import dungeon.crawler.Player.PlayerInputHandler;
import dungeon.crawler.Player.PlayerPositionHandler;
import dungeon.crawler.Utils.StringUtils;

public class WorldScreenRefactor extends ScreenAdapter
implements MenuInputObserver,
ScreenChangeObserver,
PlayerPositionObserver {
    private MainGame game;
    private SpriteBatch spriteBatch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private PartyCharacterStatusMenu statusMenu;
    private StandardStatusMenu goldMenu;

    private PlayerAnimatedSprite characterSprite;
    private boolean overWorld;


    private TiledMapTileLayer collisionLayer;

    // movement stuff
    private PlayerInputHandler playerInput;
    private PlayerPositionHandler playerPosition;
    // map bounderies


    private float movementDuration;

    // menu stuff
    private Skin skin;
    private Stage uiStage;
    private MenuInputHandler menuInputHanlder;
    private boolean menuVisible;
    private boolean statusScreenVisible;

// observers
    public ArrayList<ScreenChangeObserver> screenChangeObservers;
    private GameInputHandler gameInputHandler;


    public WorldScreenRefactor(
        MainGame game,
        SpriteBatch spriteBatch,
        float startingX,
        float startingY,
        String mapFile,
        GameConstants.GAME_SCREEN screen
    ) {
        this.game = game;
        this.spriteBatch = spriteBatch;
        this.map = new TmxMapLoader().load(mapFile);
        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.overWorld = screen.equals(GameConstants.GAME_SCREEN.WALK_OVERWORLD);
        setUpCamera();
        float screenCenterY = camera.viewportHeight / 2f;
        float screenCenterX = camera.viewportWidth / 2f;
        camera.position.y = startingY;
        camera.position.x = startingX;

        if(this.overWorld){
            this.movementDuration = GameConstants.OVERWORLD_MOVEMENT_DURATION;
        } else {
            this.movementDuration = GameConstants.TOWN_MOVEMENT_DURATION;
        }

        // build collision layers
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("Ground");
        gameInputHandler = new GameInputHandler();
        // Player stuff
        this.playerInput = new PlayerInputHandler(PlayerDirection.DOWN, gameInputHandler);

        this.playerPosition = new PlayerPositionHandler(
            map,
            playerInput,
            movementDuration,
            startingX,
            startingY
        );
        PlayerAnimatedSpriteFactory factory = new PlayerAnimatedSpriteFactory();

        this.characterSprite = factory.createAnimation(
            screenCenterX,
            screenCenterY,
            GameConstants.WALK_ANIMATIONS.get(PlayerDirection.DOWN),
            playerPosition
        );
        playerPosition.addObserver(characterSprite);
        playerPosition.addScreenChangeListener(game);
        this.skin = new Skin(Gdx.files.internal(GameConstants.MENU_SKIN));

        setUpMenu();
        //input
        InputMultiplexer multiplexer = setUpInput();
        Gdx.input.setInputProcessor(multiplexer);
        // screen change
        this.screenChangeObservers = new ArrayList<ScreenChangeObserver>();
        this.screenChangeObservers.add(game);
    }

    private void setUpCamera() {
        // set up camera
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.RESOLUTION_WIDTH, GameConstants.RESOLUTION_HEIGHT);
        camera.zoom=0.25f;
        camera.update();
    }

    private void setUpMenu() {
        this.uiStage = new Stage(new ScreenViewport());

        OverworldMenu menu = new OverworldMenu(
            this.skin,
            game.gameState
        );
        float x = 20;
        float y = Gdx.graphics.getHeight() - menu.getHeight() - 20;
        menu.setPosition(x, y);
        menu.addScreenChangeObserver(game);
        this.uiStage.addActor(menu);

        this.menuInputHanlder = new MenuInputHandler(
            uiStage,
            menu,
            gameInputHandler
        );
        menuInputHanlder.addListener(this);

        // statusMenu = new PartyCharacterStatusMenu(skin, this.game.gameState.player);
        // x = Gdx.graphics.getWidth() - statusMenu.getWidth() -20 ;
        // y = Gdx.graphics.getHeight() - statusMenu.getHeight() - 20;
        // statusMenu.setPosition(x, y + 20);
        // this.uiStage.addActor(statusMenu);
        //
        goldMenu = new StandardStatusMenu(skin);
        String gold = String.valueOf(this.game.gameState.gold);
        goldMenu.setText(StringUtils.format("Gold: %s ", gold));
        goldMenu.setPosition(x, y - 150);
        this.uiStage.addActor(goldMenu);

    }

    public InputMultiplexer setUpInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // --- Configure the InputMultiplexer ---
        multiplexer.addProcessor(gameInputHandler);
        // 6. Tell LibGDX to use the multiplexer for all input events
        return multiplexer;
    }

    @Override
    public void show() {
        // The object is fully built now, so it's safe to share 'this'
        Gdx.input.setCatchKey(Input.Keys.UP, true);
        Gdx.input.setCatchKey(Input.Keys.DOWN, true);
        Gdx.input.setCatchKey(Input.Keys.LEFT, true);
        Gdx.input.setCatchKey(Input.Keys.RIGHT, true);
        Gdx.input.setCatchKey(Input.Keys.SPACE, true);
        game.getControllerAdapter().attach(gameInputHandler);
        playerPosition.addObserver(this);
    }

    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();


        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        characterSprite.render(spriteBatch);
        spriteBatch.end();
        if(menuVisible) {
        // if(menuVisible){
            String gold = String.valueOf(this.game.gameState.gold);
            goldMenu.setText(StringUtils.format("Gold: %s ", gold));
            uiStage.act(Gdx.graphics.getDeltaTime());
            uiStage.draw();
        }

    }

    @Override
    public void render(float delta) {
        input(delta);
        draw();
    }

    private void input(float delta) {
        /*
        * check for input, project camera to a new x
        * set sprite animation
        * project camera to a new X,Y coord and check for collisions taking into account player
        *
        */

        if(!menuVisible) {
            playerInput.updateInput();
            playerPosition.update(delta);
            camera.position.x = playerPosition.x + 8;
            camera.position.y = playerPosition.y;
            characterSprite.update(
                delta
            );
        }
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        uiStage.dispose();
        skin.dispose();
    }

    @Override
    public void onMenuToggled(boolean value) {
        menuVisible = value;
    }

    public void notifyScreenChange(GameConstants.GAME_SCREEN screen){
            for (ScreenChangeObserver observer : screenChangeObservers) {
                game.gameState.overWorldCoordinates = new Vector2(playerPosition.tileX, playerPosition.tileY);
                observer.onScreenChange(screen);
            }
    }

    public void addScreenChangeObserver(ScreenChangeObserver observer){
        screenChangeObservers.add(observer);
    }

    @Override
    public void onDirectionChange(PlayerDirection newDirection){}

    @Override
    public void onEnteredNewTile(Cell tileCell){
        Vector2 newCoords = new Vector2(playerPosition.tileX, playerPosition.tileY);
        this.game.gameState.updateWorldCoordinates(newCoords);
        /* Weird pattern I know, but we never figured out a good way to transfer data between screens and
        the gamestate is now used for saving data, and enemies should not be saved
         */
        MapProperties props = tileCell.getTile().getProperties();
        if(!props.containsKey("tile_difficulty")){
            game.gameState.setTileDifficulty(0);
        } else {
            game.gameState.setTileDifficulty(props.get("tile_difficulty", Integer.class));
        }
        // this.game.gameState.screenID = this.screenID;
        if(overWorld){
            float roll = MathUtils.random();
            if ( roll < 0.10f) {
                notifyScreenChange(GameConstants.GAME_SCREEN.COMBAT);
            }
        }
    };

    @Override
    public void onTransition(int screenID){
        for (ScreenChangeObserver observer : screenChangeObservers) {
            observer.onMapChange(screenID);
        }
    };

    @Override
    public void onScreenChange(GameConstants.GAME_SCREEN screen){
        for (ScreenChangeObserver observer : screenChangeObservers) {
            observer.onScreenChange(screen);
        }
    };
    @Override
    public void hide(){
        game.getControllerAdapter().detach();

    }
    @Override
    public void onMapChange(int ScreenId){};
}
