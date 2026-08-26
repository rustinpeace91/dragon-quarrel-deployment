package dungeon.crawler.Menu.Overworld.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Bag;
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

public class InventoryBagMenu extends ScrollableLinearMenu<Item>  implements OverworldSubMenu {

    private GameState gameState;
    private ArrayList<Item> availableItems;
    private final StatusMenuObserver statusMenuObserver;
    private Bag bag;
    public BaseLinearMenu asCombatMenu(){return this;}

    public InventoryBagMenu(
        Skin skin,
        GameState gameState,
        Bag bag,
        ArrayList<Item> availableItems,
        StatusMenuObserver statusMenuObserver
    ){
        super(skin);
        this.gameState = gameState;
        this.bag = bag;
        this.availableItems = availableItems;
        this.statusMenuObserver = statusMenuObserver;

        this.initializeButtons();
    }



    protected void updateButtons(){
//        scrollableResetDefaults();
        this.clearChildren();
        setTitle(StringUtils.format("Bag"));
        this.initializeArrow();
        addScrollArrowUp();
        for (int i=pageStart; i < pageEnd; i++) {
            Item item = availableItems.get(i);
            String buttonName = item.name;

            this.addButton(buttonName,
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BaseLinearMenu nextMenu = new InventoryBagOptionsMenu(
                            skin,
                            gameState,
                            bag,
                            item
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
        availableItems = bag.inventory.getInventoryList();

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

            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
        }

        if (stage != null) {
            refreshAndSetActive();
        }
    }

    public void handleUseAction(Item item, int targetId){
        returnToParentMenu();
    }
}
