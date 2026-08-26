package dungeon.crawler.Controls;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

public class GameInputHandler  extends InputAdapter {

    private GameKey controllerHeld = null;
    private final ArrayList<GameInputObserver> listeners =
        new ArrayList<GameInputObserver>();

    public GameInputHandler() {
        // TODO: We need to only implement this on desktop

    }



    public boolean held(GameKey key) {


        switch (key) {
            case UP:
                return Gdx.input.isKeyPressed(Input.Keys.UP)
                    || controllerHeld == GameKey.UP;

            case DOWN:
                return Gdx.input.isKeyPressed(Input.Keys.DOWN)
                    || controllerHeld == GameKey.DOWN;

            case LEFT:
                return Gdx.input.isKeyPressed(Input.Keys.LEFT)
                    || controllerHeld == GameKey.LEFT;

            case RIGHT:
                return Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                    || controllerHeld == GameKey.RIGHT;

//            case CONFIRM:
//                return Gdx.input.isKeyPressed(Input.Keys.ENTER);
//
//            case CANCEL:
//                return Gdx.input.isKeyPressed(Input.Keys.BACKSPACE);
//
//            case MENU:
//                return Gdx.input.isKeyPressed(Input.Keys.E);

            default:
                return false;
        }
    }


    public boolean pressed(GameKey key) {
        switch (key) {
            case UP:
                return Gdx.input.isKeyJustPressed(Input.Keys.UP);
            case DOWN:
                return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
            case LEFT:
                return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
            case RIGHT:
                return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
            case CONFIRM:
                return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
            case CANCEL:
                return Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE);
            case MENU:
                return Gdx.input.isKeyJustPressed(Input.Keys.E);
            default:
                return false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                notifyAction(GameKey.UP);
                break;
            case Input.Keys.DOWN:
                notifyAction(GameKey.DOWN);
                break;
            case Input.Keys.LEFT:
                notifyAction(GameKey.LEFT);
                break;
            case Input.Keys.RIGHT:
                notifyAction(GameKey.RIGHT);
                break;
            case Input.Keys.ENTER:
                notifyAction(GameKey.CONFIRM);
                break;
            case Input.Keys.BACKSPACE:
                notifyAction(GameKey.CANCEL);
                break;
            case Input.Keys.E:
                notifyAction(GameKey.MENU);
                break;
            default:
                return false;
        }
        return true;
    }

    public void addListener(GameInputObserver listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(GameInputObserver listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void notifyAction(GameKey action) {
        for (GameInputObserver listener : listeners) {
            listener.onAction(action);
        }
    }

    public void setControllerHeld(GameKey key) {
        controllerHeld = key;
    }

    public void clearControllerHeld() {
        controllerHeld = null;
    }



}
