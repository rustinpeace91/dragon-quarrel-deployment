package dungeon.crawler.GameSystem.SaveGame.Serialization;


import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Inventory.ItemTypes.EquipmentSlot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameSerializer {

    public static GameSave serializeGameState(GameState gameState) {

        PartyCharacterSave player =
            serializePartyCharacter(gameState.player);

        ArrayList<PartyCharacterSave> party = new ArrayList<>();

        for (PartyCharacter character : gameState.party.values()) {
            party.add(serializePartyCharacter(character));
        }

        return new GameSave(
            player,
            (int) gameState.overWorldCoordinates.x,
            (int) gameState.overWorldCoordinates.y,
            party,
            gameState.gold
        );
    }


    private static PartyCharacterSave serializePartyCharacter(
        PartyCharacter character
    ) {

        return new PartyCharacterSave(
            character.level,
            character.xp,

            character.strength,
            character.agility,
            character.intelligence,
            character.perception,

            character.isHero,
            character.toHit,

            character.charClass.getName(),

            serializeEquipment(character),

            character.maxHp,
            character.maxMP,
            character.hp,
            character.mp,

            character.stance,
            new ArrayList<>(character.conditions),
            character.isDead,

            serializeInventory(character),
            serializeSpells(character)
        );
    }

    private static ArrayList<String> serializeInventory(
        PartyCharacter character
    ) {
        ArrayList<String> inventory = new ArrayList<>();

        for (Item item : character.inventory.inventoryList) {
            inventory.add(item.getId());
        }

        return inventory;
    }

    private static HashMap<String, String> serializeEquipment(
        PartyCharacter character
    ) {
        HashMap<String, String> equipment = new HashMap<>();

        for (Map.Entry<EquipmentSlot, Item> entry :
            character.equipment.getEquippedItemsMap().entrySet()) {

            if (entry.getValue() != null) {
                equipment.put(
                    entry.getKey().name(),
                    entry.getValue().getId()
                );
            }
        }

        return equipment;
    }

    private static ArrayList<String> serializeSpells(
        PartyCharacter character
    ) {
        ArrayList<String> spells = new ArrayList<>();

        if (character.charClass.isMagicUser()) {
            for (SpellNames spell : character.charClass
                .getMagicSystem().availableSpells) {

                spells.add(spell.name());
            }
        }

        return spells;
    }

}
