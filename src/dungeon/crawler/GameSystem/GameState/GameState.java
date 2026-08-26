package dungeon.crawler.GameSystem.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

import dungeon.crawler.GameSystem.Character.Bag;
import dungeon.crawler.GameSystem.Character.EnemyCombatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameBuild;
import dungeon.crawler.GameSystem.TestData.PlayerFactory;

public class GameState {
    public PartyCharacter player;
    public Vector2 overWorldCoordinates;

    public transient Map<Integer, PartyCharacter> party;

    // Prevents JSON issues
    public ArrayList<PartyCharacter> partySaveData = new ArrayList<>();



    public int gold;
    public boolean isPlayerDead;
    public String currentMap;
    public int screenID;
    public Bag partyBag;
    // used for enemy encounters, shop stuff?
    private transient int tileDifficulty = 0;
    private GameBuild build;

    public GameState(GameBuild build){
        this.build = build;
        // TODO: make an actual constructor
    }

    public GameState(){

    }
    // TODO: Refactor this shit so we're not repeating
    public void setUpTestData(){
        player = PlayerFactory.generate();
        PartyCharacter fighter = PlayerFactory.generatePartyMember();
        PartyCharacter wizard = PlayerFactory.generateWizard();
        PartyCharacter thief = PlayerFactory.generateThief();
        partySaveData.add(player);
        partySaveData.add(fighter);
        partySaveData.add(wizard);
        partySaveData.add(thief);
        initializeRuntimeParty();
        partyBag = new Bag();
        overWorldCoordinates = new Vector2(0,0);
        gold = 100;
        isPlayerDead = false;
        currentMap = "";
        screenID = 1;
    }

    public void SetUpClassDataFromString(ArrayList<String> partyList){
        player = PlayerFactory.generate();
        partySaveData = new ArrayList<>();
        for(int i = 0; i < partyList.size(); i++){
            String className = partyList.get(i);
            PartyCharacter chr = PlayerFactory.generateClass(className);
            partySaveData.add(chr);
        }
        initializeRuntimeParty();

        partyBag = new Bag();
        overWorldCoordinates = new Vector2(0,0);
        gold = 100;
        isPlayerDead = false;
        currentMap = "";
        screenID = 1;
    }



    public void updateWorldCoordinates(Vector2 newCoords){
        overWorldCoordinates = newCoords;
    }

    public void updateWorldMap(String mapFile){
        currentMap = mapFile;
    }
    public void updateScreenID(int id){
        screenID = id;
    }

    public void addGold(int value){
        this.gold = gold + value;
    }

    public void removeGold(int value){
        this.gold = gold - value;
    }

    public void setTileDifficulty(int tileDifficulty) {
        this.tileDifficulty = tileDifficulty;
    }
    public int getTileDifficulty() {
        return tileDifficulty;
    }
    public int getGold(){
        return gold;
    }

    public void initializeRuntimeParty() {

        party = new HashMap<>();

        for (int i = 0; i < partySaveData.size(); i++) {
            party.put(i, partySaveData.get(i));
        }

    }


    public GameBuild getBuild() {
        return build;
    }

    public void setBuild(GameBuild build) {
        this.build = build;
    }
}
