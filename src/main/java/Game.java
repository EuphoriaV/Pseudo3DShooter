import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    final int COUNT_OF_LINES = 1000;
    final int LENGTH_OF_LINE = 500;
    final double SLOW_SPEED = 1;
    final int WIDTH = 500;
    final int HEIGHT = 500;
    final double VISION = 2 * Math.PI / 3;
    final double D_SHTRIH = 10;
    final double DELTA = VISION / (double) COUNT_OF_LINES;

    private boolean forward, backward, left, right;
    private final MyLine[] lines = new MyLine[COUNT_OF_LINES];
    private final ArrayList<MyPolygon> polygons = new ArrayList<>();
    private final ArrayList<MyCircle> circles = new ArrayList<>();
    private final Camera player;

    public Game() {
        player = new Camera(new MyPoint(60, 250), Math.PI / 2);
        circles.add(new MyCircle(new MyPoint(50, 50), 50));
        circles.add(new MyCircle(new MyPoint(50, HEIGHT - 50), 50));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, HEIGHT - 50), 50));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, 50), 50));
        circles.add(new MyCircle(new MyPoint(130, 280), 20));
        circles.add(new MyCircle(new MyPoint(WIDTH - 130, HEIGHT - 280), 20));
        circles.add(new MyCircle(new MyPoint(250, 20), 5));
        circles.add(new MyCircle(new MyPoint(250, HEIGHT - 20), 5));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, 50), new MyPoint(99, 0))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, 50), new MyPoint(WIDTH - 99, 0))));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT - 50), new MyPoint(99, HEIGHT))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, HEIGHT - 50), new MyPoint(WIDTH - 99, HEIGHT))));
        polygons.add(new MyPolygon(List.of(new MyPoint(50, 100), new MyPoint(50, HEIGHT - 100))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 50, 100), new MyPoint(WIDTH - 50, HEIGHT - 100))));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, 0), new MyPoint(WIDTH - 99, 0))));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT), new MyPoint(WIDTH - 99, HEIGHT))));
        polygons.add(new MyPolygon(List.of(new MyPoint(120, 50), new MyPoint(380, 50), new MyPoint(380, 40), new MyPoint(120, 40))));
        polygons.add(new MyPolygon(List.of(new MyPoint(120, HEIGHT - 50), new MyPoint(380, HEIGHT - 50), new MyPoint(380, HEIGHT - 40), new MyPoint(120, HEIGHT - 40))));
        polygons.add(new MyPolygon(List.of(new MyPoint(100, 100), new MyPoint(150, 100), new MyPoint(150, 200), new MyPoint(100, 200))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 100, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 200), new MyPoint(WIDTH - 100, HEIGHT - 200))));
        polygons.add(new MyPolygon(List.of(new MyPoint(110, 350), new MyPoint(140, 350), new MyPoint(140, 380), new MyPoint(110, 380))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 110, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 380), new MyPoint(WIDTH - 110, HEIGHT - 380))));
        polygons.add(new MyPolygon(List.of(new MyPoint(240, 80), new MyPoint(340, 80), new MyPoint(340, 100), new MyPoint(240, 100))));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 240, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 100), new MyPoint(WIDTH - 240, HEIGHT - 100))));
        Timer timer = new Timer(7, null);
        timer.addActionListener(e -> {
            for (int i = 0; i < COUNT_OF_LINES; i++) {
                lines[i] = MyMath.lineByStartAndAngle(player.getPosition(), player.getAlpha() - VISION / 2 + (DELTA * i), LENGTH_OF_LINE);
                for (MyPolygon polygon : polygons) {
                    for (int j = 0; j < polygon.getPoints().size(); j++) {
                        MyLine wall = new MyLine(polygon.getPoints().get(j), polygon.getPoints().get((j + 1) % polygon.getPoints().size()));
                        MyPoint intersection = MyMath.lineAndLine(lines[i], wall);
                        if (intersection != null) {
                            if (MyMath.dist(player.getPosition(), intersection) < MyMath.length(lines[i])) {
                                lines[i].setB(intersection);
                            }
                        }
                    }
                }
                for (MyCircle circle : circles) {
                    MyPoint intersection = MyMath.lineAndCircle(lines[i], circle);
                    if (intersection != null) {
                        if (MyMath.dist(player.getPosition(), intersection) < MyMath.length(lines[i])) {
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
        MyLine line = MyMath.lineByStartAndAngle(player.getPosition(), angle, 50);
        MyLine curWall = null;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.getPoints().size(); j++) {
                MyLine wall = new MyLine(polygon.getPoints().get(j), polygon.getPoints().get((j + 1) % polygon.getPoints().size()));
                MyPoint intersection = MyMath.lineAndLine(line, wall);
                if (intersection != null) {
                    if (MyMath.dist(player.getPosition(), intersection) < MyMath.length(line)) {
                        line.setB(intersection);
                        curWall = wall;
                    }
                }
            }
        }
        for (MyCircle circle : circles) {
            MyPoint intersection = MyMath.lineAndCircle(line, circle);
            if (intersection != null) {
                if (MyMath.dist(player.getPosition(), intersection) < MyMath.length(line)) {
                    line.setB(intersection);
                    MyLine first = MyMath.lineByStartAndAngle(intersection, MyMath.getAngle(new MyLine(player.getPosition(), intersection)) + Math.PI / 2, 50);
                    MyLine second = MyMath.lineByStartAndAngle(intersection, MyMath.getAngle(new MyLine(player.getPosition(), intersection)) - Math.PI / 2, 50);
                    curWall = new MyLine(first.getB(), second.getB());
                }
            }
        }
        MyPoint newPosition = new MyPoint(player.getPosition().getX() + Math.sin(Math.PI / 2 - angle) / SLOW_SPEED, player.getPosition().getY() + Math.cos(Math.PI / 2 - angle) / SLOW_SPEED);
        if (curWall != null && MyMath.length(MyMath.perpendicular(newPosition, curWall)) < D_SHTRIH / 2) {
            MyLine proection = MyMath.proection(line, curWall);
            double dx = proection.getB().getX() - proection.getA().getX(), dy = proection.getB().getY() - proection.getA().getY();
            newPosition.setX(player.getPosition().getX() + dx / (MyMath.length(line) * SLOW_SPEED));
            newPosition.setY(player.getPosition().getY() + dy / (MyMath.length(line) * SLOW_SPEED));
        }
        double minX = Integer.MIN_VALUE, maxX = Integer.MAX_VALUE, minY = Integer.MIN_VALUE, maxY = Integer.MAX_VALUE;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.getPoints().size(); j++) {
                MyLine wall = new MyLine(polygon.getPoints().get(j), polygon.getPoints().get((j + 1) % polygon.getPoints().size()));
                MyLine perp = MyMath.perpendicular(newPosition, wall);
                if (MyMath.lineAndLine(perp, wall) != null && MyMath.length(perp) < D_SHTRIH / 2) {
                    double perpAngle = MyMath.getAngle(perp);
                    MyPoint newPoint = MyMath.lineByStartAndAngle(perp.getB(), perpAngle + Math.PI, D_SHTRIH / 2).getB();
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
            if (Math.abs(dist) < D_SHTRIH / 2) {
                double length = dist < 0 ? circle.radius - D_SHTRIH / 2 : circle.radius + D_SHTRIH / 2;
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
            player.getPosition().setX(Math.min(maxX, Math.max(minX, newPosition.getX())));
        }
        if (minY <= maxY) {
            player.getPosition().setY(Math.min(maxY, Math.max(minY, newPosition.getY())));
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

    public MyLine[] getLines() {
        return lines;
    }

    public ArrayList<MyPolygon> getPolygons() {
        return polygons;
    }

    public ArrayList<MyCircle> getCircles() {
        return circles;
    }

    public Camera getPlayer() {
        return player;
    }
}