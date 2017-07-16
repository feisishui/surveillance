var DmaLeakAssess = function () {

    var oTable = null;

    var createChart5 = function(data1,data2){
        $('#chart_5').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区漏损实时监测'
            },
            subtitle: {
                text: '供、漏水对比图'
            },
            xAxis: data1,
            yAxis: {
                min: 0,
                title: {
                    text: '供、漏水量(㎥)'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td nowrap="nowrap" style="color:{series.color};padding:0;font-size:10px">{series.name}: </td>' +
                '<td nowrap="nowrap" style="padding:0;font-size:10px"><b>{point.y:.1f} ㎥</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: data2
        });
    }

    var createChart6 = function(data1,data2){
        $('#chart_6').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区漏损实时监测'
            },
            subtitle: {
                text: '漏损控制率对比图'
            },
            xAxis: data1,
            yAxis: {
                min: 0,
                title: {
                    text: '控制率(%)'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td nowrap="nowrap" style="color:{series.color};padding:0;font-size:10px">{series.name}: </td>' +
                '<td nowrap="nowrap" style="padding:0;font-size:10px"><b>{point.y:.1f} %</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: data2
        });
    };

    return {

        //main function to initiate the module
        initDMALeakAssess: function () {

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
                        var len = data.aaData.length;
                        var dataX = new Array(len);
                        var dataIn = new Array(len);
                        var dataOut = new Array(len);

                        var leakRate = new Array(len);
                        var leakCtl = new Array(len);
                        for(var i=0 ;i<len;i++){
                            dataX[i]= data.aaData[i].dmaCode;
                            dataIn[i] = data.aaData[i].supplyWater;
                            dataOut[i] = data.aaData[i].leakWater;
                            leakRate[i] = data.aaData[i].leakRate;
                            leakCtl[i] = data.aaData[i].leakControlRate;
                        }
                        var jsonX = {categories:dataX};


                        var jsonY =[{
                            name: '供水量',
                            data: dataIn
                            }, {
                                name: '漏水量',
                                data: dataOut
                            }];

                        var jsonLeak = [{
                            name: '漏损率',
                            data: leakRate

                        }, {
                            name: '控制率',
                            data: leakCtl

                        }];

                        createChart5(jsonX,jsonY);

                        createChart6(jsonX,jsonLeak);

                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式

                    }
                });
            }

            oTable = $('#table_dma_leak_assess').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": true,
                "bFilter" : false,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/dma/dma-leak-assess.do",
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
                    "mDataProp" : "dmaCode"
                }, {
                    "mDataProp" : "dmaName"
                }, {
                    "mDataProp" : "reportDate"
                },{
                    "mDataProp" : "allowedMinWater"
                }, {
                    "mDataProp" : "minInstantWater"
                }, {
                    "mDataProp" : "minInstantWaterTime"
                }, {
                    "mDataProp" : "leakWater"
                }, {
                    "mDataProp" : "supplyWater"
                }, {
                    "mDataProp" : "leakRate"
                }, {
                    "mDataProp" : "leakControlRate"
                }, {
                    "mDataProp" : "pipeLength"
                }, {
                    "mDataProp" : "leakWaterPerPipeLength"
                }, {
                    "mDataProp" : "maxInstantWater"
                }, {
                    "mDataProp" : "maxInstantWaterTime"
                }, {
                    "mDataProp" : "avgInstantWater"
                }, {
                    "mDataProp" : "physiqueAnalysis"
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

            jQuery('#table_dma_leak_assess_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_dma_leak_assess_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_dma_leak_assess_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown
        },

        initModals:function(){

            //$.fn.modalmanager.defaults.resize = true;
            //$.fn.modalmanager.defaults.spinner = '<div class="loading-spinner fade" style="width: 200px; margin-left: -100px;"><img src="assets/img/ajax-modal-loading.gif" align="middle">&nbsp;<span style="font-weight:300; color: #eee; font-size: 18px; font-family:Open Sans;">&nbsp;Loading...</div>';

        }

    };

}();