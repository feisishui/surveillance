<%@page contentType="text/html;charset=UTF-8" %>

<%@include file="/taglibs.jsp" %>

<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->

<!--[if IE 10]> <html lang="en" class="ie10"> <![endif]-->

<!--[if IE 11]> <html lang="en" class="ie11"> <![endif]-->

<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<!-- BEGIN HEAD -->

<head>

    <meta charset="utf-8" />

    <title>四川省地下管线监督管理子系统</title>

    <meta content="width=device-width, initial-scale=1.0" name="viewport" />

    <meta content="" name="description" />

    <meta content="" name="author" />

    <!-- BEGIN GLOBAL MANDATORY STYLES -->

    <link href="${ctx}/s/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

    <link href="${ctx}/s/media/css/default.css" rel="stylesheet" type="text/css" id="style_color"/>

    <link href="${ctx}/s/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

    <%--<link type="text/css" rel="stylesheet" href="${ctx}/s/login/LoginCss.css" media="screen"/>--%>

    <!-- END GLOBAL MANDATORY STYLES -->

    <!-- BEGIN PAGE LEVEL STYLES -->

    <link href="${ctx}/s/media/css/login.css" rel="stylesheet" type="text/css"/>

    <!-- END PAGE LEVEL STYLES -->

</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="login"  >


<!-- BEGIN LOGO -->

<div class="logo">

    <%--<img src="${ctx}/s/media/image/logo-big.png" alt="" />--%>

</div>

<%--<div class="login_bg">--%>

    <%--<img src="s/login/images/login_bg.jpg"/>--%>

    <%--<div class="login_dw"></div>--%>

<%--</div>--%>

<!-- END LOGO -->

<!-- BEGIN LOGIN -->

<div class="content">
    <!-- BEGIN LOGIN FORM -->

    <form class="form-vertical login-form" action="" id="loginForm">

        <h3 class="form-title text-center" style="color:green">四川省地下管线监督管理子系统</h3>
        <div class="alert alert-error hide">

            <button class="close" data-dismiss="alert"></button>

            <span id="errorMessage"></span>

        </div>

        <div class="control-group">

            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->

            <label class="control-label visible-ie8 visible-ie9">用户名</label>

            <div class="controls">

                <div class="input-icon left">

                    <i class="icon-user"></i>

                    <input class="m-wrap placeholder-no-fix" type="text" placeholder="用户名" name="userName"/>

                </div>

            </div>

        </div>

        <div class="control-group">

            <label class="control-label visible-ie8 visible-ie9">密码</label>

            <div class="controls">

                <div class="input-icon left">

                    <i class="icon-lock"></i>

                    <input class="m-wrap placeholder-no-fix" type="password" placeholder="密码" name="password"/>

                </div>

            </div>

        </div>

        <div class="form-actions">

            <button type="submit" class="btn green pull-right">

                登录 <i class="m-icon-swapright m-icon-white"></i>

            </button>

            <button type="button" class="btn green pull-left"
                    onclick="window.location.href='${ctx}/visitor.do';">

                公众入口 <i class="m-icon-swapright m-icon-white"></i>

            </button>

        </div>

        <div class="form-actions">

            <h4 class="form-title " style="">快捷登录</h4>

            <button type="button" class="btn yellow pull-left "
                    onclick="fastLogin('张文举')">

                监督中心 <i class="m-icon-swapright m-icon-white"></i>

            </button>

            <button type="button" class="btn blue pull-right"
                    onclick="fastLogin('许娟')">

                指挥中心 <i class="m-icon-swapright m-icon-white"></i>

            </button>

            <button type="button" class="btn purple pull-left"
                    onclick="fastLogin('陈鑫')">

                巡查部门 <i class="m-icon-swapright m-icon-white"></i>

            </button>

            <button type="button" class="btn red pull-right"
                    onclick="fastLogin('黄可')">

                专业公司 <i class="m-icon-swapright m-icon-white"></i>

            </button>
        </div>

    </form>

    <!-- END LOGIN FORM -->

    <input type="hidden" id="context" value="${ctx}">

</div>

<!-- END LOGIN -->

<!-- BEGIN COPYRIGHT -->

<div class="copyright">

    2016 &copy;

</div>

<!-- END COPYRIGHT -->

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

<!-- BEGIN CORE PLUGINS -->

<script src="${ctx}/s/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

<script src="${ctx}/s/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/bootstrap.min.js" type="text/javascript"></script>

<!--[if lt IE 9]>

<script src="${ctx}/s/media/js/excanvas.min.js"></script>

<script src="${ctx}/s/media/js/respond.min.js"></script>

<![endif]-->

<script src="${ctx}/s/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.blockui.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.cookie.min.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/jquery.uniform.min.js" type="text/javascript" ></script>

<!-- END CORE PLUGINS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->

<script src="${ctx}/s/media/js/jquery.validate.min.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/s/media/js/additional-methods.min.js"></script>

<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->

<script src="${ctx}/s/media/js/app.js" type="text/javascript"></script>

<script src="${ctx}/s/media/js/login.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->

<script>

    var fastLogin = function (userName) {

        $.ajax({

            type: "POST",

            url: $('#context').val()+"/user/fast-login.do",

            data: {
                userName:userName
            },

            success: function (data) {

                var jData = eval(data);

                if (jData.success == true) {

                    window.location.href = $('#context').val()+"/portal/index.do";

                }
                else {
                    $("#errorMessage").html(jData.message);
                }
            },

            error: function (request) {
                $("#errorMessage").html("连接服务器失败");
            }
        });

    };

    jQuery(document).ready(function() {

        App.init();

        Login.init();

/*
        $('#loginForm').css('opacity', '0.3');
*/

    });

</script>

<!-- END JAVASCRIPTS -->

<!-- END BODY -->
</body>

</html>