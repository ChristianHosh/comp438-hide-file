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

        String hashToCheckAgainst = generate();

        return (hash.equals(hashToCheckAgainst));
    }

    public String generate() {
        if (currentFile == null)
            return null;

        String hash;
        try (InputStream stream = new FileInputStream(currentFile)) {
            byte[] bytes = stream.readAllBytes();

            hash = createHashFromBytes(bytes);

            return hash;
        } catch (IOException exception) {
            return null;
        }
    }

    private String createHashFromBytes(byte[] bytes) {
        long hash = 0;

        for (int i = 0; i < bytes.length; i++) {
            hash ^= (bytes[i] + (i * 31L));
            hash = Long.rotateLeft(hash, 13);
        }

        return Long.toHexString(hash);
    }
}
