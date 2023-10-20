package data;

public enum EntityType {
    UNIT_GROUND("Ground unit"),
    UNIT_AIR("Air unit"),
    BUILDING("Building unit")
    ;

    private String name;

    EntityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
