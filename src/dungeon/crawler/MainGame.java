package dungeon.crawler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dungeon.crawler.AssetManager.Assets;
import dungeon.crawler.Controls.ControllerAdapter;
import dungeon.crawler.Data.Maps.MapRegistry;
import dungeon.crawler.Data.Maps.ScreenTransitionProperties;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.Combat.CombatStateManager;
import dungeon.crawler.GameSystem.Enemies.EnemySpawner;
import dungeon.crawler.GameSystem.GameBuild;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ShopItemSpawner;
import dungeon.crawler.GameSystem.TestData.EnemyFactory;
import dungeon.crawler.Menu.MainMenu.MainMenu;
//import dungeon.crawler.Menu.TestMenus.MenuTestScreen;
import dungeon.crawler.Observers.CombatScreenObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Screens.*;

import java.util.ArrayList;

public class MainGame extends Game implements ScreenChangeObserver,
    CombatScreenObserver {
    SpriteBatch spriteBatch;
    public GameState gameState;
    public Assets assets;
    private GameBuild build;
    private ControllerAdapter controllerAdapter;

    public MainGame(GameBuild gameBuild, ControllerAdapter controllerAdapter) {
        this.build = gameBuild;
        this.controllerAdapter = controllerAdapter;
    }

    public MainGame(){

    }

    @Override
    public void create() {
        gameState = new GameState(build);

        spriteBatch = new SpriteBatch();
        gameState.updateScreenID(1);
        assets = new Assets();
        assets.load();
        assets.finishLoading();
        setScreen(new MainMenuScreen(
            this
        ));

    }

    public void startGame(){
        String mapFile = GameConstants.TEST_MAP;
        gameState.updateWorldMap(mapFile);
        setScreen(new WorldScreenRefactor(
            this,
            spriteBatch,
            13f,
            12f,
            mapFile,
            GameConstants.GAME_SCREEN.WALK_TOWN
        ));
    }

    @Override
    public void onScreenChange(GameConstants.GAME_SCREEN screen){

        // this.gameState.currentEnemyRoster.put(2, enemy)
        if(screen == GameConstants.GAME_SCREEN.INN){
            InnScreen Inn = new InnScreen(this, this);
            setScreen(Inn);

        }
        if(screen == GameConstants.GAME_SCREEN.WALK_TOWN){
            backToOverworld();
        }
        else if(screen == GameConstants.GAME_SCREEN.COMBAT){
            CombatStateManager combatState = new CombatStateManager(EnemySpawner.spawnEnemies(gameState));
            CombatScreen combatScreen = new CombatScreen(this, combatState);
            setScreen(combatScreen);
        } else if(screen == GameConstants.GAME_SCREEN.SHOP_SCREEN){
            ScreenTransitionProperties worldScreenData = MapRegistry.WORLD_MAP_DATA.get(gameState.screenID);
            int shopIndex = worldScreenData.shopIndex;
            ArrayList<Item> inventory = ShopItemSpawner.spawnItems(shopIndex);
            ShopScreen shopScreen = new ShopScreen(this, inventory);
            setScreen(
                shopScreen
            );

            setScreen(shopScreen);
        } else if(screen == GameConstants.GAME_SCREEN.CHURCH_SCREEN){
            ScreenTransitionProperties worldScreenData = MapRegistry.WORLD_MAP_DATA.get(gameState.screenID);
            int shopIndex = worldScreenData.shopIndex;
            ChurchScreen  churchScreen = new ChurchScreen(
                this,
                shopIndex
            );
            setScreen(churchScreen);

        }
    }

    @Override
    public void onMapChange(int ScreenId){
        ScreenTransitionProperties worldScreenData = MapRegistry.WORLD_MAP_DATA.get(ScreenId);
        gameState.updateWorldCoordinates(new Vector2(
            worldScreenData.startingX,
            worldScreenData.startingY
        ));
        gameState.updateWorldMap(worldScreenData.mapFile);
        gameState.updateScreenID(ScreenId);
        setScreen(new WorldScreenRefactor(
            this,
            spriteBatch,
            worldScreenData.startingX,
            worldScreenData.startingY,
            worldScreenData.mapFile,
            worldScreenData.screen
        ));
    };

    @Override
    public void onCombatVictory(){
        backToOverworld();
    }

    @Override
    public void onCombatLoss(){
        // TODO: set up actual losslogic
        //TODO: BAD
        gameState.player.hp = 1;
        String mapFile = "Maps/testmap.tmx";
        gameState.updateWorldMap(mapFile);
        gameState.updateScreenID(1);

        spriteBatch = new SpriteBatch();
        setScreen(new WorldScreenRefactor(
            this,
            spriteBatch,
            13f,
            12f,
            mapFile,
            GameConstants.GAME_SCREEN.WALK_TOWN
        ));
    }

    public void backToOverworld(){
        ScreenTransitionProperties worldScreenData = MapRegistry.WORLD_MAP_DATA.get(gameState.screenID);
        setScreen(new WorldScreenRefactor(
            this,
            spriteBatch,
            gameState.overWorldCoordinates.x,
            gameState.overWorldCoordinates.y,
            // worldScreenData.startingX,
            // worldScreenData.startingY,
            worldScreenData.mapFile,
            worldScreenData.screen
        ));
    }

    public void loadGameState(GameState state){
        state.initializeRuntimeParty();
        this.gameState = state;
    }

    @Override
    public void dispose(){
        assets.dispose();
    }

    public ControllerAdapter getControllerAdapter() {
        return controllerAdapter;
    }
}
