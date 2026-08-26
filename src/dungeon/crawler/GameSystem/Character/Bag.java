package dungeon.crawler.GameSystem.Character;

import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Inventory.InventorySystem.InventorySystem;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;

public class Bag implements Inventory{
    public InventorySystem inventory;

    public Bag(){
        this.inventory = new InventorySystem(
            new ArrayList<Item>(),
            GameConstants.MAX_PLAYER_BAG_SPACE
        );
    }

    @Override
    public String addToInventory(Item item){
        if(inventory.enoughSpace()){
            inventory.addToInventory(item);
            return StringUtils.format("%s added", item.name);
        }
        return StringUtils.format("inventory full!");
    }

    @Override
    public String removeFromInventory(Item item){
        inventory.removeFromInventory(item);
        return StringUtils.format("%s removed", item.name);
    }

    @Override
    public boolean enoughSpace() {
        return inventory.enoughSpace();
    }

    @Override
    public String getName(){
        return "Bag";
    }

}
