package dungeon.crawler.Menu.Overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.SaveGame.SaveGame;
import dungeon.crawler.Menu.BaseLinearMenu;

public class OptionsMenu extends BaseLinearMenu{
    private GameState gameState;
    private String saveGame;
    // bad. Do not keep this here
    private SaveGame saveSystem;
    public OptionsMenu(
        Skin skin,
        GameState gameState
    ){
        super(skin);
        this.gameState = gameState;
        this.isToggleable = true;
        this.saveSystem = new SaveGame();

    }
    @Override
    protected void setStage(Stage stage){
        // TODO: Move this logic OUTTA here. This runs when the menu closes too
        super.setStage(stage);
        if(stage == null) return;



        this.addOptionsButtons();


        float h = this.getHeight();
        if(parentMenu != null){
            this.setPosition(this.parentMenu.getOriginX() + 200, Gdx.graphics.getHeight() - (this.getHeight() + 20));
        }

        refreshAndSetActive();
        // this.addFocusListeners();
        this.buttonList = populateButtonList();
        this.resetMenuSelection();
    }

    protected void addOptionsButtons() {
        addButton(
            "Music",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // nothing for now. This is for hovering
                }
            }
        );


        addButton(
            "Appearance",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // nothing for now. This is for hovering
                }
            }
        );


        addButton(
            "Save",
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String s = saveSystem.saveGameState(gameState);
                    showPopup("Game Saved", 2f);
//                    if(gameState.getBuild() == GameBuild.DESKTOP){
//                        String s = saveSystem.saveGameState(gameState);
//                        showPopup("Game Saved", 2f);
//                    } else {
//                        showPopup("Save works in Desktop Mode only", 2f);
//                    }

                }
            }
        );
        pack();
    }
}
