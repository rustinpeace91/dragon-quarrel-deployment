package dungeon.crawler.Menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent;
import com.badlogic.gdx.utils.Array;

import dungeon.crawler.GameConstants;
import dungeon.crawler.AssetManager.Assets;
import dungeon.crawler.Menu.Misc.PopUpUtils;
import dungeon.crawler.Observers.ScreenChangeObserver;
// TODO: Consider passing the stage in here, then using it to spawn submenus
public class BaseLinearMenu extends Table {

    protected Array<TextButton> buttonList;
    protected ArrayList<ScreenChangeObserver> screenChangeObservers;
    protected Skin skin;
    protected boolean showMenu;
    public boolean isToggleable;

    protected BaseLinearMenu subMenu;
    protected BaseLinearMenu parentMenu;
    protected StandardStatusMenu subStatusMenu;
    protected Image arrow;
    protected int currentButtonIndex;

    public BaseLinearMenu(
        Skin skin
    ){
        super(skin);
        this.skin = skin;
        Drawable background = skin.getDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT);
        this.buttonList = new Array<TextButton>();
        this.screenChangeObservers = new ArrayList<ScreenChangeObserver>();
        Color semiTransparentGray = new Color(0.2f, 0.2f, 0.2f, 0.8f);
        this.setBackground(skin.newDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT, semiTransparentGray));
        this.defaults().pad(10).fillX().minWidth(150); // Set default padding for all cells
        this.initializeArrow();
        this.handleTouchLogic();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        this.buttonList = populateButtonList();
    }


    public void notifyScreenChange(GameConstants.GAME_SCREEN screen){
        for (ScreenChangeObserver observer : screenChangeObservers) {
            observer.onScreenChange(screen);
        }
    }

    public void addScreenChangeObserver(ScreenChangeObserver observer){
        screenChangeObservers.add(observer);
    }
    public void addButton(
        String buttonName,
        ChangeListener listener
    ){
        addButton(buttonName, listener, null);
    }

    public void addFocusButton(
        String buttonName,
        FocusListener listener
    ){
        addFocusButton(buttonName, listener, null);
    }

    public void addButton(
        String buttonName,
        ChangeListener listener,
        Object userObject
    ){
        TextButton newButton = new TextButton(buttonName, skin);

        this.add(newButton).row();
        newButton.addListener(listener);
        applyFocusBehavior(newButton);
        if(userObject != null){
            newButton.setUserObject(userObject);
        }
    }

    public void addFocusButton(
        String buttonName,
        FocusListener listener,
        Object userObject
    ){
        TextButton newButton = new TextButton(buttonName, skin);

        this.add(newButton).row();
        newButton.addListener(listener);
        applyFocusBehavior(newButton);
        if(userObject != null){
            newButton.setUserObject(userObject);
        }
    }



    /* call after all button logic is added */
    public Array<TextButton> populateButtonList(){

        Array<TextButton> newList = new Array<TextButton>();
        for(Actor actor:this.getChildren()){
            if(actor instanceof TextButton){
                newList.add((TextButton) actor);
            }
        }
        return newList;
    }

    public void advanceMenuSelection(int step){
        Actor focusedItem = getStage().getKeyboardFocus();

        // int activeItem
        currentButtonIndex = buttonList.indexOf((TextButton)focusedItem, true);
        if(currentButtonIndex == -1){
            Gdx.app.log("Menu", "Error: index is outside buttonList");
            getStage().setKeyboardFocus(buttonList.get(0));
            return;
        }

        int newIndex = currentButtonIndex + step;
        if (0 <= newIndex && newIndex < buttonList.size){

            getStage().setKeyboardFocus(buttonList.get(newIndex));
            currentButtonIndex = newIndex;
        }

    }

    public void resetMenuSelection(){

        if (buttonList != null && buttonList.size > 0) {
            getStage().setKeyboardFocus(buttonList.first());
        } else {
            getStage().setKeyboardFocus(null);
        }
    }

    public void refreshAndSetActive(){
        if(getStage() == null) {
            Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
            // no return. Let it break the game
        }
        setVisible(true);

        this.buttonList = populateButtonList();
        resetMenuSelection();
    }

    public void recallMenuSelection(){
        if(
            currentButtonIndex != -1 &&
            getStage() != null &&
            currentButtonIndex < buttonList.size
        ){
            getStage().setKeyboardFocus(buttonList.get(currentButtonIndex));
        }
    }

    public void showPopup(String message, float time){
        PopUpUtils.showToast(this.getStage(), this.getSkin(),  message, time);
    }


    public void unFocus(){
        if(getStage() == null) {
            Gdx.app.log("Menu Error", "unFocus called BEFORE linear menu added to stage");
            // no return. Let it break the game
        }
        getStage().setKeyboardFocus(null);
    }

    public void initializeArrow(){
        // 2. Setup the single Arrow instance for this menu
        this.arrow = new Image(skin.getDrawable("arrow"));
        this.arrow.setSize(12, 12);
        this.arrow.setVisible(false);
        this.addActor(arrow); // Add it once; we just move it later
        this.currentButtonIndex = -1;
    }


    public void applyFocusBehavior(Actor actor){
        if(!(actor instanceof TextButton)){
            return;
        }
        actor.addListener(new FocusListener(){
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor a, boolean focused) {
                    TextButton button = (TextButton) actor;
                    Label label = button.getLabel();
                    if (focused) {
                        final Vector2 pos = new Vector2();
                        label.setColor(Color.YELLOW);
                        // Get position relative to the WHOLE screen/stage
                        // 2. MATH FIX:
                        // Get the button's position relative to the OVERWORLDMENU (this)
                        // instead of the whole Stage.
                        pos.set(button.getX(), button.getY());

                        // Position the arrow to the left of the button's local X/Y


                        arrow.setPosition(
                            pos.x - arrow.getWidth(),
                            pos.y + (button.getHeight() - arrow.getHeight()) / 2
                        );
                        arrow.setVisible(true);
                    } else {
                        label.setColor(Color.WHITE);
                    }
            }
        });
    }

    /*Handle Mouse/touchpad input. For now we are going to disable it completely as to not break focus
    logic */
    public void handleTouchLogic(){

        this.setTouchable(Touchable.disabled);
    }

