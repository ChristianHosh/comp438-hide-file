package com.comp438.hidefile.lsb;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelArrayStrategy {

    public static Pixel[] getPixelsArray(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        Pixel[] pixels = new Pixel[height * width];

        boolean flag = true;
        int index = 0;
        int x = 0, y = 0, count;

        while (x < width && y < height) {
            count = 0;
            if (flag) {
                for (; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    pixels[index] = new Pixel(x, y, color);
                    count++;
                    index++;
                }
                y++;
                x -= count;
            } else {
                for (; y < height; y++) {
                    Color color = new Color(image.getRGB(x, y));
                    pixels[index] = new Pixel(x, y, color);
                    count++;
                    index++;
                }
                x++;
                y -= count;
            }
            flag = !flag;
        }

        return pixels;
    }

}
