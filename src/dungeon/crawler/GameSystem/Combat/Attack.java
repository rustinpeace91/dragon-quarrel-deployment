package dungeon.crawler.GameSystem.Combat;

import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.Utils.StringUtils;
// so we do not need to repeat ourselves for magic attacks
public class Attack {
    public static int handleDamage(Combatant attacker, Combatant defender, AttackDamage damage){
        int defense = defender.defend(damage);
        int damageDealt = 0;
        if(damage.toHit > defense){
            damageDealt = defender.takeHit(damage);

        }
        return damageDealt;
    }
}
