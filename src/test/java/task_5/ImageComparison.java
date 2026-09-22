package task_5;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageComparison {

    /**
     * Сравнивает два изображения попиксельно.
     * diff-изображение сохраняется ТОЛЬКО если есть различия.
     *
     * @return количество различающихся пикселей
     */
    public static long compare(Path actual, Path expected, Path diff) throws IOException {
        BufferedImage a = ImageIO.read(actual.toFile());
        BufferedImage e = ImageIO.read(expected.toFile());

        if (a == null || e == null) {
            throw new IOException("Cannot read one of the images: " + actual + " / " + expected);
        }

        int w = Math.max(a.getWidth(), e.getWidth());
        int h = Math.max(a.getHeight(), e.getHeight());
        BufferedImage d = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        long count = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int ra = (x < a.getWidth() && y < a.getHeight()) ? a.getRGB(x, y) : 0xFFFFFF;
                int re = (x < e.getWidth() && y < e.getHeight()) ? e.getRGB(x, y) : 0xFFFFFF;
                if (ra != re) {
                    d.setRGB(x, y, 0xFF0000);
                    count++;
                } else {
                    d.setRGB(x, y, ra);
                }
            }
        }

        // Сохраняем diff и actual только при наличии различий
        if (count > 0) {
            File df = diff.toFile();
            if (df.getParentFile() != null) {
                df.getParentFile().mkdirs();
            }
            ImageIO.write(d, "png", df);
        } else {
            Files.deleteIfExists(diff);
            Files.deleteIfExists(actual);
        }

        return count;
    }
}