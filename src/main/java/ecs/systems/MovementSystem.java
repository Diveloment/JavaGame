package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import ecs.components.*;
import ecs.components.physics.PhysicsComponent;

import java.util.Objects;

import static app.Powers.DEFAULT_SCALING;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<VelocityComponent> velocityMapper;

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        PositionComponent positionComponent = positionMapper.get(entity);
        VelocityComponent velocityComponent = velocityMapper.get(entity);

        if (Objects.nonNull(physicsComponent)) {
            if (!physicsComponent.initialized) {
                physicsComponent.body.setTransform(positionComponent.getPosition().scl(1/DEFAULT_SCALING), 0.0f);
                physicsComponent.initialized = true;
            }
            physicsComponent.body.setLinearVelocity(velocityComponent.x * deltaTime,
                    velocityComponent.y * deltaTime);
            positionComponent.setPosition(physicsComponent.body.getPosition().scl(DEFAULT_SCALING));
        } else {
            positionComponent.x += velocityComponent.x * deltaTime;
            positionComponent.y += velocityComponent.y * deltaTime;
        }
    }
}
