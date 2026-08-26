package dungeon.crawler.Menu.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.GameBuild;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryCharSelectMenu;
import dungeon.crawler.Menu.Shop.ShopCharSelectMenu;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Observers.MenuInputObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Screens.MainMenuScreen;
import dungeon.crawler.Screens.ShopScreen;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends BaseLinearMenu{
    private final List<MenuInputObserver> listeners = new ArrayList<>();
    protected final GameState gameState;
    private final StatusMenuObserver statusMenuObserver;

    public MainMenu(
        Skin skin,
        GameState gameState,
        MainMenuScreen main
    ) {
        super(
            skin
        );
        this.gameState = gameState;
        this.statusMenuObserver = new StatusMenuObserver();
        this.addButton("New Game", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                NewOrGenerateMenu newMenu = new NewOrGenerateMenu(
                    skin,
                    gameState,
                    main
                );
                setSubMenu(newMenu);
                openSubMenu(newMenu);
            }
        });
        this.addButton("Load", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                String message = main.loadGame();

                showPopup(message, 2f);
//                if(gameState.getBuild() == GameBuild.DESKTOP){
//                    String message = main.loadGame();
//                    showPopup(message, 2f);
//                } else {
//                    showPopup("Load works in Desktop Mode only", 2f);
//                }


            }
        });

        this.pack();
        this.addFocusListeners();
//        this.setPosition(10, Gdx.graphics.getHeight() - this.getHeight() - 50);
        this.setPosition(
            (Gdx.graphics.getWidth() - this.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - this.getHeight()) / 2f
        );


        // addMenuListeners(partyButton, searchButton, testNewMenu);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null) {
            refreshAndSetActive();
        }

    }

    @Override
    public void notifyScreenChange(GameConstants.GAME_SCREEN screen){
        for (ScreenChangeObserver observer : screenChangeObservers) {
            observer.onScreenChange(screen);
        }
    }

    @Override
    public void addScreenChangeObserver(ScreenChangeObserver observer){
        screenChangeObservers.add(observer);
    }

    public void addListener(MenuInputObserver listener) {
        if(listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(MenuInputObserver listener) {
        if(listener != null) {
            listeners.remove(listener);
        }
    }



    @Override
    public void openSubMenu(BaseLinearMenu nextMenu){
        super.openSubMenu(nextMenu);
        this.setVisible(false);
    }

}
