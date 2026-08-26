package dungeon.crawler.GameSystem.Inventory.EquipmentSystem;

import dungeon.crawler.GameSystem.Inventory.Armor;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.EquipmentSlot;
import dungeon.crawler.GameSystem.Inventory.Weapon;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;

import java.util.HashMap;
import java.util.Map;

public class EquipmentSystem {
    private Weapon rightHand;
    private Armor leftHand;
    private Armor head;
    private Armor body;
    private Armor feet;

    public EquipmentSystem(){

    }

    public EquipmentSystem(
        Weapon rightHand,
        Armor leftHand,
        Armor head,
        Armor body,
        Armor feet
    ) {
        this.rightHand = rightHand;
        this.leftHand = leftHand;
        this.head = head;
        this.body = body;
        this.feet = feet;
    }

    public void equipItem(Item item){
        if(item.returnItemType() == ItemType.WEAPON){
            Weapon mainWeapon = (Weapon) item;
            rightHand = mainWeapon;
            // equipItem
        } else if (item.returnItemType() == ItemType.ARMOR){
            // consider using visitor pattern if this gets more complicated
            // item.equip(this)
            // item knows which slot it belongs to and chooses method
            Armor equippableArmor = (Armor) item;
            switch(equippableArmor.slot){
                case LEFT_HAND:
                    leftHand = equippableArmor;
                case HEAD:
                    head = equippableArmor;
                case BODY:
                    body = equippableArmor;
                case FEET:
                    feet = equippableArmor;
                default:
                    throw new IllegalArgumentException("Unknown Equipment Slot");
            }
        } else {
            throw new IllegalArgumentException("Unknown Item Type");
        }

    }

    public void unEquipItem(Item item){
        if(!isEquipped(item)){
            return;
        }
        if(item.returnItemType() == ItemType.WEAPON){
            Weapon mainWeapon = (Weapon) item;
            rightHand = null;
            // equipItem
        } else if (item.returnItemType() == ItemType.ARMOR){
            // consider using visitor pattern if this gets more complicated
            // item.equip(this)
            // item knows which slot it belongs to and chooses method
            Armor equippableArmor = (Armor) item;
            switch(equippableArmor.slot){
                case LEFT_HAND:
                    leftHand = null;
                case HEAD:
                    head = null;
                case BODY:
                    body = null;
                case FEET:
                    feet = null;
                default:
                    throw new IllegalArgumentException("Unknown Equipment Slot");
            }
        } else {
            throw new IllegalArgumentException("Unknown Item Type");
        }

    }

    public Weapon getWeapon(){
        return rightHand;
    }

    public Weapon getRightHand(){
        return rightHand;
    }
    public Armor getLeftHand(){
        return leftHand;
    }
    public Armor getHead(){
        return head;
    }
    public Armor getBody(){
        return body;
    }
    public Armor getFeet(){
        return feet;
    }

    public boolean isEquipped(Item item){
        if(!(
                item.returnItemType() == ItemType.ARMOR ||
                item.returnItemType() == ItemType.WEAPON
            )
        ){
            return false;
        }
        Item[] equipslots = {rightHand, leftHand, body, feet, head};

        for(Item e: equipslots){
            if(e == item){
                return true;
            }
        }
        return false;
    }

    public int getDefenseBonus(){

        Armor[] equipslots = {leftHand, body, feet, head};
        int bonus = 0;
        for(Armor e: equipslots){
            if(e != null) {
                bonus = bonus + e.defenseBonus;
            }
        }
        return bonus;
    }

    public Item getItemBySlot(EquipmentSlot slot){
        switch(slot){
            case RIGHT_HAND:
                return rightHand;
            case LEFT_HAND:
                return leftHand;
            case HEAD:
                return head;
            case BODY:
                return body;
            case FEET:
                return feet;
            default:
                throw new IllegalArgumentException("Unknown Equipment Slot");
        }

    }

    public Map<EquipmentSlot, Item> getEquippedItemsMap() {
        /* get equipped items as a hashmap (for savegames) */
        Map<EquipmentSlot, Item> equipped = new HashMap<>();

        equipped.put(EquipmentSlot.RIGHT_HAND, rightHand);
        equipped.put(EquipmentSlot.LEFT_HAND, leftHand);
        equipped.put(EquipmentSlot.HEAD, head);
        equipped.put(EquipmentSlot.BODY, body);
        equipped.put(EquipmentSlot.FEET, feet);

        return equipped;
    }


}
