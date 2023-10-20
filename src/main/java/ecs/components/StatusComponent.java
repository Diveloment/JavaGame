package ecs.components;

import com.badlogic.ashley.core.Component;
import data.StatusEffect;

import java.util.HashSet;
import java.util.Set;

public class StatusComponent implements Component {
    public Set<StatusEffect> effects = new HashSet<>();

    public Set<StatusEffect> getEffects() {
        return effects;
    }
}
