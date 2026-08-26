package dungeon.crawler.GameSystem.Character.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dungeon.crawler.Data.Spells.Spell;
import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.GameConstants;
import static dungeon.crawler.GameConstants.PLAYER_STATS.AGILITY;
import static dungeon.crawler.GameConstants.PLAYER_STATS.INTELLIGENCE;
import static dungeon.crawler.GameConstants.PLAYER_STATS.PERCEPTION;
import static dungeon.crawler.GameConstants.PLAYER_STATS.STRENGTH;

import dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.ArmorTypes.*;
import static dungeon.crawler.GameSystem.Inventory.ItemTypes.WeaponTypes.*;

import dungeon.crawler.GameSystem.Magic.MagicSystem;

public class WizardClass implements ClassLogic{

    private String name;
    private MagicSystem magicSystem;

    public WizardClass(){
        this.name = "Wizard";
        this.magicSystem = new MagicSystem();
    }

    private Map<Integer, ArrayList<SpellNames>> getWizardSpellLevels(){
        Map<Integer, ArrayList<SpellNames>> map = new HashMap<>();
        map.put(1, new ArrayList<>(Arrays.asList(SpellNames.FIREBOLT, SpellNames.HEALMINOR)));
        return map;
    }

    @Override
    public Map<GameConstants.PLAYER_STATS, Integer> returnBaseStats() {
        Map<GameConstants.PLAYER_STATS, Integer> statMap = new HashMap<>();
        statMap.put(STRENGTH, 8);
        statMap.put(AGILITY, 8);
        statMap.put(INTELLIGENCE, 12);
        statMap.put(PERCEPTION, 12);
        return statMap;
    }

    @Override
    public void fillSpells(int level){
        ArrayList<SpellNames> spells = new ArrayList();
        Map<Integer, ArrayList<SpellNames>> spellMap = getWizardSpellLevels();
        for(int i = 1; i <= level; i++){
            if(spellMap.containsKey(i)){
                spells.addAll(spellMap.get(i));
            }
        }
        magicSystem.setSpells(spells);
    }

    @Override
    public ArrayList<SpellNames> getSpellNames() {
        return this.magicSystem.availableSpells;
    }

    @Override
    public Map<GameConstants.PLAYER_STATS, Integer> returnLevelUpStats() {
        Map<GameConstants.PLAYER_STATS, Integer> statMap = new HashMap<>();

        Random random = new Random();
        statMap.put(STRENGTH, 0);
        statMap.put(AGILITY, 0);
        statMap.put(INTELLIGENCE, random.nextInt(4) + 1);
        statMap.put(PERCEPTION, 0);

        GameConstants.PLAYER_STATS[] otherStats = new  GameConstants.PLAYER_STATS[]{
            STRENGTH, AGILITY, PERCEPTION
        };


        int index = random.nextInt(otherStats.length);
        statMap.put(otherStats[index], 1);
        return statMap;

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
        return 10;
    }

    @Override
    public int getBaseMP() {
        // TODO Auto-generated method stub
        return 10;
    }

    @Override
    public int getLevelUpMP() {
        // TODO Auto-generated method stub
        return 10;
    }

    @Override
    public MagicSystem getMagicSystem() {
        // TODO Auto-generated method stub
        return magicSystem;
    }

    @Override
    public boolean isMagicUser() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public ArrayList<ArmorTypes> getArmorRestrictions() {
        ArmorTypes[] types = {
            BASIC
        };
        ArrayList<ArmorTypes> typeList = new ArrayList<ArmorTypes>(Arrays.asList(types));
        return typeList;
    }

    @Override
    public ArrayList<WeaponTypes> getWeaponRestrictions() {
        WeaponTypes[] types = {
            STAFF
        };
        ArrayList<WeaponTypes> typeList = new ArrayList<WeaponTypes>(Arrays.asList(types));
        return typeList;
    }
}
