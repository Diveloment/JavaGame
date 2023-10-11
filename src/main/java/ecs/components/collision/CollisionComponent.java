package ecs.components.collision;

import com.badlogic.ashley.core.Component;

public class CollisionComponent implements Component {
    public float radius = 16;

    public CollisionComponent() {
    }

    public CollisionComponent(float radius) {
        this.radius = radius;
    }
}
