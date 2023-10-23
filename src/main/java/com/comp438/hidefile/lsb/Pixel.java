package com.comp438.hidefile.lsb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.Color;

@Getter
@Setter
@AllArgsConstructor
public class Pixel {
    private int x;
    private int y;
    private Color color;

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }
}