package org.example.infrastructure.adapter.output.walrus;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.infrastructure.adapter.input.dto.responses.walrus.WalrusUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.PUT;

@Component
@RequiredArgsConstructor
public class WalrusCloudService implements CloudServiceUseCase {
    @Value("${walrus.epochs}")
    private String epochs;
    @Value("${walrus.address}")
    private String walrusUploadAddress;
    @Value("${walrus.upload.url}")
    private String walrusUploadURL;
    @Value("${walrus.download.url}")
    private String walrusDownloadURL;

    private final RestTemplate restTemplate;

    @Override
    public String upload(MultipartFile file) {
        return extractBlobIdFrom(restTemplate.exchange(walrusUploadURL, PUT,
                buildUploadRequest(file), WalrusUploadResponse.class, createQueryParam()));
    }

    @Override
    public byte[] getFileBy(String blobId) {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(walrusDownloadURL + "/" + blobId, byte[].class);
        return response.getBody();
    }

    private HttpEntity<?> buildUploadRequest(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource resource = file.getResource();
        return new HttpEntity<>(resource, headers);
    }

    private static String extractBlobIdFrom(ResponseEntity<WalrusUploadResponse> response) {
        WalrusUploadResponse walrusUploadResponse = response.getBody();
        boolean isFileAlreadyExists = walrusUploadResponse != null && walrusUploadResponse.getNewlyCreated() == null;
        if (isFileAlreadyExists) return walrusUploadResponse.getAlreadyCertified().getBlobId();
        assert walrusUploadResponse != null;
        return walrusUploadResponse.getNewlyCreated().getBlobObject().getBlobId();
    }

    private Map<String, ?> createQueryParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("epochs", epochs);
        params.put("send_object_to", walrusUploadAddress);
        return params;
    }
}