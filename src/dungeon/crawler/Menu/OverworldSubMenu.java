package dungeon.crawler.Menu;


import com.badlogic.gdx.Gdx;
import dungeon.crawler.GameConstants;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.compression.lzma.Base;
public interface OverworldSubMenu {
    /*
       A collection of methods to set the size and dimensions of padding of sub menus in combat. The only
       way I've gotten the menus to work is to do:
       - setDefaults();
       - Whatever the menu does to construct the buttons (varies heavily by menu)
       - setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
       so it's not as modular as I would like, but it will work for the scope of this game.
    */
    BaseLinearMenu asCombatMenu();


    default void setDefaults(){
        asCombatMenu().defaults().size(
            GameConstants.COMBAT_SUBMENU_WIDTH,
            GameConstants.COMBAT_MENU_HEIGHT
        ).pad(5f);
    }

    default void setTitle(String text){
        asCombatMenu().add(text)
            .expandX() // Stretches the cell across the width of the table
            .getActor()
            .setAlignment(Align.center); // Centers text INSIDE the Label
        asCombatMenu().row();
    }
    /* Why do we need to reset defaults for Scrollable menus?
    Becasue it turns out clearChildren() also clears all size settings.
    Why do we need seperate widths for Scrollable menus?  No idea.  Hate it.
     */
    default void scrollableResetDefaults(){
        asCombatMenu().defaults().size(
            GameConstants.SCROLLABLE_SUBMENU_WIDTH,
            GameConstants.COMBAT_MENU_HEIGHT
        ).pad(5f);
    }

    default void FinishMenuComplete(){
    };

    default void setSizeandPosition(GameConstants.SUBMENU_SIZE size){
        /* Honestly....I have no idea how tf this works and I don't care anymore.  The main point
        is it's modular, and can be edited from the constants file if we want to change the font size later
         */
        float menuHeight;
        float menuWidth = GameConstants.COMBAT_SUBMENU_WIDTH + GameConstants.COMBAT_SUBMENU_WIDTH_DIFF;
        switch(size){
            case SMALL:
                menuHeight = 200f;
                break;
            case MEDIUM:
                menuHeight = 300f;
                break;
            case WIDE:
                menuHeight = 300f;
                menuWidth = 400f;
                break;
            default:
                menuHeight = 400f;
                break;

        }

        asCombatMenu().top();
        float targetX = asCombatMenu().parentMenu.getX();
        float targetY = asCombatMenu().parentMenu.getTop();
//        float menuHeight = targetY;
        // remove hardcoding.  have env variables?
        asCombatMenu().setSize(
            menuWidth,
            menuHeight
        );
        asCombatMenu().setPosition(
            asCombatMenu().parentMenu.getOriginX() + 200,
            Gdx.graphics.getHeight() - asCombatMenu().getHeight() - 20)
        ;

//        asCombatMenu().setOrigin(Align.topRight);
//        asCombatMenu().setPosition(targetX, targetY);


    }
}



// example setStage

//    @Override
//    protected void setStage(Stage stage) {
//        super.setStage(stage);
//        if(parentMenu != null){
//            combatMenu = (CombatMenu)parentMenu;
//
//            setDefaults();
//
//            this.clearChildren();
//            this.attackButtons();
//            setSizeandPosition(GameConstants.SUBMENU_SIZE.TALL);
//
//
//        if (stage != null) {
//            refreshAndSetActive();
//        }
//}
