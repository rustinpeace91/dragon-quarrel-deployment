package dungeon.crawler.GameSystem.Combat;

import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.Inventory.Item;

public class CombatAction {
    public Combatant combatant;
    public CombatActionState action;
    public int combatantID;
    public int iniative;
    public Combatant target;
    public SpellNames spell;
    public Item item;
    public CombatAction(
        int id,
        int initiative,
        Combatant combatant,
        CombatActionState action
    ){
        this.combatantID = id;
        this.iniative = initiative;
        this.combatant = combatant;
        this.action = action;
    }
    public CombatAction(
        int id,
        int initiative,
        Combatant combatant,
        CombatActionState action,
        Combatant target
    ) {
        this.combatant = combatant;
        this.action = action;
        this.target = target;
        this.iniative = initiative;
    }
    public CombatAction(
        int id,
        int initiative,
        Combatant combatant,
        CombatActionState action,
        Combatant target,
        SpellNames spell
    ) {
        this.combatant = combatant;
        this.action = action;
        this.target = target;
        this.iniative = initiative;
        this.spell = spell;
    }

    public CombatAction(
        int id,
        int initiative,
        Combatant combatant,
        CombatActionState action,
        SpellNames spell
    ){
        this.combatant = combatant;
        this.action = action;
        this.iniative = initiative;
        this.spell = spell;
    }

    public CombatAction(
        int id,
        int initiative,
        Combatant combatant,
        CombatActionState action,
        Combatant target,
        Item item
    ){
        this.combatant = combatant;
        this.action = action;
        this.target = target;
        this.iniative = initiative;
        this.item = item;
    }
}
