package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {

    private int color;
    BufferedImage img;

    public Renderer(BufferedImage img) {
        this.img = img;
        color = Color.RED.getRGB();
    }

    private void drawPixel(int x, int y) {
        img.setRGB(x, y, color);
    }

    public void lineTrivial(int x1, int y1, int x2, int y2) {
        // y = kx + q
        int dx = x1 - x2;
        int dy = y1 - y2;

        if (Math.abs(dx) > Math.abs(dy)) {
            // Řídící osa x
            //float k .. q
            //for(...){
            //  drawPixel(x, y)
            //}
            if (x1 > x2) {
                int p = x1;
                x1 = x2;
                x2 = p;

                p = y1;
                y1 = y2;
                //y2 = p; už s tím dále nepracujeme, tak není potřeba
            }

            float k = (float) dy / (float) dx;

            for (int x = x1; x < x2; x++) {
                int y = y1 + (int) (k * (x - x1));

                drawPixel(x, y);
            }
        } else {
            //Řídící osa y

            // Řídící osa x
            //float k .. q
            //for(...){
            //  drawPixel(x, y)
            //}
            if (x1 > x2) {
                int p = x1;
                x1 = x2;
                x2 = p;

                p = y1;
                y1 = y2;
                //y2 = p; už s tím dále nepracujeme, tak není potřeba
            }

            float k = (float) dy / (float) dx;

            for (int x = x1; x < x2; x++) {
                int y = y1 + (int) (k * (x - x1));

                drawPixel(x, y);

            }
        }
    }
}
