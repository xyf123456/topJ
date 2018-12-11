var url;

$(function () {
    // 初始化accordion
    $("#nav").accordion({
        fillSpace: true,
        fit: true,
        border: false,
        animate: false
    });

    // 加载左侧Accordion菜单
    $.ajax({
        type: "GET",
        url: '/loadResource',
        data: {resourcepid: 0},
        dataType: "json",
        success: function (result) {
            // 1、删除左侧Accordion菜单的内容
            var panel = $("#nav").accordion("panels");
            var titles = '';
            if (panel) {
                $.each(panel, function (i) {
                    var title = panel[i].panel("options").title;
                    titles += title + ',';
                })
            }
            var arr_title = titles.split(",");
            for (var i = 0; i < arr_title.length; i++) {
                if (arr_title[i] != "") {
                    $('#nav').accordion("remove", arr_title[i]);
                }
            }

            // 2、重新添加左侧Accordion菜单的内容
            $.each(result, function (i, n) {
                var content = '';
                content += '<div style="padding:10px"><ul name="' + n.resourcename + '">';
                $.each(n.children, function (index, m) {
                    content += '<li><a href="#" onclick="addTab(\'' + m.resourcename + '\', \'' + m.resourceurl + '\', \'icon ' + m.resourceicon + '\')">'
                        + '<span class="icon ' + m.resourceicon + '">&nbsp;</span>&nbsp;' + m.resourcename + '</a></li>';
                });
                content += '</ul></div>';

                $('#nav').accordion('add', {
                    title: n.resourcename,
                    selected: false,
                    content: content
                });
            });

            // 3、默认选择第一个
            $('#nav').accordion('select', 0);
        }
    });

    // 监听右键事件，创建右键菜单
    $('#tabs').tabs({
        onContextMenu: function (e, title, index) {
            e.preventDefault();
            if (index > 0) {
                $('#menu').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data("tabTitle", title);
            }
        }
    });

    // 右键菜单click
    $("#menu").menu({
        onClick: function (item) {
            closeTab(this, item.name);
        }
    });
});

var closeTab = function (menu, type) {
    var allTabs = $("#tabs").tabs('tabs');
    var allTabtitle = [];
    $.each(allTabs, function (i, n) {
        var opt = $(n).panel('options');
        if (opt.closable) {
            allTabtitle.push(opt.title);
        }
    });
    var curTabTitle = $(menu).data("tabTitle");
    var curTabIndex = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getTab", curTabTitle));
    switch (type) {
        case 1: // 刷新当前标签页
            var panel = $("#tabs").tabs("getTab", curTabTitle).panel("refresh");
            break;
        case 2: // 关闭当前标签页
            $("#tabs").tabs("close", curTabIndex);
            return false;
            break;
        case 3: // 关闭全部标签页
            for (var i = 0; i < allTabtitle.length; i++) {
                $('#tabs').tabs('close', allTabtitle[i]);
            }
            break;
        case 4: // 关闭其他标签页
            for (var i = 0; i < allTabtitle.length; i++) {
                if (curTabTitle != allTabtitle[i])
                    $('#tabs').tabs('close', allTabtitle[i]);
            }
            $('#tabs').tabs('select', curTabTitle);
            break;
        case 5: // 关闭右侧标签页
            for (var i = curTabIndex; i < allTabtitle.length; i++) {
                $('#tabs').tabs('close', allTabtitle[i]);
            }
            $('#tabs').tabs('select', curTabTitle);
            break;
        case 6: // 关闭左侧标签页
            for (var i = 0; i < curTabIndex - 1; i++) {
                $('#tabs').tabs('close', allTabtitle[i]);
            }
            $('#tabs').tabs('select', curTabTitle);
            break;
        default:
            break;
    }
};

var logout = function () {
    $.messager.confirm("系统提示", "您确定要退出系统吗？", function (result) {
        if (result) {
            window.location.href = '/logout';
        }
    });
};