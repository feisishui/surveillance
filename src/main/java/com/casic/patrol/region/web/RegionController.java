package com.casic.patrol.region.web;

import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.region.domain.Region;
import com.casic.patrol.region.domain.RegionPoint;
import com.casic.patrol.region.dto.RegionDTO;
import com.casic.patrol.region.manager.RegionManager;
import com.casic.patrol.region.manager.RegionPointManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("reg")
public class RegionController {

    public final static String POINT_SEGMENTATION = ",";
    public final static String XY_SEGMENTATION = " ";

    @Resource
    private RegionManager regionManager;
    @Resource
    private RegionPointManager regionPointManager;

    @RequestMapping("region-add")
    @ResponseBody
    public Map<String,Object> add(@RequestParam(value = "code",required = false) String code,
                                     @RequestParam(value = "name",required = true) String name,
                                     @RequestParam(value = "points",required = true) String points,
                                     @RequestParam(value = "description",required = false) String description) {
        Map<String,Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(name)) {
            map.put("success",false);
            map.put("message","工地名称或工地编码不能为空！");
            return map;
        }
        try {
            if (regionManager.getRegionByName(URLDecoder.decode(name, "UTF-8")) != null ) {
                map.put("success", false);
                map.put("message", "工地名称已存在！");
                return map;
            }
            List<RegionPoint> pointlist = analysisPoints(points);
            if (pointlist.size() < 3) {
                map.put("success", false);
                map.put("message", "请画出一个多边形！");
                return map;
            }
            Region region = new Region();
            region.setName(URLDecoder.decode(name, "UTF-8"));
            if (!StringUtils.isBlank(code))
                region.setCode(URLDecoder.decode(code, "UTF-8"));
            if (!StringUtils.isBlank(description))
                region.setDescription(URLDecoder.decode(description,"UTF-8"));
            regionManager.save(region);
            for (RegionPoint temp : pointlist) {
                temp.setRegion(region);
            }
            regionPointManager.saveAll(pointlist);
            map.put("success", true);
            map.put("dbid", region.getDbId());
            map.put("points", pointlist);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "区域信息保存失败！");
        }
        return map;
    }

    private List<RegionPoint> analysisPoints(String pointsstring) {
        String points[] = pointsstring.split(POINT_SEGMENTATION);
        List<RegionPoint> result = new ArrayList<RegionPoint>();
        for (int i = 0; i < points.length - 1; i ++) {
            RegionPoint temp = new RegionPoint();
            String[] xy = points[i].split(XY_SEGMENTATION);
            temp.setX(xy[0]);
            temp.setY(xy[1]);
            temp.setIndex(i);
            result.add(temp);
        }
        return result;
    }

    @RequestMapping("region-delete")
    @ResponseBody
    public Map<String,Object> regionDelete(@RequestParam(value = "dbId",required = true) String dbId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            Region region = regionManager.get(Long.parseLong(dbId));
            region.setStatus(0);
            regionManager.save(region);
            map.put("success", true);
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "删除失败:" + e.getMessage());
        }
        return  map;
    }

    @RequestMapping("region-list")
    @ResponseBody
    public Map<String, Object> listUserInfo() {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            List<RegionDTO> dt = regionManager.findAllRegions();
            map.put("success", true);
            map.put("data", dt);
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

}
