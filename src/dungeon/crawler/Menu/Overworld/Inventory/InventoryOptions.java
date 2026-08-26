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
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.ItemUtils;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryOptions extends BaseLinearMenu implements OverworldSubMenu {
    private final GameState gameState;
    private final PartyCharacter currentCombatant;
    private final Item selectedItem;
    private final boolean canSellItems;


    public InventoryOptions(
        Skin skin,
        GameState gameState,
        PartyCharacter currentCombatant,
        Item selectedItem,
        boolean canSellItems
    ){
        super(skin);
        this.gameState = gameState;
        this.currentCombatant = currentCombatant;
        this.selectedItem = selectedItem;
        this.canSellItems = canSellItems;
        this.initializeButtons();
    }

    public BaseLinearMenu asCombatMenu(){return this;}


    protected void updateButtons(){

        this.clearChildren();
        String title = selectedItem.getName() + "\n" + ItemUtils.itemStats(
            currentCombatant,
            selectedItem
        );
        if(canSellItems){
            title = title + "\n" + selectedItem.value + " Gold";
        }
        setTitle(title);
        this.initializeArrow();
        if(canSellItems){

            this.addButton("Sell",
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ItemUtils.useItem(currentCombatant, selectedItem);
                        gameState.addGold(selectedItem.value);
                        // unequip is safe. returns blank if not equippable
                        currentCombatant.unEquip(selectedItem);
                        currentCombatant.removeFromInventory(selectedItem);
                        showPopup(StringUtils.format(
                            "%s sold a %s for %s gold",
                            currentCombatant.getName(),
                            selectedItem.getName(),
                            String.valueOf(selectedItem.value)
                        ), 1.5f);
                        FinishMenuComplete();
                    }
                }
            );
        }

        this.addButton("Transfer",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                        BaseLinearMenu nextMenu = new InventoryTransferMenu(
                            skin,
                            gameState,
                            currentCombatant,
                            selectedItem
                        );
                        setSubMenu(nextMenu);
                        openSubMenu(nextMenu);
                }
            }
        );
        if(Arrays.asList(GameConstants.USABLE_ITEMS).contains(selectedItem.returnItemType())){
            this.addButton("Use",
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ItemUtils.useItem(currentCombatant, selectedItem);
                        showPopup(StringUtils.format(
                            "%s used a %s",
                            currentCombatant.getName(), selectedItem.getName()
                        ), 1f);
                        FinishMenuComplete();
//                        BaseLinearMenu nextMenu = new InventoryTransferMenu(
//                            skin,
//                            gameState,
//                            currentCombatant,
//                            selectedItem
//                        );
//                        setSubMenu(nextMenu);
//                        openSubMenu(nextMenu);
                    }
                }
            );
        }
        if(Arrays.asList(GameConstants.EQUIPPABLE_ITEMS).contains(selectedItem.returnItemType())){
            if(currentCombatant.equipment.isEquipped(selectedItem)){
                this.addButton("Unequip",
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            ItemUtils.unEquipItem(currentCombatant, selectedItem);
                            showPopup(StringUtils.format(
                                "%s Removed the %s",
                                currentCombatant.getName(), selectedItem.getName()
                            ), 1f);
                            FinishMenuComplete();
                        }
                    }
                );
            } else {
                this.addButton("Equip",
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if(selectedItem.canEquip(currentCombatant.charClass)){
                                ItemUtils.equipItem(currentCombatant, selectedItem);
                                showPopup(StringUtils.format(
                                    "%s equiped a %s",
                                    currentCombatant.getName(), selectedItem.getName()
                                ), 1f);
                            } else {
                                showPopup(StringUtils.format(
                                    "%ss cannot equip a %s",
                                    currentCombatant.charClass.getName(), selectedItem.getName()
                                ), 2f);
                            }

                            FinishMenuComplete();
                        }
                    }
                );
            }

        }

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

    @Override
    public void FinishMenuComplete(){
        InventoryMenu inventoryMenu = (InventoryMenu)parentMenu;
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
