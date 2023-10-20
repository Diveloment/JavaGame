package data;

public enum DamageModifiers {
    NONE("none", 1.0f),
    ELECTRICAL("electrically", 1.2f),
    FIRE("fire", 1.2f),
    FROST("frost", 1.1f),
    EXPLOSION("explosion", 1.5f),
    KINETIC("kinetic", 1.0f),
    RAY("ray", 1.0f),
    ;

    private String name;
    private float damageMod;

    DamageModifiers(String name, float damageMod) {
        this.name = name;
        this.damageMod = damageMod;
    }

    public String getName() {
        return name;
    }

    public float getDamageMod() {
        return damageMod;
    }
}
