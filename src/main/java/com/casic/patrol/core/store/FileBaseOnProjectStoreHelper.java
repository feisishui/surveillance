package com.casic.patrol.core.store;

import com.casic.patrol.core.servlet.CompositeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileBaseOnProjectStoreHelper implements StoreHelper {
    private static Logger logger = LoggerFactory
            .getLogger(FileBaseOnProjectStoreHelper.class);
    private String baseDir;

    public StoreResult getStore(String model, String key) throws Exception {
        if (key == null) {
            logger.info("key cannot be null");

            return null;
        }

        HttpServletRequest request = getRequest();

        if (key.indexOf("../") != -1) {
            StoreResult storeResult = new StoreResult();
            storeResult.setModel(model);
            storeResult.setKey(key);

            return storeResult;
        }

        String path = baseDir + "/" + model + "/" + key;
        File file = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        path);

        if (!file.exists()) {
            logger.info("cannot find : {}", file);

            return null;
        }

        StoreResult storeResult = new StoreResult();
        storeResult.setModel(model);
        storeResult.setKey(key);
        storeResult.setDataSource(new FileDataSource(file));
        storeResult.setAbsolutePath(path);

        return storeResult;
    }

    public void removeStore(String model, String key) throws Exception {
        if (key.indexOf("../") != -1) {
            return;
        }
        HttpServletRequest request = getRequest();

        File file = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        baseDir + "/" + model + "/" + key);
        file.delete();
    }

    public StoreResult saveStore(String model, DataSource dataSource)
            throws Exception {
        HttpServletRequest request = getRequest();
        String prefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String suffix = this.getSuffix(dataSource.getName());
        String path = prefix + "/" + UUID.randomUUID() + suffix;
        File dir = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        baseDir + "/" + model + "/" + prefix);

        if (!dir.exists()) {
            boolean success = dir.mkdirs();

            if (!success) {
                logger.error("cannot create directory : {}", dir);
            }
        }

        File targetFile = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        baseDir + "/" + model + "/" + path);
        FileOutputStream fos = new FileOutputStream(targetFile);

        try {
            FileCopyUtils.copy(dataSource.getInputStream(), fos);
            fos.flush();
        } finally {
            fos.close();
        }

        StoreResult storeResult = new StoreResult();
        storeResult.setModel(model);
        storeResult.setKey(path);
        storeResult.setDataSource(new FileDataSource(targetFile));

        return storeResult;
    }

    public StoreResult saveStore(String model, String key, DataSource dataSource)
            throws Exception {
        HttpServletRequest request = getRequest();
        String path = key;
        File dir = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        baseDir + "/" + model);
        dir.mkdirs();

        File targetFile = new File(
                request.getSession().getServletContext().getRealPath("/") +
                        baseDir + "/" + model + "/" + path);
        FileOutputStream fos = new FileOutputStream(targetFile);

        try {
            FileCopyUtils.copy(dataSource.getInputStream(), fos);
            fos.flush();
        } finally {
            fos.close();
        }

        StoreResult storeResult = new StoreResult();
        storeResult.setModel(model);
        storeResult.setKey(path);
        storeResult.setDataSource(new FileDataSource(targetFile));

        return storeResult;
    }

    public String getSuffix(String name) {
        int lastIndex = name.lastIndexOf(".");

        if (lastIndex != -1) {
            return name.substring(lastIndex);
        } else {
            return "";
        }
    }

    private HttpServletRequest getRequest() {
        HttpServletRequest request =  (HttpServletRequest) CompositeFilter.getRequest();
        if (request == null) {
            throw new RuntimeException("cannot find the HttpServletRequest!");
        }
        return request;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
