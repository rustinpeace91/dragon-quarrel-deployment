package dungeon.crawler.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.SaveGame.SaveGame;
import dungeon.crawler.MainGame;
import dungeon.crawler.Menu.InputHandlers.MenuInputHandler;
import dungeon.crawler.Menu.MainMenu.MainMenu;
import dungeon.crawler.Observers.MenuInputObserver;

import java.util.ArrayList;

public class MainMenuScreen extends ScreenAdapter  implements MenuInputObserver {
    private MainGame game;
    private MainMenu mainMenu;
    private SpriteBatch batch;
    private Stage uiStage;
    private MenuInputHandler menuInputHandler;
    private GameInputHandler gameInputHandler;
    private Skin skin;

    private Texture backgroundTexture;
    private ArrayList<String> partySelection;

    public MainMenuScreen(
        MainGame game
    ){
        this.uiStage = new Stage(new FitViewport(GameConstants.RESOLUTION_WIDTH, GameConstants.RESOLUTION_HEIGHT));
        this.game = game;
        this.batch = new SpriteBatch();
        this.partySelection = new ArrayList<>();
        this.backgroundTexture = new Texture(Gdx.files.internal("Misc/storefront.jpg"));
        // 1. Load the PNG
        Texture texture = new Texture(Gdx.files.internal("Misc/storefront.jpg"));

        // 2. Wrap it in an Image actor
        Image imageActor = new Image(texture);



        // 3. Position and add it
        // imageActor.setPosition(100, 100);
        imageActor.setScaling(Scaling.stretch); // This forces it to stretch to the actor's bounds

        // 2. Tell it to fill the entire stage
        imageActor.setFillParent(true);

        uiStage.addActor(imageActor);
        resetClassString();
    }

    @Override
    public void show(){
        skin = new Skin(Gdx.files.internal(GameConstants.MENU_SKIN));
        gameInputHandler = new GameInputHandler();
        game.getControllerAdapter().attach(gameInputHandler);
        mainMenu = new MainMenu(
            skin,
            game.gameState,
            this
        );
        this.uiStage.addActor(mainMenu);
        this.menuInputHandler = new MenuInputHandler(
            uiStage,
            mainMenu,
            gameInputHandler
        );
        InputMultiplexer multiplexer = setUpInput();
        Gdx.input.setInputProcessor(multiplexer);
    }
    public InputMultiplexer setUpInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // --- Configure the InputMultiplexer ---
        this.menuInputHandler.addListener(this);

        multiplexer.addProcessor(gameInputHandler);
        multiplexer.addProcessor(uiStage);
        // 6. Tell LibGDX to use the multiplexer for all input events
        return multiplexer;
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2. Draw directly to the screen (Manual Layer)
        batch.begin();
        // Draws the image at x=100, y=100 with its original size
        // batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();


        // Update and Draw the Stage
        uiStage.act(delta);
        uiStage.draw();
        // input(delta);
    }

    @Override
    public void onMenuToggled(boolean menuVisible){};

    public void addClassString(String name){
        partySelection.add(name);
    }

    public void resetClassString(){
        partySelection = new ArrayList<>();
        partySelection.add("Hero");
    }

    public void removeLastClassString(){
        if (
            partySelection != null &&
            !partySelection.isEmpty() &&
            partySelection.size() > 1
        ) {
            partySelection.remove(partySelection.size() - 1);
        }
    }

    public ArrayList<String> getPartySelection(){
        return partySelection;
    }

    public void startGame(){
        game.gameState.SetUpClassDataFromString(partySelection);
        game.startGame();
    }

    public void startGamePreset(){
        game.gameState.setUpTestData();
        game.startGame();
    }

    public String loadGame(){
        SaveGame saveSystem = new SaveGame();
        GameState newState = saveSystem.loadGameState();
//        if(newState == null){
//            return "No valid save file present";
//        }
//
//        game.loadGameState(newState);
//        game.backToOverworld();
        return "Game loaded";
    }

    @Override
    public void hide(){
        game.getControllerAdapter().detach();

    }
    @Override
    public void dispose() {
        skin.dispose();

        uiStage.dispose();
        this.backgroundTexture .dispose();
    }
}
