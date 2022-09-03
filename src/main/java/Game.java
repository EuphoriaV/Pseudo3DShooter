import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    final ArrayList<Player> players = new ArrayList<>();
    final double[] moveAngles = new double[3];
    final double[] msAfterShot = new double[3];
    final MyTexture WOOD = new MyTexture("wood.png"), BOARD = new MyTexture("board.png"), STONE = new MyTexture("stone.png"), BRICK = new MyTexture("brick.png"),
            FEDYAS_FRONT = new MyTexture("fedyas_front.png", true), FEDYAS_BACK = new MyTexture("fedyas_back.png", true),
            FEDYAS_SIDE = new MyTexture("fedyas_side.png", true), DIMAS_FRONT = new MyTexture("dimas_front.png", true),
            DIMAS_BACK = new MyTexture("dimas_back.png", true), DIMAS_SIDE = new MyTexture("dimas_side.png", true),
            PASHAS_FRONT = new MyTexture("pashas_front.png", true), PASHAS_BACK = new MyTexture("pashas_back.png", true),
            PASHAS_SIDE = new MyTexture("pashas_side.png", true), OXXXYMIRON = new MyTexture("oxxxymiron.png", true);
    private int kills = 0, deaths = 0;
    private boolean forward, backward, left, right;
    private Player mainPlayer;

    public Game() {
        players.add(new Player(new Camera(new MyPoint(60, 160), 0), OXXXYMIRON, OXXXYMIRON, OXXXYMIRON, OXXXYMIRON, 4));
        players.add(new Player(new Camera(new MyPoint(250, 210), Math.PI), FEDYAS_FRONT, FEDYAS_BACK, FEDYAS_SIDE, FEDYAS_SIDE, 3));
        players.add(new Player(new Camera(new MyPoint(250, 250), Math.PI), DIMAS_FRONT, DIMAS_BACK, DIMAS_SIDE, DIMAS_SIDE, 4));
        players.add(new Player(new Camera(new MyPoint(250, 290), Math.PI), PASHAS_FRONT, PASHAS_BACK, PASHAS_SIDE, PASHAS_SIDE, 5));
        mainPlayer = players.get(0);

        circles.add(new MyCircle(new MyPoint(50, 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(50, HEIGHT - 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, HEIGHT - 50), 50, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 50, 50), 50, WOOD));

        circles.add(new MyCircle(new MyPoint(130, 280), 20, WOOD));
        circles.add(new MyCircle(new MyPoint(WIDTH - 130, HEIGHT - 280), 20, WOOD));

        circles.add(new MyCircle(new MyPoint(245, HEIGHT - 20), 10, OXXXYMIRON));
        circles.add(new MyCircle(new MyPoint(245, 20), 10, OXXXYMIRON));

        polygons.add(new MyPolygon(List.of(new MyPoint(99, 50), new MyPoint(99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, 50), new MyPoint(WIDTH - 99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT - 50), new MyPoint(99, HEIGHT)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 99, HEIGHT - 50), new MyPoint(WIDTH - 99, HEIGHT)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(50, 100), new MyPoint(50, HEIGHT - 100)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 50, 100), new MyPoint(WIDTH - 50, HEIGHT - 100)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, 0), new MyPoint(WIDTH - 99, 0)), BOARD));
        polygons.add(new MyPolygon(List.of(new MyPoint(99, HEIGHT), new MyPoint(WIDTH - 99, HEIGHT)), BOARD));

        polygons.add(new MyPolygon(List.of(new MyPoint(120, 50), new MyPoint(380, 50), new MyPoint(380, 40), new MyPoint(120, 40), new MyPoint(120, 50)), BRICK));
        polygons.add(new MyPolygon(List.of(new MyPoint(120, HEIGHT - 50), new MyPoint(380, HEIGHT - 50), new MyPoint(380, HEIGHT - 40), new MyPoint(120, HEIGHT - 40), new MyPoint(120, HEIGHT - 50)), BRICK));
        polygons.add(new MyPolygon(List.of(new MyPoint(100, 100), new MyPoint(150, 100), new MyPoint(150, 200), new MyPoint(100, 200), new MyPoint(100, 100)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 100, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 100), new MyPoint(WIDTH - 150, HEIGHT - 200), new MyPoint(WIDTH - 100, HEIGHT - 200), new MyPoint(WIDTH - 100, HEIGHT - 100)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(110, 350), new MyPoint(140, 350), new MyPoint(140, 380), new MyPoint(110, 380), new MyPoint(110, 350)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 110, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 350), new MyPoint(WIDTH - 140, HEIGHT - 380), new MyPoint(WIDTH - 110, HEIGHT - 380), new MyPoint(WIDTH - 110, HEIGHT - 350)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(240, 80), new MyPoint(340, 80), new MyPoint(340, 100), new MyPoint(240, 100), new MyPoint(240, 80)), STONE));
        polygons.add(new MyPolygon(List.of(new MyPoint(WIDTH - 240, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 80), new MyPoint(WIDTH - 340, HEIGHT - 100), new MyPoint(WIDTH - 240, HEIGHT - 100), new MyPoint(WIDTH - 240, HEIGHT - 80)), STONE));
        Timer timer1 = new Timer(1000, null);
        timer1.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                moveAngles[i] = (double) (new Random().nextInt() % 4) * (Math.PI / 4);
            }
        });
        timer1.start();
        Timer timer = new Timer(7, null);
        timer.addActionListener(e -> {
            for (Player player : players) {
                if (!player.equals(mainPlayer)) {
                    move(player.camera.getAlpha() + moveAngles[players.indexOf(player) - 1], player);
                    double minD = Integer.MAX_VALUE;
                    Player closestPlayer = null;
                    ArrayList<Player> visiblePlayers = new ArrayList<>();
                    for (Player player2 : players) {
                        if (!player.equals(player2)) {
                            double angle = MyMath.getAngle(new MyLine(player.camera.position, player2.camera.position));
                            MyPoint endPoint = intersect(player, angle).getB();
                            outbreak:
                            for (MyPolygon polygon : player2.sides) {
                                for (int j = 0; j < polygon.points.size() - 1; j++) {
                                    MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                                    if (MyMath.pointInLine(endPoint, wall)) {
                                        visiblePlayers.add(player2);
                                        break outbreak;
                                    }
                                }
                            }
                        }
                    }
                    ArrayList<Player> curList = visiblePlayers;
                    if (visiblePlayers.size() == 0) {
                        curList = players;
                    }
                    for (Player player2 : curList) {
                        double dist = (MyMath.getAngle(new MyLine(player.camera.position, player2.camera.position)) - player.camera.getAlpha()) % (2 * Math.PI);
                        if (dist > Math.PI) {
                            dist = dist - 2 * Math.PI;
                        } else if (dist < -Math.PI) {
                            dist = dist + 2 * Math.PI;
                        }
                        if (!player.equals(player2) && Math.abs(dist) < minD) {
                            minD = Math.abs(dist);
                            closestPlayer = player2;
                        }
                    }
                    assert closestPlayer != null;
                    double turnAngle = (MyMath.getAngle(new MyLine(player.camera.position, closestPlayer.camera.position)) - player.camera.getAlpha()) % (2 * Math.PI);
                    if (turnAngle > Math.PI) {
                        turnAngle = turnAngle - 2 * Math.PI;
                    } else if (turnAngle < -Math.PI) {
                        turnAngle = turnAngle + 2 * Math.PI;
                    }
                    turn(turnAngle / 10, player);
                    if (msAfterShot[players.indexOf(player) - 1] < 100) {
                        msAfterShot[players.indexOf(player) - 1] += 7;
                    } else {
                        outbreak:
                        for (MyPolygon polygon : closestPlayer.sides) {
                            for (int j = 0; j < polygon.points.size() - 1; j++) {
                                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                                if (MyMath.pointInLine(intersect(player, player.camera.getAlpha()).getB(), wall)) {
                                    shoot(player);
                                    msAfterShot[players.indexOf(player) - 1] = 0;
                                    break outbreak;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < COUNT_OF_LINES; i++) {
                lines[i] = intersect(mainPlayer, mainPlayer.camera.getAlpha() - VISION / 2 + (DELTA * i));
            }
            if (forward && !backward && left == right) {
                move(mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (backward && !forward && left == right) {
                move(Math.PI + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (right && !left && backward == forward) {
                move(Math.PI / 2 + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (left && !right && backward == forward) {
                move(-Math.PI / 2 + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (left && forward && !backward) {
                move(-Math.PI / 4 + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (right && forward && !backward) {
                move(Math.PI / 4 + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (left && backward && !forward) {
                move(-3 * Math.PI / 4 + mainPlayer.camera.getAlpha(), mainPlayer);
            } else if (right && backward && !forward) {
                move(3 * Math.PI / 4 + mainPlayer.camera.getAlpha(), mainPlayer);
            }
        });
        timer.start();
    }

    public void turn(double angle, Player turningPlayer) {
        turningPlayer.camera.setAlpha(turningPlayer.camera.getAlpha() + angle);
        turningPlayer.updateSides();
    }

    public void shoot(Player shootingPlayer) {
        if (shootingPlayer.weapon.getAmmo() <= 0) {
            return;
        }
        shootingPlayer.weapon.shoot();
        MyLine line = intersect(shootingPlayer, shootingPlayer.camera.getAlpha());
        for (Player player : players) {
            if (!player.equals(shootingPlayer)) {
                for (MyPolygon polygon : player.sides) {
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        if (MyMath.pointInLine(line.getB(), wall)) {
                            player.setHealth(player.getHealth() - 30);
                            if (player.getHealth() <= 0) {
                                if (shootingPlayer.equals(mainPlayer)) {
                                    kills++;
                                }
                                kill(player);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public void kill(Player deadPlayer) {
        Player newPlayer = new Player(new Camera(randomPoint(deadPlayer.size), new Random().nextDouble() % 2 * Math.PI), deadPlayer.sides[0].texture, deadPlayer.sides[2].texture, deadPlayer.sides[1].texture, deadPlayer.sides[3].texture, deadPlayer.size);
        if (deadPlayer.equals(mainPlayer)) {
            mainPlayer = newPlayer;
            deaths++;
        }
        players.set(players.indexOf(deadPlayer), newPlayer);
    }

    public MyPoint randomPoint(double distToWall) {
        boolean good = false;
        double x, y;
        MyPoint newPoint = null;
        while (!good) {
            good = true;
            x = 50 + (new Random().nextInt() % WIDTH + WIDTH) % (WIDTH - 100);
            y = 50 + (new Random().nextInt() % HEIGHT + HEIGHT) % (HEIGHT - 100);
            newPoint = new MyPoint(x, y);
            for (MyPolygon polygon : polygons) {
                double minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
                for (int j = 0; j < polygon.points.size() - 1; j++) {
                    MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                    minX = Math.min(minX, Math.min(wall.getB().getX(), wall.getA().getX()));
                    maxX = Math.max(maxX, Math.max(wall.getB().getX(), wall.getA().getX()));
                    minY = Math.min(minY, Math.min(wall.getB().getY(), wall.getA().getY()));
                    maxY = Math.max(maxY, Math.max(wall.getB().getY(), wall.getA().getY()));
                    if (MyMath.length(MyMath.perpendicular(newPoint, wall)) < distToWall || (x <= maxX && x >= minX && y >= minY && y <= maxY)) {
                        good = false;
                    }
                }
            }
            for (Player player : players) {
                for (MyPolygon polygon : player.sides) {
                    double minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        minX = Math.min(minX, Math.min(wall.getB().getX(), wall.getA().getX()));
                        maxX = Math.max(maxX, Math.max(wall.getB().getX(), wall.getA().getX()));
                        minY = Math.min(minY, Math.min(wall.getB().getY(), wall.getA().getY()));
                        maxY = Math.max(maxY, Math.max(wall.getB().getY(), wall.getA().getY()));
                        if (MyMath.length(MyMath.perpendicular(newPoint, wall)) < distToWall || (x <= maxX && x >= minX && y >= minY && y <= maxY)) {
                            good = false;
                        }
                    }
                }
            }
            for (MyCircle circle : circles) {
                if (MyMath.dist(circle.center, newPoint) < circle.radius + distToWall) {
                    good = false;
                }
            }
        }
        return newPoint;
    }

    public void move(double angle, Player movingPlayer) {
        MyLine line = intersect(movingPlayer, angle);
        MyLine curWall = null;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                if (MyMath.pointInLine(line.getB(), wall)) {
                    curWall = wall;
                }
            }
        }
        for (Player player : players) {
            if (!player.equals(movingPlayer)) {
                for (MyPolygon polygon : player.sides) {
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        if (MyMath.pointInLine(line.getB(), wall)) {
                            curWall = wall;
                        }
                    }
                }
            }
        }
        for (MyCircle circle : circles) {
            if (MyMath.pointInCircle(line.getB(), circle)) {
                MyLine first = MyMath.lineByStartAndAngle(line.getB(), MyMath.getAngle(new MyLine(movingPlayer.camera.position, line.getB())) + Math.PI / 2, 50);
                MyLine second = MyMath.lineByStartAndAngle(line.getB(), MyMath.getAngle(new MyLine(movingPlayer.camera.position, line.getB())) - Math.PI / 2, 50);
                curWall = new MyLine(first.getB(), second.getB());
            }
        }
        MyPoint newPosition = new MyPoint(movingPlayer.camera.position.getX() + Math.sin(Math.PI / 2 - angle) / SLOW_SPEED, movingPlayer.camera.position.getY() + Math.cos(Math.PI / 2 - angle) / SLOW_SPEED);
        if (curWall != null && MyMath.length(MyMath.perpendicular(newPosition, curWall)) < movingPlayer.size) {
            MyLine proection = MyMath.proection(line, curWall);
            double dx = proection.getB().getX() - proection.getA().getX(), dy = proection.getB().getY() - proection.getA().getY();
            newPosition.setX(movingPlayer.camera.position.getX() + dx / (MyMath.length(line) * SLOW_SPEED));
            newPosition.setY(movingPlayer.camera.position.getY() + dy / (MyMath.length(line) * SLOW_SPEED));
        }
        double minX = Integer.MIN_VALUE, maxX = Integer.MAX_VALUE, minY = Integer.MIN_VALUE, maxY = Integer.MAX_VALUE;
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                MyLine perp = MyMath.perpendicular(newPosition, wall);
                if (MyMath.lineAndLine(perp, wall) != null && MyMath.length(perp) < movingPlayer.size) {
                    double perpAngle = MyMath.getAngle(perp);
                    MyPoint newPoint = MyMath.lineByStartAndAngle(perp.getB(), perpAngle + Math.PI, movingPlayer.size).getB();
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
        for (Player player : players) {
            if (!player.equals(movingPlayer)) {
                for (MyPolygon polygon : player.sides) {
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        MyLine perp = MyMath.perpendicular(newPosition, wall);
                        if (MyMath.lineAndLine(perp, wall) != null && MyMath.length(perp) < movingPlayer.size) {
                            double perpAngle = MyMath.getAngle(perp);
                            MyPoint newPoint = MyMath.lineByStartAndAngle(perp.getB(), perpAngle + Math.PI, movingPlayer.size).getB();
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
            }
        }
        for (MyCircle circle : circles) {
            double dist = MyMath.dist(newPosition, circle.center) - circle.radius;
            if (Math.abs(dist) < movingPlayer.size) {
                double length = dist < 0 ? circle.radius - movingPlayer.size : circle.radius + movingPlayer.size;
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
            movingPlayer.camera.position.setX(Math.min(maxX, Math.max(minX, newPosition.getX())));
        }
        if (minY <= maxY) {
            movingPlayer.camera.position.setY(Math.min(maxY, Math.max(minY, newPosition.getY())));
        }
        movingPlayer.updateSides();
    }

    public MyLine intersect(Player playerr, double angle) {
        MyLine line = MyMath.lineByStartAndAngle(playerr.camera.position, angle, LENGTH_OF_LINE);
        for (MyPolygon polygon : polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                MyPoint intersection = MyMath.lineAndLine(line, wall);
                if (intersection != null) {
                    if (MyMath.dist(playerr.camera.position, intersection) < MyMath.length(line)) {
                        line.setB(intersection);
                    }
                }
            }
        }
        for (MyCircle circle : circles) {
            MyPoint intersection = MyMath.lineAndCircle(line, circle);
            if (intersection != null) {
                if (MyMath.dist(playerr.camera.position, intersection) < MyMath.length(line)) {
                    line.setB(intersection);
                }
            }
        }
        for (Player player : players) {
            if (!player.equals(playerr)) {
                for (MyPolygon polygon : player.sides) {
                    for (int j = 0; j < polygon.points.size() - 1; j++) {
                        MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                        MyPoint intersection = MyMath.lineAndLine(line, wall);
                        if (intersection != null) {
                            if (MyMath.dist(playerr.camera.position, intersection) < MyMath.length(line)) {
                                line.setB(intersection);
                            }
                        }
                    }
                }
            }
        }
        return line;
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

    public boolean isMoving() {
        return forward || backward || left || right;
    }

    public Player getMainPlayer() {
        return mainPlayer;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }
}