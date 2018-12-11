// 【角色】页面用JS
var url;
var rowIndex;

// 页面加载
$(function () {
    // 右侧树加载
    $('#resourceTree').tree({
        url: '/resource/tree',
        animate: true,
        lines: true,
        checkbox: true
    });

    // 左侧表格加载
    $("#dg").datagrid({
        url: '/role/list',
        queryParams: {},
        title: "角色列表",
        rownumbers: true,
        fit: true,
        toolbar: "#tb",
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        pageList: [5, 10],
        columns: [[
            {field: 'roleid', title: '角色编号', width: 50, align: 'center', halign: 'center', hidden: 'true'},
            {field: 'rolename', title: '角色名称', width: '50%', align: 'center', halign: 'center'},
            {
                field: 'operation', title: '操作', align: 'center', halign: 'center', width: '50%',
                formatter: function (val, row, index) {
                    var temp = '';

                    if ($("#hdupdate").css("display")) {
                        temp += '<a href="#" class="linkbutton-update" ' +
                            'onclick="openModifyDialog(' + index + ')"></a>' + '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    }

                    if ($("#hddelete").css("display")) {
                        temp += '<a href="#" class="linkbutton-delete" ' +
                            'onclick="deleteAction(' + index + ')"></a>' + '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    }

                    if ($("#hdconfig").css("display")) {
                        temp += '<a href="#" class="linkbutton-config" ' +
                            'onclick="relResources(' + index + ')"></a>';
                    }

                    return temp;
                }
            }
        ]],
        onClickRow: function (index, row) {
            setResources(row);
        },
        onLoadSuccess: function () {
            // 表格中formatter数据后使用linkbutton
            $(".linkbutton-update").linkbutton({plain: true, iconCls: 'icon icon-edit'});
            $(".linkbutton-delete").linkbutton({plain: true, iconCls: 'icon icon-delete'});
            $(".linkbutton-config").linkbutton({plain: true, iconCls: 'icon icon-config'});

            $('#dg').datagrid('selectRow', 0);
            // 获取选中的数据
            var row = $('#dg').datagrid("getSelected");
            setResources(row);
        }
    });
});

// 设置资源树
var setResources = function (row) {
    $.ajax({
        type: "POST",
        url: '/role/resource/match',
        data: {roleid: row.roleid},
        dataType: "json",
        success: function (result) {
            // 判定为数组
            if (result instanceof Array) {
                // 回显数据全部清除
                var rootNodes = $("#resourceTree").tree('getRoots');
                for (var i = 0; i < rootNodes.length; i++) {
                    var node = $("#resourceTree").tree('find', rootNodes[i].id);
                    $("#resourceTree").tree('uncheck', node.target);
                }

                // Tree重新勾选
                var length = result.length;
                for (var i = 0; i < length; i++) {
                    var node = $('#resourceTree').tree('find', result[i]);
                    if ($('#resourceTree').tree('isLeaf', node.target)) {
                        // 只绑定叶子节点，父节点都根据叶子节点的状态同步变化
                        $('#resourceTree').tree('check', node.target);
                    }
                }
            } else {
                $.messager.alert("系统提示", "模块加载失败，请联系工作人员！");
            }
        }
    });
};

// 关联资源树
var relResources = function (index) {
    $('#dg').datagrid('selectRow', index);

    var row = $('#dg').datagrid('getSelected');
    var selectArr = getTreeSelected($('#resourceTree'));
    var resourceids = [];
    for (var i = 0; i < selectArr.length; i++) {
        resourceids[i] = selectArr[i].id;
    }

    parent.$.messager.confirm({
        title: '关联资源',
        msg: '确定要进行数据关联吗？',
        draggable: false,
        fn: function (boolean) {
            if (boolean) {
                $.ajax({
                    type: "POST",
                    url: '/role/relResources',
                    data: {resourceids: resourceids.toString(), roleid: row.roleid},
                    dataType: "json",
                    success: function (result) {
                        if (result) {
                            $.messager.alert("系统提示", "关联成功！");
                            // 回显数据全部清除
                            var rootNodes = $("#resourceTree").tree('getRoots');
                            for (var i = 0; i < rootNodes.length; i++) {
                                var node = $("#resourceTree").tree('find', rootNodes[i].id);
                                $("#resourceTree").tree('uncheck', node.target);
                            }

                            // Tree重新勾选
                            var length = resourceids.length;
                            for (var i = 0; i < length; i++) {
                                var node = $('#resourceTree').tree('find', resourceids[i]);
                                if ($('#resourceTree').tree('isLeaf', node.target)) {
                                    // 只绑定叶子节点，父节点都根据叶子节点的状态同步变化
                                    $('#resourceTree').tree('check', node.target);
                                }
                            }
                        } else {
                            $.messager.alert("系统提示", "关联失败！");
                        }
                    }
                });
            }
        }
    });
};

// 获取树所选节点，以数据数组
var getTreeSelected = function (targetObj) {
    var nodes = targetObj.tree('getChecked');
    var arr = new Array();
    for (var i = 0; i < nodes.length; i++) {
        arr.push(nodes[i]);
        myFuc(nodes[i]);
    }
    return arr;

    // 内部递归函数
    function myFuc(n) {
        var parent = targetObj.tree('getParent', n.target);
        if (parent == null) {
            return;
        }
        if (isExistItem(parent)) {
            return;
        }
        arr.push(parent);
        myFuc(parent);
    }

    // 验证节点是否已存在数组中
    function isExistItem(item) {
        var flag = false;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] == item) {
                flag = true;
                break;
            }
        }
        return flag;
    }
};

