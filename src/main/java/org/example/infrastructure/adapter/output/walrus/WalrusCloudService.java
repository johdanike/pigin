package org.example.infrastructure.adapter.output.walrus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.adapter.input.dto.responses.walrus.WalrusUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.PUT;

@Slf4j
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
        log.info("Starting file upload to Walrus Cloud");
        
        try {
            ResponseEntity<WalrusUploadResponse> response = restTemplate.exchange(
                    walrusUploadURL, 
                    PUT,
                    buildUploadRequest(file), 
                    WalrusUploadResponse.class,
                    createQueryParams()
            );
            
            log.info("Upload response status: {}", response.getStatusCode());
            log.info("Upload response body: {}", response.getBody());
            
            return extractBlobIdFrom(response);
            
        } catch (Exception e) {
            log.error("Failed to upload file to Walrus Cloud", e);
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public byte[] getFileBy(String blobId) {
        log.info("Downloading file with blob ID: {}", blobId);
        
        try {
            String downloadUrl = walrusDownloadURL.concat(blobId);
            log.debug("Download URL: {}", downloadUrl);
            
            ResponseEntity<byte[]> response = restTemplate.getForEntity(downloadUrl, byte[].class);
            
            log.info("Download response status: {}", response.getStatusCode());
            log.debug("Downloaded file size: {} bytes", 
                    response.getBody() != null ? response.getBody().length : "null");
            
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Failed to download file with blob ID: {}", blobId, e);
            throw new RuntimeException("File download failed for blob ID: " + blobId, e);
        }
    }

    private static String extractBlobIdFrom(ResponseEntity<WalrusUploadResponse> response) {
        WalrusUploadResponse walrusUploadResponse = response.getBody();
        
        if (walrusUploadResponse == null) {
            throw new RuntimeException("Received null response from Walrus Cloud");
        }
        
        // Check if file already exists
        if (walrusUploadResponse.getNewlyCreated() == null) {
            log.info("File already exists in Walrus Cloud, returning existing blob ID");
            return walrusUploadResponse.getAlreadyCertified().getBlobId();
        }
        
        // File is newly created
        log.info("File successfully uploaded to Walrus Cloud as new blob");
        return walrusUploadResponse.getNewlyCreated().getBlobObject().getBlobId();
    }

    private HttpEntity<?> buildUploadRequest(MultipartFile file) {
        log.debug("Building upload request for file: {}", file.getOriginalFilename());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("file", new MultipartInputStreamFileResource(
                    file.getInputStream(), 
                    file.getOriginalFilename()
            ));
            
            log.debug("Upload request built successfully");
            return new HttpEntity<>(body, headers);
            
        } catch (IOException e) {
            log.error("Failed to get input stream from multipart file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Failed to process multipart file: " + file.getOriginalFilename(), e);
        }
    }

    private Map<String, ?> createQueryParams() {
        log.debug("Creating query parameters for Walrus upload");
        
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("epoch", Integer.parseInt(epochs));
            params.put("send_object_to", walrusUploadAddress);
            
            log.debug("Query parameters created: epoch={}, send_object_to={}", epochs, walrusUploadAddress);
            return params;
            
        } catch (NumberFormatException e) {
            log.error("Invalid epoch value: {}", epochs, e);
            throw new RuntimeException("Invalid epoch configuration: " + epochs, e);
        }
    }
}