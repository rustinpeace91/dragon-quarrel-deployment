package dungeon.crawler.Menu.Misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import dungeon.crawler.GameConstants;


public class PopUpUtils {
    private static Table activeToast = null;

    public static void showToast(Stage stage, Skin skin, String message, float time) {
        // 1. Dismiss active toast immediately if one is already showing
        if (activeToast != null && activeToast.hasParent()) {
            activeToast.remove();
        }

        // 2. Create the table container
        Table toast = new Table(skin);

//        // Optional: Set a background drawable from your skin if available
//        if (skin.has("default-pane", com.badlogic.gdx.scenes.scene2d.utils.Drawable.class)) {
//            toast.setBackground("default-pane");
//        }
        // Set the background and gray tint
//        toast.setBackground(skin.getDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT));
        Color semiTransparentGray = new Color(0.2f, 0.2f, 0.2f, 1f);
        toast.setBackground(skin.newDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT, semiTransparentGray));

        // 3. Add text label and set padding
        Label label = new Label(message, skin);
        toast.add(label).pad(10f, 15f, 10f, 15f);

        // 4. CRITICAL: Pack calculates the table's width & height based on text size
        toast.pack();

        // 5. Position bottom-center
        float x = (stage.getWidth() - toast.getWidth()) / 2f;
        float y = 50f; // 50 pixels up from the bottom edge
        toast.setPosition(x, y);

        // 6. Disable touch so clicks pass through to menus underneath
        toast.setTouchable(Touchable.disabled);

        activeToast = toast; // Save reference

        // 7. Add animation sequence
        toast.addAction(Actions.sequence(
            Actions.alpha(0f),
            Actions.fadeIn(0.2f),
            Actions.delay(time),
            Actions.fadeOut(0.4f),
            Actions.run(() -> {
                toast.remove();
                if (activeToast == toast) activeToast = null;
            })
        ));

        stage.addActor(toast);
    }
}
