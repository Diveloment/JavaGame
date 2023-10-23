package managers;

import com.badlogic.ashley.core.Engine;

public abstract class GameManager {

    Engine engine;

    abstract void update(float deltaTime);
}
