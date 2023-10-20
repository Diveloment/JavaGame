package ecs.components;

import com.badlogic.ashley.core.Component;
import data.DamageModifiers;
import data.EntityType;

public class HealthComponent implements Component {
    public float maxHealth = 100.0f;
    public float health;
    public boolean isDamaged;
    public float damageTaken;
    public DamageModifiers takenDamageType = DamageModifiers.NONE;
    public EntityType entityType = EntityType.UNIT_GROUND;

    public HealthComponent() {
        this.health = maxHealth;
    }

    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public HealthComponent(float maxHealth, EntityType entityType) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.entityType = entityType;
    }
}