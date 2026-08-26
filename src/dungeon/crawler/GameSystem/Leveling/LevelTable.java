package dungeon.crawler.GameSystem.Leveling;

import com.badlogic.gdx.utils.IntIntMap;

public class LevelTable {
    private static final IntIntMap table = new IntIntMap();

    static {
        table.put(1, 0);       
        table.put(2, 100);
        table.put(3, 300);
        table.put(4, 750);
        table.put(5, 1500);
        table.put(6, 3000);
    }

    public static int getRequiredXp(int level) {
        return table.get(level, 999999);
    }
}
