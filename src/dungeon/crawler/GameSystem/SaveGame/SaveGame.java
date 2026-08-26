package dungeon.crawler.GameSystem.SaveGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.SaveGame.Serialization.GameSave;
import dungeon.crawler.GameSystem.SaveGame.Serialization.GameSaveJson;
import dungeon.crawler.GameSystem.SaveGame.Serialization.GameSerializer;

import java.util.HashMap;

public class SaveGame {
    FileHandle saveFile = Gdx.files.local("savegame.json");

    public SaveGame(){
        saveFile = Gdx.files.local("savegame.json");
    }

    public String saveGameState(GameState gameState){
        GameSave save = GameSerializer.serializeGameState(gameState);
        String saveData = GameSaveJson.encode(save);
        Gdx.app.log("[BROWSER SAVE]", saveData);
        saveFile.writeString(saveData, false);
        return saveData;

    }

    public GameState loadGameState() {

        if (!saveFile.exists()) {
            return null;
        }

        String saveData = saveFile.readString();

        GameSave decodedData = GameSaveJson.decode(saveData);

        Gdx.app.log("[BROWSER LOAD]", "gold = " + decodedData.getGold());
        Gdx.app.log("[BROWSER LOAD]", "player = " + decodedData.getPlayer());
        Gdx.app.log("[BROWSER LOAD]", "party size = " + decodedData.getParty().size());

        return null;
    }
}
