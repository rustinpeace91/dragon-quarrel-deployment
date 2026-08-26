package dungeon.crawler.Menu;

import dungeon.crawler.GameConstants;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.compression.lzma.Base;
public interface CombatSubMenu {
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

    default void setSizeandPosition(GameConstants.SUBMENU_SIZE size){
        /* Honestly....I have no idea how tf this works and I don't care anymore.  The main point
        is it's modular, and can be edited from the constants file if we want to change the font size later
         */

        asCombatMenu().top();

        float targetX = asCombatMenu().parentMenu.getStage().getWidth();
        float targetY = asCombatMenu().parentMenu.getTop();
        float menuHeight = targetY;

        asCombatMenu().setSize(
            GameConstants.COMBAT_SUBMENU_WIDTH + GameConstants.COMBAT_SUBMENU_WIDTH_DIFF,
            menuHeight
        );

        asCombatMenu().setOrigin(Align.topRight);
        asCombatMenu().setPosition(targetX, targetY, Align.topRight);
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
