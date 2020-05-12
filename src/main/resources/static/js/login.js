$(function(){
    $('#div-login').dialog({
        title: '阿里巴后台管理系统',
        width: 400,
        closed: false,
        cache: false,
        modal: true, //遮罩效果
        iconCls:"icon-user1",
        buttons:"#div-tollbar"
    });
    $("input[name=username]").validatebox({
        required:true,
        missingMessage:"用户名不能为空"
    });
    $("input[name=psw]").validatebox({
        required:true,
        missingMessage:"密码不能为空",
        validType:["length[6,20]"]
    });
    $("#btn-clear").click(function () {
        $('#form-login').form('clear');
    })
    $("#btn-ok").click(function () {
        $('#form-login').form('validate');
        $('#form-login').form('submit', {
            url:ctxPath+"login",
            success: function(data){
                data=$.parseJSON(data);
                if (data.code==20001) {
                    $.messager.alert('登录失败',data.msg,'warning');
                }else if(data.code==20000){
                    window.location=ctxPath+"index";
                }else{
                    $.messager.alert('登录失败','未知错误,请联系管理员','error');
                }
            }
        });
    })
});
/*
$(function(){
    function currentTime() {
        var d = new Date(), str = '';
        str += d.getFullYear() + '年';
        str += d.getMonth() + 1 + '月';
        str += d.getDate() + '日';
        str += d.getHours() + '时';
        str += d.getMinutes() + '分';
        str += d.getSeconds() + '秒';
        return str;
    }
    setInterval(function() {$('#time').html(currentTime)}, 1000);
});*/
