package data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationDecoder {
    public static Animation<TextureRegion> makeAnimation(Texture texture, float frameDuration) {
        final int FRAMES_NUM = texture.getWidth() / texture.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/FRAMES_NUM, texture.getHeight());

        TextureRegion[] frames = new TextureRegion[FRAMES_NUM];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < FRAMES_NUM; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        Animation<TextureRegion> animation = new Animation<TextureRegion>(frameDuration, frames);
        return animation;
    }
}
