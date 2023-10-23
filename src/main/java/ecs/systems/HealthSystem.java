package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import data.DamageModifiers;
import data.StatusEffect;
import ecs.components.HealthComponent;
import ecs.components.StatusComponent;
import ecs.components.physics.PhysicsComponent;
import models.PhysicsWorldModel;

import java.util.ArrayList;
import java.util.List;


public class HealthSystem extends IteratingSystem {
    private final ComponentMapper<HealthComponent> healthMapper;
    private final Engine engine;
    private final PhysicsWorldModel physics;

    public HealthSystem(Engine engine) {
        super(Family.all(HealthComponent.class).get());
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
        this.engine = engine;
        this.physics = PhysicsWorldModel.getInstance();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = healthMapper.get(entity);
        StatusComponent statusComponent = entity.getComponent(StatusComponent.class);

        if (healthComponent != null) {
            // Проверка, был ли нанесен урон
            if (healthComponent.isDamaged) {
                // Обработка урона, например, уменьшение здоровья
                applyDamage(healthComponent, statusComponent);
                healthComponent.isDamaged = false;
                healthComponent.takenDamageType = DamageModifiers.NONE;

                if (healthComponent.health <= 0) {
                    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
                    if (physicsComponent != null) {
                        physics.world.destroyBody(physicsComponent.body);
                    }
                    engine.removeEntity(entity);
                }
            }
        }
    }

    private void applyDamage(HealthComponent healthComponent, StatusComponent statusComponent) {
        List<Float> statusDamageModifiers = new ArrayList<>();
        if (statusComponent != null) {
            if (statusComponent.getEffects().stream().anyMatch(effect -> effect.type == StatusEffect.StatusType.WET)
            && healthComponent.takenDamageType == DamageModifiers.ELECTRICAL) {
            }
        }
        healthComponent.health -= healthComponent.damageTaken * healthComponent.takenDamageType.getDamageMod();
    }
}
