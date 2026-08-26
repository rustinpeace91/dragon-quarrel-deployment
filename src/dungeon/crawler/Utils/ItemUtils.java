package dungeon.crawler.Utils;

import com.badlogic.gdx.Gdx;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Bag;
import dungeon.crawler.GameSystem.Character.Inventory;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Armor;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.GameSystem.Inventory.Weapon;
import dungeon.crawler.GameSystem.TestData.ItemFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemUtils {
    public static String getItemName(Item item){
        if(item == null){
            return "None";
        }
        return item.getName();
    }

    public static boolean canTransferItem(PartyCharacter sender, PartyCharacter reciever, Item item){
        return reciever.inventory.enoughSpace();
    }

    public static boolean anySpace(PartyCharacter sender, Bag bag){
        if(sender.inventory.enoughSpace() || bag.enoughSpace()){
            return true;
        }
        return false;
    }


    public static boolean enoughGold(GameState gamestate, Item selectedItem){
        if(gamestate.gold < Math.round(selectedItem.value * GameConstants.SHOP_MARKUP)){
            return false;
        };
        return true;
    }

    public static int getStorePrice(Item selectedItem){
        return Math.round(selectedItem.value * GameConstants.SHOP_MARKUP);
    }

    public static Item buyItem(GameState gamestate, Item selectedItem){
        int price = Math.round(selectedItem.value * GameConstants.SHOP_MARKUP);
        if(gamestate.gold > price){
            gamestate.removeGold(price);
        };
        ItemFactory factory = new ItemFactory();
        Item newItem = factory.createItemById(selectedItem.getId());
        if(newItem == null){
            gamestate.addGold(price);
            Gdx.app.log("INVENTORY", "ERROR: An item was requested that does not exist in the registry");
            return null;
        }
        return newItem;
    }

    public static void transferItem(Inventory sender, Inventory reciever, Item item){
        sender.removeFromInventory(item);
        reciever.addToInventory(item);
    }

    public static void useItem(PartyCharacter user, Item item){
        item.use(user);
        user.removeFromInventory(item);
        // TODO: handle strings and other stuff here
    }


    public static void equipItem(PartyCharacter user, Item item){
        item.use(user);
        user.equip(item);
    }

    public static void unEquipItem(PartyCharacter user, Item item){
        item.use(user);
        user.unEquip(item);
    }

    public static ArrayList<Item>  returnItemsByType(ArrayList<Item> items, ItemType type){
        ArrayList<Item> storeItems = new ArrayList<Item>();
        for(Item item: items){
            if(item.returnItemType() == type){
                storeItems.add(item);
            }
        }
        return storeItems;
    }


    public static String itemStats(PartyCharacter user, Item item){
        ItemType type = item.returnItemType();
        String statString = "";
        if(Arrays.asList(GameConstants.EQUIPPABLE_ITEMS).contains(type)){
            if(type == ItemType.WEAPON){
                Weapon newWeapon = (Weapon)item;
                Weapon currentWeapon = user.getWeapon();
                statString = "Damage: " + newWeapon.getAttackDamageString() + "\n" +
                    "Current: " + currentWeapon.getAttackDamageString();


            } else if(type == ItemType.ARMOR){
                Armor newArmor = (Armor)item;

                Item equippedItem = user.equipment.getItemBySlot(newArmor.slot);
                if(equippedItem.returnItemType() == ItemType.ARMOR){
                    Armor currentArmor = (Armor)equippedItem;
                    statString = "Protection: " + String.valueOf(newArmor.defenseBonus) + "\n" +
                        "Current: " + String.valueOf(currentArmor.defenseBonus);
                }
            }
        }
        return statString;
    }
}
