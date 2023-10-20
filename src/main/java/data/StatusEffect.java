package data;

import java.util.List;

public class StatusEffect {

    public static final StatusEffect WET = new StatusEffect(StatusType.WET, 5.0f, null);
    public static final StatusEffect IMMOBILIZED = new StatusEffect(StatusType.IMMOBILIZED, 5.0f, null);
    public static final StatusEffect BURNING = new StatusEffect(StatusType.BURNING, 5.0f, null);

    public enum StatusType {
        WET("Wet"),
        IMMOBILIZED("Immobilized"),
        BURNING("Burning")
        ;

        private String name;

        StatusType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public StatusType type;
    public float duration;
    public Runnable action;

    public StatusEffect(StatusType type, float duration, Runnable action) {
        this.type = type;
        this.duration = duration;
        this.action = action;
    }
}
