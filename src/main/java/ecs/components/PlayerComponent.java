package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerComponent implements Component {
    public float maxSpeed = 100.0f;
    public float scale = 2;
    public OrthographicCamera camera;

    public PlayerComponent(OrthographicCamera camera) {
        this.camera = camera;
    }
}
