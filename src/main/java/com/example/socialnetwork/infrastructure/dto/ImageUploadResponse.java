package com.example.socialnetwork.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}