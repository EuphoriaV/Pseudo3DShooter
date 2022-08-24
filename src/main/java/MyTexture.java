import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyTexture {
    final BufferedImage BLANK_IMG = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final boolean stretched;
    private Image[] image;


    public MyTexture(String name) {
        this.stretched = false;
        getImage(name);
    }

    public MyTexture(String name, boolean stretched) {
        this.stretched = stretched;
        getImage(name);
    }

    private void getImage(String name) {
        BufferedImage img = BLANK_IMG;
        try {
            img = ImageIO.read(new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = new Image[img.getWidth()];
        for (int i = 0; i < image.length; i++) {
            image[i] = img.getSubimage(Math.min(i, img.getWidth() - 1), 0, 1, img.getHeight());
        }
    }

    public Image[] getImage() {
        return image;
    }
}
