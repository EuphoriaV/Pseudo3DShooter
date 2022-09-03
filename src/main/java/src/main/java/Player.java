import java.util.List;

public class Player {
    final Camera camera;
    private int health;
    final MyPolygon[] sides = new MyPolygon[4];
    final Weapon weapon;
    final double size;

    public Player(Camera camera, MyTexture front, MyTexture back, MyTexture left, MyTexture right, double size) {
        this.camera = camera;
        this.size = size;
        this.weapon = new Weapon(10000);
        health = 100;
        sides[0] = new MyPolygon(List.of(new MyPoint(0, 0), new MyPoint(0, 0)), front);
        sides[1] = new MyPolygon(List.of(new MyPoint(0, 0), new MyPoint(0, 0)), back);
        sides[2] = new MyPolygon(List.of(new MyPoint(0, 0), new MyPoint(0, 0)), left);
        sides[3] = new MyPolygon(List.of(new MyPoint(0, 0), new MyPoint(0, 0)), right);
        updateSides();
    }

    public void updateSides() {
        MyPoint first = MyMath.lineByStartAndAngle(camera.position, camera.getAlpha() + Math.PI / 4, size).getB();
        MyPoint second = MyMath.lineByStartAndAngle(camera.position, camera.getAlpha() + 3 * Math.PI / 4, size).getB();
        MyPoint third = MyMath.lineByStartAndAngle(camera.position, camera.getAlpha() + 5 * Math.PI / 4, size).getB();
        MyPoint fourth = MyMath.lineByStartAndAngle(camera.position, camera.getAlpha() + 7 * Math.PI / 4, size).getB();
        sides[0].points.get(0).setX(first.getX());
        sides[0].points.get(0).setY(first.getY());
        sides[0].points.get(1).setX(fourth.getX());
        sides[0].points.get(1).setY(fourth.getY());
        sides[1].points.get(0).setX(third.getX());
        sides[1].points.get(0).setY(third.getY());
        sides[1].points.get(1).setX(second.getX());
        sides[1].points.get(1).setY(second.getY());
        sides[2].points.get(0).setX(first.getX());
        sides[2].points.get(0).setY(first.getY());
        sides[2].points.get(1).setX(second.getX());
        sides[2].points.get(1).setY(second.getY());
        sides[3].points.get(0).setX(fourth.getX());
        sides[3].points.get(0).setY(fourth.getY());
        sides[3].points.get(1).setX(third.getX());
        sides[3].points.get(1).setY(third.getY());
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}