package dungeon.crawler.GameSystem.Inventory;


import dungeon.crawler.GameSystem.TestData.ItemFactory;

import java.util.*;

public class ShopItemSpawner {
    public static ArrayList<Item> spawnItems(int shopIndex) {
        ArrayList<Item> inventory = new ArrayList<>();
        ItemFactory factory = new ItemFactory();
        inventory.add(factory.createWeaponFromID("iron_sword"));
        inventory.add(factory.createWeaponFromID("wooden_staff"));
        inventory.add(factory.createWeaponFromID("wooden_club"));
        inventory.add(factory.createWeaponFromID("wooden_hammer"));
        inventory.add(factory.createPotionFromID("small_health_potion"));
        return inventory;
    }

//    private static ArrayList<String> difficultyCurve(int value){
//        switch(value){
//            default:
//                ArrayList<Item> inventory = new ArrayList<>();
//                inventory.add()
//
//        }
//    }
}


//public class EnemySpawner {
//    public static Map<Integer, EnemyCombatant> spawnEnemies(GameState gameState, TiledMapTileLayer.Cell tileCell) {
//        MapProperties props = tileCell.getTile().getProperties();
//        Map<Integer, EnemyCombatant> enemies = new HashMap<>();
//        Random diceRoller = new Random();
//        EnemyFactory factory = new EnemyFactory();
////        if(!props.containsKey("tile_difficulty")){
////
////        }
//        int numberOfEnemies = diceRoller.nextInt(3) + 1;
//        ArrayList<String> enemySelection = difficultyCurve(0);
//        for(int i = 0; i < numberOfEnemies; i++){
//            Collections.shuffle(enemySelection);
//            enemies.put(i, factory.createEnemyFromID(enemySelection.get(0)));
//        }
//        return enemies;
//    }
//
//    private static ArrayList<String> difficultyCurve(int value){
//        switch(value){
//            default:
//                return new ArrayList<String>(Arrays.asList("rat", "rat", "rat", "rat", "spider"));
////                return new ArrayList<String>(Arrays.asList("skeleton", "skeleton"));
//
//        }
//    }
//}
