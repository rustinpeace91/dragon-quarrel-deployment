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
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryBagOptionsMenu extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private Bag bag;
    private final Item selectedItem;


    public InventoryBagOptionsMenu(
        Skin skin,
        GameState gameState,
        Bag bag,
        Item selectedItem
    ){
        super(skin);
        this.gameState = gameState;
        this.bag = bag;
        this.selectedItem = selectedItem;
        this.initializeButtons();
    }

    public BaseLinearMenu asCombatMenu(){return this;}


    protected void updateButtons(){

        this.clearChildren();
        String title = selectedItem.getName();
        setTitle(title);
        this.initializeArrow();

        this.addButton("Transfer",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    BaseLinearMenu nextMenu = new InventoryTransferMenu(
                        skin,
                        gameState,
                        bag,
                        selectedItem
                    );
                    setSubMenu(nextMenu);
                    openSubMenu(nextMenu);
                }
            }
        );

        if(getStage() == null) {
            Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
        }
        setVisible(true);

        if (parentMenu != null) {
            refreshAndSetActive();
        }

    }
    protected void initializeButtons(){

        updateButtons();

    }

    public void FinishMenuComplete(){
        InventoryBagMenu inventoryMenu = (InventoryBagMenu)parentMenu;
        returnToParentMenu();
        inventoryMenu.finishItemOption();
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
