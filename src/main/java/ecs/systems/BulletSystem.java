package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import data.CollidersResponse;
import ecs.components.BulletComponent;
import ecs.components.PositionComponent;
import ecs.components.SpriteComponent;
import ecs.components.TeamComponent;
import managers.CollisionComputer;

import java.util.Objects;

public class BulletSystem extends IteratingSystem {
    private ComponentMapper<BulletComponent> bulletMapper;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Engine engine;
    private CollisionComputer collisionComputer;

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
            Array<Entity> colliders = response.collidersEntities;

            // Проверяем, были ли столкновения с другими объектами
            boolean collidedWithNonProjectile = false;
            for (Entity collider : colliders) {
                String colliderTeam = "-";
                TeamComponent colliderTeamComponent = collider.getComponent(TeamComponent.class);
                if (Objects.nonNull(colliderTeamComponent))
                    colliderTeam = colliderTeamComponent.teamName;
                if (!isProjectile(collider) && !Objects.equals(collider, bullet.owner) && !bulletTeam.equals(colliderTeam)) {
                    collidedWithNonProjectile = true;
                    break;
                }
            }

            // Если столкновение было только с другими снарядами, ничего не делаем
            if (collidedWithNonProjectile) {
                engine.removeEntity(entity);
            }
        }

        bullet.ttl -= deltaTime;
        if (bullet.ttl < 0) {
            engine.removeEntity(entity);
        }
    }

    private boolean isProjectile(Entity entity) {
        return Objects.nonNull(entity.getComponent(BulletComponent.class));
    }
}

