package shared;

import java.io.Serializable;

public class Entity implements Serializable {

    private static final long serialVersionUID = 1567L;
    private float x;
    private float y;
    private String id;

    public Entity(float x, float y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
