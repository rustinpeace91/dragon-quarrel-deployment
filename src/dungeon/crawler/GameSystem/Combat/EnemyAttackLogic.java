package dungeon.crawler.GameSystem.Combat;

import java.util.Random;

import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.GameState.GameState;

public class EnemyAttackLogic {
    public static CombatAction decideAction(
        EnemyCombatant enemy,
        int id,
        GameState gameState
    ){
        // for now we're just gonna roll some dice
        Random dice = new Random();
        int roll = (dice.nextInt(20) + 1);
        CombatActionState enemyAction;
        if(roll >=5){
            enemyAction = CombatActionState.ATTACK;
        } else {            
            enemyAction = CombatActionState.DEFEND;
        }
        int initiative = enemy.rollInitiative();
        // TODO: Implement logic for selecting a target
        Random rand = new Random();
        int partySize = gameState.party.size(); 
        int randomIndex = rand.nextInt(partySize); 
        Combatant target = gameState.party.get(randomIndex);

        CombatAction newAction = new CombatAction(
            id,
            initiative,
            enemy,
            enemyAction,
            target
        );
        return newAction;

    } 
}
