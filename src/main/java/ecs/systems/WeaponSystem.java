package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import ecs.components.*;
import ecs.components.collision.CollisionComponent;

public class WeaponSystem extends IteratingSystem {
    private ComponentMapper<WeaponComponent> weaponMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<RotationComponent> rotationMapper;
    private ComponentMapper<AimPointComponent> aimPointMapper;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private Engine engine;

    public WeaponSystem(Engine engine) {
        super(Family.all(WeaponComponent.class, PositionComponent.class, RotationComponent.class, SpriteComponent.class, AimPointComponent.class).get());
        weaponMapper = ComponentMapper.getFor(WeaponComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        rotationMapper = ComponentMapper.getFor(RotationComponent.class);
        aimPointMapper = ComponentMapper.getFor(AimPointComponent.class);
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        WeaponComponent weapon = weaponMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        RotationComponent rotation = rotationMapper.get(entity);
        AimPointComponent aimPoint = aimPointMapper.get(entity);

        rotation.a = MathUtils.atan2(aimPoint.y - position.y, aimPoint.x - position.x) * MathUtils.radiansToDegrees;

        weapon.updatePositionWithAnimation();

        float fireInterval = 1.0f / weapon.fireRate;
        if (weapon.isShooting) {
            weapon.accumulatedTime += deltaTime;
            while (weapon.accumulatedTime >= 0 + weapon.phase)
            {
                fireBullet(position, rotation, weapon);
                weapon.accumulatedTime -= fireInterval;
            }
        } else {
            weapon.accumulatedTime += deltaTime;
            weapon.accumulatedTime = MathUtils.clamp(weapon.accumulatedTime, -fireInterval, deltaTime);
        }
    }

    private void fireBullet(PositionComponent position, RotationComponent rotation, WeaponComponent weapon) {
        Entity bulletEntity = new Entity();
        BulletComponent bulletComponent = new BulletComponent();
        PositionComponent positionComponent = new PositionComponent(position.x, position.y);
        DamageComponent damageComponent = new DamageComponent(10.0f);
        CollisionComponent collisionComponent = new CollisionComponent(8);
        SpriteComponent spriteComponent = new SpriteComponent(new Texture(Gdx.files.internal("assets/bullet_small_v2.png")));

        float angleInRadians = MathUtils.degreesToRadians * rotation.a;
        Vector2 direction = new Vector2(MathUtils.cos(angleInRadians), MathUtils.sin(angleInRadians))
                .add(new Vector2(MathUtils.random(-weapon.spray, weapon.spray) / weapon.bulletSpeed, MathUtils.random(-weapon.spray, weapon.spray) / weapon.bulletSpeed));

        weapon.recoilOffset.add(new Vector2(-direction.x * 5, -direction.y * 5));
        weapon.recoilAnimationStart = TimeUtils.millis();

        positionComponent.setPosition(position.getPosition());
        bulletComponent.velocity = direction.scl(weapon.bulletSpeed);
        bulletComponent.owner = weapon.owner;

        bulletEntity.add(bulletComponent);
        bulletEntity.add(spriteComponent);
        bulletEntity.add(positionComponent);
        bulletEntity.add(collisionComponent);
        bulletEntity.add(damageComponent);

        engine.addEntity(bulletEntity);
    }
}

