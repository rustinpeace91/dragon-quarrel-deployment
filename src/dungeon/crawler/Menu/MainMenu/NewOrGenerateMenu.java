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

public class NewOrGenerateMenu extends BaseLinearMenu{
    private final List<MenuInputObserver> listeners = new ArrayList<>();
    protected final GameState gameState;
    private final StatusMenuObserver statusMenuObserver;

    public NewOrGenerateMenu(
        Skin skin,
        GameState gameState,
        MainMenuScreen main
    ) {
        super(
            skin
        );
        this.gameState = gameState;
        this.statusMenuObserver = new StatusMenuObserver();
        this.addButton("Create Party", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
            GenerateMenuOptions newMenu = new GenerateMenuOptions(
                skin,
                gameState,
                main
            );
            setSubMenu(newMenu);
            openSubMenu(newMenu);
            }
        }, "Create");
        this.addButton("Preset Party", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                main.startGamePreset();
            }
        }, "Generate");

        this.pack();
        this.addFocusListeners();
    //        this.setPosition(10, Gdx.graphics.getHeight() - this.getHeight() - 50);
        this.setPosition(
            (Gdx.graphics.getWidth() - this.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - this.getHeight()) / 2f
        );

        subStatusMenu = new StandardStatusMenu(skin);


        // addMenuListeners(partyButton, searchButton, testNewMenu);
    }
    @Override
    public void refreshAndSetActive(){
        super.refreshAndSetActive();
        subStatusMenu.setVisible(true);
    }
    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(stage == null) return;

        refreshAndSetActive();


        subStatusMenu.setPosition(
            (Gdx.graphics.getWidth() - subStatusMenu.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - this.getHeight()) / 2f - this.getHeight() - 50
        );
        stage.addActor(subStatusMenu);
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
    public void openSubMenu(BaseLinearMenu nextMenu){
        super.openSubMenu(nextMenu);
        this.subStatusMenu.setVisible(false);
        this.setVisible(false);
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
                        if (focused) {
                            if(button.getUserObject() == "Create"){
                                subStatusMenu.setText("Create a new party");
                            } else {
                                subStatusMenu.setText("Premade party consisting of Hero, Fighter, Wizard, Thief");

                            }
                        }
                    }
                });
            }
        }
    }
}
