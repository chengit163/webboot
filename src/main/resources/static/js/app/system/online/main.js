var systemOnlineContent;
var systemOnlineWidget;
var systemOnlineDialog;
var systemOnlineTable;
var systemOnlineLabel;

$(function () {
    systemOnlineContent = $('#system_online_content');
    systemOnlineWidget = $('#system_online_widget');
    systemOnlineDialog = $('#system_online_dialog');
    systemOnlineTable = $('#system_online_table');
    systemOnlineLabel = systemOnlineContent.find('input[name="hidden_label"]').val();
    // 加载数据
    systemOnlineLoad();
    // 对话框显示
    systemOnlineDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    systemOnlineDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });
});

function systemOnlineFix() {
    // refresh, toggle, columns 样式冲突修改
    systemOnlineWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    systemOnlineWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    systemOnlineWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    systemOnlineWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = systemOnlineWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    systemOnlineTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function systemOnlineLoad() {
    var systemUserGet = systemOnlineContent.find('input[name="hidden_system_user_get"]').val();
    var myRemove = systemOnlineContent.find('input[name="hidden_remove"]').val();
    //
    systemOnlineTable.bootstrapTable({
        url: '/system/online',
        method: 'GET',
        dataType: 'json',
        // sidePagination: 'server',
        cache: false,
        // pagination: true,
        // pageNumber: 1,
        // pageSize: 100,
        // pageList: [5, 10, 25, 50, 100],
        clickToSelect: false,
        striped: true,
        showRefresh: true,
        showToggle: true,
        showColumns: true,
        toolbar: '#system_online_toolbar',
        uniqueId: 'id',
        idField: 'id',
        // 传递参数（*）
        queryParams: function (params) {
            systemOnlineWidget.widgetBoxShowLoading();
            systemOnlineFix();
            var query = {
            };
            return query;
        },
        onLoadSuccess: function (data) {
            systemOnlineWidget.widgetBoxHideLoading();
            systemOnlineFix();
            //
            var date = new Date(data.time);
            var dateStr = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' +
                date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            $('#system_online_info').text(dateStr + ' 当前在线数: ' + data.total);
        },
        onLoadError: function (status) {
            systemOnlineWidget.widgetBoxHideLoading();
            systemOnlineFix();
            //
            $('#system_online_info').text('');
        },
        onPostBody: function (cardView) {
            systemOnlineFix();
        },
        onPageChange: function () {
            systemOnlineFix();
        },
        columns: [
            {
                checkbox: true,
                align: 'center'
            },
            {
                field: 'principals.username',
                align: 'center',
                formatter: function (value, row, index) {
                    var html = [];
                    if (systemUserGet != undefined)
                    {
                        html.push('<a href="/system/user/view/' + row.principals.id + '" class="green" data-toggle="modal" data-target="#system_online_dialog">');
                        html.push(value);
                        html.push('</a>');
                    } else {
                        html.push(value);
                    }
                    return html.join('');
                }
            },
            {
                field: 'principals.lastLoginTime',
                align: 'center',
            },
            {
                field: 'starttimestamp',
                align: 'center',
            },
            {
                field: 'lastaccesstime',
                align: 'center',
            },
            {
                field: 'timeout',
                align: 'center',
            },
            {
                field: 'host',
                align: 'center',
            },
            {
                field: 'os',
                align: 'center',
            },
            {
                field: 'browser',
                align: 'center',
            },
            {
                field: 'id',
                align: 'center',
                switchable: false,
                formatter: function (value, row, index) {
                    var html = [];
                    if (myRemove != undefined) {
                        html.push('<button class="btn btn-mini btn-danger" onclick="systemOnlineRemove(\'' + value + '\')">');
                        html.push('强制下线');
                        html.push('</button>');
                    }
                    return html.join('');
                }
            }
        ]
    });
}

function systemOnlineReload() {
    systemOnlineTable.bootstrapTable('refresh');
}

function systemOnlineRemove(id) {
    console.log(id);
    var message = '确认要强制该用户下线？';
    bootbox.confirm(message, function (result) {
        if (result) {
            systemOnlineWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/system/online',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    systemOnlineWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        systemOnlineTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    systemOnlineWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}