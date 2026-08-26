package dungeon.crawler.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.compression.lzma.Base;
import dungeon.crawler.Menu.Overworld.Inventory.InventoryOptions;

import java.sql.Array;
import java.util.ArrayList;
/* Menu generated from a dynamic list of objects */


public abstract class ScrollableLinearMenu<T> extends BaseLinearMenu {
    protected final int MAX_ROWS = 5;
    protected int pageStart;
    protected int pageEnd;
    protected int currentPage;
    private ArrayList<T> menuItems;
    private PagePosition pagePos;

    public ScrollableLinearMenu(Skin skin) {
        super(skin);
        pageStart = 0;
        menuItems = new ArrayList<>();
        pageEnd = Math.min(MAX_ROWS, menuItems.size());
        pagePos = PagePosition.FORWARD;
    }

    public void intializeItems(ArrayList<T> items){
        this.menuItems = items;
        pageEnd = Math.min(MAX_ROWS, menuItems.size());
    }

    public void addScrollArrowUp(){
        if(pageStart > 0){
            this.addFocusButton("^",
                new FocusListener() {
                    @Override
                    public void keyboardFocusChanged(
                        FocusEvent event,
                        Actor actor,
                        boolean focused
                    ) {
                        if (focused) {
                            pageBackward();
                        }
                    }
                }
            );
        }
    }

    public void addScrollArrowDown() {
        if (pageEnd < menuItems.size()) {
            this.addFocusButton("v",
                new FocusListener() {
                    @Override
                    public void keyboardFocusChanged(
                        FocusEvent event,
                        Actor actor,
                        boolean focused
                    ) {
                        if (focused) {
                            pageForward();
                        }
                    }
                }
            );
        }
    }

    @Override
    public void refreshAndSetActive(){
        if(getStage() == null) {
            Gdx.app.log("Menu Error", "refreshAndSetActive called BEFORE linear menu added to stage");
            // no return. Let it break the game
        }
        setVisible(true);

        this.buttonList = populateButtonList();
        if (buttonList != null && buttonList.size > 0) {
            if(pageStart == 0) {
                currentButtonIndex = 0;
            } else if(pagePos == PagePosition.FORWARD){
                if(buttonList.size < 2){
                    currentButtonIndex = 0;
                } else {
                    currentButtonIndex = 1;
                }
            } else {
                currentButtonIndex = MAX_ROWS - 1;
            }
            getStage().setKeyboardFocus(buttonList.get(currentButtonIndex));

            recallMenuSelection();

        } else {
            getStage().setKeyboardFocus(null);
        }
    }

    /* for dynamic menus. if there are no items we want a back button because BaseLinearMenu does not
    support empty menus (my bad)
     */
    public void addBackButton(){
        if(menuItems.size() < 1){
            this.addButton("Back",
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        returnToParentMenu();
                    }
                }
            );
        }
    }
    protected abstract void updateButtons();
    public void pageForward(){
        pageStart = pageEnd;
        pageEnd = Math.min(pageStart + MAX_ROWS, menuItems.size());
        pagePos = PagePosition.FORWARD;
        updateButtons();
    };



    public void pageBackward(){
        pageStart = Math.max(pageStart - MAX_ROWS, 0);
        pageEnd = Math.min(pageStart + MAX_ROWS, menuItems.size());
        pagePos = PagePosition.BACKWARD;
        updateButtons();
    }
}
