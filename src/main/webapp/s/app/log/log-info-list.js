/**
 * Created by yxw on 2015/6/18.
 */
var logInfo = function () {

    var handleDatePickers = function () {

        if (jQuery().datepicker) {
            $('.date-picker').datepicker({
                rtl : App.isRTL()
            });
        }
    };

    var checkDate = function() {
        if($("#txt_day_begin").val() != null
            && $("#txt_day_begin").val().length > 0
            && $("#txt_day_end").val() != null
            && $("#txt_day_end").val().length > 0
            && $("#txt_day_begin").val()>$("#txt_day_end").val()) {
            alert("起始时间大于终止时间！");
            return false;
        }
        return true;
    };

    return {

        init: function () {

            handleDatePickers();

            function retrieveData( sSource, aoData, fnCallback ) {
                if (!checkDate()) return;
                sSource += "?day1="+$("#txt_day_begin").val()+"&day2="+$("#txt_day_end").val();
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    data: "jsonParam="+JSON.stringify(aoData),
                    success: function(data) {
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }


            var oTable = $('#table_log').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, 50],
                    [5, 15, 20, 50] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",
                "sAjaxSource": $('#context').val()+"/log/log-info-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"关键字查询:",
                    "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
                    "oPaginate": {
                        "sPrevious":"上一页",
                        "sNext": "下一页",
                        "sLast": "末页",
                        "sFirst": "首页"
                    },
                    //TODO LIST;修改为加载的gif文件
                    "sProcessing": "<img src='./loading.gif' />" //TODO LIST:修改成加载的图片地址
                },
                //TODO LIST：修改为要显示的字段
                "aoColumns" : [  {
                    "mDataProp" : "dbId"
                }, {
                    "mDataProp" : "handle"
                },{
                    "mDataProp" : "staff"
                }, {
                    "mDataProp" : "logType"
                }, {
                    "mDataProp" : "time"
                } ],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': [0]
                },{
                    'bSortable': false,
                    'aTargets': [1,2,3,4]
                }]
            });

            jQuery('#table_log_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_log_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_log_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown

            $('#btnSubmit').live('click', function (e) {
                e.preventDefault();

                if (checkDate()) oTable.fnDraw();

            });
        }
    };
}();