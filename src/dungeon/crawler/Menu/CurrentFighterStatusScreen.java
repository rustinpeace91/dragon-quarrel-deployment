package dungeon.crawler.Menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import dungeon.crawler.GameConstants;

public class CurrentFighterStatusScreen extends Table {
    private Label messageLabel;
    private float statusScreenHeight;

    public CurrentFighterStatusScreen(Skin skin, float statusScreenHeight) {
        super(skin); // Pass skin to parent Table
        this.setSize(128f, statusScreenHeight); 
        this.statusScreenHeight = statusScreenHeight;

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

    public CurrentFighterStatusScreen(Skin skin, Label messageLabel) {
        super(skin);
        this.messageLabel = messageLabel;
    }

    public void setText(String text) {
        messageLabel.setText(text);
    }
}
