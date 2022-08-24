import java.util.List;

public class MyPolygon {
    final List<MyPoint> points;
    final MyTexture texture;

    public MyPolygon(List<MyPoint> list, MyTexture texture) {
        this.points = list;
        this.texture = texture;
    }
}
