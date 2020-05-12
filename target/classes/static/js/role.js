$(function () {
    $('#table-roles').datagrid({
        url:ctxPath+"role",
        fitColumns:true,
        fit:true,
        border:false,
        striped:true,
        pagination:true,
        rownumbers:true,
        toolbar:"#div-toolbar",
        columns:[[
            {field:'id',title:'编号',width:100,align:'center',checkbox:true},
            {field:'roleName',title:'角色名称',width:100,align:'center'},
            {field:'roleDesc',title:'角色描述',width:100,align:'center'},
            {field:'fcd',title:'创建时间',width:100,align:'center',
            formatter:function(value,row,index){
            if(value){
                var d=new Date(value);
                var newDate=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
                return newDate;
            }else {
                return value;
            }}},
            {field:'lcd',title:'最后修改时间',width:100,align:'center',
                formatter:function(value,row,index){
                    if(value){
                        var d=new Date(value);
                        var newDate=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
                        return newDate;
                    }else {
                        return value;
                    }}}
        ]],
        loadFilter:function (data) {
            return data.data;
        }
    });
    //搜索
    $("#btn-search").click(function () {
        $('#table-roles').datagrid('load',{
           roleName:$("#roleName").val()
        });
    });
    $("#btn-add").click(function () {
        $('#div-edit').dialog("open");
        $('#div-edit').dialog("setTitle","新增角色");
    });
    $('#div-edit').dialog({
        title: '新增角色',
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
        $('#div-edit').dialog("close");
        $('#form-add').form('clear');
    });
    $("#btn-save").click(function () {
        if(!$('#form-add').form('validate')){
            return false;
        }
        $('#form-add').form('submit',{
            url:ctxPath+"role/save",
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
        });
    });
    $('#auths').combotree({
        url: ctxPath+"menu",
        required: true,
        onlyLeafCheck:true,
        multiple:true,
        loadFilter: function(data){
            return data.data;
        },
    });
    $("#btn-edit").click(function () {
        var rows=$("#table-roles").datagrid("getSelections");
        if(rows.length>1){
            $.messager.alert('警告',"只能选择一条记录",'warning');
            return false;
        }else if(rows.length==0){
            $.messager.alert('操作失败',"请选择一条记录",'warning');
            return false;
        }
        //自动展开树
        $('#auths').combotree({
            url: ctxPath+"menu",
            required: true,
            onlyLeafCheck:true,
            multiple:true,
            loadFilter: function(data){
                return data.data;
            },
            //自动展开树
            onLoadSuccess:function(node,data){
                var _this = this;
                if(data){
                    $(data).each(function(){
                        if(this.state == 'closed'){
                            $(_this).tree("expandAll");
                        }
                    });
                }

            }
        });
        $('#form-add').form('load',ctxPath+"role/"+rows[0].id);	// load from URL
        $('#div-edit').dialog("open");
        $('#div-edit').dialog("setTitle","修改角色");
    });
    $("#btn-remove").click(function () {
        var rows=$("#table-roles").datagrid("getSelections");
        if(rows.length==0){
            $.messager.alert('操作失败',"请至少选择一条记录",'warning');
            return false;
        }
        alert()
        var arrIds=new Array();
        $(rows).each(function () {
            arrIds.push(this.id);
        });
        //确认删除
        $.messager.confirm('删除确认', '你是否确认删除选定的数据', function(r){
            if (r){
                var url=ctxPath+"role/del";
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
        });

    })
})