package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import data.CollidersResponse;
import ecs.components.*;
import managers.CollisionComputer;

import java.util.Objects;
import java.util.Set;

public class BulletSystem extends IteratingSystem {
    private final ComponentMapper<BulletComponent> bulletMapper;
    private final ComponentMapper<SpriteComponent> spriteMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final Engine engine;
    private final CollisionComputer collisionComputer;

    public BulletSystem(Engine engine) {
        super(Family.all(BulletComponent.class, PositionComponent.class, SpriteComponent.class).get());
        bulletMapper = ComponentMapper.getFor(BulletComponent.class);
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.engine = engine;
        this.collisionComputer = CollisionComputer.getInstance();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        BulletComponent bullet = bulletMapper.get(entity);
        SpriteComponent spriteComponent = spriteMapper.get(entity);
        PositionComponent positionComponent = positionMapper.get(entity);
        DamageComponent damageComponent = entity.getComponent(DamageComponent.class);

        bullet.lastPosition.x = positionComponent.x;
        bullet.lastPosition.y = positionComponent.y;

        positionComponent.x += bullet.velocity.x * deltaTime;
        positionComponent.y += bullet.velocity.y * deltaTime;
        float directionAngle = MathUtils.atan2(bullet.velocity.y, bullet.velocity.x) * MathUtils.radiansToDegrees;
        spriteComponent.sprite.setRotation(directionAngle);
        spriteComponent.sprite.setScale(bullet.scale);

        String bulletTeam = "";
        TeamComponent team = bullet.owner.getComponent(TeamComponent.class);
        if (Objects.nonNull(team)) {
            bulletTeam = team.teamName;
        }

        // Collisions
        CollidersResponse response = collisionComputer.onCollide(entity);
        if (response.isColliding) {
            Set<Entity> colliders = response.collidersEntities;

            // Проверяем, были ли столкновения с другими объектами
            for (Entity collider : colliders) {
                String colliderTeam = "-";
                TeamComponent colliderTeamComponent = collider.getComponent(TeamComponent.class);
                if (Objects.nonNull(colliderTeamComponent))
                    colliderTeam = colliderTeamComponent.teamName;
                if (!isProjectile(collider) && !Objects.equals(collider, bullet.owner) && !bulletTeam.equals(colliderTeam)) {
                    applyHit(collider, entity, damageComponent);
                    return;
                }
            }
        }

        bullet.ttl -= deltaTime;
        if (bullet.ttl < 0) {
            engine.removeEntity(entity);
        }
    }

    private void applyHit(Entity collider, Entity bulletEntity, DamageComponent damageComponent) {
        if (Objects.nonNull(damageComponent))
            dealDamage(damageComponent, collider);
        engine.removeEntity(bulletEntity);
    }

    private void dealDamage(DamageComponent damageComponent, Entity target) {
        HealthComponent healthComponent = target.getComponent(HealthComponent.class);
        if (healthComponent != null) {
            healthComponent.takenDamageType = damageComponent.damageModifier;
            healthComponent.damageTaken = damageComponent.baseDamage;
            healthComponent.isDamaged = true;
        }
    }

    private boolean isProjectile(Entity entity) {
        return Objects.nonNull(entity.getComponent(BulletComponent.class));
    }
}

