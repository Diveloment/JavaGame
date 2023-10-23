package data;

public class EntityModifier {

    public enum EffectType {

        NONE("", 1.0f, 1.0f),
        IMMOBILIZED("Immobilized", 1.0f, 0.0f),
        ;

        private String name;
        private float baseDamageMod;
        private float speedMod;

        private EffectType(String name, float baseDamageMod, float speedMod) {
            this.name = name;
            this.baseDamageMod = baseDamageMod;
            this.speedMod = speedMod;
        }

        public String getName() {
            return name;
        }

        public float getBaseDamageMod() {
            return baseDamageMod;
        }
    }
}
