package ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import data.StatusEffect;
import ecs.components.StatusComponent;

import java.util.Set;

public class StatusSystem extends IteratingSystem {
    private ComponentMapper<StatusComponent> statusMapper;

    public StatusSystem() {
        super(Family.all(StatusComponent.class).get());
        statusMapper = ComponentMapper.getFor(StatusComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatusComponent statusComponent = statusMapper.get(entity);
        Set<StatusEffect> effects = statusComponent.getEffects();

        for (StatusEffect effect : effects) {
            effect.duration -= deltaTime;
            if (effect.duration <= 0) {
                effects.remove(effect.type);
            } else {
                if (effect.action != null) {
                    effect.action.run();
                }
            }
        }
    }
}
