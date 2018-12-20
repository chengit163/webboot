var systemUserContent;
var systemUserWidget;
var systemUserDialog;
var systemUserTable;
var systemUserLabel;
var systemUserDialogs;
var systemUserExcel;
var systemUserSort;
var systemUserOrder;

$(function () {
    systemUserContent = $('#system_user_content');
    systemUserWidget = $('#system_user_widget');
    systemUserDialog = $('#system_user_dialog');
    systemUserTable = $('#system_user_table');
    systemUserLabel = systemUserContent.find('input[name="hidden_label"]').val();
    // 加载数据
    systemUserLoad();
    //显示隐藏查询
    $('#system_user_query_switch').change(function () {
        if ($(this).is(":checked")) {
            $('#system_user_query_box').trigger('show.ace.widget').widget_box('show');
        } else {
            $('#system_user_query_box').trigger('hide.ace.widget').widget_box('hide');
        }
    });
    // 重置
    $('#system_user_reset').bind("click", function () {
        $('#system_user_form').emptyForm();
        systemUserReload();
    });
    // 搜索
    $('#system_user_search').bind("click", function () {
        $('#system_user_form').trimForm();
        systemUserReload();
    });
    // 对话框显示
    systemUserDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    systemUserDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });

    // Excel
    systemUserDialogs = $('#system_user_dialogs');
    systemUserExcel = $('#system_user_excel_form');

    if (systemUserExcel.length > 0) {
        systemUserExcel.bootstrapValidator({
            feedbackIcons: {
                valid: 'ace-icon fa fa-check-circle',
                invalid: 'ace-icon fa fa-times-circle',
                validating: 'ace-icon fa fa-info-circle'
            },
            fields: {
                file: {
                    validators: {
                        notEmpty: {message: '请选择一个0~10M的Excel文件(.xlsx)'},
                        file: {
                            extension: 'xlsx',
                            minSize: 1,
                            maxSize: 10 * 1024 * 1024,
                            message: '请选择一个0~10M的Excel文件(.xlsx)'
                        }
                    }
                }
            }
        });
    }

    systemUserDialogs.on('hide.bs.modal', function () {
        if (systemUserExcel.length > 0) {
            systemUserExcel.emptyForm();
            systemUserExcel.data("bootstrapValidator").resetForm();
        }
        $(this).find('.modal-content').addClass('hidden');
    });

});

