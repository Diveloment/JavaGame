package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class BulletComponent implements Component {
    public Vector2 velocity = new Vector2();
    public Vector2 lastPosition = new Vector2();
    public Entity owner;
    public float scale = 0.75f;
    public float ttl = 5;
}

