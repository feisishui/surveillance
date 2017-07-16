package com.casic.patrol.core.store;

import org.springframework.core.io.InputStreamSource;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class DataSourceInputStreamSource implements InputStreamSource {
    private DataSource dataSource;

    public DataSourceInputStreamSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InputStream getInputStream() throws IOException {
        return dataSource.getInputStream();
    }
}
