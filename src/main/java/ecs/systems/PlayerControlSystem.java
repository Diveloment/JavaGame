package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import ecs.components.*;

public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<VelocityComponent> velocityMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<HardpointsComponent> hardpointsMapper;
    private ComponentMapper<RotationComponent> rotationMapper;

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class, PositionComponent.class, VelocityComponent.class, SpriteComponent.class, RotationComponent.class).get());
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        rotationMapper = ComponentMapper.getFor(RotationComponent.class);
        velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        hardpointsMapper = ComponentMapper.getFor(HardpointsComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        RotationComponent rotation = rotationMapper.get(entity);
        VelocityComponent velocity = velocityMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        SpriteComponent spriteComponent = spriteMapper.get(entity);
        HardpointsComponent hardpointsComponent = hardpointsMapper.get(entity);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        // Обработка клавиш управления
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -1.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = 1.0f;
        } else {
            velocity.x = 0.0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = 1.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -1.0f;
        } else {
            velocity.y = 0.0f;
        }

        hardpointsComponent.isShooting = Gdx.input.isButtonPressed(0) || Gdx.input.isKeyPressed(Input.Keys.SPACE);;

        Sprite playerSprite = spriteComponent.sprite;

        Vector3 worldCoordinates = new Vector3(mouseX, mouseY, 0);
        playerComponent.camera.unproject(worldCoordinates);

        float mouseWorldX = worldCoordinates.x;
        float mouseWorldY = worldCoordinates.y;

        rotation.a = MathUtils.atan2(mouseWorldY - position.y, mouseWorldX - position.x) * MathUtils.radiansToDegrees;
        playerSprite.setScale(playerComponent.scale);

        playerComponent.camera.position.set(playerSprite.getX() + playerSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2, 0);

        position.x += velocity.x * playerComponent.maxSpeed * deltaTime;
        position.y += velocity.y * playerComponent.maxSpeed * deltaTime;

        for (Entity weapon : hardpointsComponent.weapons) {
            weapon.getComponent(AimPointComponent.class).setAimPoint(mouseWorldX, mouseWorldY);
            weapon.getComponent(WeaponComponent.class).owner = entity;
        }
    }
}
