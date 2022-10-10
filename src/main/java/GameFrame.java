import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame {
    public GameFrame() {
        Game game = new Game();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    game.startMovingLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    game.startMovingBackward();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    game.startMovingRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    game.startMovingForward();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    game.stopMovingLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    game.stopMovingBackward();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    game.stopMovingRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    game.stopMovingForward();
                }
            }
        });
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(new GamePanel(width, height, game));
        pack();
        setVisible(true);
    }
}