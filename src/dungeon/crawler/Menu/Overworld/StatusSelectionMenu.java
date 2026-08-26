package dungeon.crawler.Menu.Overworld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import dungeon.crawler.GameConstants;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.OverworldSubMenu;
import dungeon.crawler.Utils.StringUtils;

public class StatusSelectionMenu extends BaseLinearMenu implements OverworldSubMenu {
    private GameState gameState;
    private PartyCharacterStatusMenu partyStatusMenu;
    public BaseLinearMenu asCombatMenu(){return this;}

	public StatusSelectionMenu(
        Skin skin,
        GameState gameState
    ){
        super(skin);
        this.gameState = gameState;
        this.partyStatusMenu = new PartyCharacterStatusMenu(
            skin,
            gameState.player
        );

        // shut up linter
        this.subStatusMenu = partyStatusMenu;
        this.isToggleable = true;
    }



    @Override
    protected void setStage(Stage stage){
        // TODO: Move this logic OUTTA here. This runs when the menu closes too
        super.setStage(stage);
        if(stage == null) return;
        this.addPartyButtons();


        if(parentMenu != null){
//            this.setPosition(this.parentMenu.getOriginX() + 200, Gdx.graphics.getHeight() - this.getHeight());
            setSizeandPosition(GameConstants.SUBMENU_SIZE.SMALL);

        }


//        float x = this.parentMenu.getWidth() + this.getWidth() + 40;
//        float y = Gdx.graphics.getHeight() - getHeight() - 150;
//        subStatusMenu.setPosition(
//            x, y
//        );
        subStatusMenu.alignTopRight(stage);
        stage.addActor(subStatusMenu);
        refreshAndSetActive();
        this.addFocusListeners();
        this.buttonList = populateButtonList();
        this.resetMenuSelection();

    }

    @Override
    public void closeMenuStack() {
        this.subStatusMenu.remove();
        super.closeMenuStack();
    }

    private void addPartyButtons(){
        if(gameState.party != null){
            gameState.party.forEach(
                (key, character) -> {
                    addButton(
                        character.name,
                        new ChangeListener (){
                            @Override
                            public void changed(ChangeEvent event, Actor actor){
                                // nothing for now. This is for hovering
                            }
                        },
                        character
                    );
                }
            );
        }
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
                    PartyCharacter character = (PartyCharacter)button.getUserObject();
                    if (focused) {
                        if(button.getUserObject() instanceof PartyCharacter){
                            partyStatusMenu.showCharacter(character);
                        } else {
                            partyStatusMenu.setText(StringUtils.format("This is the status text for: \n %s", button.getText()));

                        }
                    }
                }
                });
            }
        }
    }
}
