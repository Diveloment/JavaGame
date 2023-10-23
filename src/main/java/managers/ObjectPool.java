package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private List<T> objects = new ArrayList<>();
    private Supplier<T> objectSupplier;

    public ObjectPool(Supplier<T> objectSupplier, int initialSize) {
        this.objectSupplier = objectSupplier;
        for (int i = 0; i < initialSize; i++) {
            objects.add(objectSupplier.get());
        }
    }

    public T obtain() {
        if (objects.isEmpty()) {
            objects.add(objectSupplier.get());
        }
        return objects.remove(objects.size() - 1);
    }

    public void free(T object) {
        objects.add(object);
    }
}

