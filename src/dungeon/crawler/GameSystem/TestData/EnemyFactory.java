package dungeon.crawler.GameSystem.TestData;

import java.util.ArrayList;

import dungeon.crawler.GameSystem.Character.Condition;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.Character.Stance;
import dungeon.crawler.Data.Enemies.DataInitializer;
import dungeon.crawler.Data.Enemies.EnemyRegistry;
import dungeon.crawler.Data.Enemies.EnemyParams;

public class EnemyFactory {
    // TODO: this cannot be static anymore. Make this an object and
    // put the EnemyRegistry as a property
    private EnemyRegistry registry;

    public EnemyFactory(){
        this.registry = DataInitializer.initializeEnemyData();
    }
    public EnemyCombatant generate() {
        return createEnemyFromID("rat");
    }

    public EnemyCombatant createEnemyFromID(String id){
        // TODO: handle null case
        EnemyParams params = registry.getEnemyById(id);
        return new EnemyCombatant(
            params.getName(),
            params.getId(),
            params.getToHit(),
            params.getEarnedXP(),
            params.getInitiative(),
            params.getMaxHp(),
            params.getMaxMP(),
            params.getHp(),
            params.getMp(),
            params.getDefense(),
            params.getAttackDamage().component1(),
            params.getAttackDamage().component2(),
            params.getAttackText(),
            Stance.STANDING,
            new ArrayList<Condition>(), 
            false
        );
    }
    
}
