/**
 * Created by lenovo on 2016/8/30.
 */
var xform;
var TableAdvanced = function () {

    var regions;
    var oTable = null;

    function fnFormatDetails ( oTable, nTr )
    {

        var aData = oTable.fnGetData( nTr );
        var num = aData[4];
        var list = regions[num].list;
        if (list == null || list == undefined) {
            list = new Array();
        }
        var sOut = '<table>';
        if (list.length > 0) {
            for (var i = 0; i < list.length; i ++) {
                sOut += '<tr onclick="TableAdvanced.loadXform(' + list[i].id +
                    ');"><td>[' + (i+1) + ']</td><td>' + list[i].desc + '</td></tr>';
            }
        } else {
            sOut += '<tr><td>没有满足条件的事件信息</td></tr>';
        }

        sOut += '</table>';
        return sOut;
    };

    /**
     * 以下为加载Xform的相关方法——start
     */
    function _loadXform(processInstanceId) {
        $("#divForRegion").attr("style", "display:none;");
        $("#divForDetails").attr("style", "display:block;");
        $("#form-table-div").html("<div id='xf-form-table'></div>");
        $.ajax({
            type: "get",
            url: $('#context').val() + "/bpm/getInstanceForm.do",
            dataType: "json",
            data: "processInstanceId=" + processInstanceId,
            success: function (r) {
                if (r.success) {
                    xform = new xf.Xform('xf-form-table');
                    xform.render();
                    if (r.content != '') {
                        xform.doImport(r.content);
                    }
                    var data = r.jsonData;
                    var data2 = eval('(' + data + ')');
                    xform.setValue(data2);
                    //清理需要上传的按钮
                    $("input[type='file']").each(function(i){
                        $(this).before("未上传");
                        this.type = 'hidden';
                    });
                    $("div[class='xf-handler']").each(function(i){
                        var inner = $(this).html();
                        if (inner == null || inner == undefined || inner.length <= 0) {
                            $(this).html("未设置");
                        }
                    });
                    //调节表格展示样式
                    var table = $("#xf-form-table table");
                    table.attr("width", "100%");
                    var tbody = $("#xf-form-table tbody");
                    var html = "<tr>" +
                        "<td class='xf-cell-right xf-cell-bottom xf-cell-left'>" +
                        "<div class='xf-handler'><label style='display:block;text-align:right;margin-bottom:0px;'>" +
                        "详细信息" +
                        "</label></div></td>" +
                        "<td rowspan='1' colspan='3' class='xf-cell-right xf-cell-bottom' width='75%'>" +
                        "<div class='xf-handler'>" +
                        "<a onclick='TableAdvanced.viewHistory(" + processInstanceId + ")'>" +
                        "查看" +
                        "</a>" +
                        "</div></td>" +
                        "</tr>";
                    tbody.append(html);
                } else {
                    alert(r.msg);
                }
            },
            error: function (d) {//请求出错
                console.log(d.responseText);
            }
        });
    }
    /**
     * 以上为加载Xform的相关方法——end
     */

    var initTable = function() {



        /*
         * Insert a 'details' column to the table
         */
        var nCloneTh = document.createElement( 'th' );
        var nCloneTd = document.createElement( 'td' );
        nCloneTd.innerHTML = '<span class="row-details row-details-close"></span>';

        $('#regionTable thead tr').each( function () {
            this.insertBefore( nCloneTh, this.childNodes[0] );
        } );

        $('#regionTable tbody tr').each( function () {
            this.insertBefore(  nCloneTd.cloneNode( true ), this.childNodes[0] );
        } );

        /*
         * Initialse DataTables, with no sorting on the 'details' column
         */
        oTable = $('#regionTable').DataTable( {
            "aoColumnDefs": [
                {
                    'bVisible':false,
                    'aTargets': [ 1,4 ]
                },{
                    "bSortable": false,
                    "aTargets": [ 0,1,2,3,4 ]
                }
            ],
            // set the initial value
            "iDisplayLength": 5,
            "sDom": "t<'row-fluid'<'span6'i><'span6'p>>",
            "oLanguage": {
                "sLengthMenu": "每页显示 _MENU_ 条记录",
                "sZeroRecords": "抱歉， 没有找到",
                "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
                "sInfoEmpty": "没有数据",
                "sSearch":"工地名查询:",
                "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
                "oPaginate": {
                    "sPrevious":"上一页 ",
                    "sNext": "下一页",
                    "sLast": "末页",
                    "sFirst": "首页"
                }
            }
        });

        jQuery('#regionTable_wrapper .dataTables_filter input').addClass("m-wrap small"); // modify table search input
        jQuery('#regionTable_wrapper .dataTables_length select').addClass("m-wrap small"); // modify table per page dropdown
        jQuery('#regionTable_wrapper .dataTables_length select').select2(); // initialzie select2 dropdown
        jQuery('#regionTable_info').attr("style", "padding-top: 0px;");

        /* Add event listener for opening and closing details
         * Note that the indicator for showing which row is open is not controlled by DataTables,
         * rather it is done here
         */
        $('#regionTable').on('click', ' tbody td .row-details', function () {
            var nTr = $(this).parents('tr')[0];
            if ( oTable.fnIsOpen(nTr) )
            {
                /* This row is already open - close it */
                $(this).addClass("row-details-close").removeClass("row-details-open");
                oTable.fnClose( nTr );
            }
            else
            {
                /* Open this row */
                $(this).addClass("row-details-open").removeClass("row-details-close");
                oTable.fnOpen( nTr, fnFormatDetails(oTable, nTr), 'details' );
            }
        });
    }

    return {

        init: function (regionsArray) {

            regions = regionsArray;

            if (!jQuery().dataTable) {
                return;
            }

            initTable();
        },

        loadXform: function(processInstanceId) {
            _loadXform(processInstanceId);
        },

        loadTable: function() {
            $("#divForRegion").attr("style", "display:block;");
            $("#divForDetails").attr("style", "display:none;");
        },

        viewHistory: function(processInstanceId)  {
            window.open($('#context').val() + "/bpm/workspace-viewHistory.do?processInstanceId=" + processInstanceId);
        }

    };

}();