// 【加载全部】按钮押下处理
var loadAll = function () {
    // 查询条件还原为默认状态
    $('#searchcontent').val("");

    // 表格重新加载
    var param = {};
    $("#dg").datagrid('load', param);
};

// 【搜索】按钮押下处理
var queryAction = function () {
    var param = {
        searchcontent: $('#searchcontent').val()
    };

    $("#dg").datagrid('load', param);
};

// 重置角色新增弹框
var resetValue = function () {
    $('#fm').form('clear');
};

// 打开角色新增对话框
var openAddDialog = function () {
    rowIndex = null;
    resetValue();

    $('#dlg').dialog({
        modal: true,
        title: '添加角色信息'
    });
    $('#dlg').dialog("open");

    url = "/role/save";
};

// 打开角色修改对话框
var openModifyDialog = function () {
    var selectedRows = $("#dg").datagrid("getSelections");

    if (selectedRows.length == 0) {
        $.messager.alert("系统提示", "请选择要修改的数据");
        return;
    }
    var row = selectedRows[0];
    rowIndex = $("#dg").datagrid('getRowIndex', selectedRows[0]);
    $('#dlg').dialog({
        modal: true,
        title: '编辑角色信息'
    });
    $('#dlg').dialog("open");
    $("#fm").form("load", row);

    url = "/role/save?roleid=" + row.roleid;
};

// 保存处理
var saveAction = function () {
    $("#fm").form("submit", {
        url: url,
        onSubmit: function () {
            return $(this).form("validate");
        },
        success: function (result) {
            var result = eval('(' + result + ')');
            if (result.flag == 'ok') {
                $.messager.alert("系统提示", "保存成功！");
                resetValue();
                $("#dlg").dialog("close");
                $("#dg").datagrid("reload");
            } else {
                $.messager.alert("系统提示", "保存失败！");
                return;
            }
        }
    });
};

// 删除
var deleteAction = function () {
    var selectedRows = $("#dg").datagrid("getSelections");

    if (selectedRows.length == 0) {
        $.messager.alert("系统提示", "请选择要删除的数据");
        return;
    }

    $.messager.confirm("系统提示", "您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？", function (r) {
        if (r) {
            $.post("/role/delete", {
                roleid: selectedRows[0].roleid
            }, function (result) {
                if (result.success == "true") {
                    $.messager.alert("系统提示", "数据已成功删除！");
                    $("#dg").datagrid("reload");
                } else if (result.success == "false") {
                    $.messager.alert("系统提示", "数据删除失败，请联系工作人员！");
                } else {
                    $.messager.alert("系统提示", result.success);
                }
            }, "json");
        }
    });
};