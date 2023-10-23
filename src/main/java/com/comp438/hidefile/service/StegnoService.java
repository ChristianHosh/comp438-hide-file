package com.comp438.hidefile.service;

import com.comp438.hidefile.lsb.DecodeStrategy;
import com.comp438.hidefile.lsb.EncodeStrategy;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.File;

@Getter
@Setter
public class StegnoService {
    private File currentFile;

    public BufferedImage encode(String message){
        return EncodeStrategy.EncodeFile(currentFile, message);
    }

    public String decode() {
        return DecodeStrategy.DecodeFile(currentFile);
    }
}
