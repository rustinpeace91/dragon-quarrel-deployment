package dungeon.crawler.Sprites;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite {

    private Map<String, Animation<TextureRegion>> animations;
    private String currentState;
    private float stateTime;
    private Sprite sprite;

    public boolean walking;


    public AnimatedSprite(
        Map<String, Animation<TextureRegion>> animations,
        String initialState,
        float x,
        float y,
        float sx,
        float sy
    ) {
        this.currentState = initialState;
        this.stateTime = 0f;
        TextureRegion firstFrame = animations.get(initialState).getKeyFrame(0);

        this.animations = animations;
        this.sprite = new Sprite(firstFrame);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(sx, sy);
        this.walking = false;

    }

    public void setState(String newState) {
        if(!newState.equals(currentState)) {
            currentState = newState;
            stateTime = 0f;
        }
    }

    public String getState() {
        return currentState;
    }

    public void update(float delta) {
        stateTime += delta;
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
        sprite.setRegion(currentAnimation.getKeyFrame(stateTime, true));
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void moveBy(float dx, float dy) {
        sprite.translate(dx, dy);
    }

    public Sprite getSprite() {
        return sprite;
    }

}
