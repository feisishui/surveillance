var Pos = function () {

    var posTable=null;
    var success=null;
    var error=null;
    var form=null;

    return {
        //main function to initiate the module
        initPos: function () {

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

             posTable = $('#table_pos_info').dataTable({
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
                "sAjaxSource": $('#context').val()+"/position/position-list.do",
                     "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"监测点名称查询:",
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
                    "mDataProp" : "name"
                }, {
                    "mDataProp" : "longitude"
                },{
                    "mDataProp" : "latitude"
                },{
                    "mDataProp" : "bDataPosType"
                },{
                    "mDataProp" : "sortCode"
                },{
                    "mDataProp" : "direction"
                },{
                    "mDataProp" : "isUse"
                },{
                    "mDataProp" : "dmaId"
                },{
                    "mDataProp" : "comment"
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
                    'aTargets': [8]
                }, {
                    'bVisible':false,
                    'aTargets': [9]
                }]
            });

            jQuery('#table_pos_info_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_pos_info_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_pos_info_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });

            //delete userType click
            $('#table_pos_info a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该监测点 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = posTable.fnGetData(nRow);
                var posId = aData.id;
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/position/position-delete.do",
                    dataType:"json",
                    data: "id="+posId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {

                            posTable.fnDraw();

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

            //edit userType click
            $('#table_pos_info a.blue').live('click', function (e) {

                e.preventDefault();

                App.scrollTo($('#form-pos'), -200);

                success.hide();
                error.hide();
                $('#form-pos .control-group').removeClass('error');
                $('#form-pos .control-group').removeClass('success');
                $('.validate-inline').remove();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = posTable.fnGetData(nRow);
                var id = aData.id;
                var name = aData.name;
                var longitude = aData.longitude;
                var latitude = aData.latitude;
                var bDataPosType = aData.bDataPosType;
                var sortCode = aData.sortCode;
                var direction = aData.direction;
                var isUse = aData.isUse;
                var dmaId = aData.dmaId;
                var comment = aData.comment;
                //TODO LIST:有问题
                var bDataParent_DMA = aData.bDataParent_DMA;
                var gis = aData.gis;

                $('#form-pos #posId').val(id);
                $('#form-pos #posName').val(name);
                $('#form-pos #sortCode').val(sortCode);
                $('#form-pos #longitude').val(longitude);
                $('#form-pos #latitude').val(latitude);
                $('#form-pos #bDataPosType').val(bDataPosType);
                $('#form-pos #comment').val(comment);
                $('#form-pos #posName').attr("disabled","disabled");

                //TODO LIST：处理编辑相关的业务逻辑
                var count1 = $('#form-pos #isUse option').length;
                for(var i=0;i<count1;i++)
                {
                    if($('#form-pos #isUse').get(0).options[i].value==isUse)
                    {
                        $('#form-pos #isUse').get(0).options[i].selected=true;
                        $('#form-pos #isUse').parent().find('span').get(0).innerHTML = $('#form-pos #isUse').get(0).options[i].text;
                        break;
                    }
                }

                var count2 = $('#form-pos #direction option').length;
                for(var i=0;i<count2;i++)
                {
                    if($('#form-pos #direction').get(0).options[i].text==direction)
                    {
                        $('#form-pos #direction').get(0).options[i].selected=true;
                        $('#form-pos #direction').parent().find('span').get(0).innerHTML = $('#form-pos #direction').get(0).options[i].text;
                        break;
                    }
                }

                var count3 = $('#form-pos #dmaInfoId option').length;
                for(var i=0;i<count3;i++)
                {
                    if($('#form-pos #dmaInfoId').get(0).options[i].value==dmaId)
                    {
                        $('#form-pos #dmaInfoId').get(0).options[i].selected=true;
                        $('#form-pos #dmaInfoId').parent().find('span').get(0).innerHTML = $('#form-pos #dmaInfoId').get(0).options[i].text;
                        break;
                    }
                }

            });


        },

        resetPosSel:function() {

            $.ajax( {
                type: "POST",

                url: $('#context').val()+"/position/position-combo.do",

                dataType:"json",
                success: function(data) {

                    var jData = eval(data);

                    if(jData.items!=null) {

                        var result = jData.items;

                        $('#positionId option').remove();

                        $.each(result,function(index,value){

                            $('#positionId').append('<option value="' + result[index].id+ '">'+result[index].text+'</option>');

                        });

                    }
                    else{

                    }
                },
                error:function(request){


                }
            });
        },

        refreshDataTable: function () {
            posTable.fnDraw();
        },

        initForms:function(){

             form = $('#form-pos');
             error = $('.alert-error', form);
             success = $('.alert-success', form);

            $('#isUse').select2({
                allowClear: true
            });

            $('#direction').select2({
                allowClear: true
            });

            $('#dmaInfoId').select2({
                allowClear: true
            });

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //TODO LIST:进行表单数据校验
                    name: {
                        minlength:2,
                        required: true
                    },
                    sortCode:{
                        required:true
                    },
                    longitude:{
                        number:true,
                        required:true
                    },
                    latitude:{
                        number:true,
                        required:true
                    },
                    bDataPosType:{
                        required:true
                    },
                    isUse:{
                        required:true
                    },
                    direction:{
                        required:true
                    },
                    dmaId:{
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
                        url: $('#context').val()+"/position/position-save.do",
                        data: $('#form-pos').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {
                                $('#form-pos .control-group').removeClass('error');
                                $('#form-pos .control-group').removeClass('success');
                                $('#form-pos .validate-inline').remove();
                                $('#form-pos #dmaInfoId').parent().find('span').get(0).innerHTML = "Root";
                                $('#form-pos #direction').parent().find('span').get(0).innerHTML = "流入";
                                $('#form-pos #isUse').parent().find('span').get(0).innerHTML = "是";
                                $('#form-pos #posName').removeAttr("disabled");
                                posTable.fnDraw();
                                Pos.resetPosSel();
                                $('#form-pos')[0].reset();
                                success.show();
                                error.hide();
                            }
                            else{

                                success.hide();
                                $("#errorMessage4Pos").html(jData.message);
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

            $('#form-pos #cancelBtn4Pos').live('click', function (e) {

                $('#form-pos')[0].reset();
                success.hide();
                error.hide();
                $('#form-pos .control-group').removeClass('error');
                $('#form-pos .control-group').removeClass('success');
                $('#form-pos .validate-inline').remove();
                $('#form-pos #dmaInfoId').parent().find('span').get(0).innerHTML = "Root";
                $('#form-pos #direction').parent().find('span').get(0).innerHTML = "流入";
                $('#form-pos #isUse').parent().find('span').get(0).innerHTML = "是";
            });
        }

    };

}();