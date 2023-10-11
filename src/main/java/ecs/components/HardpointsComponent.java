package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class HardpointsComponent implements Component {
    public Array<Entity> weapons = new Array<Entity>();
    public boolean isShooting = false;

    public HardpointsComponent() {
    }

    public HardpointsComponent(Array<Entity> weapons) {
        for (Entity entity : weapons) {
            if (Objects.nonNull(entity.getComponent(WeaponComponent.class)))
                this.weapons.add(entity);
        }
    }
}
