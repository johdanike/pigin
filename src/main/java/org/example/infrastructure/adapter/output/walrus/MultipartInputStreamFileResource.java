package org.example.infrastructure.adapter.output.walrus;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Custom InputStreamResource implementation for handling multipart file uploads
 * with proper filename preservation.
 */
public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;
    private final long contentLength;

    /**
     * Creates a new MultipartInputStreamFileResource with unknown content length.
     *
     * @param inputStream the input stream containing the file data
     * @param filename the original filename of the file
     */
    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
        this.contentLength = -1; // Unknown length
    }

    /**
     * Creates a new MultipartInputStreamFileResource with known content length.
     *
     * @param inputStream the input stream containing the file data
     * @param filename the original filename of the file
     * @param contentLength the content length of the file in bytes
     */
    public MultipartInputStreamFileResource(InputStream inputStream, String filename, long contentLength) {
        super(inputStream);
        this.filename = filename;
        this.contentLength = contentLength;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return this.contentLength;
    }

    @Override
    public boolean isReadable() {
        return true;
    }
}