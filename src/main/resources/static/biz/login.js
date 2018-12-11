// 按键事件处理
document.onkeydown = function (e) {
    var event = e || window.event;
    var code = event.keyCode || event.which || event.charCode;
    if (code == 13) {
        login();
    }
};

// 页面初始化
$(function () {
    // 账号文本框获得焦点
    $('#username').textbox('textbox').focus();
});

// 登录处理
var login = function () {
    if ($("input[name='username']").val() == "") {
        $.messager.alert('警告', '请输入帐号！');
        $("input[name='username']").focus();
    } else if ($("input[name='password']").val() == "") {
        $.messager.alert('警告', '请输入密码！');
        $("input[name='password']").focus();
    } else {
        //ajax异步提交
        $.ajax({
            type: "POST",
            url: "/dologin",
            data: $("#loginForm").serialize(),  // 序列化
            success: function (r) {
                if (r.flag == 'ok') {
                    location.href = 'index';
                } else {
                    $.messager.alert('警告', '登录失败！');
                }
            }
        });
    }
};