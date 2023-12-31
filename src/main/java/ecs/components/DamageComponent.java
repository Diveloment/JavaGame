package ecs.components;

import com.badlogic.ashley.core.Component;
import data.DamageModifiers;

public class DamageComponent implements Component {

    public float baseDamage;
    public DamageModifiers damageModifier = DamageModifiers.NONE;

    public DamageComponent(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public DamageComponent(float baseDamage, DamageModifiers damageModifier) {
        this.baseDamage = baseDamage;
        this.damageModifier = damageModifier;
    }
}
