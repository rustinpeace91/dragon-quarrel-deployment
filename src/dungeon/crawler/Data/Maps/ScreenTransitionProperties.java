package dungeon.crawler.Data.Maps;

import dungeon.crawler.GameConstants.GAME_SCREEN;

// public record ScreenTransitionProperties(
//     int id,
//     String name,
//     GAME_SCREEN screen,
//     String mapFile,
//     float startingX,
//     float startingY
// ) {}

// on upgrade of Java from 8 to 17, use records?

public class ScreenTransitionProperties {
    public final int id;
    public final String name;
    public final GAME_SCREEN screen;
    public final String mapFile;
    public final float startingX;
    public final float startingY;
    public final int shopIndex;

    public ScreenTransitionProperties(int id, String name, GAME_SCREEN screen, String mapFile, float startingX, float startingY, int shopIndex) {
        this.id = id;
        this.name = name;
        this.screen = screen;
        this.mapFile = mapFile;
        this.startingX = startingX;
        this.startingY = startingY;
        this.shopIndex = shopIndex;
    }
}
