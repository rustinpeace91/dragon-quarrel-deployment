package dungeon.crawler.GameSystem.Combat;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Character.Stance;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.Inventory.Item;

public class CombatUtils {

    public static TreeMap<Integer, Combatant> returnCombatElegableCombatants(
        Map<Integer, ? extends Combatant> combatantMap
    ) {
        TreeMap<Integer, Combatant> filteredCombatants = new TreeMap<>();
        for (Map.Entry<Integer, ? extends Combatant> combatant : combatantMap.entrySet()) {
            if (combatant.getValue().canAttack()) {
                filteredCombatants.put(combatant.getKey(), combatant.getValue());
            }
        }
        return filteredCombatants;
    }

    public static ArrayList<Combatant> orderedEligebileCombatants(
        Map<Integer, Combatant> combatantMap
    ){
        TreeMap<Integer, Combatant> eligableCombatants = returnCombatElegableCombatants(combatantMap);
        return new ArrayList<Combatant>(eligableCombatants.values());
    }

    public static Map<Integer, Combatant> returnAliveCombatants(
        Map<Integer, ? extends Combatant> combatantMap
    ) {
        Map<Integer, Combatant> filteredCombatants = new HashMap<>();
        for (Map.Entry<Integer, ? extends Combatant> combatant : combatantMap.entrySet()) {
            if (!combatant.getValue().checkDeath()) {
                filteredCombatants.put(combatant.getKey(), combatant.getValue());
            }
        }
        return filteredCombatants;
    }

    public static Map<Integer, Combatant> returnDeadCombatants(
        Map<Integer, ? extends Combatant> combatantMap
    ) {
        Map<Integer, Combatant> filteredCombatants = new HashMap<>();
        for (Map.Entry<Integer, ? extends Combatant> combatant : combatantMap.entrySet()) {
            if (combatant.getValue().checkDeath()) {
                filteredCombatants.put(combatant.getKey(), combatant.getValue());
            }
        }
        return filteredCombatants;
    }

    public static Map<Integer, Combatant> returnItemUseCombatants(
        Map<Integer, ? extends Combatant> combatantMap, Item item
    ) {
        Map<Integer, Combatant> filteredCombatants = new HashMap<>();
        for (Map.Entry<Integer, ? extends Combatant> combatant : combatantMap.entrySet()) {
            if (
                !combatant.getValue().checkDeath() &&
                combatant.getValue().getStance() != Stance.COVER &&
                combatant.getValue().getStance() != Stance.FLYING
            ) {
                filteredCombatants.put(combatant.getKey(), combatant.getValue());
            }
        }
        return filteredCombatants;
    }

    public static int returnPartyAgility(
        Map<Integer, PartyCharacter> partyMap
    ){
        int total = 0;
        for (Map.Entry<Integer, PartyCharacter> combatant : partyMap.entrySet()) {
            total = total + combatant.getValue().agility;
        }
        return total;
    }

    public static ArrayList<CombatActionState> returnAvailableActions(
        PartyCharacter currentCombatant
    ){
        ArrayList<CombatActionState> actions = new ArrayList<>();
        actions.add(CombatActionState.RUN);
        if(
            // implement a canTakeCover method
            (currentCombatant.charClass.getName() == "ranger" ||
            currentCombatant.charClass.getName() == "thief") &&
            currentCombatant.equipment.getWeapon().ranged &&
            currentCombatant.getStance() != Stance.COVER
        ){
            actions.add(CombatActionState.TAKE_COVER);
        }
        if(currentCombatant.getStance() == Stance.PRONE){
            actions.add(CombatActionState.STAND);
        }
        if(currentCombatant.getStance() == Stance.COVER){
            actions.add(CombatActionState.LEAVE_COVER);
        }

        return actions;

    }

//    public static int returnCombinedEnemyLevel(
//        Map<Integer, EnemyCombatant> EnemyMap
//
//    )

}
