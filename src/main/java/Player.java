import java.util.List;

public class Player {
    private final Camera camera;
    private int health;
    private final MyPolygon[] sides = new MyPolygon[4];
    private final Weapon weapon;
    private final double size;

    public Player(Camera camera, MyTexture front, MyTexture back, MyTexture right, MyTexture left, double size) {
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
        MyPoint first = MyMath.lineByStartAndAngle(camera.getPosition(), camera.getAlpha() + Math.PI / 4, size).getB();
        MyPoint second = MyMath.lineByStartAndAngle(camera.getPosition(), camera.getAlpha() + 3 * Math.PI / 4, size).getB();
        MyPoint third = MyMath.lineByStartAndAngle(camera.getPosition(), camera.getAlpha() + 5 * Math.PI / 4, size).getB();
        MyPoint fourth = MyMath.lineByStartAndAngle(camera.getPosition(), camera.getAlpha() + 7 * Math.PI / 4, size).getB();
        sides[0].getPoints().get(0).setX(first.getX());
        sides[0].getPoints().get(0).setY(first.getY());
        sides[0].getPoints().get(1).setX(fourth.getX());
        sides[0].getPoints().get(1).setY(fourth.getY());
        sides[1].getPoints().get(0).setX(third.getX());
        sides[1].getPoints().get(0).setY(third.getY());
        sides[1].getPoints().get(1).setX(second.getX());
        sides[1].getPoints().get(1).setY(second.getY());
        sides[2].getPoints().get(0).setX(first.getX());
        sides[2].getPoints().get(0).setY(first.getY());
        sides[2].getPoints().get(1).setX(second.getX());
        sides[2].getPoints().get(1).setY(second.getY());
        sides[3].getPoints().get(0).setX(fourth.getX());
        sides[3].getPoints().get(0).setY(fourth.getY());
        sides[3].getPoints().get(1).setX(third.getX());
        sides[3].getPoints().get(1).setY(third.getY());
    }

    public int getHealth() {
        return health;
    }

    public Camera getCamera() {
        return camera;
    }

    public MyPolygon[] getSides() {
        return sides;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public double getSize() {
        return size;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}