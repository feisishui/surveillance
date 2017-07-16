var DMA = function () {

    var oTable=null;
    var success=null;
    var error=null;
    var form=null;

    var areaChosenCallback = function (e) {

        var longitudeLatitudeCollects = "";
        var polygon = e;

        for ( var i = 0; i < polygon.PartCount; i++) {
            var polygonParts = polygon.Item(i);
            for ( var j = 0; j < polygonParts.Count - 1; j++) {
                var point = polygonParts.Item(j);
                var x = point.X;
                var y = point.Y;
                var longitudeLatitude = (x + ":" + y);
                longitudeLatitudeCollects += (longitudeLatitude + "-");
            }
            longitudeLatitudeCollects = longitudeLatitudeCollects.substring(0,
                longitudeLatitudeCollects.lastIndexOf("-"));
        }

        $("#regionArea").val(longitudeLatitudeCollects);

        Gis.detachEvent("FireTrackPolygonEnd",areaChosenCallback);

    };


    return {
        //main function to initiate the module
        init: function () {
            //
            $("#root").click();

            $('#dma_tree_collapse').click(function () {
                $('.tree-toggle', $('#dma_tree  li')).addClass("closed");
                $('.branch', $('#dma_tree  li')).removeClass("in");
            });

            $('#dma_tree_expand').click(function () {
                $('.tree-toggle', $('#dma_tree  li')).removeClass("closed");
                $('.branch', $('#dma_tree  li')).addClass("in");
            });

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

             oTable = $('#table_dma_info').dataTable({
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
                "sAjaxSource": $('#context').val()+"/dma/dma-child-list.do",
                "fnServerData":retrieveData,
                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                "oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "抱歉， 没有找到",
                    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                    "sInfoEmpty": "没有数据",
                    "sSearch":"分区名称查询:",
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
                },{
                    "mDataProp" : "fatherDma"
                },{
                    "mDataProp" : "no"
                },{
                    "mDataProp" : "userCount"
                },{
                    "mDataProp" : "normalWater"
                },{
                    "mDataProp" : "pipeLeng"
                },{
                    "mDataProp" : "userPipeLeng"
                },{
                    "mDataProp" : "pipeLinks"
                },{
                    "mDataProp" : "icf"
                },{
                    "mDataProp" : "leakControlRate"
                },{
                    "mDataProp" : "saleWater"
                }, {
                    "mDataProp" : "bDataParent_DMA"
                }, {
                    "mDataProp" : "gis"
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
                    'aTargets': [3]
                }, {
                    'bVisible':false,
                    'aTargets': [12]
                }, {
                    'bVisible':false,
                    'aTargets': [13]
                }]
            });

            jQuery('#table_dma_info_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_dma_info_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_dma_info_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            });

            //delete  click
            $('#table_dma_info a.red').live('click', function (e) {
                e.preventDefault();

                if (confirm("确认要删除该分区 ?") == false) {
                    return;
                }
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var dmaId = aData.id;
                $.ajax( {
                    type: "POST",
                    url: $('#context').val()+"/dma/dma-delete.do",
                    dataType:"json",
                    data: "id="+dmaId,
                    success: function(data) {

                        var jData = eval(data);

                        if(jData.success==true) {
                            DMA.resetDmaSel();
                            oTable.fnDraw();
                            Pos.refreshDataTable();
                            DevPos.refreshDataTable();
                            $('#form-dma #posName').removeAttr("disabled");
                        }
                        else{

                            $("#errorMessage").html(jData.message);
                            error.show();

                        }
                    },
                    error:function(request){

                        error.show();
                    }
                });
            });

            //edit  click
            $('#table_dma_info a.blue').live('click', function (e) {

                App.scrollTo($('#form-dma'), -200);

                e.preventDefault();

                success.hide();
                error.hide();
                $('#form-dma .control-group').removeClass('error');
                $('#form-dma .control-group').removeClass('success');
                $('.validate-inline').remove();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];
                var aData = oTable.fnGetData(nRow);
                var id = aData.id;
                var name = aData.name;
                var no = aData.no;
                var userCount = aData.userCount;
                var normalWater = aData.normalWater;
                var pipeLeng = aData.pipeLeng;
                var userPipeLeng = aData.userPipeLeng;
                var pipeLinks = aData.pipeLinks;
                var icf = aData.icf;
                var leakControlRate = aData.leakControlRate;
                var saleWater= aData.saleWater;
                var bDataParent_DMA = aData.bDataParent_DMA;
                var regionArea = aData.gis;

                $('#form-dma #regionID').val(id);
                $('#form-dma #name').val(name);
                $('#form-dma #no').val(no);
                $('#form-dma #bDataParent_DMA').val(no);
                $('#form-dma #regionArea').val(regionArea);

                $('#form-dma #userCount').val(userCount);
                $('#form-dma #normalWater').val(normalWater);
                $('#form-dma #pipeLeng').val(pipeLeng);
                $('#form-dma #userPipeLeng').val(userPipeLeng);
                $('#form-dma #pipeLinks').val(pipeLinks);
                $('#form-dma #icf').val(icf);
                $('#form-dma #leakControlRate').val(leakControlRate);
                $('#form-dma #saleWater').val(saleWater);
                $('#form-dma #no').attr("disabled","disabled");

                //TODO LIST:修改为select2的属性设置
                var count = $('#form-dma #bDataParent_DMA option').length;
                for(var i=0;i<count;i++)
                {
                    if($('#form-dma #bDataParent_DMA').get(0).options[i].value==bDataParent_DMA)
                    {
                        $('#form-dma #bDataParent_DMA').get(0).options[i].selected=true;
                        $('#form-dma #bDataParent_DMA').parent().find('span').get(0).innerHTML = $('#form-dma #bDataParent_DMA').get(0).options[i].text;
                        break;
                    }
                }

            });


        },

        resetDmaSel:function() {

            $.ajax( {
                type: "POST",

                url: $('#context').val()+"/dma/dma-combo.do",

                dataType:"json",
                success: function(data) {

                    var jData = eval(data);

                    if(jData.items!=null) {

                        var result = jData.items;

                        $('#bDataParent_DMA option').remove();

                        $.each(result,function(index,value){

                            $('#bDataParent_DMA').append('<option value="' + result[index].id+ '">'+result[index].text+'</option>');

                        });

                        $('#dmaInfoId option').remove();

                        $.each(result,function(index,value){

                            $('#dmaInfoId').append('<option value="' + result[index].id+ '">'+result[index].text+'</option>');

                        });


                    }
                    else{

                    }
                },
                error:function(request){


                }
            });

            /*
            $.ajax( {
                type: "POST",

                url: $('#context').val()+"/position/position-combo",

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
            */
        },

        initForms:function(){

             form = $('#form-dma');
             error = $('.alert-error', form);
             success = $('.alert-success', form);

            $('.select2_DMA').select2({
                placeholder: "请选择",
                allowClear: false
            });


            //选择区域
            $('.chosenAreaBtn').live('click', function (e) {

                Gis.attachEvent("FireTrackPolygonEnd",areaChosenCallback);

                Gis.setAction(17);

            });


            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //TODO LIST:进行表单数据校验
                    no: {
                        minlength:2,
                        required: true
                    },
                    name: {
                        minlength:3,
                        required: true
                    },
                    userCount:{
                        number:true,
                        required:true
                    },
                    normalWater:{
                        number:true,
                        required:true
                    },
                    icf:{
                        number:true,
                        required:true
                    },
                    leakControlRate:{
                        number:true,
                        required:true
                    },
                    saleWater:{
                        number:true,
                        required:true
                    },
                    pipeLeng:{
                        number:true,
                        required:true
                    },
                    userPipeLeng:{
                        number:true,
                        required:true
                    },
                    pipeLinks:{
                        number:true,
                        required:true
                    },
                    regionArea:{
                        required:true
                    },
                    bDataParent_DMA:{
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
                    App.scrollTo(error, -100);
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
                        url: $('#context').val()+"/dma/dma-save.do",
                        data: $('#form-dma').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {
                                $('#form-dma .control-group').removeClass('error');
                                $('#form-dma .control-group').removeClass('success');
                                $('#form-dma .validate-inline').remove();
                                $('#form-dma')[0].reset();
                                $('#form-dma #no').removeAttr("disabled");
                                $('#form-dma #bDataParent_DMA').parent().find('span').get(0).innerHTML = "Root";
                                DMA.resetDmaSel();
                                oTable.fnDraw();
                                success.show();
                                error.hide();
                            }
                            else{
                                success.hide();
                                $("#errorMessage").html(jData.message);
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

            $('#form-dma #cancelBtn').live('click', function (e) {

                $('#form-dma')[0].reset();
                success.hide();
                error.hide();
                $('#form-dma .control-group').removeClass('error');
                $('#form-dma .control-group').removeClass('success');
                $('#form-dma .validate-inline').remove();
                $('#form-dma #bDataParent_DMA').parent().find('span').get(0).innerHTML = "Root";

            });
        }
    };

}();