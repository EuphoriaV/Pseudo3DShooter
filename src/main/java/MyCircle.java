public class MyCircle {
    private final MyPoint center;
    private final double radius;
    private final MyTexture texture;

    public MyPoint getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public MyTexture getTexture() {
        return texture;
    }

    public MyCircle(MyPoint center, double radius, MyTexture texture) {
        this.center = center;
        this.radius = radius;
        this.texture = texture;
    }
}
