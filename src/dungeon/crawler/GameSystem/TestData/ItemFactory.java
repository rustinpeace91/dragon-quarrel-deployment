package dungeon.crawler.GameSystem.TestData;

import dungeon.crawler.Data.Items.ItemDataInitializer;
import dungeon.crawler.Data.Items.PotionParams;
import dungeon.crawler.Data.Items.Registry;
import dungeon.crawler.Data.Items.WeaponParams;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.Potion;
import dungeon.crawler.GameSystem.Inventory.Weapon;


public class ItemFactory {

    private Registry<WeaponParams> weaponRegistry;
    private Registry<PotionParams> potionRegistry;

    public ItemFactory(){
        this.weaponRegistry = ItemDataInitializer.initializeWeaponData();
        this.potionRegistry = ItemDataInitializer.initializePotionData();
    }

    public Weapon createWeaponFromID(String id) {
        WeaponParams params = weaponRegistry.getById(id);
        return new Weapon(
            params.getName(),
            params.getId(),
            params.getToHit(),
            params.getDamageLow(),
            params.getDamageHigh(),
            params.getFlavorTextVerb(),
            params.getRanged(),
            params.getCondition(),
            params.getElemental(),
            params.getValue(),
            params.getWeaponType(),
            params.getHanded()
        );
    }

    public Potion createPotionFromID(String id){
        PotionParams params = potionRegistry.getById(id);
        return new Potion(
            params.getName(),
            params.getId(),
            params.getValue(),
            params.getItemType(),
            params.getLevel(),
            params.getCureStatus()
        );
    }

    public Item createItemById(String id) {
        if (weaponRegistry.getById(id) != null) {
            return createWeaponFromID(id);
        }
        if (potionRegistry.getById(id) != null) {
            return createPotionFromID(id);
        }
//        if (armorRegistry.getById(id) != null) {
//            return createArmorFromID(id);
//        }
        throw new IllegalArgumentException("Unknown item ID: " + id);
    }

}
