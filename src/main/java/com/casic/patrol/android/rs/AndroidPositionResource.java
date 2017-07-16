package com.casic.patrol.android.rs;

import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.user.dto.PositionDto;
import com.casic.patrol.user.manager.PositionManager;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by admin on 2015/1/15.
 */
@Component
@Path("android/position")
public class AndroidPositionResource
{
    private PositionManager positionManager;

    @Resource
    public void setPositionManager(PositionManager positionManager) {
        this.positionManager = positionManager;
    }

    @POST
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO post(@FormParam("data") String data)
    {

        BaseDTO result = new BaseDTO();
        try
        {

            Gson gson = new Gson();
            PositionDto positionDto = gson.fromJson(data, PositionDto.class);
            boolean temp = positionManager.savePosition(positionDto);
            if (temp)
            {
                result.setCode(200);
                result.setMessage("上传成功");
                result.setData("上传成功");
            }
            else
            {
                result.setCode(404);
                result.setMessage("上传失败");
                result.setData("上传失败");
            }
        } catch (Exception e)
        {
            result.setCode(404);
            result.setMessage("上传失败");
            result.setData("上传失败");
        }
        return result;
    }

}
