package dungeon.crawler.Menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import dungeon.crawler.GameConstants;

// Extend Table directly
public class CombatPartyOrderScreen extends Table {
    private Label messageLabel;

    public CombatPartyOrderScreen(Skin skin) {
        super(skin); // Pass skin to parent Table
        this.setSize(250f, 480f);

        // Set the background and gray tint
        this.setBackground(skin.getDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT));
        Color semiTransparentGray = new Color(0.2f, 0.2f, 0.2f, 0.8f);
        this.setBackground(skin.newDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT, semiTransparentGray));

        messageLabel = new Label("", skin);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.topLeft);
        // Add the label to 'this' table
        this.add(messageLabel).expand().fill().pad(20f, 20f, 20f, 20f);

    }

    public void setText(String text) {
        messageLabel.setText(text);
    }

    public static interface OverworldSubMenu {
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

        default void setSizeandPosition(){
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
}
