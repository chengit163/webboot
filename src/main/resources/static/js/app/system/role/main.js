var systemRoleContent;
var systemRoleWidget;
var systemRoleDialog;
var systemRoleTable;
var systemRoleLabel;
var systemRoleDialogs;
var systemRoleExcel;
var systemRoleSort;
var systemRoleOrder;

$(function () {
    systemRoleContent = $('#system_role_content');
    systemRoleWidget = $('#system_role_widget');
    systemRoleDialog = $('#system_role_dialog');
    systemRoleTable = $('#system_role_table');
    systemRoleLabel = systemRoleContent.find('input[name="hidden_label"]').val();
    // 加载数据
    systemRoleLoad();
    //显示隐藏查询
    $('#system_role_query_switch').change(function () {
        if ($(this).is(":checked")) {
            $('#system_role_query_box').trigger('show.ace.widget').widget_box('show');
        } else {
            $('#system_role_query_box').trigger('hide.ace.widget').widget_box('hide');
        }
    });
    // 重置
    $('#system_role_reset').bind("click", function () {
        $('#system_role_form').emptyForm();
        systemRoleReload();
    });
    // 搜索
    $('#system_role_search').bind("click", function () {
        $('#system_role_form').trimForm();
        systemRoleReload();
    });
    // 对话框显示
    systemRoleDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    systemRoleDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });

    // Excel
    systemRoleDialogs = $('#system_role_dialogs');
    systemRoleExcel = $('#system_role_excel_form');

    if (systemRoleExcel.length > 0) {
        systemRoleExcel.bootstrapValidator({
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

    systemRoleDialogs.on('hide.bs.modal', function () {
        if (systemRoleExcel.length > 0) {
            systemRoleExcel.emptyForm();
            systemRoleExcel.data("bootstrapValidator").resetForm();
        }
        $(this).find('.modal-content').addClass('hidden');
    });

});

function systemRoleFix() {
    // refresh, toggle, columns 样式冲突修改
    systemRoleWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    systemRoleWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    systemRoleWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    systemRoleWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = systemRoleWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    systemRoleTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function systemRoleLoad() {
    var myGet = systemRoleContent.find('input[name="hidden_get"]').val();
    var myUpdate = systemRoleContent.find('input[name="hidden_update"]').val();
    var myRemove = systemRoleContent.find('input[name="hidden_remove"]').val();
    var myGetPermission = systemRoleContent.find('input[name="hidden_getPermission"]').val();
    var myData = {};
    //
    systemRoleTable.bootstrapTable({
        url: '/system/role',
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
        toolbar: '#system_role_toolbar',
        uniqueId: 'id',
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        // 传递参数（*）
        queryParams: function (params) {
            systemRoleWidget.widgetBoxShowLoading();
            systemRoleFix();
            systemRoleSort = params.sort;
            systemRoleOrder = params.order;
            var query = {
                'sort': params.sort,
                'order': params.order,
                'pageSize': params.limit,
                'pageNum': (params.offset / params.limit) + 1,
            };
            $($('#system_role_form').serializeArray()).each(function () {
                if ($.trim(this.value) != '')
                    query[this.name] = $.trim(this.value);
            });
            return query;
        },
        onLoadSuccess: function (data) {
            systemRoleWidget.widgetBoxHideLoading();
            systemRoleFix();
        },
        onLoadError: function (status) {
            systemRoleWidget.widgetBoxHideLoading();
            systemRoleFix();
        },
        onPostBody: function (cardView) {
            systemRoleFix();
        },
        onPageChange: function () {
            systemRoleFix();
        },
        columns: [
            {
                checkbox: true,
                align: 'center'
            },
            {
                field: 'name',
                align: 'center',
                sortable: true,
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
                field: 'description',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'textarea',
                    emptytext: '-',
                    validate: function (v) {
                        if ($.trim(v).length < 0 || $.trim(v).length > 250) {return '长度必须在0~250之间'}
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
                field: 'userIdCreate',
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
                    if (myGetPermission != undefined) {
                        html.push('<button href="/system/rabc/role-menu/' + value + '" class="btn btn-xs btn-purple" data-toggle="modal" data-target="#system_role_dialog" title="' + myGetPermission + '">');
                        html.push('<i class="ace-icon fa fa-umbrella"></i>');
                        html.push('</button>');
                    }
                    if (myGet != undefined) {
                        html.push('<button href="/system/role/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#system_role_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<button href="/system/role/edit/' + value + '" class="btn btn-xs btn-info" data-toggle="modal" data-target="#system_role_dialog" title="' + myUpdate + '">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</button>');
                    }
                    if (myRemove != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-danger" onclick="systemRoleRemove(' + value + ')" title="' + myRemove + '">');
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
                    if (myGetPermission != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/rabc/role-menu/' + value + '" class="" data-toggle="modal" data-target="#system_role_dialog" title="' + myGetPermission + '">');
                        html.push('<span class="purple2">');
                        html.push('<i class="ace-icon fa fa-umbrella"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myGet != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/role/view/' + value + '" class="" data-toggle="modal" data-target="#system_role_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<li>');
                        html.push('<a href="/system/role/edit/' + value + '" class="" data-toggle="modal" data-target="#system_role_dialog" title="' + myUpdate + '">');
                        html.push('<span class="light-blue">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRemove != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="systemRoleRemove(' + value + ')" title="' + myRemove + '">');
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
            var title = systemRoleTable.find('thead').find('th[data-field="' + field + '"]').find('div.th-inner').find('span').text();
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
                    url: '/system/role',
                    type: 'PUT',
                    data: data,
                    success: function (json) {
                        var index = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            if (json.code == 0) {
                                systemRoleTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditSuccess();
                            } else {
                                data[field] = myData[field + row['id']];
                                systemRoleTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditFailed();
                            }
                        }
                        delete myData[field + row['id']];
                    },
                    error: function (error) {
                        var index = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            data[field] = myData[field + row['id']];
                            systemRoleTable.bootstrapTable('updateRow', {index: index, row: data});
                            var $e = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
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

function systemRoleReload() {
    systemRoleTable.bootstrapTable('refresh');
}

function systemRoleRemove(id) {
    var message = '确认要删除该[' + systemRoleLabel + ']记录？';
    bootbox.confirm(message, function (result) {
        if (result) {
            systemRoleWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/system/role',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    systemRoleWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        systemRoleTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                },
                error: function () {
                    systemRoleWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}

function systemRoleBatchRemove() {
    var rows = systemRoleTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        var message = '确认要删除选择的' + rows.length + '条[' + systemRoleLabel + ']记录？';
        bootbox.confirm(message, function (result) {
            if (result) {
                var ids = new Array();
                $.each(rows, function (i, row) {
                    ids[i] = row['id'];
                });
                systemRoleWidget.widgetBoxShowLoading();
                $.ajax({
                    url: '/system/role/batchRemove',
                    type: 'POST',
                    data: {'ids': ids},
                    success: function (json) {
                        systemRoleWidget.widgetBoxHideLoading();
                        if (json.code == 0) {
                            for (var i = 0; i < ids.length; i++) {
                                systemRoleTable.bootstrapTable('removeByUniqueId',  ids[i]);
                            }
                            toastr.success(json.msg);
                        }
                        else {
                            toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                        }
                    }, error: function () {
                        systemRoleWidget.widgetBoxHideLoading();
                        toastr.error('Server Error');
                    }
                });
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}

function systemRoleExport() {
    systemRoleDialogs.find('div[name="export"]').removeClass('hidden');
    systemRoleDialogs.modal('show');
}

function systemRoleExportExe() {
    systemRoleDialogs.modal('hide');
    var query = [];
    $($('#system_role_form').serializeArray()).each(function () {
        if ($.trim(this.value) != '')
            query.push(this.name + '=' + $.trim(this.value) + '&')
    });
    if (systemRoleSort != undefined) {
        query.push('sort=' + systemRoleSort + '&')
    }
    if (systemRoleOrder != undefined) {
        query.push('order=' + systemRoleOrder + '&')
    }
    query.push('_=' + new Date().getTime());
    var url = '/system/role/export?' + query.join('');
    window.open(encodeURI(url));
}

function systemRoleImport() {
    systemRoleDialogs.find('div[name="import"]').removeClass('hidden');
    systemRoleDialogs.modal('show');
}

function systemRoleImportExe() {
    if (systemRoleExcel.length > 0) {
        systemRoleExcel.data('bootstrapValidator').validate();
        if (systemRoleExcel.data('bootstrapValidator').isValid()) {
            systemRoleUpload();
        }
    }
}

function systemRoleUpload() {
    // 隐藏之前做一次reset
    systemRoleExcel.data("bootstrapValidator").resetForm();
    var dialogs = systemRoleDialogs;
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
    var excel = systemRoleExcel.find('input[name="file"]');
    var data = new FormData();
    data.append("file", excel.get(0).files[0]);
    $.ajax({
        type: 'POST',
        url: '/system/role/import',
        data: data,
        dataType: 'json',
        processData: false, //必须false才会自动加上正确的Content-Type
        contentType: false,//必须false才会避开jQuery对 formdata 的默认处理
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if (systemRoleProgress && xhr.upload) {
                xhr.upload.addEventListener("progress", systemRoleProgress, false);
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

function systemRoleProgress(evt) {
    var loaded = evt.loaded;
    var total = evt.total;
    if (loaded == total) {
        systemRoleDialogs.find('#tips').text('正在保存...');
    } else {
        var percent = Math.floor(100 * loaded / total);
        systemRoleDialogs.find('#tips').text('正在上传[' + percent + '%]');
    }
}