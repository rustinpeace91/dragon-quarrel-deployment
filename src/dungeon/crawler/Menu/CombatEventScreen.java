package dungeon.crawler.Menu;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import dungeon.crawler.Controls.GameInputHandler;
import dungeon.crawler.Controls.GameInputObserver;
import dungeon.crawler.Controls.GameKey;
import dungeon.crawler.GameConstants;
import dungeon.crawler.Observers.EventScreenObserver;

// Extend Table directly
public class CombatEventScreen extends Table implements GameInputObserver {
    private final GameInputHandler gameInputHandler;
    private Label messageLabel;
    private boolean isShowing;
    public LinkedList<String> messageQueue;
    private ArrayList<EventScreenObserver> observers;

    public CombatEventScreen(Skin skin, GameInputHandler gameInputHandler) {
        super(skin); // Pass skin to parent Table

        // Set the background and gray tint
        this.setBackground(skin.getDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT));
        Color semiTransparentGray = new Color(0.2f, 0.2f, 0.2f, 0.8f);
        this.setBackground(skin.newDrawable(GameConstants.SKIN_BACKGROUND_DEFAULT, semiTransparentGray));

        messageLabel = new Label("", skin);
        observers = new ArrayList<>();
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        this.gameInputHandler = gameInputHandler;
        // Add the label to 'this' table
        this.add(messageLabel).width(300f).pad(30f);
        this.messageQueue = new LinkedList<>();
        this.pack();
        // Listen for the Enter key
//        this.addListener(new InputListener() {
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                if (keycode == Keys.ENTER) {
//                    showNextMessage();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    public void showNextMessage() {
        if(!messageQueue.isEmpty()){
            messageLabel.setText(messageQueue.poll());
        } else {
            isShowing = false;
            messageLabel.setText("");
            notifyOnLastMessageRead();
        }
    }
    public void setText(String text) {
        messageLabel.setText(text);
    }

    public void addListener(EventScreenObserver observer){
        observers.add(observer);
    }

    public void notifyOnFirstMessageAdded(){
        for(EventScreenObserver observer: observers){
            observer.onFirstMessageAdded();
        }
    }

    public void notifyOnLastMessageRead(){
        for(EventScreenObserver observer: observers){
            observer.onLastMessageRead();
        }
    }

    public void addMessages(String[] messages){
        if(messageQueue.isEmpty()){
            notifyOnFirstMessageAdded();
        }
        isShowing = true;
        for(String s: messages){
            messageQueue.add(s);
        }
        if (messageLabel.getText().toString().isEmpty() && !messageQueue.isEmpty()) {
            messageLabel.setText(messageQueue.poll());
            notifyOnFirstMessageAdded();
        }
    }
    public boolean isShowingMessage() {
        return isShowing;

    }

    @Override
    public void onAction(GameKey key) {

        if (key == GameKey.CONFIRM) {
            showNextMessage();
        }
    }
}
