package dungeon.crawler.AssetManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    private final AssetManager manager = new AssetManager();

    public void load() {
        manager.load("ui/arrow.png", Texture.class);
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public Texture getArrow() {
        return manager.get("ui/arrow.png", Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }
}