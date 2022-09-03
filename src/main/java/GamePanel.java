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
        dy = height / 240.0;
        gunY = 17 * height / 24.0;
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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    game.shoot(game.getMainPlayer());
                    isShooting = true;
                }
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
            if (game.isMoving() || Math.abs(gunY - 17 * height / 24.0) >= height / 240.0) {
                if (gunY >= 5 * height / 6.0) {
                    dy = -height / 240.0;
                } else if (gunY <= 17 * height / 24.0) {
                    dy = height / 240.0;
                }
                gunY += dy;
            }
            if (isShooting) {
                gunY = 2 * height / 3.0;
                isShooting = false;
            }
            int dx = (int) (cursor.getX() - MouseInfo.getPointerInfo().getLocation().getX());
            if (Math.abs(dx) < width / 4) {
                game.turn(-Math.PI * ((double) dx / (double) width), game.getMainPlayer());
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
            double heightOnScreen = height * game.D_SHTRIH / dist;
            boolean isPlayer = false;
            MyTexture curTexture = null;
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
                        curTexture = polygon.texture;
                        break outbreak;
                    }
                }
            }
            if (numOfColumn == -1) {
                outbreak:
                for (Player player : game.players) {
                    if (!player.equals(game.getMainPlayer())) {
                        for (MyPolygon polygon : player.sides) {
                            for (int j = 0; j < polygon.points.size() - 1; j++) {
                                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                                if (MyMath.pointInLine(curLine.getB(), wall)) {
                                    if (polygon.texture.stretched) {
                                        numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / MyMath.length(wall);
                                    } else {
                                        numOfColumn = MyMath.dist(wall.getA(), curLine.getB()) / 30;
                                    }
                                    curTexture = polygon.texture;
                                    isPlayer = true;
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
                        curTexture = circle.texture;
                        break;
                    }
                }
            }
            if (curTexture != null) {
                g2d.drawImage(curTexture.getImage()[(int) (numOfColumn * curTexture.getImage().length) % curTexture.getImage().length], pixel, (int) vertical - (int) heightOnScreen, 1, 2 * (int) heightOnScreen, null);
                if (!isPlayer) {
                    g2d.setPaint(new Color(0, 0, 0, Math.min((int) (255 * (dist / game.LENGTH_OF_LINE)), 255)));
                    g2d.fillRect(pixel, (int) vertical - (int) heightOnScreen, 1, 2 * (int) heightOnScreen);
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
            if (player.equals(game.getMainPlayer())) {
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
        g2d.drawString(String.valueOf(game.getMainPlayer().weapon.getAmmo()), width / 50, 89 * height / 100);
        g2d.drawString("Kills: " + game.getKills(), width / 50, 79 * height / 100);
        g2d.drawString("Deaths: " + game.getDeaths(), width / 50, 69 * height / 100);
        g2d.drawImage(new ImageIcon("images/ammo.png").getImage(), 0, 84 * height / 100, width / 50, height / 20, null);
        g2d.setPaint(new Color(200 - 2 * game.getMainPlayer().getHealth(), 2 * game.getMainPlayer().getHealth(), 0));
        g2d.fillRect(width / 100, 92 * height / 100, game.getMainPlayer().getHealth() * width / 1000, height / 20);
        g2d.setPaint(Color.WHITE);
        g2d.drawRect(width / 100, 92 * height / 100, width / 10, height / 20);
    }
}