function systemUserFix() {
    // refresh, toggle, columns 样式冲突修改
    systemUserWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    systemUserWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    systemUserWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    systemUserWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = systemUserWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    systemUserTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function systemUserLoad() {
    var myGet = systemUserContent.find('input[name="hidden_get"]').val();
    var myUpdate = systemUserContent.find('input[name="hidden_update"]').val();
    var myRemove = systemUserContent.find('input[name="hidden_remove"]').val();
    var myGetPermission = systemUserContent.find('input[name="hidden_getPermission"]').val();
    var myPassword = systemUserContent.find('input[name="hidden_password"]').val();
    var myData = {};
    //
    systemUserTable.bootstrapTable({
        url: '/system/user',
        method: 'GET',
        dataType: 'json',
        sidePagination: 'server',
        cache: false,
        pagination: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [5, 10, 25, 50, 100],
        clickToSelect: false,
        striped: true,
        showRefresh: true,
        showToggle: true,
        showColumns: true,
        toolbar: '#system_user_toolbar',
        uniqueId: 'id',
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        // 传递参数（*）
        queryParams: function (params) {
            systemUserWidget.widgetBoxShowLoading();
            systemUserFix();
            systemUserSort = params.sort;
            systemUserOrder = params.order;
            var query = {
                'sort': params.sort,
                'order': params.order,
                'pageSize': params.limit,
                'pageNum': (params.offset / params.limit) + 1,
            };
            $($('#system_user_form').serializeArray()).each(function () {
                if ($.trim(this.value) != '')
                    query[this.name] = $.trim(this.value);
            });
            return query;
        },
        onLoadSuccess: function (data) {
            systemUserWidget.widgetBoxHideLoading();
            systemUserFix();
        },
        onLoadError: function (status) {
            systemUserWidget.widgetBoxHideLoading();
            systemUserFix();
        },
        onPostBody: function (cardView) {
            systemUserFix();
        },
        onPageChange: function () {
            systemUserFix();
        },
        columns: [
            {
                checkbox: true,
                align: 'center'
            },
            {
                field: 'username',
                align: 'center',
                sortable: true,
            },
            {
                field: 'nickname',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if ($.trim(v).length < 0 || $.trim(v).length > 50) {return '长度必须在0~50之间'}
                    }
                },
            },
            {
                field: 'phone',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if (!/^1[3|4|5|7|8]\d{9}$/.test(v)) {return '格式不合法';}
                    }
                },
            },
            {
                field: 'email',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if ($.trim(v).length < 0 || $.trim(v).length > 150) {return '长度必须在0~150之间'}
                        if (!/^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\.)+[A-Za-z]{2,4}$/.test(v)) {return '格式不合法';}
                    }
                },
            },
            {
                field: 'lastLoginTime',
                align: 'center',
                sortable: true,
                visible: false,
            },
            {
                field: 'lastLoginIp',
                align: 'center',
                sortable: true,
                visible: false,
            },
            {
                field: 'userIdCreate',
                align: 'center',
                sortable: true,
                visible: false,
            },
            {
                field: 'status',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'select',
                    emptytext: '-',
                    source: function () {
                        var options = [];
                        options.push({value: 0, text: '锁定'});
                        options.push({value: 1, text: '可用'});
                        return options;
                    }
                },
                formatter: myUpdate != undefined ? null : function (value, row, index) {
                    var html = [];
                    switch (value)
                    {
                        case 0:
                            html.push('锁定');
                            break;
                        case 1:
                            html.push('可用');
                            break;
                    }
                    return html.join('');
                }
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
                    if (myPassword != undefined) {
                        html.push('<button href="/system/user/password/' + value + '" class="btn btn-xs btn-primary" data-toggle="modal" data-target="#system_user_dialog" title="' + myPassword + '">');
                        html.push('<i class="ace-icon fa fa-key"></i>');
                        html.push('</button>');
                    }
                    if (myGetPermission != undefined) {
                        html.push('<button href="/system/rabc/user-role/' + value + '" class="btn btn-xs btn-purple" data-toggle="modal" data-target="#system_user_dialog" title="' + myGetPermission + '">');
                        html.push('<i class="ace-icon fa fa-users"></i>');
                        html.push('</button>');
                    }
                    if (myGet != undefined) {
                        html.push('<button href="/system/user/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#system_user_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<button href="/system/user/edit/' + value + '" class="btn btn-xs btn-info" data-toggle="modal" data-target="#system_user_dialog" title="' + myUpdate + '">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</button>');
                    }
                    if (myRemove != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-danger" onclick="systemUserRemove(' + value + ')" title="' + myRemove + '">');
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
                    if (myPassword != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/user/password/' + value + '" class="" data-toggle="modal" data-target="#system_user_dialog" title="' + myPassword + '">');
                        html.push('<span class="blue2">');
                        html.push('<i class="ace-icon fa fa-key"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myGetPermission != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/rabc/user-role/' + value + '" class="" data-toggle="modal" data-target="#system_user_dialog" title="' + myGetPermission + '">');
                        html.push('<span class="purple2">');
                        html.push('<i class="ace-icon fa fa-users"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myGet != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/user/view/' + value + '" class="" data-toggle="modal" data-target="#system_user_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/user/edit/' + value + '" class="" data-toggle="modal" data-target="#system_user_dialog" title="' + myUpdate + '">');
                        html.push('<span class="light-blue">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRemove != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="systemUserRemove(' + value + ')" title="' + myRemove + '">');
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
            var title = systemUserTable.find('thead').find('th[data-field="' + field + '"]').find('div.th-inner').find('span').text();
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
                    url: '/system/user',
                    type: 'PUT',
                    data: data,
                    success: function (json) {
                        var index = systemUserTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            if (json.code == 0) {
                                systemUserTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemUserTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditSuccess();
                            } else {
                                data[field] = myData[field + row['id']];
                                systemUserTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemUserTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditFailed();
                            }
                        }
                        delete myData[field + row['id']];
                    },
                    error: function (error) {
                        var index = systemUserTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            data[field] = myData[field + row['id']];
                            systemUserTable.bootstrapTable('updateRow', {index: index, row: data});
                            var $e = systemUserTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
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

function systemUserReload() {
    systemUserTable.bootstrapTable('refresh');
}

function systemUserRemove(id) {
    var message = '确认要删除该[' + systemUserLabel + ']记录？';
    bootbox.confirm(message, function (result) {
        if (result) {
            systemUserWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/system/user',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    systemUserWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        systemUserTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    systemUserWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}

function systemUserBatchRemove() {
    var rows = systemUserTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        var message = '确认要删除选择的' + rows.length + '条[' + systemUserLabel + ']记录？';
        bootbox.confirm(message, function (result) {
            if (result) {
                var ids = new Array();
                $.each(rows, function (i, row) {
                    ids[i] = row['id'];
                });
                systemUserWidget.widgetBoxShowLoading();
                $.ajax({
                    url: '/system/user/batchRemove',
                    type: 'POST',
                    data: {'ids': ids},
                    success: function (json) {
                        systemUserWidget.widgetBoxHideLoading();
                        if (json.code == 0) {
                            for (var i = 0; i < ids.length; i++) {
                                systemUserTable.bootstrapTable('removeByUniqueId',  ids[i]);
                            }
                            toastr.success(json.msg);
                        }
                        else {
                            toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                        }
                    }, error: function () {
                        systemUserWidget.widgetBoxHideLoading();
                        toastr.error('Server Error');
                    }
                });
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}

function systemUserExport() {
    systemUserDialogs.find('div[name="export"]').removeClass('hidden');
    systemUserDialogs.modal('show');
}

function systemUserExportExe() {
    systemUserDialogs.modal('hide');
    var query = [];
    $($('#system_user_form').serializeArray()).each(function () {
        if ($.trim(this.value) != '')
            query.push(this.name + '=' + $.trim(this.value) + '&')
    });
    if (systemUserSort != undefined) {
        query.push('sort=' + systemUserSort + '&')
    }
    if (systemUserOrder != undefined) {
        query.push('order=' + systemUserOrder + '&')
    }
    query.push('_=' + new Date().getTime());
    var url = '/system/user/export?' + query.join('');
    window.open(encodeURI(url));
}

function systemUserImport() {
    systemUserDialogs.find('div[name="import"]').removeClass('hidden');
    systemUserDialogs.modal('show');
}

function systemUserImportExe() {
    if (systemUserExcel.length > 0) {
        systemUserExcel.data('bootstrapValidator').validate();
        if (systemUserExcel.data('bootstrapValidator').isValid()) {
            systemUserUpload();
        }
    }
}

function systemUserUpload() {
    // 隐藏之前做一次reset
    systemUserExcel.data("bootstrapValidator").resetForm();
    var dialogs = systemUserDialogs;
    dialogs.find('.modal-content').addClass('hidden');
    dialogs.find('div[name="doing"]').removeClass('hidden');
    //
    dialogs.find('button[name="close"]').addClass('hidden');
    dialogs.find('#tips').text('');
    //
    dialogs.find('div[name="waiting"]').removeClass('hidden');
    dialogs.find('div[name="success"]').addClass('hidden');
    dialogs.find('div[name="failed"]').addClass('hidden');
    //
    var excel = systemUserExcel.find('input[name="file"]');
    var data = new FormData();
    data.append("file", excel.get(0).files[0]);
    $.ajax({
        type: 'POST',
        url: '/system/user/import',
        data: data,
        dataType: 'json',
        processData: false, //必须false才会自动加上正确的Content-Type
        contentType: false,//必须false才会避开jQuery对 formdata 的默认处理
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if (systemUserProgress && xhr.upload) {
                xhr.upload.addEventListener("progress", systemUserProgress, false);
                return xhr;
            }
        }, success: function (json) {
            dialogs.find('button[name="close"]').removeClass('hidden');
            dialogs.find('div[name="waiting"]').addClass('hidden');
            if (json.code == 0) {
                dialogs.find('div[name="success"]').removeClass('hidden');
                dialogs.find('div[name="failed"]').addClass('hidden');
            } else {
                dialogs.find('div[name="success"]').addClass('hidden');
                dialogs.find('div[name="failed"]').removeClass('hidden');
            }
            dialogs.find('#tips').text(json.msg);
        }, error: function (error) {
            dialogs.find('button[name="close"]').removeClass('hidden');
            dialogs.find('div[name="waiting"]').addClass('hidden');
            dialogs.find('div[name="success"]').addClass('hidden');
            dialogs.find('div[name="failed"]').removeClass('hidden');
            dialogs.find('#tips').text('服务器异常');
        }
    });
}

function systemUserProgress(evt) {
    var loaded = evt.loaded;
    var total = evt.total;
    if (loaded == total) {
        systemUserDialogs.find('#tips').text('正在保存...');
    } else {
        var percent = Math.floor(100 * loaded / total);
        systemUserDialogs.find('#tips').text('正在上传[' + percent + '%]');
    }
}