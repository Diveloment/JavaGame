package managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import data.AnimationDecoder;
import ecs.components.PositionComponent;
import ecs.components.RotationComponent;

import java.util.Objects;

public class VFXManager extends GameManager {

    private static Long nextId = 0L;

    private Long getNextId() {
        nextId++;
        return nextId;
    };

    private ObjectMap<Long, Asset> activeEffects = new ObjectMap<Long, Asset>();

    public abstract static class Asset {
        public float duration;
        public float stateTime;
        public Vector2 position;
        public float angle;
        public float scale;
        public PositionComponent positionComponent;
        public RotationComponent rotationComponent;
        public Vector2 origin;

        public abstract void draw(SpriteBatch spriteBatch);
        public abstract void dispose();
        public abstract void setDuration(float duration);

        public Asset() {
        }

        public Asset(float duration, float scale, float angle) {
            this.duration = duration;
            this.scale = scale;
            this.angle = angle;
        }

        public Asset(Asset otherAsset) {
            this.duration = otherAsset.duration;
            this.stateTime = otherAsset.stateTime;
            this.position = otherAsset.position;
            this.angle = otherAsset.angle;
            this.scale = otherAsset.scale;
            this.positionComponent = otherAsset.positionComponent;
            this.rotationComponent = otherAsset.rotationComponent;
            this.origin = otherAsset.origin;
        }
    }

    public static class SpriteAsset extends Asset {
        public Sprite sprite;

        public SpriteAsset(Sprite sprite) {
            this.sprite = sprite;
            this.origin = new Vector2(this.sprite.getWidth()/2, this.sprite.getHeight()/2);
        }

        public SpriteAsset(Sprite sprite, float duration, float scale, float rotation) {
            super(duration, scale, rotation);
            this.sprite = sprite;
            this.origin = new Vector2(this.sprite.getWidth()/2, this.sprite.getHeight()/2);
        }

        public SpriteAsset(SpriteAsset other) {
            super(other);
            this.sprite = new Sprite(other.sprite);
        }

        @Override
        public void draw(SpriteBatch spriteBatch) {
            if (positionComponent == null)
                sprite.setPosition(this.position.x - origin.x, this.position.y - origin.y);
            else
                sprite.setPosition(positionComponent.x - origin.x, positionComponent.y - origin.y);
            if (rotationComponent == null)
                sprite.setRotation(this.angle);
            else
                sprite.setRotation(rotationComponent.a);
            sprite.setScale(scale);
            sprite.draw(spriteBatch);
        }

        @Override
        public void dispose() {
        }

        @Override
        public void setDuration(float duration) {
            this.duration = duration;
        }
    }

    public static class AnimationAsset extends Asset {
        public Animation<TextureRegion> animation;

        @Override
        public void setDuration(float duration) {
            this.duration = duration;
            this.animation.setFrameDuration(duration / this.animation.getKeyFrames().length);
        }

        public AnimationAsset(Animation<TextureRegion> animation) {
            this.animation = animation;
            this.animation.setFrameDuration(duration / this.animation.getKeyFrames().length);
            this.origin = new Vector2(this.animation.getKeyFrame(0).getRegionWidth()/2, this.animation.getKeyFrame(0).getRegionHeight()/2);
        }

        public AnimationAsset(Animation<TextureRegion> animation, float duration, float scale, float rotation) {
            super(duration, scale, rotation);
            this.animation = animation;
            this.animation.setFrameDuration(duration / this.animation.getKeyFrames().length);
            this.origin = new Vector2(this.animation.getKeyFrame(0).getRegionWidth()/2, this.animation.getKeyFrame(0).getRegionHeight()/2);
        }

        public AnimationAsset(AnimationAsset otherAnimation) {
            super(otherAnimation);
            this.animation = new Animation<TextureRegion>(otherAnimation.animation.getFrameDuration(), otherAnimation.animation.getKeyFrames());
            this.animation.setFrameDuration(duration / this.animation.getKeyFrames().length);
        }

        @Override
        public void draw(SpriteBatch spriteBatch) {
            TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
            Sprite sprite = new Sprite(currentFrame);

            if (positionComponent == null)
                sprite.setPosition(this.position.x - origin.x, this.position.y - origin.y);
            else
                sprite.setPosition(positionComponent.x - origin.x, positionComponent.y - origin.y);
            if (rotationComponent == null)
                sprite.setRotation(this.angle);
            else
                sprite.setRotation(rotationComponent.a);
            sprite.setScale(scale);
            sprite.draw(spriteBatch);
        }

        @Override
        public void dispose() {
        }
    }


    public static VFXManager instance;
    private SpriteBatch batch;
    public ResourceManager resourceManager;

    public static VFXManager getInstance(SpriteBatch batch) {
        if (instance == null) {
            instance = new VFXManager();
            instance.resourceManager = ResourceManager.getInstance();
            instance.batch = batch;
        }
        return instance;
    }

    public static VFXManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("VFXManager is not initialized");
        return instance;
    }

    public Asset makeSpriteAsset(String path) {
        Sprite sprite = (Sprite) resourceManager.getResource(path, Sprite.class);
        return new SpriteAsset(sprite);
    }

    public Asset makeSpriteAsset(SpriteAsset asset) {
        return new SpriteAsset(asset);
    }

    public Asset makeAnimationAsset(String path) {
        Animation<TextureRegion> animation = AnimationDecoder.makeAnimation(
                (Texture) resourceManager.getResource(path, Texture.class),
                0.05f
        );
        return new AnimationAsset(animation);
    }

    public Asset makeAnimationAsset(AnimationAsset asset) {
        return new AnimationAsset(asset);
    }

    public Asset spawnEffect(Asset asset, Vector2 position, float duration, float angle) {
        asset.position = position;
        asset.setDuration(duration);
        asset.angle = angle;
        asset.stateTime = 0.0f;
        activeEffects.put(getNextId(), asset);
        return asset;
    }

    public Asset spawnEffect(Asset asset, Vector2 position) {
        asset.position = position;
        asset.stateTime = 0.0f;
        activeEffects.put(getNextId(), asset);
        return asset;
    }

    public Asset spawnEffect(Asset asset, PositionComponent positionComponent, float duration, RotationComponent rotationComponent) {
        asset.positionComponent = positionComponent;
        asset.setDuration(duration);
        asset.rotationComponent = rotationComponent;
        asset.stateTime = 0.0f;
        activeEffects.put(getNextId(), asset);
        return asset;
    }

    @Override
    public void update(float deltaTime) {
        for (ObjectMap.Entry<Long, Asset> entry : activeEffects.entries()) {
            Asset vfx = entry.value;
            if (Objects.isNull(vfx))
                continue;
            render(vfx);
            vfx.stateTime += deltaTime;

            if (vfx.stateTime > vfx.duration) {
                // Эффект завершился, удаляем его
                activeEffects.remove(entry.key);
            }
        }
    }

    private void render(Asset vfx) {
        vfx.draw(batch);
    }
}
