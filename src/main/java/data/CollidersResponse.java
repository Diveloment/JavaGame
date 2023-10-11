package data;

import com.badlogic.ashley.core.Entity;

import java.util.Set;

public class CollidersResponse {
    public Set<Entity> collidersEntities;
    public boolean isColliding;
    public int collisionsCount;

    public CollidersResponse() {
    }

    public CollidersResponse(Set<Entity> collidersEntities, boolean isColliding, int collisionsCount) {
        this.collidersEntities = collidersEntities;
        this.isColliding = isColliding;
        this.collisionsCount = collisionsCount;
    }
}
