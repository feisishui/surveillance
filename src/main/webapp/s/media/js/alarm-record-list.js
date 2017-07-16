var AlarmRecord = function () {

    var oTable = null;
    var form = null;
    var error = null;
    var success = null;

    var handleDatePickers = function () {

        if (jQuery().datepicker) {
            $('.date-picker').datepicker({
                rtl : App.isRTL()
            });
        }
    }

    return {

        //main function to initiate the module
        init: function () {

            $('#deviceSelect').select2({
                placeholder: "请选择要查询的设备",
                allowClear: true
            });

            handleDatePickers();

            function retrieveData( sSource, aoData, fnCallback ) {
                //查询条件称加入参数数组
                // var rentRuleId =document.getElementById('rentRuleId').value;
                //alert(rentRuleId);
                sSource += "?day1="+$("#txt_day_begin").val()+"&day2="+$("#txt_day_end").val()+"&devCode="+$("#deviceSelect").val();
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

            oTable = $('#table_alarm_record_list').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":true,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "bFilter" : false,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/alarm/alarm-record-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"类型查询:",
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
                    "mDataProp" : "deviceCode"
                }, {
                    "mDataProp" : "deviceName"
                }, {
                    "mDataProp" : "deviceTypeName"
                }, {
                    "mDataProp" : "recordDate"
                }, {
                    "mDataProp" : "alarmMsg"
                },{
                    "mDataProp" : "cfmMsg"
                }, {
                    "mDataProp" : "status"
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
                    'aTargets': [0]
                }
                ]
            });

            //edit userType click
            $('#btnSubmit').live('click', function (e) {
                e.preventDefault();
                oTable.fnDraw();
            });

            jQuery('#table_alarm_record_list_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_alarm_record_list_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_alarm_record_list_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            //delete userType click
            $('#table_alarm_record_list a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该规则 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;
                //TODO LIST:修改相应的url，提交删除信息
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/alarm/alarm-record-delete.do",
                    dataType:"json",
                    data: "redId="+id,
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
            $('#table_alarm_record_list a.blue').live('click', function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;

                //TODO LIST：提交deviceId，跳转到指定的do文件.
                location.href = $("#context").val() + "/alarm/alarm-record-confirm-edit.do?recId=" + id;

            });

            //$('#alarm_rule_add').live('click', function (e) {
            //
            //    //TODO LIST：提交到相应的.do url，导入到url连接
            //    location.href = $("#context").val() + "/alarm/alarm-rule-edit.do";
            //
            //});
        },

        initDevice : function(){
            $.ajax( {
                type: "POST",
                url: "../../deviceInfo/device-dto-list.do",
                dataType:"json",
                success: function(data) {
                    var jData = eval(data);
                    var selector = $('#deviceSelect');
                    for(var i=0;i<jData.length;i++)
                    {
                        selector.append('<option value="'+jData[i].devCode+'">'+jData[i].devName+'</option>');
                    }

                },
                error:function(request){

                    alert("删除失败");

                }
            });

        }

    };

}();