package dungeon.crawler.Menu.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import dungeon.crawler.Data.Descriptions.CharClassDescriptions;
import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Overworld.PartyCharacterStatusMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Screens.MainMenuScreen;
import dungeon.crawler.Utils.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerateMenuCharacterSelect extends BaseLinearMenu implements OverworldSubMenu {
    private final GenerateMenuOptions optionsMenu;
    private final MainMenuScreen main;
    private GameState gameState;
    public BaseLinearMenu asCombatMenu(){return this;}
    public GenerateMenuCharacterSelect(
        Skin skin,
        GameState gameState,
        GenerateMenuOptions optionsMenu,
        MainMenuScreen main
    ){
        super(skin);
        this.gameState = gameState;

        this.optionsMenu = optionsMenu;
        this.main = main;

        this.subStatusMenu = new StandardStatusMenu(skin);
        this.isToggleable = false;


    }



    @Override
    protected void setStage(Stage stage){
        super.setStage(stage);
        if(stage == null) return;
        this.addPartyButtons();


        if(parentMenu != null){
//            this.setPosition(this.parentMenu.getOriginX() + 200, Gdx.graphics.getHeight() - this.getHeight());
            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
            this.setPosition(this.getX() + 20, this.getY());

        }


//        float x = this.parentMenu.getWidth() + this.getWidth() + 40;
//        float y = Gdx.graphics.getHeight() - getHeight() - 150;
//        subStatusMenu.setPosition(
//            x, y
//        );
        subStatusMenu.setSize(250f, 420f);

//        subStatusMenu.alignTopRight(stage);
        subStatusMenu.setPosition(this.getX() + this.getWidth() + 10, this.getY() - 20);

        stage.addActor(subStatusMenu);
        this.addFocusListeners();

        refreshAndSetActive();
        this.buttonList = populateButtonList();
        this.resetMenuSelection();

    }

    @Override
    public void closeMenuStack() {
        this.subStatusMenu.remove();
        super.closeMenuStack();
    }

    private void addPartyButtons(){
        ArrayList<String> availableClasses = new ArrayList<>(Arrays.asList(GameConstants.AVAILABLE_CLASSES));
        availableClasses.forEach(
            (character) -> {
                addButton(
                    character,
                    new ChangeListener(){
                        @Override
                        public void changed(ChangeEvent event, Actor actor){
                            if(main.getPartySelection().size() >= GameConstants.MAX_PARTY_SIZE){
                                showPopup("Party Full, remove party members to add", 2f);
                            } else {
                                main.addClassString(character);
                                showPopup(character +  " Joins Party", 2f);
                                optionsMenu.updatePartyMenu();
                            }
                        }
                    },
                    character
                );
            }
        );
        this.addButton("Go Back", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                returnToParentMenu();
            }
        });
        this.pack();
    }
    @Override
    public void addFocusListeners(){
        super.addFocusListeners();
        for (Actor actor : this.getChildren()) {
            if(actor instanceof TextButton){
                TextButton button = (TextButton) actor;
                button.addListener(new FocusListener(){
                    @Override
                    public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                        TextButton button = (TextButton) actor;
                        String character = (String)button.getUserObject();
                        if (focused) {
                            if(button.getUserObject() instanceof String){
                                subStatusMenu.setText(CharClassDescriptions.Companion.getDescriptionFor(character));
                            } else {
                                subStatusMenu.setText(StringUtils.format("This is the status text for: \n %s", button.getText()));
                            }
                        }
                    }
                });
            }
        }
    }
}
