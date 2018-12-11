// 【用户】页面用JS
var url;

// 页面加载
$(function () {
    $('#searchcondition').combobox({
        width: 180,
        editable: false,
        // 面板展开时触发
        onShowPanel: function () {
            $(this).combobox('panel').height("auto");
        }
    });

    $('#roleid').combobox({
        width: 180,
        editable: false,
        // 面板展开时触发
        onShowPanel: function () {
            $(this).combobox('panel').height("auto");
        }
    });

    $("#dg").datagrid({
        url: '/user/list',
        queryParams: {},
        title: "用户列表",
        rownumbers: true,
        fit: true,
        toolbar: "#tb",
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        pageList: [5, 10],
        columns: [[
            {field: 'userid', title: '人员编号', width: 50, align: 'center', halign: 'center', hidden: 'true'},
            {field: 'username', title: '账号', width: '25%', align: 'center', halign: 'center'},
            {field: 'password', title: '密码', width: '25%', align: 'center', halign: 'center'},
            {
                field: 'role',
                title: '角色名称',
                width: '25%',
                align: 'center',
                halign: 'center',
                formatter: function (value, row, index) {
                    if (value.rolename) {
                        return value.rolename;
                    } else {
                        return '';
                    }
                }
            },
            {
                field: 'operation', title: '操作', align: 'center', halign: 'center', width: '25%',
                formatter: function (val, row, index) {
                    var temp = '';

                    if ($("#hdupdate").css("display")) {
                        temp += '<a href="#" class="linkbutton-update" ' +
                            'onclick="openModifyDialog(' + index + ')"></a>' + '&nbsp;&nbsp;|&nbsp;&nbsp;';
                    }

                    if ($("#hddelete").css("display")) {
                        temp += '<a href="#" class="linkbutton-delete" ' +
                            'onclick="deleteAction(' + index + ')"></a>';
                    }

                    return temp;
                }
            }
        ]],
        onLoadSuccess: function () {
            // 表格中formatter数据后使用linkbutton
            $(".linkbutton-update").linkbutton({plain: true, iconCls: 'icon icon-edit'});
            $(".linkbutton-delete").linkbutton({plain: true, iconCls: 'icon icon-delete'});
        }
    });

    // 【类别名称】下拉框动态绑定
    $.ajax({
        type: "get",
        url: '/role/findAllRole',
        data: {},
        success: function (data) {
            var dataList = [];

            $("#roleid").empty();

            $.each(data.list, function (i, item) {
                dataList.push({"value": data.list[i].roleid, "text": data.list[i].rolename});
            });

            $("#roleid").combobox("loadData", dataList);
        }
    });
});

// 【加载全部】按钮押下处理
var loadAll = function () {
    // 查询条件还原为默认状态
    $('#searchcondition').combobox('setValue', 'username');
    $('#searchcontent').val("");

    // 表格重新加载
    var param = {};
    $("#dg").datagrid('load', param);
};

// 【搜索】按钮押下处理
var queryAction = function () {
    var param = {
        searchcondition: $('#searchcondition').combobox('getValue'),
        searchcontent: $('#searchcontent').val()
    };

    $("#dg").datagrid('load', param);
};

// 重置表单内容
var resetValue = function () {
    $('#fm')[0].reset();
};

// 打开添加对话框
var openAddDialog = function () {
    resetValue();
    $('#dlg').dialog({
        modal: true,
        title: '添加用户信息'
    });
    $('#dlg').dialog("open");
    url = "/user/save";
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

// 关闭对话框
var closeDialog = function () {
    $("#dlg").dialog("close");
    resetValue();
};

// 打开编辑对话框
var openModifyDialog = function (index) {
    // 根据索引获取表格中的行
    $('#dg').datagrid('selectRow', index);

    var row = $('#dg').datagrid('getSelected');

    $("#dlg").dialog("open").dialog("setTitle", "编辑用户信息");
    $("#fm").form("load", row);
    $('#roleid').combobox('setValue', row.role.roleid);

    url = "save?userid=" + row.userid;
};

// 删除处理
var deleteAction = function (index) {
    // 根据索引获取表格中的行
    $('#dg').datagrid('selectRow', index);

    var row = $('#dg').datagrid('getSelected');

    $.messager.confirm("系统提示", "您确定要删除这条数据吗？", function (r) {
        if (r) {
            $.post("delete", {
                userid: row.userid
            }, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "数据已成功删除！");
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "数据删除失败，请联系工作人员！");
                }
            }, "json");
        }
    });
};