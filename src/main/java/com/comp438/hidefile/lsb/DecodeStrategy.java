package com.comp438.hidefile.lsb;

import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DecodeStrategy {
    public static String DecodeFile(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            Pixel[] pixels = GetPixelArray(image);

            return DecodeMessageFromPixels(pixels);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
            return null;
        }
    }

    private static Pixel[] GetPixelArray(BufferedImage imageToEncrypt) {
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

    private static String DecodeMessageFromPixels(Pixel[] pixels) {
        boolean completed = false;
        int pixelArrayIndex = 0;
        StringBuilder messageBuilder = new StringBuilder();
        while (!completed) {
            Pixel[] pixelsToRead = new Pixel[3];
            for (int i = 0; i < 3; i++) {
                pixelsToRead[i] = pixels[pixelArrayIndex];
                pixelArrayIndex++;
            }
            messageBuilder.append(ConvertPixelsToCharacter(pixelsToRead));
            if (IsEndOfMessage(pixelsToRead[2])) {
                completed = true;
            }
        }
        return messageBuilder.toString();
    }

    private static char ConvertPixelsToCharacter(Pixel[] pixelsToRead) {
        ArrayList<String> binaryValues = new ArrayList<>();
        for (Pixel pixel : pixelsToRead) {
            String[] currentBinary = turnPixelIntoBinary(pixel);
            binaryValues.add(currentBinary[0]);
            binaryValues.add(currentBinary[1]);
            binaryValues.add(currentBinary[2]);
        }
        return ConvertBinaryValuesToCharacter(binaryValues);
    }

    private static String[] turnPixelIntoBinary(Pixel pixel) {
        String[] values = new String[3];
        values[0] = Integer.toBinaryString(pixel.getRed());
        values[1] = Integer.toBinaryString(pixel.getGreen());
        values[2] = Integer.toBinaryString(pixel.getBlue());
        return values;
    }

    private static boolean IsEndOfMessage(Pixel pixel) {
        return !turnPixelIntoBinary(pixel)[2].endsWith("1");
    }

    private static char ConvertBinaryValuesToCharacter(ArrayList<String> binaryValues) {
        StringBuilder endBinary = new StringBuilder();
        for (int i = 0; i < binaryValues.size() - 1; i++) {
            endBinary.append(binaryValues.get(i).charAt(binaryValues.get(i).length() - 1));
        }
        String endBinaryString = endBinary.toString();
        String noZeros = RemovePaddedZeros(endBinaryString);
        int ascii = Integer.parseInt(noZeros, 2);
        return (char) ascii;
    }

    private static String RemovePaddedZeros(String endBinary) {
        StringBuilder builder = new StringBuilder(endBinary);
        int paddedZeros = 0;
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '0') {
                paddedZeros++;
            } else {
                break;
            }
        }
        for (int i = 0; i < paddedZeros; i++) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}
