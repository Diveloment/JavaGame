package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import ecs.components.*;

public class HardpointsSystem extends IteratingSystem {
    private ComponentMapper<HardpointsComponent> hardpointsMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<RotationComponent> rotationMapper;

    public HardpointsSystem() {
        super(Family.all(HardpointsComponent.class, PlayerComponent.class, RotationComponent.class).get());
        hardpointsMapper = ComponentMapper.getFor(HardpointsComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        rotationMapper = ComponentMapper.getFor(RotationComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        HardpointsComponent hardpoints = hardpointsMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        RotationComponent rotation = rotationMapper.get(entity);

        for (Entity weapon : hardpoints.weapons) {
            WeaponComponent weaponComponent = weapon.getComponent(WeaponComponent.class);
            SpriteComponent spriteComponent = weapon.getComponent(SpriteComponent.class);
            PositionComponent weaponPosition = weapon.getComponent(PositionComponent.class);
            RotationComponent weaponRotation = weapon.getComponent(RotationComponent.class);

            weaponComponent.isShooting = hardpoints.isShooting;

            float weaponX = position.x + weaponComponent.offset.x;
            float weaponY = position.y + weaponComponent.offset.y;

            float offsetX = weaponComponent.offset.x;
            float offsetY = weaponComponent.offset.y;

            float rotatedOffsetX = offsetX * MathUtils.cosDeg(rotation.a) - offsetY * MathUtils.sinDeg(rotation.a);
            float rotatedOffsetY = offsetX * MathUtils.sinDeg(rotation.a) + offsetY * MathUtils.cosDeg(rotation.a);

            float finalWeaponX = position.x + rotatedOffsetX;
            float finalWeaponY = position.y + rotatedOffsetY;

            weaponPosition.x = finalWeaponX + weaponComponent.recoilOffset.x;
            weaponPosition.y = finalWeaponY + weaponComponent.recoilOffset.y;
        }
    }
}

