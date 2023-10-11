package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class WeaponComponent implements Component {
    public Vector2 offset = new Vector2(0, 0);
    public Vector2 recoilOffset = new Vector2(0, 0);
    public boolean isShooting = false;
    public float phase = 0.0f;
    public float accumulatedTime = 0;
    public Entity owner;
    public int fireRate = 7;
    public float spray = 35;
    public float bulletSpeed = 1000;

    public long recoilAnimationDuration = 1000;
    public long recoilAnimationStart = -1;

    public WeaponComponent() {
    }

    public WeaponComponent(float x, float y) {
        this.offset.x = x;
        this.offset.y = y;
    }

    public void updatePositionWithAnimation() {
        if (recoilAnimationStart < 0) {
            return;
        }
        float progress = MathUtils.clamp((float) TimeUtils.timeSinceMillis(recoilAnimationStart) / recoilAnimationDuration, -1.0f, 1.0f);
        recoilOffset.lerp(new Vector2(0, 0), progress);
        if (progress == 1) {
            recoilAnimationStart = -1;
        }
    }

}
