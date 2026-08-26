package dungeon.crawler.GameSystem.Combat;

import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.GameState.GameState;

import java.util.ArrayList;
import java.util.List;

/*
This class is to keep track of which player party characters are able to act for selecting actions
The tracker is referenced by the Menu and the Combat Logic. The menu only needs to know the current
combatant's ID. All turn advance logic will be handled by the Combat Logic handler. This way
the menu does not need to concern itself with that.

 */
public class PartyActionTracker {
    private GameState gameState;
    private ArrayList<Integer> partyIDs;
    private int currentCombatantIndex;


    public PartyActionTracker(GameState gameState) {
        this.gameState = gameState;
        this.currentCombatantIndex = 0;
        this.partyIDs = new ArrayList<>(CombatUtils.returnCombatElegableCombatants(gameState.party).keySet());
    }

    public Combatant getCurrentCombatant(){
        return gameState.party.get(this.partyIDs.get(currentCombatantIndex));
    }

    public int getCurrentCombatantID(){
        return partyIDs.get(currentCombatantIndex);
    }

    public Boolean nextEligibleCombatant(){
        while(currentCombatantIndex + 1 < partyIDs.size()){
            Combatant currentCombatant = getCurrentCombatant();
            if(currentCombatant.canAttack()){
                currentCombatantIndex = currentCombatantIndex + 1;
                return true;
            } else {
                currentCombatantIndex = currentCombatantIndex + 1;
            }
        }
        return false;
    }

    public void resetTracker(){
        currentCombatantIndex = 0;
        partyIDs = new ArrayList<>(CombatUtils.returnCombatElegableCombatants(gameState.party).keySet());
    }
}
