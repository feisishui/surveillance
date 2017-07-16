package com.casic.patrol.core.store;

import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MultipartFileDataSource implements DataSource {
    private MultipartFile multipartFile;

    public MultipartFileDataSource(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getName() {
        return multipartFile.getOriginalFilename();
    }

    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }

    public OutputStream getOutputStream() {
        throw new IllegalStateException("not support");
    }

    public String getContentType() {
        return multipartFile.getContentType();
    }
}
