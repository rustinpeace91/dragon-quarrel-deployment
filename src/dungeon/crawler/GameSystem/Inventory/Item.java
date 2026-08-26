package dungeon.crawler.GameSystem.Inventory;

import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Character.Class.ClassLogic;
// import dungeon.crawler.GameSystem.TestData.PlayerCharacter;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;

import java.util.ArrayList;

public class Item implements ItemLogic {
    private final ItemType itemType;
    public String name;
    public int value;
    private String id;

    public Item(){
        itemType = ItemType.QUEST_ITEM;
    };
    public Item(
        String name,
        String id,
        int value,
        ItemType itemType
    ) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.itemType = itemType;
    }
    @Override
    public ItemType returnItemType() {
        // TODO Auto-generated method stub
        return itemType;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPurchaseValue(){
        float subTotal = value * GameConstants.SHOP_MARKUP;
        int finalPrice = (int) Math.round(subTotal);
        return finalPrice;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equippable() {
        ItemType itemType = returnItemType();
        return itemType == ItemType.ARMOR || itemType == ItemType.WEAPON;
    }

    @Override
    public boolean canEquip(ClassLogic charClass) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ArrayList<String> use(Combatant target) {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// TODO: Item logic
// create a factory method for Item types
    // must respect existing weapon logic
    // use method?
    // Items and Weapons: can be added and removed form inventory, purchased, transferred between party members
    // Weapons: can be equipped and unequipped, cannot be used
        // creation logic: needs a bunch of stats
            // maybe have a registry of weapon names and stats decoupled from logic. this is where JSON should come in
    // Items (potions):can be used, cannot be equipped and unequipped
        // creation logic ("potion name", ITEM_TYPE_ENUM, int powerLevel)
// create a registry/repository for items that lazy loads (JSON?)
// create a registry for items by storefront (?maybe next ticket)
// how do we balance potions with weapons?
