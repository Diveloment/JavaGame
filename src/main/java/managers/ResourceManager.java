package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager<T> {
    private Map<String, T> resourceCache = new HashMap<>();

    public T getResource(String filePath, Class<T> resourceType) {
        if (resourceCache.containsKey(filePath)) {
            return resourceCache.get(filePath);
        } else {
            T resource = loadResource(filePath, resourceType);
            if (resource != null) {
                resourceCache.put(filePath, resource);
            }
            return resource;
        }
    }

    private T loadResource(String filePath, Class<T> resourceType) {
        if (resourceType == Texture.class) {
            return (T) new Texture(Gdx.files.internal(filePath));
        }
        if (resourceType == Sprite.class) {
            return (T) new Sprite(new Texture(Gdx.files.internal(filePath)));
        }

        return null;
    }

    public void disposeResource(String filePath) {
        if (resourceCache.containsKey(filePath)) {
            T resource = resourceCache.get(filePath);
            if (resource instanceof Disposable) {
                ((Disposable) resource).dispose();
            }
            resourceCache.remove(filePath);
        }
    }

    public void disposeAllResources() {
        for (T resource : resourceCache.values()) {
            if (resource instanceof Disposable) {
                ((Disposable) resource).dispose();
            }
        }
        resourceCache.clear();
    }
}
