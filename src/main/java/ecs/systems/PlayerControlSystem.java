package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import ecs.components.PlayerComponent;
import ecs.components.PositionComponent;
import ecs.components.SpriteComponent;
import ecs.components.VelocityComponent;

public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<VelocityComponent> velocityMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<SpriteComponent> spriteMapper;

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class, PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        VelocityComponent velocity = velocityMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        SpriteComponent spriteComponent = spriteMapper.get(entity);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Обработка клавиш управления
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -1.0f * playerComponent.maxSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = 1.0f * playerComponent.maxSpeed;
        } else {
            velocity.x = 0.0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = 1.0f * playerComponent.maxSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -1.0f * playerComponent.maxSpeed;
        } else {
            velocity.y = 0.0f;
        }

        float angle = MathUtils.atan2(mouseY - spriteComponent.sprite.getY(), mouseX - spriteComponent.sprite.getX()) * MathUtils.radiansToDegrees;
        spriteComponent.sprite.setRotation(angle);
        spriteComponent.sprite.setScale(playerComponent.scale);
        spriteComponent.sprite.setPosition(position.x - spriteComponent.sprite.getWidth() / 2, position.y - spriteComponent.sprite.getHeight() / 2);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }
}
