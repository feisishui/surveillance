/**
 * Created by wp on 2017/1/18.
 */
var OvertimeInfoList = function () {

    var oTable;
    function retrieveData( sSource, aoData, fnCallback ) {
        $.ajax( {
            type: "POST",
            url: sSource,
            dataType: "json",
            data: "jsonParam="+JSON.stringify(aoData),
            success: function(data) {
                fnCallback(data);
            }
        });
    }

    return {

        init: function () {
            oTable =  $('#table_rules').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"]
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,
                "bServerSide": true,
                "bPaginate": true,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "sAjaxSource": $('#context').val()+"/overtime/overtime-info-list.do",
                "fnServerData":retrieveData,
                "oLanguage": {
                    "sSearch":"规则名:",
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
                    "mDataProp" : "regulationName"
                }, {
                    "mDataProp" : "eventType"
                }, {
                    "mDataProp" : "emergencyLevel"
                }, {
                    "mDataProp" : "startTaskName"
                }, {
                    "mDataProp" : "alarmTime"
                }, {
                    "mDataProp" : "btnEdit"
                } ],
                "aoColumnDefs": [{
                        'aTargets': ['_all'],
                        sDefaultContent: ''
                    }, {
                        'bVisible':false,
                        'aTargets': [0,7]
                    }, {
                        'bSortable': false,
                        'aTargets': [1,2,3,4,5,6]
                    }
                ]
            });
            $('#table_rules a.red').live('click', function (e) {
                if (confirm("确认要删除该规则 ?") === false) {
                    return;
                }
                alert("暂时不支持删除操作");
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/overtime/overtime-info-delete.do",
                    dataType: "json",
                    data: "id="+id,
                    success: function(data) {
                        var jData = eval(data);
                        if(jData.success==true) {
                            oTable.fnDraw();
                        } else{
                            alert("删除失败[" + jData.message + "]");
                        }
                    },
                    error:function(request){
                        alert("删除失败[" + request.message + "]");
                    }
                });
            });
            $('#table_rules a.blue').live('click', function (e) {
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;
                location.href = $("#context").val() + "/overtime/overtime-info-edit.do?id=" + id;
            });

            $("#add_rule").live("click", function (e) {
                location.href = $('#context').val()+"/overtime/overtime-info-edit.do";
            });

            $("#documentation_btn").live("click", function (e) {
                //location.href = $('#context').val()+"/document/shuoming.pdf";
                window.open($('#context').val()+"/document/shuoming.pdf");
            });
        }
    };

}();