import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    final int COUNT_OF_LINES = 1000;
    final int LENGTH_OF_LINE = 700;
    final double SLOW_SPEED = 1;
    final int WIDTH = 500;
    final int HEIGHT = 500;
    final double VISION = 2 * Math.PI / 3;
    final double D_SHTRIH = 10;
    final double DELTA = VISION / (double) COUNT_OF_LINES;
    final MyLine[] lines = new MyLine[COUNT_OF_LINES];
    final ArrayList<MyPolygon> polygons = new ArrayList<>();
    final ArrayList<MyCircle> circles = new ArrayList<>();
    final Camera player;
    final MyTexture WOOD = new MyTexture("wood.png"), BOARD = new MyTexture("board.png"), STONE = new MyTexture("stone.png"),
            FEDYAS_FRONT = new MyTexture("fedyas_front.png", true), FEDYAS_BACK = new MyTexture("fedyas_back.png", true),
            FEDYAS_SIDE = new MyTexture("fedyas_side.png", true), DIMAS_FRONT = new MyTexture("dimas_front.png", true),
            DIMAS_BACK = new MyTexture("dimas_back.png", true), DIMAS_SIDE = new MyTexture("dimas_side.png", true),
            PASHAS_FRONT = new MyTexture("pashas_front.png", true), PASHAS_BACK = new MyTexture("pashas_back.png", true),
            PASHAS_SIDE = new MyTexture("pashas_side.png", true), OXXXYMIRON = new MyTexture("oxxxymiron.png", true);
    private boolean forward, backward, left, right;

    public Game() {
        player = new Camera(new MyPoint(60, 250), 0);

        circles.add(new MyCircle(new MyPoint(50, 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(50, HEIGHT - 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, HEIGHT - 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, 50), 50, WOOD));

        circles.add(new MyCircle(new MyPoint(130, 280), 20, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 130, HEIGHT - 280), 20, WOOD));

        circles.add(new MyCircle(new MyPoint(245, HEIGHT - 20), 10, OXXXYMIRON));
        circles.add(new MyCircle(new MyPoint(245, 20), 10, OXXXYMIRON));

        polygons.add(new MyPolygon(List.of(new MyPoint(245, 205), new MyPoint(245, 215)), FEDYAS_FRONT));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 205), new MyPoint(255, 205)), FEDYAS_SIDE));
        polygons.add(new MyPolygon(List.of(new MyPoint(255, 215), new MyPoint(255, 205)), FEDYAS_BACK));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 215), new MyPoint(255, 215)), FEDYAS_SIDE));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 285), new MyPoint(245, 295)), DIMAS_FRONT));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 285), new MyPoint(255, 285)), DIMAS_SIDE));
        polygons.add(new MyPolygon(List.of(new MyPoint(255, 295), new MyPoint(255, 285)), DIMAS_BACK));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 295), new MyPoint(255, 295)), DIMAS_SIDE));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 245), new MyPoint(245, 255)), PASHAS_FRONT));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 245), new MyPoint(255, 245)), PASHAS_SIDE));
        polygons.add(new MyPolygon(List.of(new MyPoint(255, 255), new MyPoint(255, 245)), PASHAS_BACK));
        polygons.add(new MyPolygon(List.of(new MyPoint(245, 255), new MyPoint(255, 255)), PASHAS_SIDE));

        polygons.add(new MyPolygon(List.of(new MyPoint(99, 50), new MyPoint(99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, 50), new MyPoint(WIDTH - 99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT - 50), new MyPoint(99, HEIGHT)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, HEIGHT - 50), new MyPoint(WIDTH - 99, HEIGHT)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(50, 100), new MyPoint(50, HEIGHT - 100)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 50, 100), new MyPoint(WIDTH - 50, HEIGHT - 100)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, 0), new MyPoint(WIDTH - 99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT), new MyPoint(WIDTH - 99, HEIGHT)), BOARD));

        polygons.add(new MyPolygon(List.of(new MyPoint(120, 50), new MyPoint(380, 50), new MyPoint(380, 40), new MyPoint(120, 40), new MyPoint(120, 50)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(120, HEIGHT - 50), new MyPoint(380, HEIGHT - 50), new MyPoint(380, HEIGHT - 40), new MyPoint(120, HEIGHT - 40), new MyPoint(120, HEIGHT - 50)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(100, 100), new MyPoint(150, 100), new MyPoint(150, 200), new MyPoint(100, 200), new MyPoint(100, 100)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 100, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 200), new MyPoint(WIDTH - 100, HEIGHT - 200), new MyPoint(WIDTH - 100, HEIGHT - 100)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(110, 350), new MyPoint(140, 350), new MyPoint(140, 380), new MyPoint(110, 380), new MyPoint(110, 350)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 110, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 380), new MyPoint(WIDTH - 110, HEIGHT - 380), new MyPoint(WIDTH - 110, HEIGHT - 350)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(240, 80), new MyPoint(340, 80), new MyPoint(340, 100), new MyPoint(240, 100), new MyPoint(240, 80)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 240, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 100), new MyPoint(WIDTH - 240, HEIGHT - 100), new MyPoint(WIDTH - 240, HEIGHT - 80)), STONE));

        Timer timer = new Timer(7, null);
        timer.addActionListener(e -> {
            for (int i = 0; i < COUNT_OF_LINES; i++) {
                lines[i] = MyMath.lineByStartAndAngle(player.position, player.getAlpha() - VISION / 2 + (DELTA * i), LENGTH_OF_LINE);
                for (MyPolygon polygon : polygons) {
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        MyPoint intersection = MyMath.lineAndLine(lines[i], wall);
                        if (intersection != null) {
                            if (MyMath.dist(player.position, intersection) < MyMath.length(lines[i])) {
                                lines[i].setB(intersection);
                            }
                        }
                    }
                }
                for (MyCircle circle : circles) {
                    MyPoint intersection = MyMath.lineAndCircle(lines[i], circle);
                    if (intersection != null) {
                        if (MyMath.dist(player.position, intersection) < MyMath.length(lines[i])) {
                            lines[i].setB(intersection);
                        }
                    }

                }
            }
            if (forward && !backward && left == right) {
                move(player.getAlpha());
            } else if (backward && !forward && left == right) {
                move(Math.PI + player.getAlpha());
            } else if (right && !left && backward == forward) {
                move(Math.PI / 2 + player.getAlpha());
            } else if (left && !right && backward == forward) {
                move(-Math.PI / 2 + player.getAlpha());
            } else if (left && forward && !backward) {
                move(-Math.PI / 4 + player.getAlpha());
            } else if (right && forward && !backward) {
                move(Math.PI / 4 + player.getAlpha());
            } else if (left && backward && !forward) {
                move(-3 * Math.PI / 4 + player.getAlpha());
            } else if (right && backward && !forward) {
                move(3 * Math.PI / 4 + player.getAlpha());
            }
        });
        timer.start();
    }

    public void move(double angle) {
        MyLine line = MyMath.lineByStartAndAngle(player.position, angle, 50);
        MyLine curWall = null;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                MyPoint intersection = MyMath.lineAndLine(line, wall);
                if (intersection != null) {
                    if (MyMath.dist(player.position, intersection) < MyMath.length(line)) {
                        line.setB(intersection);
                        curWall = wall;
                    }
                }
            }
        }
        for (MyCircle circle : circles) {
            MyPoint intersection = MyMath.lineAndCircle(line, circle);
            if (intersection != null) {
                if (MyMath.dist(player.position, intersection) < MyMath.length(line)) {
                    line.setB(intersection);
                    MyLine first = MyMath.lineByStartAndAngle(intersection, MyMath.getAngle(new MyLine(player.position, intersection)) + Math.PI / 2, 50);
                    MyLine second = MyMath.lineByStartAndAngle(intersection, MyMath.getAngle(new MyLine(player.position, intersection)) - Math.PI / 2, 50);
                    curWall = new MyLine(first.getB(), second.getB());
                }
            }
        }
        MyPoint newPosition = new MyPoint(player.position.getX() + Math.sin(Math.PI / 2 - angle) / SLOW_SPEED, player.position.getY() + Math.cos(Math.PI / 2 - angle) / SLOW_SPEED);
        if (curWall != null && MyMath.length(MyMath.perpendicular(newPosition, curWall)) < D_SHTRIH / 4) {
            MyLine proection = MyMath.proection(line, curWall);
            double dx = proection.getB().getX() - proection.getA().getX(), dy = proection.getB().getY() - proection.getA().getY();
            newPosition.setX(player.position.getX() + dx / (MyMath.length(line) * SLOW_SPEED));
            newPosition.setY(player.position.getY() + dy / (MyMath.length(line) * SLOW_SPEED));
        }
        double minX = Integer.MIN_VALUE, maxX = Integer.MAX_VALUE, minY = Integer.MIN_VALUE, maxY = Integer.MAX_VALUE;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                MyLine perp = MyMath.perpendicular(newPosition, wall);
                if (MyMath.lineAndLine(perp, wall) != null && MyMath.length(perp) < D_SHTRIH / 4) {
                    double perpAngle = MyMath.getAngle(perp);
                    MyPoint newPoint = MyMath.lineByStartAndAngle(perp.getB(), perpAngle + Math.PI, D_SHTRIH / 4).getB();
                    if (minX < newPoint.getX()) {
                        minX = newPoint.getX();
                    }
                    if (minY < newPoint.getY()) {
                        minY = newPoint.getY();
                    }
                    if (maxX > newPoint.getX()) {
                        maxX = newPoint.getX();
                    }
                    if (maxY > newPoint.getY()) {
                        maxY = newPoint.getY();
                    }
                }
            }
        }
        for (MyCircle circle : circles) {
            double dist = MyMath.dist(newPosition, circle.center) - circle.radius;
            if (Math.abs(dist) < D_SHTRIH / 4) {
                double length = dist < 0 ? circle.radius - D_SHTRIH / 4 : circle.radius + D_SHTRIH / 4;
                MyPoint newPoint = MyMath.lineByStartAndAngle(circle.center, MyMath.getAngle(new MyLine(circle.center, newPosition)), length).getB();
                if (minX < newPoint.getX()) {
                    minX = newPoint.getX();
                }
                if (minY < newPoint.getY()) {
                    minY = newPoint.getY();
                }
                if (maxX > newPoint.getX()) {
                    maxX = newPoint.getX();
                }
                if (maxY > newPoint.getY()) {
                    maxY = newPoint.getY();
                }
            }
        }
        if (minX <= maxX) {
            player.position.setX(Math.min(maxX, Math.max(minX, newPosition.getX())));
        }
        if (minY <= maxY) {
            player.position.setY(Math.min(maxY, Math.max(minY, newPosition.getY())));
        }
    }

    public void startMovingForward() {
        forward = true;
    }

    public void stopMovingForward() {
        forward = false;
    }

    public void startMovingBackward() {
        backward = true;
    }

    public void stopMovingBackward() {
        backward = false;
    }

    public void startMovingLeft() {
        left = true;
    }

    public void stopMovingLeft() {
        left = false;
    }

    public void startMovingRight() {
        right = true;
    }

    public void stopMovingRight() {
        right = false;
    }
}
