package com.comp438.hidefile.lsb;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelStrategy {
    public static Pixel[] getPixelArraySequential(BufferedImage imageToEncrypt) {
        int height = imageToEncrypt.getHeight();
        int width = imageToEncrypt.getWidth();
        Pixel[] pixels = new Pixel[height * width];

        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color colorToAdd = new Color(imageToEncrypt.getRGB(x, y));
                pixels[count] = new Pixel(x, y, colorToAdd);
                count++;
            }
        }
        return pixels;
    }

    public static Pixel[] getPixelArrayNonSequential(BufferedImage imageToEncrypt) {
        int height = imageToEncrypt.getHeight();
        int width = imageToEncrypt.getWidth();
        Pixel[] pixels = new Pixel[height * width];

        int count = 0;
        int seed = 42;

        for (int i = 0; i < height * width; i++) {
            seed = (214013 * seed + 2531011);
            int index = (seed >> 16) & 0x7FFF;
            int x = index % width;
            int y = index / width;

            Color colorToAdd = new Color(imageToEncrypt.getRGB(x, y));
            pixels[count] = new Pixel(x, y, colorToAdd);
            count++;
        }

        return pixels;
    }
}
