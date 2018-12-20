var systemMenuContent;
var systemMenuWidget;
var systemMenuDialog;
var systemMenuTable;
var systemMenuLabel;

$(function () {
    systemMenuContent = $('#system_menu_content');
    systemMenuWidget = $('#system_menu_widget');
    systemMenuDialog = $('#system_menu_dialog');
    systemMenuTable = $('#system_menu_table');
    systemMenuLabel = systemMenuContent.find('input[name="hidden_label"]').val();
    // 加载数据
    systemMenuLoad();
    // 对话框显示
    systemMenuDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    systemMenuDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });
});

function systemMenuFix() {
    // refresh, toggle, columns 样式冲突修改
    systemMenuWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    systemMenuWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    systemMenuWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    systemMenuWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    // checkbox居中
    systemMenuTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function systemMenuLoad() {
    var myGet = systemMenuContent.find('input[name="hidden_get"]').val();
    var myUpdate = systemMenuContent.find('input[name="hidden_update"]').val();
    var myRemove = systemMenuContent.find('input[name="hidden_remove"]').val();
    var mySave = systemMenuContent.find('input[name="hidden_save"]').val();
    var myData = {};
    var mySet = new Set();
    //
    systemMenuTable.bootstrapTable({
        url: '/system/menu/all',
        method: 'GET',
        dataType: 'json',
        sidePagination: 'server',
        cache: false,
        clickToSelect: false,
        striped: true,
        showRefresh: true,
        // showToggle: true,
        showColumns: true,
        toolbar: '#system_menu_toolbar',
        uniqueId: 'id',
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        parentIdField: 'pid',
        treeShowField: 'name',
        // 传递参数（*）
        queryParams: function (params) {
            systemMenuWidget.widgetBoxShowLoading();
            systemMenuFix();
            var query = {
                'sort': params.sort,
                'order': params.order,
            };
            return query;
        },
        onLoadSuccess: function (data) {
            systemMenuWidget.widgetBoxHideLoading();
            systemMenuFix();
        },
        onLoadError: function (status) {
            systemMenuWidget.widgetBoxHideLoading();
            systemMenuFix();
        },
        onPostBody: function (cardView) {
            systemMenuFix();
            systemMenuTable.treegrid({
                initialState: 'collapsed',
                treeColumn: 0,
                onChange: function() {
                    systemMenuTable.bootstrapTable('resetWidth');
                },
                onExpand:function () {
                    mySet.add($(this).attr('data-uniqueid'));
                },
                onCollapse: function () {
                    mySet.delete($(this).attr('data-uniqueid'));
                }
            });
            systemMenuTable.find('tbody tr').each(function () {
                if (mySet.has($(this).attr('data-uniqueid'))) {
                    $(this).treegrid('expand');
                }
            });
            systemMenuFormat();
        },
        onPageChange: function () {
            systemMenuFix();
        },
        columns: [
            // {
            //     checkbox: true,
            //     align: 'center'
            // },
            {
                field: 'name',
                sortable: true,
                switchable: false,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if (!$.trim(v)) {return '不能为空';}
                        if ($.trim(v).length < 1 || $.trim(v).length > 50) {return '长度必须在1~50之间'}
                    }
                },
            },
            {
                field: 'url',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if ($.trim(v).length < 0 || $.trim(v).length > 100) {return '长度必须在0~100之间'}
                    }
                },
            },
            {
                field: 'permission',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if ($.trim(v).length < 0 || $.trim(v).length > 100) {return '长度必须在0~100之间'}
                    }
                },
            },
            {
                field: 'orders',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if(isNaN(v)) {return '必须为数字';}
                    }
                },
            },
            {
                field: 'gmtCreate',
                align: 'center',
                sortable: true,
                visible: false,
            },
            {
                field: 'gmtModified',
                align: 'center',
                sortable: true,
                visible: false,
            },
            {
                field: 'id',
                align: 'center',
                sortable: true,
                switchable: false,
                formatter: function (value, row, index) {
                    var html = [];
                    html.push('<div class="hidden-sm hidden-xs btn-group">');
                    if (mySave != undefined && (row.type == 0 || row.type == 1)) {
                        html.push('<button href="/system/menu/add/' + value + '" class="btn btn-xs btn-purple" data-toggle="modal" data-target="#system_menu_dialog" title="' + mySave + '">');
                        html.push('<i class="ace-icon fa fa-plus"></i>');
                        html.push('</button>');
                    }
                    if (myGet != undefined) {
                        html.push('<button href="/system/menu/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#system_menu_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<button href="/system/menu/edit/' + value + '" class="btn btn-xs btn-info" data-toggle="modal" data-target="#system_menu_dialog" title="' + myUpdate + '">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</button>');
                    }
                    if (myRemove != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-danger" onclick="systemMenuRemove(' + value + ')" title="' + myRemove + '">');
                        html.push('<i class="ace-icon fa fa-trash"></i>');
                        html.push('</button>');
                    }
                    html.push('</div>');
                    //
                    html.push('<div class="hidden-md hidden-lg">');
                    html.push('<div class="inline pos-rel">');
                    html.push('<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">');
                    html.push('<i class="ace-icon fa fa-cog icon-only bigger-110"></i>');
                    html.push('</button>');
                    html.push('<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">');
                    if (mySave != undefined && (row.type == 0 || row.type == 1)) {
                        html.push('<li>');
                        html.push('<a href="/system/menu/add/' + value + '" class="" data-toggle="modal" data-target="#system_menu_dialog" title="' + mySave + '">');
                        html.push('<span class="purple2">');
                        html.push('<i class="ace-icon fa fa-plus"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myGet != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/menu/view/' + value + '" class="" data-toggle="modal" data-target="#system_menu_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/menu/edit/' + value + '" class="" data-toggle="modal" data-target="#system_menu_dialog" title="' + myUpdate + '">');
                        html.push('<span class="light-blue">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRemove != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="systemMenuRemove(' + value + ')" title="' + myRemove + '">');
                        html.push('<span class="light-red">');
                        html.push('<i class="ace-icon fa fa-trash"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    html.push('</ul>');
                    html.push('</div>');
                    html.push('</div>');
                    return html.join('');
                }
            }
        ],
        onEditableShown: function (field, row, $el, editable) {
            var title = systemMenuTable.find('thead').find('th[data-field="' + field + '"]').find('div.th-inner').find('span').text();
            if (title != undefined) {
                $el.next().find('.popover-title').css('display', 'block').html(title);
            }
            return false;
        },
        onEditableSave: function (field, row, oldValue, $el) {
            myData[field + row['id']] = oldValue;
            return false;
        },
        onEditableHidden: function(field, row, $el, reason) {
            if (reason == 'save') {
                var data = {};
                data['id'] = row['id'];
                data[field] = row[field];
                $el.tdEditLoading();
                $.ajax({
                    url: '/system/menu',
                    type: 'PUT',
                    data: data,
                    success: function (json) {
                        var index = systemMenuTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            if (json.code == 0) {
                                systemMenuTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemMenuTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditSuccess();
                            } else {
                                data[field] = myData[field + row['id']];
                                systemMenuTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemMenuTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditFailed();
                            }
                        }
                        delete myData[field + row['id']];
                    },
                    error: function (error) {
                        var index = systemMenuTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            data[field] = myData[field + row['id']];
                            systemMenuTable.bootstrapTable('updateRow', {index: index, row: data});
                            var $e = systemMenuTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                            $e.tdEditError();
                        }
                        delete myData[field + row['id']];
                    }
                });
            }
            return false;
        }
    });
}

