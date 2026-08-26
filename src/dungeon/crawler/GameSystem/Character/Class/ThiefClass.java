package dungeon.crawler.GameSystem.Character.Class;

import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes;
import dungeon.crawler.GameSystem.Magic.MagicSystem;

import java.util.*;

import static dungeon.crawler.GameConstants.PLAYER_STATS.AGILITY;
import static dungeon.crawler.GameConstants.PLAYER_STATS.INTELLIGENCE;
import static dungeon.crawler.GameConstants.PLAYER_STATS.PERCEPTION;
import static dungeon.crawler.GameConstants.PLAYER_STATS.STRENGTH;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes.*;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes.HEAVY;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes.*;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes.LONGBOW;

public class ThiefClass implements ClassLogic{
    private String name;
    public ThiefClass(){
        this.name = "Thief";
    }

    @Override
    public Map<GameConstants.PLAYER_STATS, Integer> returnBaseStats() {
        Map<GameConstants.PLAYER_STATS, Integer> statMap = new HashMap<>();
        statMap.put(STRENGTH, 8);
        statMap.put(AGILITY, 16);
        statMap.put(INTELLIGENCE, 6);
        statMap.put(PERCEPTION, 15);
        return statMap;
    }

    @Override
    public Map<GameConstants.PLAYER_STATS, Integer> returnLevelUpStats() {
        Map<GameConstants.PLAYER_STATS, Integer> statMap = new HashMap<>();

        Random random = new Random();
        statMap.put(STRENGTH, 0);
        statMap.put(AGILITY, random.nextInt(4) + 1);
        statMap.put(INTELLIGENCE, 0);
        statMap.put(PERCEPTION, random.nextInt(2) + 1);

        GameConstants.PLAYER_STATS[] otherStats = new  GameConstants.PLAYER_STATS[]{
            INTELLIGENCE, STRENGTH
        };


        int index = random.nextInt(otherStats.length);
        statMap.put(otherStats[index], 1);
        return statMap;

    }

    @Override
    public int getBaseMP() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getLevelUpMP() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getBaseHP() {
        // TODO Auto-generated method stub
        return 20;
    }

    @Override
    public int getLevelUpHP() {
        // TODO Auto-generated method stub
        return 12;
    }

    @Override
    public MagicSystem getMagicSystem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMagicUser() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void fillSpells(int level) {
        // TODO Auto-generated method stub

    }

    @Override
    public ArrayList<SpellNames> getSpellNames() {
        return null;
    }

    @Override
    public ArrayList<ArmorTypes> getArmorRestrictions() {
        ArmorTypes[] types = {
            BASIC,
            LIGHT,
        };
        ArrayList<ArmorTypes> typeList = new ArrayList<ArmorTypes>(Arrays.asList(types));
        return typeList;
    }

    @Override
    public ArrayList<WeaponTypes> getWeaponRestrictions() {
        WeaponTypes[] types = {
            SHORTSWORD,
            STAFF,
            SLING,
            CROSSBOW,
            SHORTBOW
        };
        ArrayList<WeaponTypes> typeList = new ArrayList<WeaponTypes>(Arrays.asList(types));
        return typeList;
    }
}
