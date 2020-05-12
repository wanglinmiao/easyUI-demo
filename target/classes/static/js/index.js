$(function(){
    $('#ul-menu').tree({
        url:ctxPath+"menu",
        animate:true,
        lines:true,
        loadFilter: function(data){
            return data.data;
        },
        onClick:function (node) {
            //判断标签是否存在
           var flag= $('#div-tab').tabs('exists',node.text);
            if(flag){
                $('#div-tab').tabs('select',node.text);
            }else {
                $('#div-tab').tabs('add', {
                    title: node.text,
                    selected: true,
                    closable: true,
                    content:'<iframe scrolling="auto" frameborder="0" src="' + node.url + '" style="width:100%;height:100%;display:block;"></iframe>'
                });
            }
        }
    });
    $('#div-tab').tabs({
        fit:true,
        border:false
    })
});