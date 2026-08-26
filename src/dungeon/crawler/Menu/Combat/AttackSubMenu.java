package dungeon.crawler.Menu.Combat;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Combat.CombatStateManager;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.CombatSubMenu;

public class AttackSubMenu extends BaseLinearMenu implements CombatSubMenu {

    private final CombatStateManager combatState;
    private GameState gameState;
    private CombatMenu combatMenu;
    public AttackSubMenu(
        Skin skin,
        GameState gameState,
        CombatStateManager combatState

    ){
        super(skin);
        this.gameState = gameState;
        this.combatState = combatState;
    }

    @Override
    public BaseLinearMenu asCombatMenu(){return this;}

    protected void attackButtons(){

        // redundant for loops here are fine. There won't be more than 5 enemies max
        Map<Integer, Combatant> availableCombatants = CombatUtils.returnAliveCombatants(
            this.combatState.getCurrentEnemyRoster()
        );

        for (Map.Entry<Integer, Combatant> entry : availableCombatants.entrySet()) {
            Integer id = entry.getKey();
            Combatant c = entry.getValue();
            this.addButton(c.getName(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        returnToParentMenu();
                        combatMenu.handleAttackSelection(id);
                    }
                }
            );

        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(parentMenu != null){
            combatMenu = (CombatMenu)parentMenu;


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

    // spawn menu
    // take in GameState as parametera
    // spin up button for each enemy able to attack (make extra function for that?)
    // CombatUtils.returnCombatElegableCombatants(this.gameState.enemyRoster);
    // on button Press, notify CombatMenu of attack selection

}
