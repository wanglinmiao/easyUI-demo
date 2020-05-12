$(function () {
    $('#table-roles').datagrid({
        url:ctxPath+"user",
        fitColumns:true,
        fit:true,
        border:false,
        striped:true,
        pagination:true,
        rownumbers:true,
        toolbar:"#div-toolbar",
        columns:[[
            {field:'id',title:'编号',width:100,align:'center',checkbox:true},
            {field:'username',title:'用户名',width:100,align:'center'},
            {field:'psw',title:'密码',width:100,align:'center'},
        ]],
        loadFilter:function (data) {
            return data.data;
        }

    });
    $('#div-edit').dialog({
        title: '新增用户',
        width: 400,
        closed: true,
        cache: false,
        modal: true,
        buttons:"#div-buttons",
        //点红叉叉清空表单
        onClose:function () {
            $('#form-add').form('clear');
        }
    });
    $("#btn-close").click(function () {
        $('#form-add').form('clear');
        $('#div-edit').dialog("close");
    })
    $("#btn-add").click(function () {
        $("#div-edit").dialog("open");
    })
    $('#auths').combobox({
        url:ctxPath+"user/role",
        valueField:'id',
        textField:'roleName',
        editable:false,
        multiple:true,
        loadFilter: function(data){
            return data.data;
        },
    });
    $("#btn-save").click(function () {
       if(!$("#form-add").form("validate")){
           return false;
        }
       $("#form-add").form("submit",{
           url:ctxPath+"user/add",
           success: function (data) {
               data=$.parseJSON(data);
               if(data.code==20000){
                   $('#div-edit').dialog("close");
                   $('#form-add').form('clear');
                   $('#table-roles').datagrid("reload");
                   $.messager.show({
                       title:'操作成功',
                       msg:data.msg,
                       showType:'slide'
                   });
               }else{
                   $.messager.alert('操作失败',data.msg,'warning');
               }
           }
       })
    })
    $("#btn-edit").click(function () {
        var rows=$("#table-roles").datagrid("getSelections");
        if(rows.length>1){
            $.messager.alert('警告',"只能选择一条记录",'warning');
            return false;
        }else if(rows.length==0){
            $.messager.alert('警告',"最少选择一条记录",'warning');
            return false;
        }
        $('#form-add').form('load',ctxPath+"user/"+rows[0].id);	// load from URL
        $('#div-edit').dialog("open");
        $('#div-edit').dialog("setTitle","修改用户");

    })
    $("#btn-remove").click(function () {
        var rows=$("#table-roles").datagrid("getSelections");
        var arrIds=new Array();
        $(rows).each(function () {
            arrIds.push(this.id);
        });
        if(rows.length==0){
            $.messager.alert('警告',"最少选择一条记录",'warning');
            return false;
        }
        $.messager.confirm('删除确认', '你是否确认删除选定的数据', function(r){
            if(r){
                var url=ctxPath+"user/del";
                var param={
                    ids:arrIds.join(",")
                }
                $.post(url,param,function (data) {
                    $('#table-roles').datagrid("reload");
                    $.messager.show({
                        title:'删除成功',
                        msg:data.msg,
                        showType:'slide'
                    });
                });
            }
        })
    })
});