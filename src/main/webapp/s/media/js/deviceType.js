var DeviceType = function () {

    var oTable = null;
    var form = null;
    var error = null;
    var success = null;

    return {

        //main function to initiate the module
        initDeviceType: function () {

            function retrieveData( sSource, aoData, fnCallback ) {
                //查询条件称加入参数数组
                // var rentRuleId =document.getElementById('rentRuleId').value;
                //alert(rentRuleId);
                $.ajax( {
                    type: "POST",
                    url: sSource,
                    dataType:"json",
                    //TODO LIST：按条件查询服务器数据
                    data: "jsonParam="+JSON.stringify(aoData)+"&isHistory=0&rentRuleId="+1,
                    success: function(data) {
                        //$("#url_sortdata").val(data.aaData);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
                    }
                });
            }

            oTable = $('#table_deviceType').dataTable({
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
                "sAjaxSource": $('#context').val()+"/deviceType/deviceType-list.do",
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
                    "mDataProp" : "typeCode"
                }, {
                    "mDataProp" : "typeName"
                }, {
                    "mDataProp" : "location"
                },{
                    "mDataProp" : "btnEdit"
                }, {
                    "mDataProp" : "btnDelete"
                } ],
                "aoColumnDefs": [{
                        'aTargets': ['_all'],
                        sDefaultContent:''
                    }, {
                    'bVisible':true,
                    'aTargets': [0]
                    }
                ]
            });

            jQuery('#table_deviceType_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_deviceType_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_deviceType_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown


            //delete userType click
            $('#table_deviceType a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该设备类型 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var typeId = aData.id;
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/deviceType/deviceType-delete.do",
                    dataType:"json",
                    data: "typeId="+typeId,
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
            $('#table_deviceType a.blue').live('click', function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;
                var typeCode = aData.typeCode;
                var typeName = aData.typeName;
                var location = aData.location;

                $('#submit_form #typeId').val(id);
                $('#submit_form #typeCode').val(typeCode);
                $('#submit_form #typeName').val(typeName);
                $('#submit_form #location').val(location);
                $('#submit_form #typeCode').attr("disabled","disabled");

                success.hide();
                error.hide();
                $('#submit_form .control-group').removeClass('error');
                $('#submit_form .control-group').removeClass('success');
                $('.validate-inline').remove();
                //初始化表单
                $('#addDeviceTypeDialog').modal('show');

            });

            $('#equipmentType_add').live('click', function (e) {

                $('#addDeviceTypeDialog').modal('show');
                $('#submit_form')[0].reset();
                $('#submit_form #typeCode').removeAttr("disabled");
                success.hide();
                error.hide();
                $('#submit_form .control-group').removeClass('error');
                $('#submit_form .control-group').removeClass('success');
                $('.validate-inline').remove();

            });
        },

        initModals:function(){

            $.fn.modalmanager.defaults.resize = true;
            $.fn.modalmanager.defaults.spinner = '<div class="loading-spinner fade" style="width: 200px; margin-left: -100px;"><img src="assets/img/ajax-modal-loading.gif" align="middle">&nbsp;<span style="font-weight:300; color: #eee; font-size: 18px; font-family:Open Sans;">&nbsp;Loading...</div>';

        },

        initForms:function(){

            form = $('#submit_form');
            error = $('.alert-error', form);
            success = $('.alert-success', form);

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //account
                    typeCode: {
                        required: true,
                        remote: {
                            url: $('#context').val()+'/deviceType/exist.do',
                            data:{typeCode: function() {
                                        return $('#typeCode').val();
                                }
                            }
                        }
                    },
                    typeName: {
                        minlength: 3,
                        required: true
                    },
                    location: {
                        minlength: 3,
                        required: true
                    }
                },
                messages: { // custom messages for radio buttons and checkboxes
                    typeCode: {
                        remote: "该用户名已被注册"
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
                    //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
                    $.ajax( {
                        type: "POST",
                        url: $('#context').val()+"/deviceType/deviceType-save.do",
                      //  dataType:'json',
                        //TODO LIST：按条件查询服务器数据
                     //   data: JSON.stringify($('#submit_form').serialize()),
                        data: $('#submit_form').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {

                                form.reset();
                                $('#addDeviceTypeDialog').modal('hide');
                                oTable.fnDraw();

                            }
                            else{
                                success.hide();
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
        }

    };

}();