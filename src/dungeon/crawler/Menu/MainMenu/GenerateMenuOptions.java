package dungeon.crawler.Menu.MainMenu;

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
import dungeon.crawler.Menu.Observers.StatusMenuObserver;
import dungeon.crawler.Menu.StandardStatusMenu;
import dungeon.crawler.Observers.MenuInputObserver;
import dungeon.crawler.Observers.ScreenChangeObserver;
import dungeon.crawler.Screens.MainMenuScreen;
import dungeon.crawler.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GenerateMenuOptions extends BaseLinearMenu {
    private final List<MenuInputObserver> listeners = new ArrayList<>();
    protected final GameState gameState;
    private final StatusMenuObserver statusMenuObserver;
    private PartyCompositionStatusMenu partyCompStatusMenu;
    private MainMenuScreen main;

    public GenerateMenuOptions(
        Skin skin,
        GameState gameState,
        MainMenuScreen main
    ) {
        super(
            skin
        );
        this.gameState = gameState;
        this.main = main;
        this.statusMenuObserver = new StatusMenuObserver();
        this.addButton("Add Party Member", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                GenerateMenuCharacterSelect newMenu = new GenerateMenuCharacterSelect(
                skin,
                gameState,
                    GenerateMenuOptions.this,
                main
            );
            setSubMenu(newMenu);
            openSubMenu(newMenu);
            }
        });
        this.addButton("Remove Party Member", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                main.removeLastClassString();
                updatePartyMenu();
            }
        });

        this.addButton("Go Back", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                main.resetClassString();
                returnToParentMenu();
            }
        });

        this.addButton("Start Adventure!", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                main.startGame();
            }
        });

        this.pack();
        this.addFocusListeners();
        this.setPosition(10, Gdx.graphics.getHeight() - this.getHeight() - 50);


        partyCompStatusMenu = new PartyCompositionStatusMenu(skin);

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(stage == null) return;

        refreshAndSetActive();


        partyCompStatusMenu.setPosition(
            (Gdx.graphics.getWidth() - partyCompStatusMenu.getWidth() - 30),
            (Gdx.graphics.getHeight() - this.getHeight() - 20)
        );
        stage.addActor(partyCompStatusMenu);
        partyCompStatusMenu.setHeight(partyCompStatusMenu.getHeight() + 20);

    }



    @Override
    public void notifyScreenChange(GameConstants.GAME_SCREEN screen){
        for (ScreenChangeObserver observer : screenChangeObservers) {
            observer.onScreenChange(screen);
        }
    }

    @Override
    public void addScreenChangeObserver(ScreenChangeObserver observer){
        screenChangeObservers.add(observer);
    }

    public void addListener(MenuInputObserver listener) {
        if(listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(MenuInputObserver listener) {
        if(listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void refreshAndSetActive(){
        super.refreshAndSetActive();
        partyCompStatusMenu.setVisible(true);
        partyCompStatusMenu.showParty(main.getPartySelection());
    }


    public void updatePartyMenu(){
        partyCompStatusMenu.setVisible(true);
        partyCompStatusMenu.showParty(main.getPartySelection());
    }

    @Override
    public void openSubMenu(BaseLinearMenu nextMenu){
        super.openSubMenu(nextMenu);
        this.setVisible(true);
    }

    @Override
    public void returnToParentMenu(){
        super.returnToParentMenu();
        if(this.partyCompStatusMenu != null){
            this.partyCompStatusMenu.setVisible(false);
            this.partyCompStatusMenu.remove();
        }
    }

//    @Override
//    public void addFocusListeners(){
//        super.addFocusListeners();
//        for (Actor actor : this.getChildren()) {
//            if(actor instanceof TextButton){
//                TextButton button = (TextButton) actor;
//                button.addListener(new FocusListener(){
//                    @Override
//                    public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
//                        if (focused) {
//                            if(button.getUserObject() == "Create"){
//                                subStatusMenu.setText("Create a new party");
//                            } else {
//                                subStatusMenu.setText("Premade party consisting of Hero, Fighter, Wizard, Thief");
//
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }
}
