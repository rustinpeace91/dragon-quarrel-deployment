package dungeon.crawler.Menu.Shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.TestData.ItemFactory;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;

import java.util.Arrays;

public class ShopItemTransferOptions extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private final Item selectedItem;
    private final PartyCharacter currentCombatant;


    public ShopItemTransferOptions(
        Skin skin,
        GameState gameState,
        PartyCharacter currentCombatant,
        Item selectedItem
    ){
        super(skin);
        this.gameState = gameState;
        this.selectedItem = selectedItem;
        this.currentCombatant = currentCombatant;
        this.initializeButtons();
    }

    public BaseLinearMenu asCombatMenu(){return this;}


    protected void updateButtons(){

        this.clearChildren();
        String title = selectedItem.getName() + "\n" +
            selectedItem.value + " Gold \n"+
            "Where would you like it to go";

        setTitle(title);
        this.initializeArrow();
        this.addButton("Inventory",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!currentCombatant.enoughSpace()) {
                        showPopup("Inventory completely full", 2f);

                    } else {
                        /* we do this to prevent having the same object references as the shop.
                        Not sure what would happen if we did that. Do not intend on finding out.
                         */
                        Item newItem=ItemUtils.buyItem(gameState, selectedItem);
                        if(newItem == null){
                            showPopup("An error occured purchasing this item", 2f);

                        } else {
                            currentCombatant.addToInventory(selectedItem);
                            showPopup(selectedItem.getName() + " purchased!", 2f);
                            FinishMenuComplete();
                        }

                    }
//                        setSubMenu(nextMenu);
//                        openSubMenu(nextMenu);
                }
            }
        );
        this.addButton("Bag",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!gameState.partyBag.enoughSpace()) {
                        showPopup("Bag completely full", 2f);
                    } else {
                        Item newItem=ItemUtils.buyItem(gameState, selectedItem);
                        if(newItem == null){
                            showPopup("An error occured purchasing this item", 2f);

                        } else {
                            gameState.partyBag.addToInventory(selectedItem);
                            showPopup(selectedItem.getName() + " purchased!", 2f);
                            FinishMenuComplete();
                        }
                    }
//                        setSubMenu(nextMenu);
//                        openSubMenu(nextMenu);
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

    public void finishItemOption(){
        returnToParentMenu();
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
    @Override
    public void FinishMenuComplete(){
        OverworldSubMenu inventoryMenu = (OverworldSubMenu)parentMenu;
        returnToParentMenu();
        inventoryMenu.FinishMenuComplete();
    }
}
