package com.kn.wedding.manager.utils;

import org.springframework.util.Assert;

import java.util.Arrays;

public enum FileType {
    PNG("image/png"),
    JPEG("image/jpeg"),
    SVG("image/svg+xml");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static FileType from(String type) {
        Assert.hasText(type, "Provide a value");

        return Arrays.stream(FileType.values())
                .filter(fType -> type.equals(fType.value()))
                .findFirst().orElseThrow();
    }
}
