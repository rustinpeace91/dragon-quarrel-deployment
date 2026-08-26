package dungeon.crawler.GameSystem.Enemies;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.TestData.EnemyFactory;

import java.util.*;

public class EnemySpawner {
    public static Map<Integer, EnemyCombatant> spawnEnemies(GameState gameState) {
        Map<Integer, EnemyCombatant> enemies = new HashMap<>();
        Random diceRoller = new Random();
        EnemyFactory factory = new EnemyFactory();
        int tileDiffuclty = gameState.getTileDifficulty();
        int numberOfEnemies = diceRoller.nextInt(3) + 1;
        ArrayList<String> enemySelection = difficultyCurve(tileDiffuclty);
        for(int i = 0; i < numberOfEnemies; i++){
            Collections.shuffle(enemySelection);
            enemies.put(i, factory.createEnemyFromID(enemySelection.get(0)));
        }
        return enemies;
    }

    private static ArrayList<String> difficultyCurve(int value){
        switch(value){
            default:
                return new ArrayList<String>(Arrays.asList("rat", "rat", "rat", "rat", "spider"));
//                return new ArrayList<String>(Arrays.asList("skeleton", "skeleton"));

        }
    }
}
