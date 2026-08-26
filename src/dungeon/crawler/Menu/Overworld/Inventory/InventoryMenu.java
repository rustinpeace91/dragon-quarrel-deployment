package dungeon.crawler.Menu.Overworld.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Combat.CombatMenu;
import dungeon.crawler.Menu.Combat.Inventory.ItemSelectMenu;
import dungeon.crawler.Menu.Combat.Inventory.ItemTargetSelectMenu;
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Menu.ScrollableLinearMenu;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;

public class InventoryMenu extends ScrollableLinearMenu<Item>  implements OverworldSubMenu {

    private GameState gameState;
    private PartyCharacter currentCombatant;
    private ArrayList<Item> availableItems;
    private final StatusMenuObserver statusMenuObserver;
    private final boolean canSellItems;

    public BaseLinearMenu asCombatMenu(){return this;}

    public InventoryMenu(
        Skin skin,
        GameState gameState,
        PartyCharacter currentCombatant,
        ArrayList<Item> availableItems,
        StatusMenuObserver statusMenuObserver,
        boolean canSellItems
    ){
        super(skin);
        this.gameState = gameState;
        this.currentCombatant = currentCombatant;
        this.availableItems = availableItems;
        this.statusMenuObserver = statusMenuObserver;
        this.canSellItems = canSellItems;

        this.initializeButtons();
    }



    protected void updateButtons(){
//        scrollableResetDefaults();
        this.clearChildren();
        setTitle(StringUtils.format("%s's Inventory", currentCombatant.getName()));
        this.initializeArrow();
        addScrollArrowUp();
        for (int i=pageStart; i < pageEnd; i++) {
            Item item = availableItems.get(i);
            String buttonName = item.name;
            if(currentCombatant.equipment.isEquipped(item)){
                buttonName = "(E) " + item.name;
            }

            this.addButton(buttonName,
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BaseLinearMenu nextMenu = new InventoryOptions(
                            skin,
                            gameState,
                            currentCombatant,
                            item,
                            canSellItems
                        );
                        setSubMenu(nextMenu);
                        openSubMenu(nextMenu);
                    }
                }
            );
        }
        addBackButton();
        if(getStage() == null) {
            Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
            // no return. Let it break the game
        }
        setVisible(true);

        addScrollArrowDown();
        if (parentMenu != null) {
            refreshAndSetActive();
        }

    }
    protected void initializeButtons(){
        availableItems = currentCombatant.inventory.getInventoryList();

        this.intializeItems(availableItems);
        updateButtons();

    }

    public void finishItemOption(){
        this.initializeButtons();
        updateButtons();
        statusMenuObserver.refreshObservers();
    }

    @Override
    protected void setStage(Stage stage) {

        super.setStage(stage);

        if(parentMenu != null){
//            float wif = this.getWidth();

//            combatMenu = (CombatMenu)parentMenu;
            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
        }

        if (stage != null) {
            refreshAndSetActive();
        }
    }

    public void handleUseAction(Item item, int targetId){
        returnToParentMenu();
//        combatMenu.handleItemAction(item, targetId);
    }
}
