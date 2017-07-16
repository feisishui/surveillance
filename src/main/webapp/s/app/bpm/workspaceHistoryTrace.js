/**
 * Created by lenovo on 2017/1/18.
 */
var HistoryTrace = function (){

    function drawLine(map,pointlist){
        map.clearOverLays()
        var points = new Array();
        var start,end;
        for (var i = 0; i < pointlist.length; i++) {
            points.push(new TLngLat(pointlist[i].y, pointlist[i].x));
        }
        if(pointlist.length<2){//轨迹至少两个点
            return;
        }
        start = points[0];
        end =  points[pointlist.length-1];
        //添加折线
        var line = new TPolyline(points,{strokeColor:"green", strokeWeight:4, strokeOpacity:1});
        map.addOverLay(line);
        var startIcon = new TIcon($('#context').val()+'/images/devMarker/icon_st.png',new TSize(19,27),{anchor:new TPixel(9,27)});
        var endIcon = new TIcon($('#context').val()+'/images/devMarker/icon_en.png',new TSize(19,27),{anchor:new TPixel(9,27)});
        //向地图上添加自定义标注
        var marker = new TMarker(start,{icon:startIcon});
        var marker2 = new TMarker(end,{icon:endIcon});
        map.addOverLay(marker);
        map.addOverLay(marker2);
        map.setViewport(points);
    }

    var Point = function(x,y){
        this.x = x;
        this.y = y;
    }
   return {
       init:function(){
           $("#BD_container").attr('style', 'height:' + ($(".page-content").height() - $(".space20").height() - $(".footer").height()) + "px;");
           // 图层加载
           var map=new TMap("BD_container");
           map.enableHandleMouseScroll();
           map.centerAndZoom(new TLngLat(104.06,30.67),12);
           $('.date-picker').datepicker();
           //查询按钮点击事件绑定
           $("#searchButton").click(function(){
               var userId = $("#patrols").val();
               var locaTime = $("#txt-date").val();
               if(!locaTime) {
                   alert("请选择巡检日期");
                   return;
               }
               $.ajax({
                   type: "POST",
                   url:  $('#context').val()+"/position/trace.do",
                   dataType: "json",
                   data: {"userId":userId,"locaTime":locaTime},
                   success: function (data) {
                      var positions = data.positions;
                       var points = new Array();
                       for(var i in positions){
                           var point = new Point(positions[i].latitude,positions[i].longitude);
                           points.push(point);
                       }
                       if(points.length==0){
                           alert("该巡检员在选定日期没有轨迹信息！");
                           return;
                       }
                       drawLine(map,points);
                   },
                   error:function(){
                       alert("请求轨迹出现异常");
                   }
               });
               });

       }
   }
}();