package dungeon.crawler.Menu.Shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.compression.lzma.Base;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryCharSelectMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Menu.ScrollableLinearMenu;
import dungeon.crawler.Observers.MenuInputObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Screens.ShopScreen;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ShopItemCategoryMenu extends BaseLinearMenu implements OverworldSubMenu {
    private final List<MenuInputObserver> listeners = new ArrayList<>();
    protected final GameState gameState;
    private final ArrayList<Item> shopInventory;
    private final StatusMenuObserver statusMenuObserver;
    private final PartyCharacter currentCombatant;

    public BaseLinearMenu asCombatMenu(){return this;}

    public ShopItemCategoryMenu (
        Skin skin,
        GameState gameState,
        ArrayList<Item> shopInventory,
        PartyCharacter currentCombatant
    ) {
        super(
            skin
        );
        this.gameState = gameState;
        this.shopInventory = shopInventory;
        this.statusMenuObserver = new StatusMenuObserver();
        this.currentCombatant = currentCombatant;

//        this.setPosition(10, Gdx.graphics.getHeight() - this.getHeight() - 50);

        // addMenuListeners(partyButton, searchButton, testNewMenu);
    }

    public void addPartyButtons(){
        this.clearChildren();
        setTitle(StringUtils.format("Item Shop"));
        this.initializeArrow();
        this.addButton("Weapons", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                ArrayList<Item> weapons = ItemUtils.returnItemsByType(
                    shopInventory,
                    ItemType.WEAPON
                );
                ShopItemMenu newMenu = new ShopItemMenu(
                    skin,
                    gameState,
                    weapons,
                    currentCombatant,
                    false
                );
                setSubMenu(newMenu);
                openSubMenu(newMenu);
            }
        });
        this.addButton("Armor", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
//                InventoryCharSelectMenu newMenu = new InventoryCharSelectMenu(
//                    skin,
//                    gameState,
//                    false
//                );
//                newMenu.setCanSellItems(true);
//                setSubMenu(newMenu);
//                openSubMenu(newMenu);
            }
        });
        this.addButton("Potions", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
//                InventoryCharSelectMenu newMenu = new InventoryCharSelectMenu(
//                    skin,
//                    gameState,
//                    false
//                );
//                setSubMenu(newMenu);
//                openSubMenu(newMenu);
            }
        });
        this.pack();

    }
    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(parentMenu != null){
//            float wif = this.getWidth();

//            combatMenu = (CombatMenu)parentMenu;
            setSizeandPosition(GameConstants.SUBMENU_SIZE.SMALL);
        }
        if (stage != null) {

            refreshAndSetActive();
            this.buttonList = populateButtonList();
            this.resetMenuSelection();
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
    public void refreshAndSetActive(){
        addPartyButtons();
        this.addFocusListeners();
        super.refreshAndSetActive();
    }

    public void finishItemOption(){
        returnToParentMenu();
    }
    @Override
    public void openSubMenu(BaseLinearMenu nextMenu){
        super.openSubMenu(nextMenu);
//        this.setVisible(true);
    }
}
