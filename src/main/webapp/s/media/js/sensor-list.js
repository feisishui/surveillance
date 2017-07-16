var Sensor = function () {

    var oTable = null;
    var form = null;
    var error = null;
    var success = null;


    return {

        //main function to initiate the module
        initSensor: function () {

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

            oTable = $('#table_sensor').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":true,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/sensor/sensor-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"查询:",
                    "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
                    "oPaginate": {
                        "sPrevious":"上一页",
                        "sNext": "下一页",
                        "sLast": "末页",
                        "sFirst": "首页"
                    },
                    //TODO LIST;修改为加载的gif文件
                        "sProcessing": "<img src='./loading.gif' />"
                },
                "aoColumns" : [  {
                    "mDataProp" : "id"
                }, {
                    "mDataProp" : "sensorCode"
                }, {
                    "mDataProp" : "sensorTypeCode"
                }, {
                    "mDataProp" : "sensorTypeName"
                }, {
                    "mDataProp" : "deviceCode"
                },{
                    "mDataProp" : "deviceName"
                },  {
                    "mDataProp" : "btnEdit"
                }, {
                    "mDataProp" : "btnDelete"
                } ],
                "aoColumnDefs": [{
                        'aTargets': ['_all'],
                        sDefaultContent:''
                    }, {
                    'bVisible':false,
                    'aTargets': [4]
                    },{
                    'bVisible':false,
                    'aTargets': [0]
                }
                ]
            });

            jQuery('#table_sensor_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_sensor_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_sensor_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            //delete userType click
            $('#table_sensor a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该传感器 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var sensorId = aData.id;
                //TODO LIST:修改相应的url，提交删除信息
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/sensor/sensor-delete.do",
                    dataType:"json",
                    data: "id="+sensorId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {

                            oTable.fnDraw();

                        }
                        else{

                            alert("删除失败");

                        }
                    },
                    error:function(request){

                        alert("删除失败");

                    }
                });
            });

            //edit userType click
            $('#table_sensor a.blue').live('click', function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;

                //TODO LIST：提交deviceId，跳转到指定的do文件.
                location.href = $("#context").val() + "/sensor/sensor-edit.do?sensorId=" + id;

            });

            $('#sensor_add').live('click', function (e) {

                //TODO LIST：提交到相应的.do url，导入到url连接
                location.href = $("#context").val() + "/sensor/sensor-edit.do";

            });
        }

    };

}();