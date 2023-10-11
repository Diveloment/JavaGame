package ecs.components;

import com.badlogic.ashley.core.Component;

public class RotationComponent implements Component {
    public float a;

    public RotationComponent(float a) {
        this.a = a;
    }
}
