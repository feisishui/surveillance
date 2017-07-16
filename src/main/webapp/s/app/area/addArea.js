var AddArea = function () {
    var map = null;
    var layer,vectors,vlayer;  //图层对象
    var controls;
    var featureLayer;
    function drawLine(polygon){
        initFeatureLayer();

        var style = {
            strokeWidth: 4,
            strokeOpacity: 1,
            strokeColor: "#00CC00",
            strokeDashstyle: "solid	"
        };
        var lineFeature = new TXGIS.Feature.Vector(polygon,null,style);
        featureLayer.addFeatures([lineFeature]);
    }
    function initFeatureLayer() {
        var style = new TXGIS.StyleMap({
            "default": new TXGIS.Style({
                //strokeColor: "#00FF00",
                strokeColor: "#FF3E96",
                strokeDashstyle: "dash",
                strokeOpacity: 1,
                strokeWidth: 3,
                fillColor: "#FF5500",
                fillOpacity: 0.5,
                pointRadius: 6,
                pointerEvents: "visiblePainted",
                // label with \n linebreaks
                label: "${name}",

                fontColor: "${favColor}",
                fontSize: "12px",
                fontFamily: "Courier New, monospace",
                fontWeight: "bold",
                labelAlign: "${align}",
                labelXOffset: "${xOffset}",
                labelYOffset: "${yOffset}",
                labelOutlineColor: "white",
                labelOutlineWidth: 3
            })
        });
        if (featureLayer == null) {
            featureLayer = new TXGIS.Layer.Vector('Feature Layer', {
                isBaseLayer: false,
                styleMap: style
            });
        } else {
            featureLayer.styleMap = style;
        }
        if (map.getLayersByName("Feature Layer") == "") {
            map.addLayer(featureLayer);
        } else {
            featureLayer.removeAllFeatures();
        }
    }

    TXGIS.Control.Click = TXGIS.Class(TXGIS.Control, {
        defaultHandlerOptions: {
            'single': true,
            'double': false,
            'pixelTolerance': 0,
            'stopSingle': false,
            'stopDouble': false
        },

        initialize: function(options) {
            this.handlerOptions = TXGIS.Util.extend(
                {}, this.defaultHandlerOptions
            );
            TXGIS.Control.prototype.initialize.apply(
                this, arguments
            );
            this.handler = new TXGIS.Handler.Click(
                this, {
                    'click': this.trigger
                }, this.handlerOptions
            );
        },

        trigger: function(e) {
            var x = e.xy.x;
            var y = e.xy.y;
            var pixel = new TXGIS.Pixel(x,y);
            var lonlat = map.getLonLatFromPixel(pixel);
            alert("屏幕坐标："+x +"," + y +
            "\n经纬度坐标：" + lonlat.lat + "N, " +
            + lonlat.lon + "E");
        }

    });

    return {

        init: function () {

            var mapOptions = {
                maxExtent: new TXGIS.Bounds(102.95197097727191,29.928088079658153,104.98318773843562,31.457618773305626),
                projection: 'EPSG:4490'
            };
            //实例化map对象
            map = new TXGIS.Map('container', mapOptions);
            //var _url = 'http://apps.tianditucd.cn/cdmap/services/TiandituCD/TiandituCDDLG/MapServer/WmsServer?';;
            var wmts = 'http://apps.tianditucd.cn/cdmap/rest/services/TiandituCD/TiandituCDDLG/MapServer/WMTS';
            //实例化图层对象
            var layer = new TXGIS.Layer.WMTS({
                name: "My WMTS Layer",
                url: wmts,
                layer: "TiandituCD_TiandituCDDLG",
                style: "default",
                matrixSet: "default028mm"
            });
            map.addLayer(layer);   //添加图层到map对象中
            map.zoomTo(3);
            map.setCenter(new TXGIS.LonLat(104.065,30.654));
            map.addControl(new TXGIS.Control.MousePosition());

            //var editbar = new TXGIS.Control.EditingToolbar(layer)
            //map.addControl(editbar);


            var pts = [];
            var pt1 = new TXGIS.Geometry.Point(103.99634,30.72745);
            var pt2 = new TXGIS.Geometry.Point(104.09225,30.68802);
            var pt3 = new TXGIS.Geometry.Point(104.09651,30.63405);
            var pt4 = new TXGIS.Geometry.Point(104.02882,30.64017);
            pts.push(pt1);pts.push(pt2);pts.push(pt3);pts.push(pt4);pts.push(pt1);
            var pts2 = [];
            var pt11= new TXGIS.Geometry.Point(104.11114,30.61636);
            var pt21 = new TXGIS.Geometry.Point(104.18404,30.68951);
            var pt31 = new TXGIS.Geometry.Point(104.18751,30.64066);
            var pt41 = new TXGIS.Geometry.Point(104.12379,30.63843);
            pts2.push(pt11);pts2.push(pt21);pts2.push(pt31);pts2.push(pt41);pts2.push(pt11);

            var lineRingStr = new TXGIS.Geometry.LinearRing(pts);
            var lineRingStr2 = new TXGIS.Geometry.LinearRing(pts2);
            var polygon = new TXGIS.Geometry.Polygon([lineRingStr,lineRingStr2]);
            drawLine(polygon);

            var renderer = TXGIS.Layer.Vector.prototype.renderers;
            vectors = new TXGIS.Layer.Vector("Vector Layer", {
                renderers: renderer
            });
            map.addLayer(vectors);

            controls = {
                point: new TXGIS.Control.DrawFeature(vectors,
                    TXGIS.Handler.Point),
                line: new TXGIS.Control.DrawFeature(vectors,
                    TXGIS.Handler.Path),
                polygon: new TXGIS.Control.DrawFeature(vectors,
                    TXGIS.Handler.Polygon),
                drag: new TXGIS.Control.DragFeature(vectors)
            };

            for(var key in controls) {
                map.addControl(controls[key]);
            }

        },
        toggleControl:function (element) {
            for(key in controls)
            {
                var control = controls[key];
                if(element.value == key && element.checked) {
                    control.activate();
                } else {
                    control.deactivate();
                }
            }
        }
    };
}();