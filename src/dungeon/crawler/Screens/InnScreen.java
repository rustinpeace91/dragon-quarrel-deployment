package dungeon.crawler.Screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.MainGame;
import dungeon.crawler.Observers.ScreenChangeObserver;

import java.util.HashMap;
import java.util.Map;

public class InnScreen extends SplashScreen{
    public InnScreen(
        MainGame game,
        ScreenChangeObserver listener
    ){
        super(game, listener);
    }

    @Override
    public void show(){
        timeAmount = 3.0f;
        if(game.gameState.gold >= 10){
            textDisplay = "You sleep in the inn for 10 gold";
            // TODO: bad, use setter
            this.game.gameState.gold = this.game.gameState.gold - 10;

            for (Map.Entry<Integer, PartyCharacter> combatant : this.game.gameState.party.entrySet()) {
                if (!combatant.getValue().checkDeath()) {
                    PartyCharacter character = combatant.getValue();
                    character.longRest();
                }
            }
        } else {
            textDisplay = "You are too broke to sleep in the inn";
        }
        Vector2 newCoords = new Vector2();
        newCoords.y = this.game.gameState.overWorldCoordinates. y - 1;
        newCoords.x = this.game.gameState.overWorldCoordinates. x;

        this.game.gameState.updateWorldCoordinates(newCoords);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (listener != null) listener.onScreenChange(GameConstants.GAME_SCREEN.WALK_TOWN);
            }
        }, timeAmount);
    }

}
