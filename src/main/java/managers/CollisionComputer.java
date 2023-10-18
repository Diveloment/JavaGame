package managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import data.CollidersResponse;
import ecs.components.BulletComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.collision.CollisionComponent;

import java.util.*;

public class CollisionComputer extends GameManager {

    private static CollisionComputer instance;
    private Map<Entity, Set<Entity>> collidedEntities = new HashMap<Entity, Set<Entity>>();

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
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(CollisionComponent.class, PositionComponent.class, BulletComponent.class).get());
        ImmutableArray<Entity> entitiesB = engine.getEntitiesFor(Family.all(CollisionComponent.class, PositionComponent.class).get());
        collidedEntities.clear();

        for (Entity entityA : entities) {
            CollisionComponent collisionComponentA = entityA.getComponent(CollisionComponent.class);
            PositionComponent positionComponentA = entityA.getComponent(PositionComponent.class);
            BulletComponent bulletComponent = entityA.getComponent(BulletComponent.class);

            for (int i = 0; i < entitiesB.size(); i++) {
                Entity entityB = entitiesB.get(i);
                if (entityA != entityB) {
                    CollisionComponent collisionComponentB = entityB.getComponent(CollisionComponent.class);
                    PositionComponent positionComponentB = entityB.getComponent(PositionComponent.class);

                    boolean isCollide;
                    if (Objects.nonNull(bulletComponent))
                        isCollide = isLineIntersectingCircle(new Vector2(bulletComponent.lastPosition.x, bulletComponent.lastPosition.y),
                                new Vector2(positionComponentA.x, positionComponentA.y),
                                new Vector2(positionComponentB.x, positionComponentB.y),
                                collisionComponentB.radius);
                    else
                        isCollide = checkCollision(collisionComponentA, positionComponentA, collisionComponentB, positionComponentB);

                    if (isCollide) {
                        collidedEntities.computeIfAbsent(entityA, k -> new HashSet<>()).add(entityB);
                        if (Objects.nonNull(bulletComponent))
                            break;
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

    public boolean isLineIntersectingCircle(Vector2 A, Vector2 B, Vector2 C, float R) {
        Vector2 AB = B.cpy().sub(A);
        Vector2 AC = C.cpy().sub(A);

        float dotProduct = AC.dot(AB);

        // Найдите ближайшую точку P на прямой к центру круга
        Vector2 P = A.cpy().add(AB.cpy().scl(dotProduct / AB.len2()));

        // Проверьте, находится ли P внутри круга
        return C.cpy().dst(P) <= R && P.cpy().dst(A.cpy().lerp(B.cpy(), 0.5f)) <= (AB.len() / 2 + R);
    }

    public CollidersResponse onCollide(Entity entity) {
        CollidersResponse response = new CollidersResponse();
        Set<Entity> entities = collidedEntities.getOrDefault(entity, new HashSet<>());
        response.collisionsCount = entities.size();
        response.isColliding = entities.size() > 0;
        response.collidersEntities = entities;
        return response;
    }

    public ImmutableArray<Entity> getAllColliders() {
        return engine.getEntitiesFor(Family.all(CollisionComponent.class, PositionComponent.class).get());
    }
}
