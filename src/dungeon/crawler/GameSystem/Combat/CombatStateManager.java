package dungeon.crawler.GameSystem.Combat;

import dungeon.crawler.GameSystem.Character.EnemyCombatant;

import java.util.Map;

public class CombatStateManager {
    private Map<Integer, EnemyCombatant> currentEnemyRoster;

    public CombatStateManager(Map<Integer, EnemyCombatant> currentEnemyRoster){
        this.currentEnemyRoster = currentEnemyRoster;
    }

    public Map<Integer, EnemyCombatant> getCurrentEnemyRoster() {
        return currentEnemyRoster;
    }
}
