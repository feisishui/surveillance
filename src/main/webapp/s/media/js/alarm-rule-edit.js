var AlarmRuleEdit = function () {

    var validateForm = function(){
        var highValue = $('#highValue').val();
        var lowValue = $('#lowValue').val();
        var msg = "";

        if(highValue!=""&&!lowValue!="")
        {
            if(highValue<=lowValue){
                msg = '报警上限必须大于下限！';
            }
        }
        return msg;
    }

    return {

        initForms:function(){

            var form = $('#submit_form');
            var error = $('.alert-error', form);
            var success = $('.alert-success', form);

            $('#cancelBtn').live('click', function (e) {

                location.href = $("#context").val() + "/content/alarm/alarm-rule-list.jsp";

            });

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    highValue: {
                        required: true,
                        number: true
                    },
                    lowValue: {
                        required: true,
                        number: true
                    },
                    saltation: {
                        required: true,
                        number: true
                    }
                    ,
                    overTime: {
                        required: true,
                        number: true
                    }
                },
                messages: { // custom messages for radio buttons and checkboxes
                    highValue: {
                        required: "报警上限必须填写！",
                        number : "报警上限必须为数字！"
                    },
                    lowValue:{
                        required: "报警下限必须填写！",
                        number : "报警下限必须为数字！"
                    },
                    saltation:{
                        required: "突变范围必须填写！",
                        number : "突变范围必须为数字！"
                    },
                    overTime:{
                        required: "超时范围必须填写！",
                        number : "超时范围必须为数字！"
                    }
                },
                errorPlacement: function (error, element) { // render error placement for each input type

                    error.insertAfter(element); // for other inputs, just perform default behavoir

                },

                invalidHandler: function (event, validator) { //display error alert on form submit
                    success.hide();
                    $("#errorMessage").html(validateForm());
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
                    var checkmsg = validateForm();
                    if(checkmsg!='')
                    {
                        $("#errorMessage").html(validateForm());
                        error.show();
                        App.scrollTo(error, -200);
                        return;
                    }
                    $.ajax( {
                        type: "POST",
                        url: $('#context').val()+"/alarm/alarm-rule-save.do",
                        //  dataType:'json',
                        //TODO LIST：按条件查询服务器数据
                        //   data: JSON.stringify($('#submit_form').serialize()),
                        data: $('#submit_form').serialize(),
                        success: function(data) {

                            var jData = eval(data);
                            if(jData.success==true) {
                                location.href = $("#context").val() + "/content/alarm/alarm-rule-list.jsp";
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
        }

    };

}();