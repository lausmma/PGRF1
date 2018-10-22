package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {

    public Color color;
    BufferedImage img;

    public Renderer(BufferedImage img) {
        this.img = img;
    }

    public void drawPixel(int x, int y, Color color) {
        if(x < 0 || x >= img.getWidth()){
            return;
        }
        if(y < 0 || y >= img.getHeight()){
            return;
        }
        img.setRGB(x, y, color.getRGB());
    }

    public void lineDDA(double x1, double y1, double x2, double y2) {
        double dx, dy;
        float k, g, h; // G = prirustek X, H = prirustek Y;

        dx = x1 - x2;
        dy = y1 - y2;

        k = (float)dy / (float)dx;

        if (Math.abs(dx) > Math.abs(dy)) {
            g = 1; //jdeme po x - prirustek po 1
            h = k;
            if (x2 < x1) { // prohozeni
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        } else {
            g = 1 / k;
            h = 1;//jdeme po y - prirustek po 1
            if (y2 < y1) {// prohozeni
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        }
        float x = (float)x1;
        float y = (float)y1;

        for (int l = 1; l < Math.max(Math.abs(dx), Math.abs(dy)); l++) {

            drawPixel(Math.round(x), Math.round(y)+1, color.RED.brighter());
            drawPixel(Math.round(x), Math.round(y), color.RED);
            drawPixel(Math.round(x), Math.round(y)-1, color.RED.brighter());
            x = x + g;
            y = y + h;
        }
    }

    public void drawPolygon(int x1, int y1, int x2, int y2, int ax, int ay){
        double x0 = x2 - x1;
        double y0 = y2 - y1;
        int a1 = ax;
        int a2 = ay;

        double circleRadius = 2*Math.PI;

        int count = (int)Math.sqrt(Math.pow(Math.abs(a1 - x2), 2) + Math.pow(Math.abs(a2 - y2), 2)) / 15;

        if(count < 3){
            count = 3;
        }
        double step = circleRadius / (double)count;
        for(double i = 0; i < circleRadius; i += step) {
            //dle rotační matice
            double x = x0 * Math.cos(step) + y0 * Math.sin(step);
            double y = y0 * Math.cos(step) - x0 * Math.sin(step);
            lineDDA((int) x0 + x1, (int) y0 + y1, (int) x + x1, (int) y + y1);
            x0 = x;
            y0 = y;
        }
    }
}
