package dungeon.crawler.Menu.MainMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dungeon.crawler.GameConstants;
import dungeon.crawler.Menu.StandardStatusMenu;

import java.util.ArrayList;

public class PartyCompositionStatusMenu extends StandardStatusMenu {
    public PartyCompositionStatusMenu(Skin skin) {
        super(skin);
    }

    public void showParty(ArrayList<String> partyClasses){
        String status = "Current Party: \n";
        for(int i=0; i < GameConstants.MAX_PARTY_SIZE; i++){
            int printIndex = i + 1;
            if(i > partyClasses.size() -1){
                status = status + "\n" +
                    printIndex + ". " + "-";
            } else {
                status = status + "\n" +
                    printIndex + ". " + partyClasses.get(i);
            }
        }
        setText(status);
    }
}
