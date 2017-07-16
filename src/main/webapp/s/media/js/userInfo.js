var UsrInfo = function () {

    var userTypeTable = null;
    var userInfoTable = null;
    var error = null;
    var success = null;

    var loadSelect = function()
    {
        $.ajax( {
            type: "POST",
            url: "../../user/user-type-dto-list.do",
            dataType:"json",
            success: function(data) {
                var jData = eval(data);
                var selector = $('#deviceSelect');
                $('#deviceSelect').find('option').remove();
                for(var i=0;i<jData.length;i++)
                {
                    selector.append('<option value="'+jData[i].id+'">'+jData[i].name+'</option>');
                }

            },
            error:function(request){

                alert("删除失败");

            }
        });
    }

    return {

        //main function to initiate the module
        initUserType: function () {

            function retrieveData( sSource, aoData, fnCallback ) {
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

            userTypeTable = $('#table_user_type').dataTable({
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
                "sAjaxSource": $('#context').val()+"/user/user-type-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "bFilter" : false,
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
                },{
                    "mDataProp" : "name"
                }, {
                    "mDataProp" : "describe"
                }, {
                    "mDataProp" : "btnEdit"
                }, {
                    "mDataProp" : "btnDelete"
                } ],
                "aoColumnDefs": [{
                        'aTargets': ['_all'],
                        sDefaultContent:''
                    },{
                    'bVisible':false,
                    'aTargets': [0]
                }]
            });

            jQuery('#table_user_type_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_user_type_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_user_type_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });


            $('#userType_add').live('click', function (e) {

                $('#addUserTypeDialog').modal('show');
                $('#user_type_form')[0].reset();
                success.hide();
                error.hide();
                $('#user_type_form .control-group').removeClass('error');
                $('#user_type_form .control-group').removeClass('success');
                $('.validate-inline').remove();

            });

            //delete userType click
            $('#table_user_type a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该用户类型 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = userTypeTable.fnGetData(nRow);
                var typeId = aData.id;
                //TODO LIST:修改相应的url，提交删除信息
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/user/user-type-delete.do",
                    dataType:"json",
                    data: "typeId="+typeId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {

                            userTypeTable.fnDraw();

                        }
                        else{

                            alert(jData.message);

                        }
                    },
                    error:function(request){

                        alert("删除失败");

                    }
                });
            });

            //edit userType click
            $('#table_user_type a.blue').live('click', function (e) {
                e.preventDefault();
                var nRow = $(this).parents('tr')[0];
                var aData = userTypeTable.fnGetData(nRow);
                var id = aData.id
                var typeName = aData.name;
                var describe = aData.describe;

                $('#user_type_form #typeId').val(id);
                $('#user_type_form #typeName').val(typeName);
                $('#user_type_form #describe').val(describe);

                success.hide();
                error.hide();
                $('#user_type_form .control-group').removeClass('error');
                $('#user_type_form .control-group').removeClass('success');
                $('.validate-inline').remove();
                //初始化表单
                $('#addUserTypeDialog').modal('show');

                //TODO LIST:弹出人员编辑对话框

            });
        },

        initUserTypeForms:function(){

            form = $('#user_type_form');
            error = $('.alert-error', form);
            success = $('.alert-success', form);

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //account
                    name: {
                        minlength: 3,
                        required: true
                    },
                    describe: {
                        minlength: 3,
                        required: true
                    }
                },
                messages: { // custom messages for radio buttons and checkboxes
                    name:{
                        minlength:"用户类型名称必须大于3个字符",
                        required:"用户类型名称为必填项！"
                    }
                },
                errorPlacement: function (error, element) { // render error placement for each input type

                    error.insertAfter(element); // for other inputs, just perform default behavoir

                },

                invalidHandler: function (event, validator) { //display error alert on form submit
                    success.hide();
                    error.show();
                    App.scrollTo(error, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.help-inline').removeClass('ok'); // display OK icon
                    $(element)
                        .closest('.control-group').removeClass('success').addClass('error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change dony by hightlight
                    $(element)
                        .closest('.control-group').removeClass('error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .addClass('valid ok') // mark the current input as valid and display OK icon
                        .closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group

                },

                submitHandler: function (form) {
                    success.show();
                    error.hide();
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    $.ajax( {
                        type: "POST",
                        url: $('#context').val()+"/user/user-type-save.do",
                        data: $('#user_type_form').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {

                                form.reset();
                                $('#addUserTypeDialog').modal('hide');
                                userTypeTable.fnDraw();
                                loadSelect();
                            }
                            else{
                                success.hide();
                                $("#errorMessageUserType").html(jData.message);
                                error.show();
                            }
                        },
                        error:function(request){
                            success.hide();
                            error.show();
                        }
                    });
                }
            });

        },

        initUser: function () {

            loadSelect();

            function retrieveData( sSource, aoData, fnCallback ) {
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

            userInfoTable = $('#table_user_info').dataTable({
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
                "sAjaxSource": $('#context').val()+"/user/user-type-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "sPaginationType": "bootstrap",
                "bFilter" : false,
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
                },{
                    "mDataProp" : "name"
                }, {
                    "mDataProp" : "describe"
                }, {
                    "mDataProp" : "btnEdit"
                }, {
                    "mDataProp" : "btnDelete"
                } ],
                "aoColumnDefs": [{
                    'aTargets': ['_all'],
                    sDefaultContent:''
                },{
                    'bVisible':false,
                    'aTargets': [0]
                }]
            });

            jQuery('#table_user_info_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_user_info_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_user_info_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });


            $('#btn_user_add').live('click', function (e) {

                $('#addUserInfoDialog').modal('show');
                $('#user_info_form')[0].reset();
                success.hide();
                error.hide();
                $('#user_info_form .control-group').removeClass('error');
                $('#user_info_form .control-group').removeClass('success');
                $('.validate-inline').remove();

            });

            //delete userType click
            $('#table_user_info a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该用户类型 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = userTypeTable.fnGetData(nRow);
                var typeId = aData.id;
                //TODO LIST:修改相应的url，提交删除信息
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/user/user-info-delete.do",
                    dataType:"json",
                    data: "typeId="+typeId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {

                            userTypeTable.fnDraw();

                        }
                        else{

                            alert(jData.message);

                        }
                    },
                    error:function(request){

                        alert("删除失败");

                    }
                });
            });

            //edit userType click
            $('#table_user_info a.blue').live('click', function (e) {
                e.preventDefault();
                var nRow = $(this).parents('tr')[0];
                var aData = userTypeTable.fnGetData(nRow);
                var id = aData.id
                var typeName = aData.name;
                var describe = aData.describe;

                $('#user_info_form #typeId').val(id);
                $('#user_info_form #typeName').val(typeName);
                $('#user_info_form #describe').val(describe);

                success.hide();
                error.hide();
                $('#user_info_form .control-group').removeClass('error');
                $('#user_info_form .control-group').removeClass('success');
                $('.validate-inline').remove();
                //初始化表单
                $('#addUserInfoDialog').modal('show');

                //TODO LIST:弹出人员编辑对话框

            });

        },

        initModals:function(){

            $.fn.modalmanager.defaults.resize = true;
            $.fn.modalmanager.defaults.spinner = '<div class="loading-spinner fade" style="width: 200px; margin-left: -100px;"><img src="assets/img/ajax-modal-loading.gif" align="middle">&nbsp;<span style="font-weight:300; color: #eee; font-size: 18px; font-family:Open Sans;">&nbsp;Loading...</div>';

        }

    };

}();