//    public void rePositionArrow() {
//        Actor focused = getStage().getKeyboardFocus();
//
//        if (!(focused instanceof TextButton)) {
//            arrow.setVisible(false);
//            return;
//        }
//
//        TextButton button = (TextButton) focused;
//
//        arrow.setPosition(
//            button.getX() - arrow.getWidth(),
//            button.getY() + (button.getHeight() - arrow.getHeight()) / 2
//        );
//
//        arrow.setVisible(true);
//    }


    @Override
    public void layout() {
        super.layout();

        if (getStage() != null) {
            Actor focused = getStage().getKeyboardFocus();
            if (focused instanceof TextButton && buttonList.contains((TextButton) focused, true)) {
                TextButton button = (TextButton) focused;

                arrow.setPosition(
                    button.getX() - arrow.getWidth(),
                    button.getY() + (button.getHeight() - arrow.getHeight()) / 2
                );
                arrow.setVisible(true);
                arrow.toFront();
                return;
            }
        }

        // Hide it if nothing is focused on this page
        if (arrow != null) {
            arrow.setVisible(false);
        }
    }
    public void addFocusListeners(){

    }
    public void resize(int width, int height) {
        throw new UnsupportedOperationException("Unimplemented method 'resize'");
    }

    public void openSubMenu(BaseLinearMenu nextMenu){
        this.setVisible(false);
        nextMenu.setParentMenu(this);
        this.getStage().addActor(nextMenu);
        nextMenu.refreshAndSetActive();
    }

    public void returnToParentMenu(){
        if (parentMenu != null) {
            this.setVisible(false);
            parentMenu.refreshAndSetActive();
            parentMenu.recallMenuSelection();
            this.remove();
            if(this.subStatusMenu != null){
                this.subStatusMenu.setVisible(false);
                this.subStatusMenu.remove();
            }
        } else {
            Gdx.app.log("ERROR", "RETURN TOI PARENT MENU CALLED WHEN NO PARENT MENU EXISTS");
        }
    }


    public BaseLinearMenu getRootMenu() {
        return (parentMenu == null) ? this : parentMenu.getRootMenu();
    }

    public void closeMenuStack() {
        this.setVisible(false);
        if (parentMenu != null) {
            this.remove();
            parentMenu.closeMenuStack();
        } else {
            if (getStage() != null) {
                getStage().setKeyboardFocus(this.buttonList.first());
            }
        }
    }

    public void setSubMenu(BaseLinearMenu subMenu) {
        this.subMenu = subMenu;
    }

    public void setParentMenu(BaseLinearMenu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setSubStatusMenu(StandardStatusMenu subStatusMenu) {
        this.subStatusMenu = subStatusMenu;
    }

    public void setToggleable(boolean toggle){
        isToggleable = toggle;
    }

}
