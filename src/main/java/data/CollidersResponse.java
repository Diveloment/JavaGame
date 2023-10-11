package data;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class CollidersResponse {
    public Array<Entity> collidersEntities;
    public boolean isColliding;
    public int collisionsCount;

    public CollidersResponse() {
    }

    public CollidersResponse(Array<Entity> collidersEntities, boolean isColliding, int collisionsCount) {
        this.collidersEntities = collidersEntities;
        this.isColliding = isColliding;
        this.collisionsCount = collisionsCount;
    }
}
