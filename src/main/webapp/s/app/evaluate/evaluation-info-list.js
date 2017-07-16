/**
 * Created by yxw on 2015/6/18.
 */
var EvaluationInfoList = function () {

    var partyEntityIds = new Array();
    var objectIds = new Array();

    return {
        //main function to initiate the module
        //指挥中心
        init_command_year: function () {

            $('#txt_command_year_begin').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_command_year_end').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_command_year').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_command_year').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_command_year( sSource, aoData, fnCallback) {

                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#commandCenter_text").val(),
                        type : $("#year_text").val(),
                        day1 : $("#txt_command_year_begin").val(),
                        day2 : $("#txt_command_year_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_command_year = $('#table_evaluation_command_year').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_command_year,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "dispatch_confirm_score"
                }, {
                    "mDataProp" : "dispatch_dispose_score"
                }, {
                    "mDataProp" : "inTime_dispatch_score"
                }, {
                    "mDataProp" : "overTime_mis_dispatch_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_command_year_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_command_year_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_command_year_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_command_year').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_command_year").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_command_year_begin").val() != null
                    && $("#txt_command_year_begin").val().length > 0
                    && $("#txt_command_year_end").val() != null
                    && $("#txt_command_year_end").val().length > 0
                    && $("#txt_command_year_begin").val()>$("#txt_command_year_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_command_year.fnDraw();
            });

        },

        init_command_quarter: function () {

            $('#txt_command_quarter').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });

            $('#partyEntity_search_command_quarter').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_command_quarter').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_command_quarter( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#commandCenter_text").val(),
                        type : $("#quarter_text").val(),
                        day1 : $("#txt_command_quarter").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_command_quarter = $('#table_evaluation_command_quarter').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_command_quarter,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "evaluateQuarter"
                }, {
                    "mDataProp" : "dispatch_confirm_score"
                }, {
                    "mDataProp" : "dispatch_dispose_score"
                }, {
                    "mDataProp" : "inTime_dispatch_score"
                }, {
                    "mDataProp" : "overTime_mis_dispatch_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_command_quarter_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_command_quarter_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_command_quarter_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown

            $('#btnSearch_command_quarter').live('click', function (e) {

                if($("#partyEntity_search_command_quarter").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }
                e.preventDefault();
                oTable_command_quarter.fnDraw();
            });
        },

        init_command_month: function () {
            $('#txt_command_month_begin').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_command_month_end').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_command_month').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_command_month').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_command_month( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#commandCenter_text").val(),
                        type : $("#month_text").val(),
                        day1 : $("#txt_command_month_begin").val(),
                        day2 : $("#txt_command_month_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_command_month = $('#table_evaluation_command_month').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_command_month,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateMonth"
                }, {
                    "mDataProp" : "dispatch_confirm_score"
                }, {
                    "mDataProp" : "dispatch_dispose_score"
                }, {
                    "mDataProp" : "inTime_dispatch_score"
                }, {
                    "mDataProp" : "overTime_mis_dispatch_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_command_month_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_command_month_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_command_month_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_command_month').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_command_month").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_command_month_begin").val() != null
                    && $("#txt_command_month_begin").val().length > 0
                    && $("#txt_command_month_end").val() != null
                    && $("#txt_command_month_end").val().length > 0
                    && $("#txt_command_month_begin").val()>$("#txt_command_month_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_command_month.fnDraw();
            });
        },

        init_command_any: function () {
            $('#txt_command_any_begin').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_command_any_end').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_command_any').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_command_any').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_command_any( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#commandCenter_text").val(),
                        type : $("#any_text").val(),
                        day1 : $("#txt_command_any_begin").val(),
                        day2 : $("#txt_command_any_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_command_any = $('#table_evaluation_command_any').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_command_any,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "dispatch_confirm_score"
                }, {
                    "mDataProp" : "dispatch_dispose_score"
                }, {
                    "mDataProp" : "inTime_dispatch_score"
                }, {
                    "mDataProp" : "overTime_mis_dispatch_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5]
                }]
            });

            jQuery('#table_evaluation_command_any_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_command_any_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_command_any_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_command_any').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_command_any").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_command_any_begin").val() == null
                    ||$("#txt_command_any_begin").val().length == 0
                    ||$("#txt_command_any_end").val().length == 0
                    ||$("#txt_command_any_end").val() == null) {
                    alert("请选择查询周期！");
                    return;
                }

                if($("#txt_command_any_begin").val()>$("#txt_command_any_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_command_any.fnDraw();
            });
        },

        //巡查部门
        init_patrol_year: function () {

            $('#txt_patrol_year_begin').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_patrol_year_end').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_patrol_year').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_patrol_year').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_patrol_year( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#patrol_text").val(),
                        type : $("#year_text").val(),
                        day1 : $("#txt_patrol_year_begin").val(),
                        day2 : $("#txt_patrol_year_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_patrol_year = $('#table_evaluation_patrol_year').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_patrol_year,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "confirm_score"
                }, {
                    "mDataProp" : "inTime_confirm_score"
                }, {
                    "mDataProp" : "overTime_mis_confirm_score"
                }, {
                    "mDataProp" : "mis_report_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_patrol_year_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_patrol_year_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_patrol_year_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_patrol_year').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_patrol_year").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_patrol_year_begin").val() != null
                    && $("#txt_patrol_year_begin").val().length > 0
                    && $("#txt_patrol_year_end").val() != null
                    && $("#txt_patrol_year_end").val().length > 0
                    && $("#txt_patrol_year_begin").val()>$("#txt_patrol_year_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_patrol_year.fnDraw();
            });

        },

        init_patrol_quarter: function () {

            $('#txt_patrol_quarter').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });

            $('#partyEntity_search_patrol_quarter').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_patrol_quarter').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_patrol_quarter( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#patrol_text").val(),
                        type : $("#quarter_text").val(),
                        day1 : $("#txt_patrol_quarter").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_patrol_quarter = $('#table_evaluation_patrol_quarter').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_patrol_quarter,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "evaluateQuarter"
                }, {
                    "mDataProp" : "confirm_score"
                }, {
                    "mDataProp" : "inTime_confirm_score"
                }, {
                    "mDataProp" : "overTime_mis_confirm_score"
                }, {
                    "mDataProp" : "mis_report_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_patrol_quarter_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_patrol_quarter_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_patrol_quarter_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown

            $('#btnSearch_patrol_quarter').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_patrol_quarter").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }
                oTable_patrol_quarter.fnDraw();
            });
        },

        init_patrol_month: function () {
            $('#txt_patrol_month_begin').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_patrol_month_end').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_patrol_month').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_patrol_month').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_patrol_month( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#patrol_text").val(),
                        type : $("#month_text").val(),
                        day1 : $("#txt_patrol_month_begin").val(),
                        day2 : $("#txt_patrol_month_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_patrol_month = $('#table_evaluation_patrol_month').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_patrol_month,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateMonth"
                }, {
                    "mDataProp" : "confirm_score"
                }, {
                    "mDataProp" : "inTime_confirm_score"
                }, {
                    "mDataProp" : "overTime_mis_confirm_score"
                }, {
                    "mDataProp" : "mis_report_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_patrol_month_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_patrol_month_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_patrol_month_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_patrol_month').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_patrol_month").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_patrol_month_begin").val() != null
                    && $("#txt_patrol_month_begin").val().length > 0
                    && $("#txt_patrol_month_end").val() != null
                    && $("#txt_patrol_month_end").val().length > 0
                    && $("#txt_patrol_month_begin").val()>$("#txt_patrol_month_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_patrol_month.fnDraw();
            });
        },

        init_patrol_any: function () {
            $('#txt_patrol_any_begin').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_patrol_any_end').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_patrol_any').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#region_search_patrol_any').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_patrol_any( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#patrol_text").val(),
                        type : $("#any_text").val(),
                        day1 : $("#txt_patrol_any_begin").val(),
                        day2 : $("#txt_patrol_any_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_patrol_any = $('#table_evaluation_patrol_any').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_patrol_any,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "confirm_score"
                }, {
                    "mDataProp" : "inTime_confirm_score"
                }, {
                    "mDataProp" : "overTime_mis_confirm_score"
                }, {
                    "mDataProp" : "mis_report_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5]
                }]
            });

            jQuery('#table_evaluation_patrol_any_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_patrol_any_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_patrol_any_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_patrol_any').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_patrol_any").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_patrol_any_begin").val() == null
                    ||$("#txt_patrol_any_begin").val().length == 0
                    ||$("#txt_patrol_any_end").val().length == 0
                    ||$("#txt_patrol_any_end").val() == null) {
                    alert("请选择查询周期！");
                    return;
                }

                if($("#txt_patrol_any_begin").val()>$("#txt_patrol_any_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_patrol_any.fnDraw();
            });
        },

        //专业公司
        init_company_year: function () {

            $('#txt_company_year_begin').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_company_year_end').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_company_year').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#department_search_company_year').select2({
                placeholder: "请选择专业公司"
                //allowClear: true
            });

            $('#region_search_company_year').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });


            function retrieveData_company_year( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#company_text").val(),
                        type : $("#year_text").val(),
                        day1 : $("#txt_company_year_begin").val(),
                        day2 : $("#txt_company_year_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_company_year = $('#table_evaluation_company_year').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_company_year,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "dispose_score"
                }, {
                    "mDataProp" : "inTime_dispose_score"
                }, {
                    "mDataProp" : "overTime_dispose_score"
                }, {
                    "mDataProp" : "rework_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_company_year_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_company_year_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_company_year_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_company_year').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_company_year").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_company_year_begin").val() != null
                    && $("#txt_company_year_begin").val().length > 0
                    && $("#txt_company_year_end").val() != null
                    && $("#txt_company_year_end").val().length > 0
                    && $("#txt_company_year_begin").val()>$("#txt_company_year_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_company_year.fnDraw();
            });

        },

        init_company_quarter: function () {

            $('#txt_company_quarter').datetimepicker({
                format: 'yyyy',
                weekStart: 1,
                autoclose: true,
                startView: 4,
                minView: 4,
                forceParse: false,
                language: 'cn'
            });

            $('#partyEntity_search_company_quarter').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#department_search_company_quarter').select2({
                placeholder: "请选择专业公司"
                //allowClear: true
            });

            $('#region_search_company_quarter').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_company_quarter( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#company_text").val(),
                        type : $("#quarter_text").val(),
                        day1 : $("#txt_company_quarter").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_company_quarter = $('#table_evaluation_company_quarter').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_company_quarter,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateYear"
                }, {
                    "mDataProp" : "evaluateQuarter"
                }, {
                    "mDataProp" : "dispose_score"
                }, {
                    "mDataProp" : "inTime_dispose_score"
                }, {
                    "mDataProp" : "overTime_dispose_score"
                }, {
                    "mDataProp" : "rework_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_company_quarter_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_company_quarter_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_company_quarter_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown

            $('#btnSearch_company_quarter').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_company_quarter").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }
                oTable_company_quarter.fnDraw();
            });
        },

        init_company_month: function () {
            $('#txt_company_month_begin').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_company_month_end').datetimepicker({
                format: 'yyyy-mm',
                weekStart: 1,
                autoclose: true,
                startView: 3,
                minView: 3,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_company_month').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#department_search_company_month').select2({
                placeholder: "请选择专业公司"
                //allowClear: true
            });

            $('#region_search_company_month').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_company_month( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#company_text").val(),
                        type : $("#month_text").val(),
                        day1 : $("#txt_company_month_begin").val(),
                        day2 : $("#txt_company_month_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_company_month = $('#table_evaluation_company_month').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_company_month,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "evaluateMonth"
                }, {
                    "mDataProp" : "dispose_score"
                }, {
                    "mDataProp" : "inTime_dispose_score"
                }, {
                    "mDataProp" : "overTime_dispose_score"
                }, {
                    "mDataProp" : "rework_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5,6]
                }]
            });

            jQuery('#table_evaluation_company_month_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_company_month_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_company_month_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_company_month').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_company_month").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_company_month_begin").val() != null
                    && $("#txt_company_month_begin").val().length > 0
                    && $("#txt_company_month_end").val() != null
                    && $("#txt_company_month_end").val().length > 0
                    && $("#txt_company_month_begin").val()>$("#txt_company_month_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_company_month.fnDraw();
            });
        },

        init_company_any: function () {
            $('#txt_company_any_begin').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#txt_company_any_end').datetimepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                startView: 2,
                minView: 2,
                forceParse: false,
                language: 'cn'
            });
            $('#partyEntity_search_company_any').select2({
                placeholder: "请选择查询对象"
                //allowClear: true
            });

            $('#department_search_company_any').select2({
                placeholder: "请选择专业公司"
                //allowClear: true
            });

            $('#region_search_company_any').select2({
                placeholder: "请选择区域"
                //allowClear: true
            });

            function retrieveData_company_any( sSource, aoData, fnCallback) {
                sSource += "?objectIds="+objectIds;
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data:
                    {
                        jsonParam:JSON.stringify(aoData),
                        department : $("#company_text").val(),
                        type : $("#any_text").val(),
                        day1 : $("#txt_company_any_begin").val(),
                        day2 : $("#txt_company_any_end").val()
                    },

                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            var oTable_company_any = $('#table_evaluation_company_any').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/evaluation/evaluation-info-list.do", //TODO LIST:修改成对应的后台Controller地址
                "fnServerData":retrieveData_company_any,
                "sDom": "<'row-fluid'<'span6'l>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"按考核对象查询:",
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
                    "mDataProp" : "party_name"
                }, {
                    "mDataProp" : "dispose_score"
                }, {
                    "mDataProp" : "inTime_dispose_score"
                }, {
                    "mDataProp" : "overTime_dispose_score"
                }, {
                    "mDataProp" : "rework_score"
                }, {
                    "mDataProp" : "score"
                }],
                //TODO LIST:指定哪些字段需要隐藏
                "aoColumnDefs": [{
                    'bVisible':false,
                    'aTargets': []
                },{
                    'bSortable': false,
                    'aTargets': [0,1,2,3,4,5]
                }]
            });

            jQuery('#table_evaluation_company_any_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_evaluation_company_any_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_evaluation_company_any_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            $('#btnSearch_company_any').live('click', function (e) {
                e.preventDefault();

                if($("#partyEntity_search_company_any").val() == "")
                {
                    alert("请选择查询对象！");
                    return;
                }

                if($("#txt_company_any_begin").val() == null
                    ||$("#txt_company_any_begin").val().length == 0
                    ||$("#txt_company_any_end").val().length == 0
                    ||$("#txt_company_any_end").val() == null) {
                    alert("请选择查询周期！");
                    return;
                }

                if($("#txt_company_any_begin").val()>$("#txt_company_any_end").val()) {
                    alert("起始时间大于终止时间！");
                    return;
                }
                oTable_company_any.fnDraw();
            });
        },

        getPartyEntitiesByRegionAndDepartment:function(region,department,selectId){
            $.ajax( {
                type: "POST",
                url: $('#context').val()+"/party/get-person.do?",
                dataType:"json",
                data: {
                    region : region,
                    department : department
                },
                success: function(data) {
                    if(data.success){
                        partyEntityIds = [];
                        selectId.empty();
                        selectId.append(
                            $('<option></option>').text('').val('')
                        );
                        selectId.select2({
                            tabindex:0
                        });
                        if(data.result.length>0){
                            selectId.append(
                                $('<option></option>').text('不限').val('不限')
                            );
                            for(var i=0;i<data.result.length;i++){
                                selectId.append(
                                    $('<option></option>').text(data.result[i].name)
                                        .val(data.result[i].id)
                                );
                                partyEntityIds.push(data.result[i].id);
                            }
                            objectIds = partyEntityIds;
                        }
                    }
                },
                error:function(request){
                    //error.show();
                }
            });

            //$('#permIds').multiSelect('addOption', { value: 'test', text: 'test'});
        },

        getPartyEntitiesByRegionAndDepartment_company:function(regionTextId,departmentTextId,selectTextId){
            var region = regionTextId.val();
            var department = departmentTextId.val();
            $.ajax( {
                type: "POST",
                url: $('#context').val()+"/party/get-person.do?",
                dataType:"json",
                data: {
                    region : region,
                    department : department
                },
                success: function(data) {
                    if(data.success){
                        partyEntityIds = [];
                        selectTextId.empty();
                        selectTextId.append(
                            $('<option></option>').text('').val('')
                        );
                        if(data.result.length>0){
                            selectTextId.append(
                                $('<option></option>').text('不限').val('不限')
                            );
                            for(var i=0;i<data.result.length;i++){
                                selectTextId.append(
                                    $('<option></option>').text(data.result[i].name)
                                        .val(data.result[i].id)
                                );
                                partyEntityIds.push(data.result[i].id);
                            }
                            objectIds = partyEntityIds;
                        }
                        selectTextId.select2({
                            tabindex:0
                        });
                    }
                },
                error:function(request){
                    //error.show();
                }
            });

        },

        setPartyEntities:function(partyEntityTextId){

            if(partyEntityTextId.val()!="不限")
            {
                objectIds=[];
                objectIds.push(partyEntityTextId.val());
            }
            else
            {
                objectIds = partyEntityIds;
            }
        }

    };
}();