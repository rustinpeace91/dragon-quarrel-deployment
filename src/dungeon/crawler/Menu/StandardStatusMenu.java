package dungeon.crawler.Menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import dungeon.crawler.GameConstants;

// Extend Table directly
public class StandardStatusMenu extends Table {
    private Label messageLabel;

    public StandardStatusMenu(Skin skin) {
        super(skin); // Pass skin to parent Table
        this.setSize(156f, 120f);

        // Set the background and gray tint
        this.setBackground(skin.getDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT));
        Color semiTransparentGray = new Color(0.2f, 0.2f, 0.2f, 1f);
        this.setBackground(skin.newDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT, semiTransparentGray));



        messageLabel = new Label("", skin);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.topLeft);
        // Add the label to 'this' table
        this.add(messageLabel).expand().fill().pad(10f, 10f, 10f, 10f);

    }



    public void setText(String text) {
        messageLabel.setText(text);
    }

    /* this is a stupid hack, but because this is not a 'child' menu like the combat menus it does not have access to stage
    so stage needs to be fed in to the method. Better than refactoring the damn thing
     */
    public void alignTopRight(Stage stage) {
        float width = GameConstants.COMBAT_SUBMENU_WIDTH + GameConstants.COMBAT_SUBMENU_WIDTH_DIFF;
        float height = 600f;
        this.setSize(width, height);

        float padding = 5f;
        float targetX = stage.getWidth() - this.getWidth() - padding;
        float targetY = stage.getHeight() - this.getHeight() - padding;

        this.setPosition(targetX, targetY);
    }
}
