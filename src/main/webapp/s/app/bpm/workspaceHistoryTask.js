/**
 * Created by yxw on 2016/8/19.
 */

var historyWork = function () {

    var oTable;
    return {

        init: function () {

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
            oTable =  $('#table_historyWork').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,
                "bServerSide": true,
                "bPaginate": true,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "sAjaxSource": $('#context').val()+"/humanstak/historyWork-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData,
                "oLanguage": {
                    "sSearch":"用户名:",
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
                    "mDataProp" : ""
                }, {
                    "mDataProp" : ""
                }, {
                    "mDataProp" : ""
                }, {
                    "mDataProp" : ""
                }, {
                    "mDataProp" : ""
                }, {
                    "mDataProp" : ""
                } , {
                    "mDataProp" : ""
                }],
                "aoColumnDefs": [{
                    'aTargets': ['_all'],
                    sDefaultContent: ''
                }, {
                    'bVisible':false,
                    'aTargets': [0]
                }, {
                    'bSortable': false,
                    'aTargets': [1,2,3,4,5,6,7]
                }
                ]
            });

        }
    };

}();