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
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;

import java.util.Arrays;

public class ShopItemPurchaseMenu extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private final Item selectedItem;
    private final PartyCharacter currentCombatant;


    public ShopItemPurchaseMenu(
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
            ItemUtils.getStorePrice(selectedItem);

        setTitle(title);
        this.initializeArrow();

        this.addButton("Buy",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!ItemUtils.anySpace(currentCombatant, gameState.partyBag)) {
                        showPopup("Inventory and bag completely full", 2f);

                    } else if(!ItemUtils.enoughGold(gameState, selectedItem)) {
                        showPopup("Not enough gold for this item", 2f);
                    } else {
                        BaseLinearMenu nextMenu = new ShopItemTransferOptions(
                            skin,
                            gameState,
                            currentCombatant,
                            selectedItem
                        );
                        setSubMenu(nextMenu);
                        openSubMenu(nextMenu);
                    }

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
//        OverworldSubMenu inventoryMenu = (OverworldSubMenu)parentMenu;
        returnToParentMenu();
//        inventoryMenu.FinishMenuComplete();
    }

    public void handleUseAction(Item item, int targetId){
        returnToParentMenu();
    }
}
