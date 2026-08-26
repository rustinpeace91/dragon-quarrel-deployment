package dungeon.crawler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import dungeon.crawler.GameConstants;
import dungeon.crawler.MainGame;
import dungeon.crawler.Observers.ScreenChangeObserver;

public class SplashScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final BitmapFont font;
    protected final ScreenChangeObserver listener;
    protected float timeAmount;
    protected String textDisplay;
    protected MainGame game;

    public interface SplashListener {
        void onSplashFinished();
    }

    public SplashScreen(MainGame game, ScreenChangeObserver listener) {
        this.listener = listener;
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    @Override
    public void show(){
        timeAmount = 3.0f;
        textDisplay = "yeah";
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (listener != null) listener.onScreenChange(GameConstants.GAME_SCREEN.WALK_TOWN);
            }
        }, timeAmount); 
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, textDisplay, 0, Gdx.graphics.getHeight() / 2f, 
                  Gdx.graphics.getWidth(), Align.center, false);
        batch.end();
    }

    @Override
    public void hide() {
        Timer.instance().clear(); 
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}