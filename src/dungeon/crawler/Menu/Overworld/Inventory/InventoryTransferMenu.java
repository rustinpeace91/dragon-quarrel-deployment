package dungeon.crawler.Menu.Overworld.Inventory;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.Inventory;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
import java.util.Map;

public class InventoryTransferMenu extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private final Inventory currentCombatant;
    private final Item selectedItem;



    public InventoryTransferMenu(
        Skin skin,
        GameState gameState,
        Inventory currentCombatant,
        Item selectedItem
    ){
        super(skin);
        this.gameState = gameState;
        this.currentCombatant = currentCombatant;
        this.selectedItem = selectedItem;
        this.initializeButtons();
    }

    public BaseLinearMenu asCombatMenu(){return this;}


    protected void updateButtons(){

        this.clearChildren();
        setTitle(StringUtils.format("%s's Inventory", currentCombatant.getName()));
        this.initializeArrow();


        for (Map.Entry<Integer, PartyCharacter> entry : gameState.party.entrySet()) {
            Integer id = entry.getKey();
            // It's OK to typecast here since we control the input of the util function
            PartyCharacter c = (PartyCharacter) entry.getValue();
            if (entry.getValue().getName() != currentCombatant.getName()) {
                this.addButton(c.getName(),
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if(c.enoughSpace()){
                                ItemUtils.transferItem(currentCombatant, c, selectedItem);
                                showPopup("Successfully transferred item", 1f);
                            } else {
                                showPopup(StringUtils.format(
                                    "%s inventory is full",
                                    c.getName()
                                ), 1f);
                            }
                            // TODO! go back to Inventory Menu
                            FinishMenuComplete();
//                            itemMenu.handleUseAction(selectedItem, id);
                        }
                    }
                );
            }

        }

        if(currentCombatant.getName() != "Bag"){
            this.addButton(gameState.partyBag.getName(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if(gameState.partyBag.enoughSpace()){
                            ItemUtils.transferItem(currentCombatant, gameState.partyBag, selectedItem);
                            showPopup("Successfully transferred item", 1f);
                        } else {
                            showPopup(StringUtils.format(
                                "%s inventory is full",
                                gameState.partyBag.getName()
                            ), 1f);
                        }
                        // TODO! go back to Inventory Menu
                        FinishMenuComplete();
//                            itemMenu.handleUseAction(selectedItem, id);
                    }
                }
            );
        }

//        if(selectedItem.returnItemType() == Item)
//            if(getStage() == null) {
//                Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
//                // no return. Let it break the game
//            }
        setVisible(true);

        if (parentMenu != null) {
            refreshAndSetActive();
        }

    }
    protected void initializeButtons(){

        updateButtons();

    }

    @Override
    public void FinishMenuComplete(){
        OverworldSubMenu inventoryMenu = (OverworldSubMenu)parentMenu;
        returnToParentMenu();
        inventoryMenu.FinishMenuComplete();
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
