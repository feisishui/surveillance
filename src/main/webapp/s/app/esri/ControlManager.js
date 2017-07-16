/**
 * Created by wp on 2016/8/18.
 */
var ControlManager = function () {
    var map = null; // Map类
    var tdLayer, markLayer, regionLayer;
    //触发器变量
    var markClick, positionSelect, drawRegion, delRegion;
    var markClickName = "markClick", positionSelectName = "positionSelect", drawRegionName = "drawRegion", delRegionName = "delRegion";
    var measureControls = {};
    var regions;// 数据结构[{region:feature1,list:array1},{region:feature2,list:array2}]

    /**
     * mark的封装类
     * @param markerDto
     * @constructor
     */
    function Bean(markerDto) {
        this.id = markerDto["position"].processInstanceId;
        this.useful = false;
        this.data = markerDto;
        var position = markerDto["position"].value;
        this.desc = markerDto["description"].value;
        var temps = position.split(",");
        if (temps.length > 1) {
            this.lng = temps[0];
            this.lat = temps[1];
            if (temps.length > 2) {this.place = temps[2];}
            this.useful = true;
        }
        if (this.useful) {
            this.processid = markerDto["position"].processInstanceId;
        }
    }

    /**
     * 检查point是否在regionObject范围内
     * @param regionObject
     * @param point
     * @returns {*}
     */
    function checkPointExistInRegion(regionObject, point) {
        return regionObject.geometry.containsPoint(point);
    }

    /**
     * 测试obj是array
     * @param obj
     * @returns {boolean}
     */
    function isArray(obj) {
        return obj && typeof(obj) === "object" &&
            Object.prototype.toString.call(obj) === '[object Array]';
    }

    /**
     * 将null与undefined转化为空字符串
     * @param o
     * @returns {*}
     */
    function getValue(o) {
        return o ? o : "";
    }

    /**
     * 检查文件后缀名是否在给的集合中
     * @param name
     * @param suffix
     * @returns {boolean}
     */
    function matchSuffix(name, suffix) {
        var index = name.lastIndexOf(".");
        name = name.substring(index + 1, name.length).toUpperCase();
        suffix = suffix.toUpperCase();
        return suffix.indexOf(name) > -1;
    }

    /**
     * 解决ajax并发问题
     * @param o         {object}
     * @param callback  {function}
     */
    function async(o, callback) {
        if (o.result) {
            setTimeout(function(){async(o, callback)}, 500);
        } else {
            o.result = true;
            callback(function(){o.result = false;});
        }
    }
    var regionAsyncObject = {}; // 用于控制刷新图层{ControlManager.flashRegions}使用的锁

    return {
        register : function(key, control) {
            measureControls[key] = control;
        },

        toggleControl : function(_value) {
            for(key in measureControls) {
                var control = measureControls[key];
                if(_value == key) {
                    control.activate();
                } else {
                    control.deactivate();
                }
            }
        },

        /**
         * 控制所有触发器，统一进行开启和关闭
         * @param open true : activate所有control; false : deactivate所有control
         */
        controlFires : function(open) {
            for(key in measureControls) {
                var control = measureControls[key];
                if(open) {
                    control.activate();
                } else {
                    control.deactivate();
                }
            }
        },

        initMap : function(container) {
            if (!map) {
                map = new OpenLayers.Map(container);
            }
            if (!tdLayer) {
                tdLayer = new OpenLayers.Layer.SCGISTileMapEx("dlgMapLayer", CST.tilemap, {
                    token:CST.token, zoomToExtent:CST.extent
                });
                map.addLayer(tdLayer);
            }
        },

        /**
         * 增加mark图层
         */
        addMarkLayer : function() {
            if (!markLayer) {
                markLayer = new OpenLayers.Layer.Vector();
                map.addLayer(markLayer);
            }
        },

        /**
         * 获取具体位置信息
         * @param lon
         * @param lat
         * @param callback
         */
        searchNearestPlaceByLonLat : function(lon, lat, callback) {
            var nearName = "";
            var _content = {
                typeCode: "02",
                centerX: lon,
                centerY: lat,
                radius: 1000,
                token: CST.token,
                StartIndex: 0,
                StopIndex: 1
            };
            var jsonp = new OpenLayers.Protocol.Script();
            jsonp.createRequest(CST.nameSearchUrl + "/BufferSearch", _content, function (json) {
                    if (json.message != null && json.message.features.length > 0) {
                        var feature = json.message.features[0];
                        nearName = getValue(feature.attributes[5])
                            + getValue(feature.attributes[6])
                            + getValue(feature.attributes[3]);
                    }
                    if (callback) callback(lon, lat, nearName);
                }
            )
        },

        /**
         * 刷新地图中的mark图层
         * @param key
         */
        flashMarkers : function(key, callback) {
            if (map && markLayer) {
                $.ajax({
                    type: "get",
                    url: $('#context').val() + "/bpm/console-listProcessInstancesWithLocation.do",
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded:charset=UTF-8",
                    data: "key=" + encodeURI(encodeURI(key)),
                    cache: false,
                    success: function (r) {
                        if (r.success) {
                            var data = [];
                            for (var t = 0; t < r.data.length; t++) {
                                var markerDto = r.data[t];
                                var bean = new Bean(markerDto);
                                data[t] = bean;
                            }
                            ControlManager.__addMarker(data, markLayer);
                            if (callback) callback();
                        } else {
                            alert(r.msg);
                        }
                    },
                    error: function (d) {//请求出错
                        console.log(d.responseText);
                    }
                });
            }
        },

        /**
         *
         * @param data
         * @param layer 用于限制该方法不能在外部调用
         * @private
         */
        __addMarker : function(data, layer){
            layer.removeAllFeatures();
            ControlManager.__clearRegions();
            for (var i = 0; i < data.length; i++) {
                if (!data[i].useful) {
                    continue;
                }
                var stylenamesearch = {
                    externalGraphic: $('#context').val() + "/s/app/esri/maker.png",
                    graphicWidth: 20,
                    graphicHeight: 20,
                    graphicTitle: data[i].desc
                };
                var geometrypt = new OpenLayers.Geometry.Point(data[i].lng, data[i].lat);
                var namefeat = new OpenLayers.Feature.Vector(geometrypt, {processid : data[i].processid}, stylenamesearch);
                layer.addFeatures(namefeat);
                for (var j = 0; j < regions.length; j ++) {
                    if (checkPointExistInRegion(regions[j].region, geometrypt)) {
                        ControlManager.__addPointDataToRegions(regions[j], data[i]);
                    }
                }
            }
        },

        /**
         * 清理regions中list的mark点记录
         * @private
         */
        __clearRegions : function(){
            if (regions && regions.length > 0) {
                for (var j = 0; j < regions.length; j ++) {
                    regions[j].list = new Array();
                }
            }
        },

        /**
         * 向regions[i]中的list属性增加“事故点mark”信息
         * @param regionObject
         * @param data
         * @private
         */
        __addPointDataToRegions : function(regionObject, data) {
            if (!regionObject.list || !isArray(regionObject.list)) {
                regionObject.list = new Array();
            }
            regionObject.list.push(data);
        },

        /**
         * 增加region图层
         */
        addRegionLayer : function() {
            if (!regionLayer) {
                var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
                renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderer;
                regionLayer = new OpenLayers.Layer.Vector("layer geometry", {
                    styleMap: new OpenLayers.StyleMap({
                        "default": new OpenLayers.Style(null, {
                            rules: [
                                new OpenLayers.Rule({
                                    symbolizer: {
                                        "Polygon": {
                                            label:"${regionname}",
                                            strokeWidth: 2,
                                            strokeOpacity: 1,
                                            strokeColor: "#666666",
                                            fillColor: "blue",
                                            fillOpacity: 0.3
                                        }
                                    }
                                })
                            ]
                        })
                    }),
                    renderer: renderer
                });
                map.addLayer(regionLayer);
            }
        },

        /**
         * 刷新地图中的region图层
         */
        flashRegions : function(key, callback) {
            async(regionAsyncObject, function(finalAction) {
                if (regionLayer) {
                    regions = new Array();
                    regionLayer.removeAllFeatures();
                    var final = function() {
                        if (callback) callback();
                        if (finalAction) finalAction();
                    };
                    $.ajax({
                        type: "get",
                        url: $('#context').val() + "/reg/region-list.do",
                        dataType: "json",
                        cache: false,
                        success: function (r) {
                            if (r.success) {
                                for (var t = 0; t < r.data.length; t++) {
                                    var regiondto = r.data[t];
                                    ControlManager.__addRegionToLayer(
                                        regiondto.points, regiondto.regionName, regiondto.dbId, regionLayer
                                    );
                                }
                                if (key || callback) {
                                    ControlManager.flashMarkers(key, final);
                                } else {
                                    final();
                                }
                            } else {
                                alert(r.msg);
                                final();
                            }
                        },
                        error: function (d) {//请求出错
                            console.log(d.responseText);
                            final();
                        }
                    });
                } else {
                    if (finalAction) finalAction();
                }
            });
        },

        /**
         * 根据区域点集合、区域名称、区域id创建feature，
         * 同时将feature保存在regions序列、region layer中（便于动态统计）
         * @param pointsdto
         * @param regionname
         * @param regionid
         * @param layer         用于限制该方法不能在外部调用
         * @private
         */
        __addRegionToLayer : function(pointsdto, regionname, regionid, layer) {
            var points = new Array();
            for (var i = 0; i < pointsdto.length; i++) {
                points.push(new OpenLayers.Geometry.Point(pointsdto[i].x, pointsdto[i].y));
            }
            var linearRing = new OpenLayers.Geometry.LinearRing(points);
            var polygonFeature = new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Polygon([linearRing]),
                {regionname: regionname, regionid: regionid}
            );
            regions.push({region: polygonFeature});
            layer.addFeatures([polygonFeature]);
        },

        //以下是事件绑定方法
        activateMarkClick : function(callback) {
            if (!markClick && markLayer) {
                markClick = new OpenLayers.Control.SelectFeature(markLayer, {
                    clickFeature: callback,
                    hover:true
                });
                map.addControl(markClick);
                markClick.activate();
            } else {
                console.warn("绑定失败：mark图层不存在或事件已经绑定。");
            }
        },

        activateSelectPosition : function(callback) {
            if (!positionSelect) {
                positionSelect = new OpenLayers.Control.DrawFeature(
                    map, OpenLayers.Handler.Point, {
                        drawFeature: function(geometry) {
                            callback(geometry);
                        }
                    }
                );
                map.addControl(positionSelect);
                ControlManager.register(positionSelectName, positionSelect);
            }
            positionSelect.activate();
        },

        registerDrawRegion : function(dealwith) {
            if (!drawRegion && regionLayer) {
                drawRegion = new OpenLayers.Control();
                OpenLayers.Util.extend(drawRegion, {
                    draw: function() {
                        this.polygon = new OpenLayers.Handler.Polygon(drawRegion ,
                            { "done": this.notice },{ "persist": true},
                            { keyMask: OpenLayers.Handler.MOD_SHIFT });
                    },
                    notice: function(bounds) {
                        dealwith(bounds);
                    }
                });
                map.addControl(drawRegion);
                ControlManager.register(drawRegionName, drawRegion.polygon);
            } else {
                console.warn("绑定失败：region图层不存在或事件已经绑定。");
            }
        },

        registerDeleteRegion : function(dealwith) {
            if (!delRegion && regionLayer) {
                delRegion = new OpenLayers.Control.SelectFeature(regionLayer, {
                    onSelect: function(feature) {
                        dealwith(feature);
                    }
                });
                map.addControl(delRegion);
                ControlManager.register(delRegionName, delRegion);
            } else {
                console.warn("绑定失败：region图层不存在或事件已经绑定。");
            }
        },

        //以下是工具方法
        isArray : function(obj) {
            return isArray(obj);
        },

        checkFileSize : function(name, maxpicsize) {
            var f = document.getElementsByName(name)[0].files;
            if (f.length > 0) {
                var totalsize = 0;
                for(var i = f.length - 1; i > -1; i--) {
                    totalsize += f[i].size;
                    if (totalsize > maxpicsize || totalsize < 0) {
                        return false;
                    }
                }
            }
            return true;
        },

        checkFileSuffix : function(name, suffix) {
            var f = document.getElementsByName(name)[0].files;
            if (f.length > 0) {
                for(var i = f.length - 1; i > -1; i--) {
                    if (!matchSuffix(f[i].name, suffix)) {
                        return false;
                    }
                }
            }
            return true;
        },

        getRegions : function() {
            if (!isArray(regions)) {
                regions = new Array();
            }
            return regions;
        },

        getDrawRegionName : function() {
            return drawRegionName;
        },

        getDelRegionName : function() {
            return delRegionName;
        },
        getMap:function(){
            return map;
        }
    };
}();