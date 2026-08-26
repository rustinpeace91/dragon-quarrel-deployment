package dungeon.crawler.Data.Maps;

import java.util.Map;

import dungeon.crawler.GameConstants;
// TODO: This is a horrible way to input data. JSON?
public class MapRegistry {
    public static final Map<Integer, ScreenTransitionProperties> WORLD_MAP_DATA = Map.of(
        1, new ScreenTransitionProperties(
            1,
            "Portantia",
            GameConstants.GAME_SCREEN.WALK_TOWN,
            GameConstants.TEST_MAP,
            13f,
            12f,
            0
        ),
        2, new ScreenTransitionProperties(
            2,
            "OverWorld",
            GameConstants.GAME_SCREEN.WALK_OVERWORLD,
            "Maps/overworld.tmx",
            9f,
            9f,
            0
        ),
        // TODO: build these out
        3, new ScreenTransitionProperties(
            3,
            "Portantia",
            GameConstants.GAME_SCREEN.WALK_TOWN,
            GameConstants.TEST_MAP,
            13f,
            12f,
            0
        ),
        4, new ScreenTransitionProperties(
            4,
            "Portantia",
            GameConstants.GAME_SCREEN.WALK_TOWN,
            GameConstants.TEST_MAP,
            9f,
            19f,
            0
        )
    );
}
