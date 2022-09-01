import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    final BufferedImage BLANK_IMG = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(BLANK_IMG, new Point(0, 0), "blank cursor");
    final Game game;
    final int width, height;
    final double vertical;
    private Point cursor;
    private Robot robot;
    private double gunY, dy;
    private boolean isShooting = false;

    public GamePanel(int width, int height, Game game) {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        robot.mouseMove(width / 2, height / 2);
        cursor = MouseInfo.getPointerInfo().getLocation();
        this.width = width;
        this.height = height;
        this.game = game;
        dy = height / 240;
        gunY = 17 * height / 24;
        vertical = (double) height / 2;
        setCursor(BLANK_CURSOR);
        setLayout(null);
        setPreferredSize(new Dimension(width, height));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                game.shoot(game.mainPlayer);
                isShooting = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        Timer timer = new Timer(7, null);
        timer.addActionListener(e -> {
            if (game.isMoving() || Math.abs(gunY - 17*height / 24) >= height/240) {
                if (gunY >= 20 * height / 24) {
                    dy = -height / 240;
                } else if (gunY <= 17*height / 24) {
                    dy = height / 240;
                }
                gunY += dy;
            }
            if (isShooting) {
                gunY = 2*height / 3;
                dy = height / 240;
                isShooting = false;
            }
            int dx = (int) (cursor.getX() - MouseInfo.getPointerInfo().getLocation().getX()), dy = (int) (cursor.getY() - MouseInfo.getPointerInfo().getLocation().getY());
            if (Math.abs(dx) < width / 4 && Math.abs(dy) < height / 4) {
                game.turn(-Math.PI * ((double) dx / (double) width), game.mainPlayer);
            }
            cursor = MouseInfo.getPointerInfo().getLocation();
            if (cursor.getX() < (double) width / 10 || cursor.getX() > 9 * (double) width / 10) {
                robot.mouseMove(width / 2, height / 2);
                cursor = MouseInfo.getPointerInfo().getLocation();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, width, (int) vertical);
        g2d.setPaint(Color.DARK_GRAY);
        g2d.fillRect(0, (int) vertical, width, height);
        for (int pixel = 0; pixel < width; pixel++) {
            int i = (int) (Math.min(game.COUNT_OF_LINES - 1, game.COUNT_OF_LINES * ((double) pixel / (double) width)));
            MyLine curLine = game.lines[i];
            double numOfColumn = -1;
            double dist = MyMath.length(curLine);
            outbreak:
            for (MyPolygon polygon : game.polygons) {
                for (int j = 0; j < polygon.points.size() - 1; j++) {
                    MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                    if (MyMath.pointInLine(curLine.getB(), wall)) {
                        if (polygon.texture.stretched) {
                            numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / MyMath.length(wall);
                        } else {
                            numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / 30;
                        }
                        double heightOnScreen = height * game.D_SHTRIH / dist;
                        g2d.drawImage(polygon.texture.getImage()[(int) (numOfColumn * polygon.texture.getImage().length) % polygon.texture.getImage().length], pixel, (int) vertical - (int) heightOnScreen, 1, 2 * (int) heightOnScreen, null);
                        break outbreak;
                    }
                }
            }
            if (numOfColumn == -1) {
                outbreak:
                for (Player player : game.players) {
                    if (!player.equals(game.mainPlayer)) {
                        for (MyPolygon polygon : player.sides) {
                            for (int j = 0; j < polygon.points.size() - 1; j++) {
                                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                                if (MyMath.pointInLine(curLine.getB(), wall)) {
                                    if (polygon.texture.stretched) {
                                        numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / MyMath.length(wall);
                                    } else {
                                        numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / 30;
                                    }
                                    double heightOnScreen = height * game.D_SHTRIH / dist;
                                    g2d.drawImage(polygon.texture.getImage()[(int) (numOfColumn * polygon.texture.getImage().length) % polygon.texture.getImage().length], pixel, (int) vertical - (int) heightOnScreen, 1, 2 * (int) heightOnScreen, null);
                                    break outbreak;
                                }
                            }
                        }
                    }
                }
            }
            if (numOfColumn == -1) {
                for (MyCircle circle : game.circles) {
                    if (MyMath.pointInCircle(curLine.getB(), circle)) {
                        double angle = MyMath.getAngle(new MyLine(circle.center, curLine.getB()));
                        angle += Math.PI;
                        if (circle.texture.stretched) {
                            numOfColumn = 0.5 * angle / Math.PI;
                        } else {
                            numOfColumn = 4 * angle / Math.PI;
                        }
                        double heightOnScreen = height * game.D_SHTRIH / dist;
                        g2d.drawImage(circle.texture.getImage()[(int) (numOfColumn * circle.texture.getImage().length) % circle.texture.getImage().length], pixel, (int) vertical - (int) heightOnScreen, 1, 2 * (int) heightOnScreen, null);
                        break;
                    }
                }
            }
        }
        double coef = (double) (height / 6) / (double) game.HEIGHT;
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, height / 6, height / 6);
        g2d.setPaint(Color.WHITE);
        for (MyPolygon polygon : game.polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                g2d.drawLine((int) (wall.getA().getX() * coef),
                        (int) (wall.getA().getY() * coef),
                        (int) (wall.getB().getX() * coef),
                        (int) (wall.getB().getY() * coef));
            }
        }
        for (Player player : game.players) {
            if (player.equals(game.mainPlayer)) {
                g2d.setPaint(Color.GREEN);
            } else {
                g2d.setPaint(Color.RED);
            }
            for (MyPolygon polygon : player.sides) {
                for (int j = 0; j < polygon.points.size() - 1; j++) {
                    MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                    g2d.drawLine((int) (wall.getA().getX() * coef),
                            (int) (wall.getA().getY() * coef),
                            (int) (wall.getB().getX() * coef),
                            (int) (wall.getB().getY() * coef));
                }
            }
        }
        g2d.setPaint(Color.WHITE);
        for (MyCircle circle : game.circles) {
            g2d.drawOval((int) ((circle.center.getX() - circle.radius) * coef),
                    (int) ((circle.center.getY() - circle.radius) * coef),
                    (int) (2 * circle.radius * coef),
                    (int) (2 * circle.radius * coef));
        }
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(width / 2, height / 2 - 5, width / 2, height / 2 + 5);
        g2d.drawLine(width / 2 - 5, height / 2, width / 2 + 5, height / 2);
        g2d.drawImage(new ImageIcon("images/gun.png").getImage(), 2 * width / 3, (int) gunY, width / 3, height / 3, null);
        g2d.setFont(new Font("Verdana", Font.BOLD, height / 20));
        g2d.drawString(String.valueOf(game.mainPlayer.weapon.getAmmo()), width / 50, 89 * height / 100);
        g2d.drawImage(new ImageIcon("images/ammo.png").getImage(), 0, 84 * height / 100, width / 50, height / 20, null);
        g2d.setPaint(new Color(200 - 2 * game.mainPlayer.getHealth(), 2 * game.mainPlayer.getHealth(), 0));
        g2d.fillRect(width / 100, 92 * height / 100, game.mainPlayer.getHealth() * width / 1000, height / 20);
        g2d.setPaint(Color.WHITE);
        g2d.drawRect(width / 100, 92 * height / 100, width / 10, height / 20);
    }
}