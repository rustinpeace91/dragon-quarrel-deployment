package dungeon.crawler.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationBuilder {

    // probably make util class or something
    public static Animation<TextureRegion> createAnimation(Texture sheet, int cols, int rows, float frameDuration) {
    // TODO: select texture animation by row itself
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    // If one row of a spritesheet contains your animation, use this
    public static Animation<TextureRegion> createAnimationByRow(
        Texture sheet,
        int cols,
        int rows,
        int selectedRow,
        float frameDuration
    ){
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols];
        int index = 0;
        // TODO: Find a more performant and more efficient way to do this
        for (int i = 0; i < rows; i++) {
            if(i + 1 == selectedRow) {
                for (int j = 0; j < cols; j++) {
                    frames[index++] = tmp[i][j];
                }
                break;
            }
        }
        return new Animation<>(frameDuration, frames);
    }
}
