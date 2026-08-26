package dungeon.crawler;

import java.util.Map;

import dungeon.crawler.GameSystem.Inventory.ItemTypes.ItemType;
import dungeon.crawler.Player.PlayerDirection;
public class GameConstants {
    public enum GAME_SCREEN {
        MENU,
        WALK_OVERWORLD,
        WALK_TOWN,
        COMBAT,
        INN,
        TEST_SCREEN,
        SHOP_SCREEN,
        CHURCH_SCREEN
    };

    public enum PLAYER_STATS {
        STRENGTH,
        AGILITY,
        INTELLIGENCE,
        PERCEPTION
    }

    public enum SUBMENU_SIZE {
        SMALL,
        MEDIUM,
        TALL,
        WIDE,
        DYNAMIC
    }

    public static final int MAX_PARTY_SIZE=4;
    public static final String[] AVAILABLE_CLASSES = {"Fighter", "Thief", "Wizard"};

    // ITEM STATS
    public static final ItemType[] USABLE_ITEMS = new ItemType[]{
        ItemType.CURE_POTION,
        ItemType.HEALTH_POTION
    };

    public static final ItemType[] EQUIPPABLE_ITEMS = new ItemType[]{
        ItemType.WEAPON,
        ItemType.ARMOR
    };


    // screen
    public static final int RESOLUTION_WIDTH = 960;
    public static final int RESOLUTION_HEIGHT = 720;

//    public static final int RESOLUTION_WIDTH = 640;
//    public static final int RESOLUTION_HEIGHT = 480;

    // player
    public static final float TOWN_MOVEMENT_DURATION = 0.15f;
    public static final float OVERWORLD_MOVEMENT_DURATION = 0.25f;

    public static final float ENEMY_ANIMATION_SPEED = 0.5f;
    // public static final float SPRITE_WIDTH = 16;
    // public static final float SPRITE_HEIGHT = 16;
    public static final float SPRITE_WIDTH = 16;
    public static final float SPRITE_HEIGHT = 16;
    public static final float TILE_WIDTH = 16;
    public static final float TILE_HEIGHT = 16;

    public static final float FRAME_DURATION = .15f;



    public static final String SAVE_FILE_PATH = "savegame.json";
    public static final String TEST_MAP = "Maps/testmap.tmx";
    public static final String CURRENT_SPRITE = "Sprites/real_sheet.png";
    // public static final String MENU_SKIN = "skins/plainjames_modded/plainjames.json";
    public static final String MENU_SKIN = "skins/defaultmodded/uiskinaltered.json";
    public static final String EXPLOSION_SPRITES = "Sprites/Explosion/Explosion.png";
    public static final String SKIN_BACKGROUND_DEFAULT = "default-round";
    public static final Map<PlayerDirection, String> WALK_ANIMATIONS = Map.of(
        PlayerDirection.UP, "WalkUp",
        PlayerDirection.RIGHT, "WalkRight",
        PlayerDirection.DOWN, "WalkDown",
        PlayerDirection.LEFT, "WalkLeft"
    );
    public static final Map<PLAYER_STATS, String> STAT_NAMES = Map.of(
        PLAYER_STATS.STRENGTH, "Strength",
        PLAYER_STATS.AGILITY, "Agility",
        PLAYER_STATS.INTELLIGENCE, "Intelligence",
        PLAYER_STATS.PERCEPTION, "Perception"

    );

    public static final int MAX_PLAYER_INVENTORY_SPACE = 15;
    public static final int MAX_PLAYER_BAG_SPACE = 128;
    public static final float SHOP_MARKUP = 4f;


    // COMBAT MENU DIMENSIONS
    public static final float COMBAT_MENU_HEIGHT=60f;
    public static final float COMBAT_MENU_PAD=5f;
    public static final float COMBAT_MENU_WIDTH=185f;
    public static final float COMBAT_SUBMENU_WIDTH=180f;
    public static final float SCROLLABLE_SUBMENU_WIDTH=195f;
    public static final float COMBAT_SUBMENU_PAD=5f;
    public static final float COMBAT_SUBMENU_WIDTH_DIFF=30f;


}
