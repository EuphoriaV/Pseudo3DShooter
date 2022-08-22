public class LineEquation {
    final double a, b, c;

    public LineEquation(MyLine line) {
        if (Math.abs((line.getA().getX() - line.getB().getX()) / (line.getA().getY() - line.getB().getY())) <= MyMath.EPSILON) {
            a = 0;
            b = 1;
            c = -line.getA().getX();
        } else {
            a = 1;
            b = (line.getA().getY() - line.getB().getY()) / (line.getB().getX() - line.getA().getX());
            c = -line.getA().getY() - b * line.getA().getX();
        }
    }
}
