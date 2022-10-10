public class Camera {
    private final MyPoint position;
    private double alpha;

    public Camera(MyPoint point, double alpha) {
        this.position = point;
        this.alpha = alpha;
    }

    public MyPoint getPosition() {
        return position;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}