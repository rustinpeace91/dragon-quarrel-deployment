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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.MainGame;
import dungeon.crawler.Menu.InputHandlers.MenuInputHandler;
import dungeon.crawler.Menu.Shop.ShopMainMenu;
import dungeon.crawler.Menu.TestMenus.TestShopMenu;
import dungeon.crawler.Observers.MenuInputObserver;

import java.util.ArrayList;

public class ShopScreen extends ScreenAdapter  implements MenuInputObserver {
    private MainGame game;
    private ShopMainMenu shopMenu;
    private SpriteBatch batch;
    private Stage uiStage;
    private MenuInputHandler menuInputHandler;

    private Texture backgroundTexture;
    private ArrayList<Item> inventory;
    private Skin skin;
    private GameInputHandler gameInputHandler;

    public ShopScreen(
        MainGame game,
        ArrayList<Item> inventory
    ){
        this.uiStage = new Stage(new FitViewport(GameConstants.RESOLUTION_WIDTH, GameConstants.RESOLUTION_HEIGHT));
        this.game = game;
        this.batch = new SpriteBatch();
        this.inventory = inventory;
        skin = new Skin(Gdx.files.internal(GameConstants.MENU_SKIN));

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
    }

    @Override
    public void show(){
        gameInputHandler = new GameInputHandler();
        game.getControllerAdapter().attach(gameInputHandler);

        shopMenu = new ShopMainMenu(
            skin,
            this,
            game.gameState,
            inventory
        );
        this.uiStage.addActor(shopMenu);
        this.menuInputHandler = new MenuInputHandler(
            uiStage,
            shopMenu,
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


    @Override
    public void dispose() {
        skin.dispose();
        uiStage.dispose();
        this.backgroundTexture .dispose();
    }

    @Override
    public void hide(){
        game.getControllerAdapter().detach();

    }

    public void exitShop(){
        game.onScreenChange(GameConstants.GAME_SCREEN.WALK_TOWN);
    }
}
