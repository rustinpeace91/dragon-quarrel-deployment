package dungeon.crawler.GameSystem.TestData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Class.*;
import dungeon.crawler.GameSystem.Character.Condition;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Character.Stance;

import static dungeon.crawler.GameConstants.PLAYER_STATS.*;

import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.Weapon;

public class PlayerFactory{

    public static PartyCharacter generateClass(String selector){
        // TODO: for when we implement savegames
        switch(selector){
            case "Hero":
                return generate();
            case "Fighter":
                return generatePartyMember();
            case "Wizard":
                return generateWizard();
            case "Thief":
                return generateThief();
            default:
                throw new IllegalArgumentException("Unknown class type: " + selector);

        }
    }

	public static PartyCharacter generate() {

    	HeroClass hc = new HeroClass();
    	// TODO: Get base stats from charClass method
        //
        Map<GameConstants.PLAYER_STATS, Integer> statMap = hc.returnBaseStats();
        ItemFactory items = new ItemFactory();

        PartyCharacter pc = new PartyCharacter(
            "Hero",
            hc.getBaseHP(),
            hc.getBaseMP(),
            hc.getBaseHP(),
            hc.getBaseMP(),
            0,
            Stance.STANDING,
            new ArrayList<Condition>(),
            false,
            1,
            statMap.get(STRENGTH),
            statMap.get(AGILITY),
            statMap.get(INTELLIGENCE),
            statMap.get(PERCEPTION),
            hc,
            true
        );
        pc.generateFist();
        Item sword = items.createWeaponFromID("iron_sword");
        pc.addToInventory(sword);
        pc.equip(sword);
        pc.addToInventory(items.createPotionFromID("small_health_potion"));

        pc.charClass.fillSpells(1);
        return pc;
    }


    public static PartyCharacter generatePartyMember() {
        ItemFactory items = new ItemFactory();
        FighterClass fc = new FighterClass();
        Map<GameConstants.PLAYER_STATS, Integer> statMap = fc.returnBaseStats();
        PartyCharacter pc = new PartyCharacter(
            "Fighter",
            45,
            0,
            45,
            10,
            0,
            Stance.STANDING,
            new ArrayList<Condition>(),
            false,
            1,
            statMap.get(STRENGTH),
            statMap.get(AGILITY),
            statMap.get(INTELLIGENCE),
            statMap.get(PERCEPTION),
            fc,
            false
        );
        pc.generateFist();
        pc.equip(items.createWeaponFromID("iron_sword"));
        return pc;
    }

    public static PartyCharacter generateWizard() {
        WizardClass fc = new WizardClass();
        Map<GameConstants.PLAYER_STATS, Integer> statMap = fc.returnBaseStats();
        PartyCharacter pc = new PartyCharacter(
            "Wizard",
            15,
            0,
            15,
            20,
            0,
            Stance.STANDING,
            new ArrayList<Condition>(),
            false,
            1,
            statMap.get(STRENGTH),
            statMap.get(AGILITY),
            statMap.get(INTELLIGENCE),
            statMap.get(PERCEPTION),
            fc,
            false
        );
        pc.generateFist();

        pc.charClass.fillSpells(1);
        return pc;

    }

    public static PartyCharacter generateThief() {
        ItemFactory items = new ItemFactory();
        ThiefClass tc = new ThiefClass();
        Map<GameConstants.PLAYER_STATS, Integer> statMap = tc.returnBaseStats();
        PartyCharacter pc = new PartyCharacter(
            "Thief",
            15,
            0,
            15,
            0,
            0,
            Stance.STANDING,
            new ArrayList<Condition>(),
            false,
            1,
            statMap.get(STRENGTH),
            statMap.get(AGILITY),
            statMap.get(INTELLIGENCE),
            statMap.get(PERCEPTION),
            tc,
            false
        );
        pc.generateFist();

        Weapon bow = items.createWeaponFromID("wooden_bow");
        pc.addToInventory(bow);
        pc.equip(bow);
        return pc;

    }


}
