/**
 * Created by lenovo on 2017/1/18.
 */
var TaskTrace = function (){

    var oTable;
    var map;

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
            map=new TMap("BD_container");
            map.enableHandleMouseScroll();
            map.centerAndZoom(new TLngLat(104.06,30.67),12);
        },

        initTable:function(){

            function retrieveData( sSource, aoData, fnCallback ) {
                //查询条件称加入参数数组
                // var rentRuleId =document.getElementById('rentRuleId').value;
                //alert(rentRuleId);
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data: "jsonParam="+JSON.stringify(aoData),
                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }
            oTable =  $('#taskTraceTable').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":true,
                "bServerSide": true,
                "bPaginate": true,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "sAjaxSource": $('#context').val()+"/position/get-confirmed-task.do",
                "fnServerData":retrieveData,
                "oLanguage": {
                    "sSearch":"流程号:",
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "oPaginate": {
                        "sPrevious":"上一页",
                        "sNext": "下一页",
                        "sLast": "末页",
                        "sFirst": "首页"
                    }
                },
                "aoColumns" : [  {
                    "mDataProp" : "id"
                }, {
                    "mDataProp" : "taskId"
                }, {
                    "mDataProp" : "taskName"
                }, {
                    "mDataProp" : "confirmUser_id"
                }, {
                    "mDataProp" : "confirmUser_name"
                }, {
                    "mDataProp" : "startTime"
                }, {
                    "mDataProp" : "endTime"
                },{
                    "mDataProp" : "btnTrace"
                } ],
                "aoColumnDefs": [{
                    'aTargets': ['_all'],
                    sDefaultContent: ''
                }, {
                    'bVisible':false,
                    'aTargets': [0,3,5]
                }, {
                    'bSortable': false,
                    'aTargets': [1,2,3,4,5,6,7]
                }
                ]
            });
            jQuery('#table_user_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_user_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_user_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });
            //TODO LIST:删除资源n操作
            $('#taskTraceTable a.blue').live('click', function (e) {
                e.preventDefault();
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var userId = aData.confirmUser_id;
                var startTime = aData.startTime;
                var endTime = aData.endTime;
                $.ajax({
                    type: "POST",
                    url:  $('#context').val()+"/position/taskTrace.do",
                    dataType: "json",
                    data: {
                        "userId":userId,
                        "startTime":startTime,
                        "endTime":endTime
                    },
                    success: function (data) {
                        var positions = data.positions;
                        var points = new Array();
                        for(var i in positions){
                            var point = new Point(positions[i].latitude,positions[i].longitude);
                            points.push(point);
                        }
                        if(points.length==0){
                            alert("该任务没有轨迹信息！");
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