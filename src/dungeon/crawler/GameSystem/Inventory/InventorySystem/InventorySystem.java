package dungeon.crawler.GameSystem.Inventory.InventorySystem;

import dungeon.crawler.GameConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dungeon.crawler.GameSystem.Character.Inventory;
import dungeon.crawler.GameSystem.Inventory.Item;

public class InventorySystem{
    //
    public ArrayList<Item> inventoryList;
    private final int maxRoom;

    public InventorySystem(){
        this.maxRoom = GameConstants.MAX_PLAYER_INVENTORY_SPACE;
    };
    public InventorySystem(
        ArrayList<Item> inventoryList,
        int maxRoom
    ) {
        this.inventoryList = inventoryList;
        this.maxRoom = maxRoom;
    }

    public boolean enoughSpace(){
        if(inventoryList.size() >= maxRoom) {
            return false;
        }
        return true;
    }

    public void addToInventory(Item item){
        inventoryList.add(item);
    }

    public void removeFromInventory(Item item){
        inventoryList.remove(item);
    }

    public ArrayList<Item> getInventoryList() {
        // TODO: Sort by type, then alphabetically
        return new ArrayList<>(this.inventoryList);

    }


}
