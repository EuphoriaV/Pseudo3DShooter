import java.util.List;

public class MyPolygon {
    private final List<MyPoint> points;

    public MyPolygon(List<MyPoint> list) {
        this.points = list;
    }

    public List<MyPoint> getPoints() {
        return points;
    }
}
