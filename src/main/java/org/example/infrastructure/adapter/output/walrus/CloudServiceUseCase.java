package org.example.infrastructure.adapter.output.walrus;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface CloudServiceUseCase {
    String upload(MultipartFile file);
    byte[] getFileBy(String blobId);
}
