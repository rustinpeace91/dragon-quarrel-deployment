package dungeon.crawler.GameSystem.Inventory;

import dungeon.crawler.GameSystem.Character.Class.ClassLogic;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.EquipmentSlot;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;

public class Armor extends Item{
    public int defenseBonus;
    public EquipmentSlot slot;
    public ArmorTypes armorType;
    public Armor(){};
    public Armor(
        String name,
        String id,
        int defenseBonus,
        int value,
        ArmorTypes armorType,
        EquipmentSlot slot
    ) {
        super(
            name,
            id,
            value,
            ItemType.ARMOR
        );
        this.defenseBonus = defenseBonus;
        this.slot = slot;
        this.armorType = armorType;
    }


    public boolean canEquip(ClassLogic charClass){
        return charClass.getArmorRestrictions().contains(armorType);
    }

    @Override
    public ItemType returnItemType() {
        return ItemType.ARMOR;
    }
}
