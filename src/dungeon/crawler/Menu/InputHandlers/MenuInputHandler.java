package dungeon.crawler.Menu.InputHandlers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.Controls.GameInputObserver;
import dungeon.crawler.Controls.GameKey;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Observers.MenuInputObserver;

public class MenuInputHandler extends InputAdapter implements GameInputObserver {
public Stage uiStage;
public BaseLinearMenu currentMenuTable;
public BaseLinearMenu rootMenu;
private GameInputHandler gameInputHanlder;
public boolean showMenu = false;

public int menuColumns = 0;
public int menuRows = 0;
public int menuColumnIndex = 0;
public int menuRowIndex = 0;
public TextButton currentButton;

// if another input handler is used but the current menu is visible
private boolean handlerDisabled;

private final List<MenuInputObserver> listeners = new ArrayList<>();
    public MenuInputHandler(
        Stage uiStage,
        BaseLinearMenu currentMenuTable
    ){
        this.uiStage = uiStage;
        this.currentMenuTable = currentMenuTable;
        this.rootMenu = currentMenuTable;

        handlerDisabled = false;
    }

    public MenuInputHandler (
        Stage uiStage,
        BaseLinearMenu currentMenuTable,
        GameInputHandler gameInputHanlder
    ) {
        this.uiStage = uiStage;
        this.currentMenuTable = currentMenuTable;
        this.rootMenu = currentMenuTable;
        this.gameInputHanlder = gameInputHanlder;
        this.gameInputHanlder.addListener(this);
        handlerDisabled = false;
    }


    private void updateCurrentMenuFromFocus() {
        Actor focused = uiStage.getKeyboardFocus();
        if (focused != null) {
            Actor parent = focused;
            while (parent != null) {
                if (parent instanceof BaseLinearMenu) {
                    this.currentMenuTable = (BaseLinearMenu) parent;
                    return;
                }
                parent = parent.getParent();
            }
        } else {
            this.currentMenuTable = rootMenu;
            return;
        }
    }

    @Override
    public void onAction(GameKey key) {
        if(handlerDisabled) {
            return;
        }
        updateCurrentMenuFromFocus();
        if(key == GameKey.MENU && rootMenu.isToggleable) {
            boolean rootVisible = rootMenu.isVisible();

            if(rootVisible) {
                currentMenuTable.closeMenuStack();
            } else {
                rootMenu.refreshAndSetActive();
            }

            notifyOnMenuToggled(!rootVisible);
            return;
        }

        if(menuFocusAvailable()) {
            if(key == GameKey.CONFIRM){
                Actor focused = uiStage.getKeyboardFocus();
                if (focused instanceof Button) {
                    ((Button) focused).toggle(); // Toggles isChecked and fires the listener
                }
            }
            if(key == GameKey.DOWN) {
                currentMenuTable.advanceMenuSelection(1);
            }
            if(key == GameKey.UP) {
                currentMenuTable.advanceMenuSelection(-1);
            }
            if(key == GameKey.CANCEL){
                currentMenuTable.returnToParentMenu();
            }
        }
    }
    public void setCurrentMenuTable(BaseLinearMenu currentMenuTable) {
        this.currentMenuTable = currentMenuTable;
    }

    public boolean menuFocusAvailable(){
        if(
            currentMenuTable != null &&
            currentMenuTable.isVisible()
        ){
            return true;
        }
        return false;
    }

    public void notifyOnMenuToggled(boolean showMenu) {
        for (MenuInputObserver listener : listeners) {
            listener.onMenuToggled(showMenu);
        }
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

    public void setHandlerDisabled(boolean value){
        handlerDisabled = value;
    }


}
