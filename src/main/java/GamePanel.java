import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    final BufferedImage BLANK_IMG = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(BLANK_IMG, new Point(0, 0), "blank cursor");
    final Game game;
    final int width, height;
    final double vertical;
    private Point cursor;
    private Robot robot;

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
        vertical = (double) height / 2;
        setCursor(BLANK_CURSOR);
        setLayout(null);
        setPreferredSize(new Dimension(width, height));
        Timer timer = new Timer(7, null);
        timer.addActionListener(e -> {
            int dx = (int) (cursor.getX() - MouseInfo.getPointerInfo().getLocation().getX()), dy = (int) (cursor.getY() - MouseInfo.getPointerInfo().getLocation().getY());
            if (Math.abs(dx) < width / 4 && Math.abs(dy) < height / 4) {
                game.player.setAlpha(game.player.getAlpha() - Math.PI * ((double) dx / (double) width));
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
        g2d.fillOval((int) (game.player.position.getX() * coef) - 3,
                (int) (game.player.position.getY() * coef) - 3, 6, 6);
        for (MyPolygon polygon : game.polygons) {
            for (int j = 0; j < polygon.points.size() - 1; j++) {
                MyLine wall = new MyLine(polygon.points.get(j), polygon.points.get(j + 1));
                g2d.drawLine((int) (wall.getA().getX() * coef),
                        (int) (wall.getA().getY() * coef),
                        (int) (wall.getB().getX() * coef),
                        (int) (wall.getB().getY() * coef));
            }
        }
        for (MyCircle circle : game.circles) {
            g2d.drawOval((int) ((circle.center.getX() - circle.radius) * coef),
                    (int) ((circle.center.getY() - circle.radius) * coef),
                    (int) (2 * circle.radius * coef),
                    (int) (2 * circle.radius * coef));
        }
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(width / 2, height / 2 - 5, width / 2, height / 2 + 5);
        g2d.drawLine(width / 2 - 5, height / 2, width / 2 + 5, height / 2);
    }
}