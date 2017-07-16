var DevPos = function () {

    var devPosTable=null;
    var form=null;
    var error=null
    var success=null;

    return {
        //main function to initiate the module
        initDevPos: function () {

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

             devPosTable = $('#table_dev_pos').dataTable({
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
                "sAjaxSource": $('#context').val()+"/devPos/devPos-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"设备名称查询:",
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
                    "mDataProp" : "sensorType"
                }, {
                    "mDataProp" : "pipeMaterial"
                },{
                    "mDataProp" : "pipeSize"
                },{
                    "mDataProp" : "startTotalValue"
                },{
                    "mDataProp" : "lowInstantValue"
                },{
                    "mDataProp" : "highInstantValue"
                },{
                    "mDataProp" : "positionName"
                },{
                    "mDataProp" : "deviceName"
                },{
                    "mDataProp" : "positionId"
                },{
                    "mDataProp" : "deviceId"
                },{
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
                }, {
                    'bVisible':false,
                    'aTargets': [9]
                }, {
                    'bVisible':false,
                    'aTargets': [10]
                }]
            });

            jQuery('#table_dev_pos_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_dev_pos_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_dev_pos_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });

            //delete  click
            $('#table_dev_pos a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该监测点 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = devPosTable.fnGetData(nRow);
                var devPosId = aData.id;
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/devPos/devPos-delete.do",
                    dataType:"json",
                    data: "id="+devPosId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {

                            devPosTable.fnDraw();

                        }
                        else{

                            $("#errorMessage4Pos").html(jData.message);
                            error.show();

                        }
                    },
                    error:function(request){

                        error.show();
                    }
                });
            });

            //edit  click
            $('#table_dev_pos a.blue').live('click', function (e) {
                e.preventDefault();
                App.scrollTo($('#form-dev-pos'), -200);
                success.hide();
                error.hide();
                $('#form-dev-pos .control-group').removeClass('error');
                $('#form-dev-pos .control-group').removeClass('success');
                $('.validate-inline').remove();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = devPosTable.fnGetData(nRow);

                var id = aData.id;
                var pipeSize = aData.pipeSize;
                var startTotalValue = aData.startTotalValue;
                var lowInstantValue = aData.lowInstantValue;
                var highInstantValue = aData.highInstantValue;

                var pipeMaterial = aData.pipeMaterial;
                var sensorType = aData.sensorType;
                var positionId = aData.positionId;
                var deviceId = aData.deviceId;

                $('#form-dev-pos #devPosId').val(id);
                $('#form-dev-pos #pipeSize').val(pipeSize);
                $('#form-dev-pos #startTotalValue').val(startTotalValue);
                $('#form-dev-pos #lowInstantValue').val(lowInstantValue);
                $('#form-dev-pos #highInstantValue').val(highInstantValue);

                var count1 = $('#form-dev-pos #sensorType option').length;
                for(var i=0;i<count1;i++)
                {
                    if($('#form-dev-pos #sensorType').get(0).options[i].text==sensorType)
                    {
                        $('#form-dev-pos #sensorType').get(0).options[i].selected = true;
                        $('#form-dev-pos #sensorType').parent().find('span').get(0).innerHTML = $('#form-dev-pos #sensorType').get(0).options[i].text;
                        break;
                    }
                }

                var count2 = $('#form-dev-pos #pipeMaterial option').length;
                for(var i=0;i<count2;i++)
                {
                    if($('#form-dev-pos #pipeMaterial').get(0).options[i].text==pipeMaterial)
                    {
                        $('#form-dev-pos #pipeMaterial').get(0).options[i].selected=true;
                        $('#form-dev-pos #pipeMaterial').parent().find('span').get(0).innerHTML = $('#form-dev-pos #pipeMaterial').get(0).options[i].text;
                        break;
                    }
                }

                var count3 = $('#form-dev-pos #positionId option').length;
                for(var i=0;i<count3;i++)
                {
                    if($('#form-dev-pos #positionId').get(0).options[i].value==positionId)
                    {
                        $('#form-dev-pos #positionId').get(0).options[i].selected=true;
                        $('#form-dev-pos #positionId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #positionId').get(0).options[i].text;
                        break;
                    }
                }

                var count4 = $('#form-dev-pos #deviceId option').length;
                for(var i=0;i<count4;i++)
                {
                    if($('#form-dev-pos #deviceId').get(0).options[i].value==deviceId)
                    {
                        $('#form-dev-pos #deviceId').get(0).options[i].selected=true;
                        $('#form-dev-pos #deviceId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #deviceId').get(0).options[i].text;
                        break;
                    }
                }

                success.hide();
                error.hide();
                $('#form-dev-pos .control-group').removeClass('error');
                $('#form-dev-pos .control-group').removeClass('success');
                $('.validate-inline').remove();

            });


        },

        refreshDataTable: function () {
            devPosTable.fnDraw();
        },

        initForms:function(){

             form = $('#form-dev-pos');
             error = $('.alert-error', form);
             success = $('.alert-success', form);

            $('#sensorType').select2({
                allowClear: true
            });

            $('#pipeMaterial').select2({
                allowClear: true
            });

            $('#positionId').select2({
                allowClear: true
            });

            $('#deviceId').select2({
                allowClear: true
            });

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //TODO LIST:进行表单数据校验
                    sensorType:{
                        required:true
                    },
                    pipeMaterial:{
                        required:true
                    },
                    pipeSize:{
                        number:true,
                        required:true
                    },
                    startTotalValue:{
                        number:true,
                        required:true
                    },
                    lowInstantValue:{
                        number:true,
                        required:true
                    },
                    highInstantValue:{
                        number:true,
                        required:true
                    },
                    positionId:{
                        required:true
                    },
                    deviceId:{
                        required:true
                    }

                },
                messages: { // custom messages for radio buttons and checkboxes

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
                        url: $('#context').val()+"/devPos/devPos-save.do",
                        data: $('#form-dev-pos').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {

                                $('#form-dev-pos .control-group').removeClass('error');
                                $('#form-dev-pos .control-group').removeClass('success');
                                $('#form-dev-pos .validate-inline').remove();
                                devPosTable.fnDraw();
                                $('#form-dev-pos')[0].reset();
                                success.show();
                                error.hide();
                                $('#form-dev-pos #deviceId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #deviceId').get(0).options[0].text;
                                $('#form-dev-pos #positionId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #positionId').get(0).options[0].text;
                            }
                            else{

                                success.hide();
                                $("#errorMessage4DevPos").html(jData.message);
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

            $('#form-dev-pos #cancelBtn4DevPos').live('click', function (e) {

                $('#form-pos')[0].reset();
                success.hide();
                error.hide();
                $('#form-dev-pos .control-group').removeClass('error');
                $('#form-dev-pos .control-group').removeClass('success');
                $('#form-dev-pos .validate-inline').remove();

                $('#form-dev-pos #deviceId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #deviceId').get(0).options[0].text;
                $('#form-dev-pos #positionId').parent().find('span').get(0).innerHTML = $('#form-dev-pos #positionId').get(0).options[0].text;

            });

        }

    };

}();