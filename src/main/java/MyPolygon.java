import java.util.List;

public class MyPolygon {
    private final List<MyPoint> points;
    private final MyTexture texture;

    public MyPolygon(List<MyPoint> list, MyTexture texture) {
        this.points = list;
        this.texture = texture;
    }

    public List<MyPoint> getPoints() {
        return points;
    }

    public MyTexture getTexture() {
        return texture;
    }
}
