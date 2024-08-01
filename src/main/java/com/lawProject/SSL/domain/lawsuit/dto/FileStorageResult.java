package com.lawProject.SSL.domain.lawsuit.dto;

public class FileStorageResult {
    private final String originalFileName;
    private final String storedFileName;

    public FileStorageResult(String originalFileName, String storedFileName) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }
}
