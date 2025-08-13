package com.example.socialnetwork.application.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

public interface FileStorageService {
    void init();
    String save(MultipartFile file);
    Resource load(String filename);
    void delete(String filename);
}