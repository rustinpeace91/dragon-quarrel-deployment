package dungeon.crawler.GameSystem.Inventory;

import dungeon.crawler.GameSystem.Character.Class.ClassLogic;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.Condition;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Combat.Elemental;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.Handed;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes;

import java.util.ArrayList;


public class Weapon extends Item{
    public int toHit;
    public int damageLow;
    public int damageHigh;
    public String flavorTextVerb;
    public boolean ranged;
    public Condition condition;
    public Elemental elemental;
    public WeaponTypes weaponType;
    public Handed handed;
    public Weapon(){};
    public Weapon(
        String name,
        String id,
        int toHit,
        int damageLow,
        int damageHigh,
        String flavorTextVerb,
        boolean ranged,
        Condition condition,
        Elemental elemental,
        int value,
        WeaponTypes weaponType,
        Handed handed
    ) {
        super(
            name,
            id,
            value,
            ItemType.WEAPON
        );
        this.toHit = toHit;
        this.damageLow = damageLow;
        this.damageHigh = damageHigh;
        this.flavorTextVerb = flavorTextVerb;
        this.ranged = ranged;
        this.condition = condition;
        this.elemental = elemental;
        this.weaponType = weaponType;
        this.handed = handed;
    }
//    public ArrayList<CharacterClass> classRestrictions;
//

    public boolean canEquip(ClassLogic charClass){
        return charClass.getWeaponRestrictions().contains(weaponType);
    }

    @Override
    public ItemType returnItemType() {
        return ItemType.WEAPON;
    }

    @Override
    public ArrayList<String> use(Combatant target){
        ArrayList<String> value = new ArrayList();
        value.add("Weapon cannot be used");
        return value;
    }

    public String getAttackDamageString(){
        String attackDamage = String.valueOf(damageLow) + " - " + String.valueOf(damageHigh);
        return attackDamage;
    }

}
