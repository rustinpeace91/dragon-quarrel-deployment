package dungeon.crawler.Menu.Combat.Inventory;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dungeon.crawler.Data.Spells.Spell;
import dungeon.crawler.Data.Spells.SpellType;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Combat.CombatMenu;
import dungeon.crawler.Menu.CombatSubMenu;

import java.util.Map;

public class ItemTargetSelectMenu extends BaseLinearMenu implements CombatSubMenu {

    private GameState gameState;
    private ItemSelectMenu itemMenu;
    private Item selectedItem;
    public ItemTargetSelectMenu(
        Skin skin,
        GameState gameState,
        Item item,
        ItemSelectMenu itemMenu
    ){
        super(skin);
        this.gameState = gameState;
        this.selectedItem = item;
        this.attackButtons();
        this.itemMenu = itemMenu;

    }

    public BaseLinearMenu asCombatMenu(){return this;}

    protected void attackButtons(){

        Map<Integer, Combatant> availableCombatants = CombatUtils.returnItemUseCombatants(this.gameState.party, selectedItem);

        for (Map.Entry<Integer, Combatant> entry : availableCombatants.entrySet()) {
            Integer id = entry.getKey();
            Combatant c = entry.getValue();
            this.addButton(c.getName(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        returnToParentMenu();
                        itemMenu.handleUseAction(selectedItem, id);
                    }
                }
            );

        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(parentMenu != null){


            setDefaults();
            this.clearChildren();
            this.initializeArrow();
            this.attackButtons();
            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);


        }
        if (stage != null) {
            refreshAndSetActive();
        }
    }


}
