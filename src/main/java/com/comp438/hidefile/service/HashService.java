package com.comp438.hidefile.service;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class HashService {
    private File currentFile;

    public boolean check(String hash) {
        if (currentFile == null || hash == null)
            return false;

        String hashToCheckAgainst = generate().trim();

        return (hash.trim().equals(hashToCheckAgainst));
    }

    public String generate() {
        if (currentFile == null)
            return null;

        try (InputStream stream = new FileInputStream(currentFile)) {
            byte[] bytes = stream.readAllBytes();
            return createHashFromBytes(bytes);
        } catch (IOException exception) {
            return null;
        }
    }

    private String createHashFromBytes(byte[] bytes) {
        long hash = Long.MAX_VALUE;

        for (int i = 0; i < bytes.length; i++) {
            hash ^= (bytes[i] + (i * 31L));
            hash = Long.rotateLeft(hash, 13);
        }

        hash ^= bytes.length;
        hash = Long.rotateRight(hash, 7);

        return Long.toHexString(hash).trim();
    }


}
