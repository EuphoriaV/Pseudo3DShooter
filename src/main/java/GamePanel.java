import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    final BufferedImage CURSOR_IMG = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(CURSOR_IMG, new Point(0, 0), "blank cursor");

    private final Game game;
    private final int width, height;
    private Point cursor;
    private double vertical;
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
                game.getPlayer().setAlpha(game.getPlayer().getAlpha() - Math.PI * ((double) dx / (double) width));
                vertical = Math.max(Math.min(vertical + cursor.getY() - MouseInfo.getPointerInfo().getLocation().getY(), 9 * (double) height / 10), (double) height / 10);
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
            double dist = MyMath.length(game.getLines()[i]);
            if (dist < game.LENGTH_OF_LINE - 5) {
                g2d.setPaint(new Color((int) (100 - 100 * (dist / game.LENGTH_OF_LINE)), 0, (int) (150 - 150 * (dist / game.LENGTH_OF_LINE))));
            } else {
                g2d.setPaint(Color.BLACK);
            }
            double heightOnScreen = height * game.D_SHTRIH / dist;
            g2d.drawLine(pixel, (int) vertical, pixel, (int) vertical + (int) heightOnScreen);
            g2d.drawLine(pixel, (int) vertical, pixel, (int) vertical - (int) heightOnScreen);
        }
        double coef = (double) (height / 6) / (double) game.HEIGHT;
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, height/ 6, height / 6);
        g2d.setPaint(Color.WHITE);
        g2d.fillOval((int) (game.getPlayer().getPosition().getX() * coef) - 3,
                (int) (game.getPlayer().getPosition().getY() * coef) - 3, 6, 6);
        for (MyPolygon polygon : game.getPolygons()) {
            for (int j = 0; j < polygon.getPoints().size(); j++) {
                MyLine wall = new MyLine(polygon.getPoints().get(j), polygon.getPoints().get((j + 1) % polygon.getPoints().size()));
                g2d.drawLine((int) (wall.getA().getX() * coef),
                        (int) (wall.getA().getY() * coef),
                        (int) (wall.getB().getX() * coef),
                        (int) (wall.getB().getY() * coef));
            }
        }
        for (MyCircle circle : game.getCircles()) {
            g2d.drawOval((int) ((circle.center.getX() - circle.radius) * coef),
                    (int) ((circle.center.getY() - circle.radius) * coef),
                    (int) (2 * circle.radius * coef),
                    (int) (2 * circle.radius * coef));
        }
    }
}