function systemMenuReload() {
    systemMenuTable.bootstrapTable('refresh');
}

function systemMenuRemove(id) {
    var message = '确认要删除该[' + systemMenuLabel + ']记录？';
    bootbox.confirm(message, function (result) {
        if (result) {
            systemMenuWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/system/menu',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    systemMenuWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        systemMenuTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    systemMenuWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}

function systemMenuBatchRemove() {
    var rows = systemMenuTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        var message = '确认要删除选择的' + rows.length + '条[' + systemMenuLabel + ']记录？';
        bootbox.confirm(message, function (result) {
            if (result) {
                var ids = new Array();
                $.each(rows, function (i, row) {
                    ids[i] = row['id'];
                });
                systemMenuWidget.widgetBoxShowLoading();
                $.ajax({
                    url: '/system/menu/batchRemove',
                    type: 'POST',
                    data: {'ids': ids},
                    success: function (json) {
                        systemMenuWidget.widgetBoxHideLoading();
                        if (json.code == 0) {
                            for (var i = 0; i < ids.length; i++) {
                                systemMenuTable.bootstrapTable('removeByUniqueId',  ids[i]);
                            }
                            toastr.success(json.msg);
                        }
                        else {
                            toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                        }
                    }, error: function () {
                        systemMenuWidget.widgetBoxHideLoading();
                        toastr.error('Server Error');
                    }
                });
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}

function systemMenuFormat() {
    systemMenuTable.find('tbody a[data-name="name"]').each(function () {
        var id = $(this).attr('data-pk');
        var row = systemMenuTable.bootstrapTable('getRowByUniqueId', id);
        var $icon = $('<i/>').addClass('ace-icon ' + row.icon);
        var $span = $('<span/>');
        var type = parseInt(row.type);
        switch (type) {
            case -1:
                $span.addClass('label label-inverse arrowed pull-right').text('其他');
                break;
            case 0:
                $span.addClass('label arrowed pull-right').text('目录');
                break;
            case 1:
                $span.addClass('label label-success arrowed pull-right').text('菜单');
                break;
            case 2:
                $span.addClass('label label-info arrowed pull-right').text('按钮');
                break;
        }
        $(this).before($icon);
        $(this).after($span);
        $icon.before('&nbsp;').after('&nbsp;');
    });
}