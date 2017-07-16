package com.casic.patrol.internal.store.rs;

import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.StoreDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.store.ByteArrayDataSource;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.core.util.IoUtils;
import com.casic.patrol.core.util.ServletUtils;
import com.casic.patrol.internal.store.service.StoreService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
@Path("store")
public class StoreResource {
    private static Logger logger = LoggerFactory.getLogger(StoreResource.class);
    private StoreService storeService;
    private StoreConnector storeConnector;
    private TenantHolder tenantHolder;

    @GET
    @Path("getStore")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO getStore(@QueryParam("model") String model,
            @QueryParam("key") String key,
            @QueryParam("tenantId") String tenantId) {
        try {
            BaseDTO result = new BaseDTO();

            StoreDTO storeDto = storeConnector.getStore(model, key, tenantId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IoUtils.copyStream(storeDto.getDataSource().getInputStream(), baos);

            String base64 = new String(new Base64().encodeBase64(baos
                    .toByteArray()));

            result.setCode(200);
            result.setData(base64);

            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

            BaseDTO result = new BaseDTO();
            result.setCode(500);
            result.setMessage(ex.getMessage());

            return result;
        }
    }

    @POST
    @Path("saveStore")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO saveStore(@FormParam("model") String model,
            @FormParam("content") String content,
            @FormParam("fileName") String fileName,
            @FormParam("contentType") String contentType,
            @QueryParam("tenantId") String tenantId) {
        try {
            byte[] bytes = new Base64().decodeBase64(content.getBytes("utf-8"));

            BaseDTO result = new BaseDTO();
            DataSource dataSource = new ByteArrayDataSource(fileName, bytes);
            StoreDTO storeDto = storeService.saveStore(model, dataSource,
                    tenantId);
            result.setCode(200);
            result.setData(storeDto.getKey());

            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

            BaseDTO result = new BaseDTO();
            result.setCode(500);
            result.setMessage(ex.getMessage());

            return result;
        }
    }

    @GET
    @Path("view")
    public void view(@QueryParam("model") String model,
            @QueryParam("key") String key, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tenantId = tenantHolder.getTenantId();
        StoreDTO storeDto = storeConnector.getStore(model, key, tenantId);

        InputStream is = storeDto.getDataSource().getInputStream();
        ServletUtils.setFileDownloadHeader(request, response, storeDto
                .getDataSource().getName());
        IOUtils.copy(is, response.getOutputStream());
    }

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }

    @Resource
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
