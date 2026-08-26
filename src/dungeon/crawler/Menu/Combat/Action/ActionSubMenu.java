package dungeon.crawler.Menu.Combat.Action;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Combat.CombatAction;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Combat.CombatMenu;
import dungeon.crawler.Menu.CombatSubMenu;

public class ActionSubMenu extends BaseLinearMenu implements CombatSubMenu {

    private GameState gameState;
    private CombatMenu combatMenu;
    private PartyCharacter currentCombatant;

    public ActionSubMenu(
        Skin skin,
        GameState gameState,
        PartyCharacter currentCombatant

    ){
        super(skin);
        this.gameState = gameState;
        this.currentCombatant = currentCombatant;
        this.attackButtons();
    }

    @Override
    public BaseLinearMenu asCombatMenu(){return this;}


    protected void attackButtons(){
        this.defaults().size(190f, 60f).pad(5f);

        // redundant for loops here are fine. There won't be more than 5 enemies max
        ArrayList<CombatActionState> entryavailableActions = CombatUtils.returnAvailableActions(
            currentCombatant
        );

        for (CombatActionState entry : entryavailableActions) {
            String buttonName = entry.toString();
            this.addButton(buttonName,
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        returnToParentMenu();
                        combatMenu.handleActionSelection(entry);
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
