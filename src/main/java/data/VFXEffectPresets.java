package data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import managers.ResourceManager;
import managers.VFXManager;
import managers.VFXManager.AnimationAsset;
import managers.VFXManager.SpriteAsset;

public class VFXEffectPresets {
    public static final SpriteAsset TEST_VFX = new SpriteAsset((Sprite) ResourceManager.getInstance().getResource("assets/cube.png", Sprite.class),
            2.0f, 1.0f, 0.0f);
    public static final AnimationAsset BULLET_HIT_SMALL = new AnimationAsset(AnimationDecoder.makeAnimation(
            (Texture) ResourceManager.getInstance().getResource("assets/bullet_hit_small.png", Texture.class), 0.05f
    ), 1.0f, 0.85f, 0.0f);
}
