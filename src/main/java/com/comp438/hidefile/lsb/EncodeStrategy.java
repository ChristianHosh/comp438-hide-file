package com.comp438.hidefile.lsb;

import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class EncodeStrategy {
    public static BufferedImage encodeFile(File file, String message) {
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage imageToEncrypt = getImageToEncrypt(image);

            Pixel[] pixels = getPixelArray(imageToEncrypt);
            String[] messageInBinary = convertMessageToBinary(message);

            encodeMessageBinaryInPixels(pixels, messageInBinary);
            replacePixelsInNewBufferedImage(pixels, image);
            return image;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
            return null;
        }
    }

    private static BufferedImage getImageToEncrypt(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private static Pixel[] getPixelArray(BufferedImage imageToEncrypt) {
//        int height = imageToEncrypt.getHeight();
//        int width = imageToEncrypt.getWidth();
//        Pixel[] pixels = new Pixel[height * width];
//        int count = 0;
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                Color colorToAdd = new Color(imageToEncrypt.getRGB(x, y));
//                pixels[count] = new Pixel(x, y, colorToAdd);
//                count++;
//            }
//        }
//        return pixels;
        return PixelStrategy.getPixelArrayNonSequential(imageToEncrypt);

    }

    private static String[] convertMessageToBinary(String message) {
        int[] messageInAscii = convertMessageToAscii(message);
        return convertAsciiToBinary(messageInAscii);
    }

    private static int[] convertMessageToAscii(String message) {
        int[] messageCharactersInAscii = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            int asciiValue = message.charAt(i);
            messageCharactersInAscii[i] = asciiValue;
        }
        return messageCharactersInAscii;
    }

    private static String[] convertAsciiToBinary(int[] messageInAscii) {
        String[] messageInBinary = new String[messageInAscii.length];
        for (int i = 0; i < messageInAscii.length; i++) {
            String asciiBinary = leftPadZeros(Integer.toBinaryString(messageInAscii[i]));
            messageInBinary[i] = asciiBinary;
        }
        return messageInBinary;
    }

    private static String leftPadZeros(String value) {
        StringBuilder paddedValue = new StringBuilder("00000000");
        int offSet = 8 - value.length();
        for (int i = 0; i < value.length(); i++) {
            paddedValue.setCharAt(i + offSet, value.charAt(i));
        }
        return paddedValue.toString();
    }

    private static void encodeMessageBinaryInPixels(Pixel[] pixels, String[] messageBinary) {
        int pixelIndex = 0;
        boolean isLastCharacter = false;
        for (int i = 0; i < messageBinary.length; i++) {
            Pixel[] currentPixels = new Pixel[]{pixels[pixelIndex], pixels[pixelIndex + 1], pixels[pixelIndex + 2]};
            if (i + 1 == messageBinary.length) {
                isLastCharacter = true;
            }
            changePixelsColor(messageBinary[i], currentPixels, isLastCharacter);
            pixelIndex = pixelIndex + 3;
        }
    }

    private static void changePixelsColor(String messageBinary, Pixel[] pixels, boolean isLastCharacter) {
        int messageBinaryIndex = 0;
        for (int i = 0; i < pixels.length - 1; i++) {
            char[] messageBinaryChars = new char[]{messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex + 1), messageBinary.charAt(messageBinaryIndex + 2)};
            String[] pixelRGBBinary = getPixelsRGBBinary(pixels[i], messageBinaryChars);
            pixels[i].setColor(getNewPixelColor(pixelRGBBinary));
            messageBinaryIndex = messageBinaryIndex + 3;
        }
        if (!isLastCharacter) {
            char[] messageBinaryChars = new char[]{messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex + 1), '1'};
            String[] pixelRGBBinary = getPixelsRGBBinary(pixels[pixels.length - 1], messageBinaryChars);
            pixels[pixels.length - 1].setColor(getNewPixelColor(pixelRGBBinary));
        } else {
            char[] messageBinaryChars = new char[]{messageBinary.charAt(messageBinaryIndex), messageBinary.charAt(messageBinaryIndex + 1), '0'};
            String[] pixelRGBBinary = getPixelsRGBBinary(pixels[pixels.length - 1], messageBinaryChars);
            pixels[pixels.length - 1].setColor(getNewPixelColor(pixelRGBBinary));
        }
    }

    private static String[] getPixelsRGBBinary(Pixel pixel, char[] messageBinaryChars) {
        String[] pixelRGBBinary = new String[3];
        pixelRGBBinary[0] = changePixelBinary(Integer.toBinaryString(pixel.getRed()), messageBinaryChars[0]);
        pixelRGBBinary[1] = changePixelBinary(Integer.toBinaryString(pixel.getGreen()), messageBinaryChars[1]);
        pixelRGBBinary[2] = changePixelBinary(Integer.toBinaryString(pixel.getBlue()), messageBinaryChars[2]);
        return pixelRGBBinary;
    }

    private static String changePixelBinary(String pixelBinary, char messageBinaryChar) {
        StringBuilder sb = new StringBuilder(pixelBinary);
        sb.setCharAt(pixelBinary.length() - 1, messageBinaryChar);
        return sb.toString();
    }

    private static Color getNewPixelColor(String[] colorBinary) {
        return new Color(Integer.parseInt(colorBinary[0], 2), Integer.parseInt(colorBinary[1], 2), Integer.parseInt(colorBinary[2], 2));
    }

    private static void replacePixelsInNewBufferedImage(Pixel[] newPixels, BufferedImage newImage) {
        for (Pixel newPixel : newPixels) {
            newImage.setRGB(newPixel.getX(), newPixel.getY(), newPixel.getColor().getRGB());
        }
    }
}
