package managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import data.CollidersResponse;
import ecs.components.PositionComponent;
import ecs.components.collision.CollisionComponent;

import java.util.HashMap;
import java.util.Map;

public class CollisionComputer extends GameManager {

    private static CollisionComputer instance;
    private Map<Entity, Array<Entity>> collidedEntities = new HashMap<Entity, Array<Entity>>();

    private CollisionComputer(Engine engine) {
        this.engine = engine;
    }

    public static CollisionComputer getInstance(Engine engine) {
        if (instance == null) {
            instance = new CollisionComputer(engine);
        }
        return instance;
    }

    public static CollisionComputer getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    @Override
    public void update() {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(CollisionComponent.class, PositionComponent.class).get());
        collidedEntities.clear();

        for (Entity entityA : entities) {
            CollisionComponent collisionComponentA = entityA.getComponent(CollisionComponent.class);
            PositionComponent positionComponentA = entityA.getComponent(PositionComponent.class);

            for (int i = 0; i < entities.size(); i++) {
                Entity entityB = entities.get(i);
                if (entityA != entityB) {
                    CollisionComponent collisionComponentB = entityB.getComponent(CollisionComponent.class);
                    PositionComponent positionComponentB = entityB.getComponent(PositionComponent.class);

                    // Здесь реализуйте вашу логику определения столкновения между entityA и entityB
                    if (checkCollision(collisionComponentA, positionComponentA, collisionComponentB, positionComponentB)) {
                        // Если есть столкновение, добавьте entityB в список столкнувшихся сущностей entityA
                        collidedEntities.computeIfAbsent(entityA, k -> new Array<>()).removeValue(entityB, true);
                        collidedEntities.computeIfAbsent(entityA, k -> new Array<>()).add(entityB);
                    }
                }
            }
        }
    }

    private boolean checkCollision(CollisionComponent collisionComponentA, PositionComponent positionComponentA, CollisionComponent collisionComponentB, PositionComponent positionComponentB) {
        float radiusA = collisionComponentA.radius; // Получите радиус первого круга
        Vector2 positionA = positionComponentA.getPosition(); // Получите позицию центра первого круга

        float radiusB = collisionComponentB.radius; // Получите радиус второго круга
        Vector2 positionB = positionComponentB.getPosition(); // Получите позицию центра второго круга

        // Вычисляем расстояние между центрами кругов
        float distance = positionA.dst(positionB);

        // Если расстояние меньше или равно сумме радиусов, то есть пересечение
        return distance <= (radiusA + radiusB);
    }

    public CollidersResponse onCollide(Entity entity) {
        CollidersResponse response = new CollidersResponse();
        Array<Entity> entities = collidedEntities.getOrDefault(entity, new Array<>());
        response.collisionsCount = entities.size;
        response.isColliding = entities.size > 0;
        response.collidersEntities = entities;
        return response;
    }
}
