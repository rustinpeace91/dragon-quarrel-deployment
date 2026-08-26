package dungeon.crawler.Menu.Combat.Magic;


import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import dungeon.crawler.Data.Spells.Spell;
import dungeon.crawler.Data.Spells.SpellType;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Combat.CombatStateManager;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Combat.CombatMenu;
import dungeon.crawler.Menu.CombatSubMenu;

public class SpellTargetSelectMenu extends BaseLinearMenu implements CombatSubMenu {

    private final CombatStateManager combatState;
    private GameState gameState;
    private SpellSelectMenu spellMenu;
    private Spell selectedSpell;
    public SpellTargetSelectMenu(
        Skin skin,
        GameState gameState,
        Spell spell,
        SpellSelectMenu spellMenu,
        CombatStateManager combatState
    ){
        super(skin);
        this.gameState = gameState;
        this.selectedSpell = spell;
        this.spellMenu = spellMenu;
        this.combatState = combatState;
        this.attackButtons();

    }

    @Override
    public BaseLinearMenu asCombatMenu(){return this;}

    protected void attackButtons(){

        Map<Integer, Combatant> availableCombatants;
        if(
            selectedSpell.getType() == SpellType.SINGLE_OFFENSE
        ){

            availableCombatants = CombatUtils.returnAliveCombatants(
                combatState.getCurrentEnemyRoster()
            );
        } else if(
            selectedSpell.getType() == SpellType.RESURRECTION
        ){
            availableCombatants = CombatUtils.returnDeadCombatants(
                this.gameState.party
            );
        } else {
            availableCombatants = CombatUtils.returnAliveCombatants(
                this.gameState.party
            );
        }


        for (Map.Entry<Integer, Combatant> entry : availableCombatants.entrySet()) {
            Integer id = entry.getKey();
            Combatant c = entry.getValue();
            this.addButton(c.getName(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        returnToParentMenu();
                        spellMenu.handleCastAction(selectedSpell.getId(), id);
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
