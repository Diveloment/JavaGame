package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class AimPointComponent implements Component {
    public float x, y;

    public AimPointComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 getAimPoint() {
        return new Vector2(x, y);
    }

    public void setAimPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
