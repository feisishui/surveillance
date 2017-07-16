var WaterBlance = function () {

    var oTable = null;

    var createChart5 = function(data){
        $('#chart_5').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区供水量对比图'
            },
            xAxis: {
                type: 'category',
                title: {
                    text: '分区名称'
                },
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '供水量(㎥)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '供水量： <b>{point.y:.1f}</b>'
            },
            plotOptions : {
                column : {
                    pointPadding : 0.2,
                    borderWidth:0
                }
            },
            series: [{
                name: '供水量',
                data: data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    }

    var createChart6 = function(data){
        $('#chart_6').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区售水量对比图'
            },
            xAxis: {
                type: 'category',
                title: {
                    text: '分区名称'
                },
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '售水量(㎥)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '售水量： <b>{point.y:.1f}</b>'
            },
            plotOptions : {
                column : {
                    pointPadding : 0.2,
                    borderWidth:0
                }
            },
            series: [{
                name: '售水量',
                data: data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    };

    var createChart7 = function(data){
        $('#chart_7').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区售水量对比图'
            },
            xAxis: {
                type: 'category',
                title: {
                    text: '分区'
                },
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '售水量(㎥)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '售水量： <b>{point.y:.1f}</b>'
            },
            plotOptions : {
                column : {
                    pointPadding : 0.2,
                    borderWidth:0
                }
            },
            series: [{
                name: '售水量',
                data: data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    };

    var createChart8 = function(data){
        $('#chart_8').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区售水量对比图'
            },
            xAxis: {
                type: 'category',
                title: {
                    text: '分区名'
                },
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '售水量(㎥)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '售水量： <b>{point.y:.1f}</b>'
            },
            plotOptions : {
                column : {
                    pointPadding : 0.2,
                    borderWidth:0
                }
            },
            series: [{
                name: '售水量',
                data: data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    };

    var createChart9 = function(data){
        $('#chart_9').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'DMA分区售水量对比图'
            },
            xAxis: {
                type: 'category',
                title: {
                    text: '分区名'
                },
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '售水量(㎥)'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '售水量： <b>{point.y:.1f}</b>'
            },
            plotOptions : {
              column : {
                  pointPadding : 0.2,
                  borderWidth:0
              }
            },
            series: [{
                name: '售水量',
                data: data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    };

    return {

        //main function to initiate the module
        initWaterBlance: function () {

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
                        var data5 = new Array(len+1);
                        var data6 = new Array(len+1);
                        var data7=  new Array(len+1);
                        var data8=  new Array(len+1);
                        var data9=  new Array(len+1);
                        for(var i=0 ;i<len;i++){
                            data5[i]=new Array(data.aaData[i].dmaCode,data.aaData[i].waterSupply);
                            data6[i]=new Array(data.aaData[i].dmaCode,data.aaData[i].waterSale);
                            data7[i]=new Array(data.aaData[i].dmaCode,data.aaData[i].noValueWater);
                            data8[i]=new Array(data.aaData[i].dmaCode,data.aaData[i].lrSteal);
                            data9[i]=new Array(data.aaData[i].dmaCode,data.aaData[i].lrPressure);
                        }
                        data5[len] = new Array("",0);
                        data6[len] = new Array("",0);
                        data7[len] = new Array("",0);
                        data8[len] = new Array("",0);
                        data9[len] = new Array("",0);

                        createChart5(data5);
                        createChart6(data6);
                        createChart7(data7);
                        createChart8(data8);
                        createChart9(data9);
                        fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式

                    }
                });
            }

            oTable = $('#table_water_balance').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "bAutoWidth":false,//自动宽度
                "bServerSide": true,
                "bPaginate": false,
                "bFilter" : false,
                "sPaginationType": "bootstrap",      //分页样式
                "sAjaxSource": $('#context').val()+"/dma/water-balance.do",
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
                    "mDataProp" : "analysisDate"
                },{
                    "mDataProp" : "waterSupply"
                }, {
                    "mDataProp" : "waterSale"
                }, {
                    "mDataProp" : "noValueWater"
                }, {
                    "mDataProp" : "lrLeakage"
                }, {
                    "mDataProp" : "lrWaterME"
                }, {
                    "mDataProp" : "LrMeterE"
                }, {
                    "mDataProp" : "lrFavor"
                }, {
                        "mDataProp" : "lrSteal"
                }, {
                    "mDataProp" : "lrPressure"
                }, {
                    "mDataProp" : "saleDiffWater"
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

            jQuery('#table_water_balance_wrapper .dataTables_filter input').addClass("m-wrap medium"); // modify table search input
            jQuery('#table_water_balance_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
            jQuery('#table_water_balance_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialzie select2 dropdown
        },

        initModals:function(){

            //$.fn.modalmanager.defaults.resize = true;
            //$.fn.modalmanager.defaults.spinner = '<div class="loading-spinner fade" style="width: 200px; margin-left: -100px;"><img src="assets/img/ajax-modal-loading.gif" align="middle">&nbsp;<span style="font-weight:300; color: #eee; font-size: 18px; font-family:Open Sans;">&nbsp;Loading...</div>';

        }

    };

}();