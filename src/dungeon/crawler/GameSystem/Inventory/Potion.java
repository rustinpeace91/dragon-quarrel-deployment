package dungeon.crawler.GameSystem.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.Condition;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;

public class Potion extends Item{
    public int level;
    public Condition cureStatus;
    public Potion(){};
    public Potion(
        String name,
        String id,
        int value,
        ItemType itemType,
        int level,
        Condition cureStatus
    ) {
        super(name, id, value, itemType);
        this.level = level;
        this.cureStatus = cureStatus;
    }



    @Override
    public ArrayList<String> use(Combatant target) {
        /* handles both healing and curing status. Cure potions will have a level of 0, canceling out healing.
        Healing potions have a cureStatus of null.  Potions can also do both of need be.
         */
        ArrayList<String> messages = new ArrayList<String>();

        if (target == null){
            Gdx.app.log("Potion", "Error: Potion was called without a target");
            return null;
        }

        if(cureStatus != null){
            target.removeCondition(cureStatus);
            messages.add(StringUtils.format("%s has been cured!", target.getName()));
        }

        int healingAmount = MathUtils.random(1,10) * level;
        if(healingAmount > 0){
            messages.add(StringUtils.format("%s healed for %s points", target.getName(), String.valueOf(healingAmount)));
            target.heal(healingAmount);
        }
        return messages;
    }

}
