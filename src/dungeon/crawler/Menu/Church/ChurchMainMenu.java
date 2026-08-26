package dungeon.crawler.Menu.Church;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryCharSelectMenu;
import dungeon.crawler.Menu.Shop.ShopCharSelectMenu;
import dungeon.crawler.Observers.MenuInputObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Screens.ChurchScreen;

import java.util.ArrayList;
import java.util.List;

public class ChurchMainMenu extends BaseLinearMenu{
    private final List<MenuInputObserver> listeners = new ArrayList<>();
    protected final GameState gameState;
    private final StatusMenuObserver statusMenuObserver;
    private final int shopIndex;
    private final ChurchScreen shopscreen;
    public ChurchMainMenu(
        Skin skin,
        ChurchScreen shopscreen,
        GameState gameState,
        int shopIndex
    ) {
        super(
            skin
        );
        this.gameState = gameState;
        this.statusMenuObserver = new StatusMenuObserver();
        this.shopIndex = shopIndex;
        this.shopscreen = shopscreen;
        this.addButton("Resurrection", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                int price = 500 * (shopIndex + 1);
                ChurchServiceMenu newMenu = new ChurchServiceMenu(
                    skin,
                    gameState,
                    "Resurrection",
                    price
                );
                setSubMenu(newMenu);
                openSubMenu(newMenu);
            }
        });
        this.addButton("Cure Ailments", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                int price = 100 * (shopIndex + 1);
                ChurchServiceMenu newMenu = new ChurchServiceMenu(
                    skin,
                    gameState,
                    "Cure Ailments",
                    price
                );
                setSubMenu(newMenu);
                openSubMenu(newMenu);
            }
        });

        this.addButton("Leave", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
//                InventoryCharSelectMenu newMenu = new InventoryCharSelectMenu(
//                    skin,
//                    gameState,
//                    false
//                );
//                setSubMenu(newMenu);
//                openSubMenu(newMenu);
                shopscreen.exitShop();

            }
        });


        this.pack();
        this.addFocusListeners();
        this.setPosition(10, Gdx.graphics.getHeight() - this.getHeight() - 50);

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
        this.setVisible(true);
    }

}
