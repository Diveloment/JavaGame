package ecs.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public float maxHealth = 100.0f;
    public float health;

    public HealthComponent() {
        this.health = maxHealth;
    }

    public HealthComponent(float maxHealth) {
        this.health = maxHealth;
    }
}