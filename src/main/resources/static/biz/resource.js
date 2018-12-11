// 【资源】页面用JS
var url;

// 页面加载
$(function () {
    $("#dg").treegrid({
        url: "/resource/list",
        method: 'get',
        title: '资源列表',
        idField: 'resourceid',
        treeField: 'resourcename',
        rownumbers: true,
        animate: true,
        fitColumns: true,
        resizable: true,
        columns: [[
            {title: '资源名称', field: 'resourcename', width: '30%', align: 'left', halign: 'center'},
            {title: '资源图标', field: 'resourceicon', width: '30%', align: 'center', halign: 'center'},
            {title: '资源URL', field: 'resourceurl', width: '40%', align: 'left', halign: 'center'}
        ]]
    });
});