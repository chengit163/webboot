var commonLogContent;
var commonLogWidget;
var commonLogDialog;
var commonLogTable;
var commonLogLabel;
var commonLogDialogs;
var commonLogExcel;
var commonLogSort;
var commonLogOrder;


$(function () {
    commonLogContent = $('#common_log_content');
    commonLogWidget = $('#common_log_widget');
    commonLogDialog = $('#common_log_dialog');
    commonLogTable = $('#common_log_table');
    commonLogLabel = commonLogContent.find('input[name="hidden_label"]').val();
    // 加载数据
    commonLogLoad();
    //显示隐藏查询
    $('#common_log_query_switch').change(function () {
        if ($(this).is(":checked")) {
            $('#common_log_query_box').trigger('show.ace.widget').widget_box('show');
        } else {
            $('#common_log_query_box').trigger('hide.ace.widget').widget_box('hide');
        }
    });
    // 重置
    $('#common_log_reset').bind("click", function () {
        $('#common_log_form').emptyForm();
        commonLogReload();
    });
    // 搜索
    $('#common_log_search').bind("click", function () {
        $('#common_log_form').trimForm();
        commonLogReload();
    });
    // 对话框显示
    commonLogDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    commonLogDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });

    // Excel
    commonLogDialogs = $('#common_log_dialogs');
    commonLogExcel = $('#common_log_excel_form');

    if (commonLogExcel.length > 0) {
        commonLogExcel.bootstrapValidator({
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

    commonLogDialogs.on('hide.bs.modal', function () {
        if (commonLogExcel.length > 0) {
            commonLogExcel.emptyForm();
            commonLogExcel.data("bootstrapValidator").resetForm();
        }
        $(this).find('.modal-content').addClass('hidden');
    });

});

function commonLogFix() {
    // refresh, toggle, columns 样式冲突修改
    commonLogWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    commonLogWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    commonLogWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    commonLogWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = commonLogWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    commonLogTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function commonLogLoad() {
    var myGet = commonLogContent.find('input[name="hidden_get"]').val();
    var myUpdate = commonLogContent.find('input[name="hidden_update"]').val();
    var myRemove = commonLogContent.find('input[name="hidden_remove"]').val();
    var myData = {};
    //
    commonLogTable.bootstrapTable({
        url: '/common/log',
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
        toolbar: '#common_log_toolbar',
        uniqueId: 'id',
        idField: 'id',
        sortName: 'id',
        sortOrder: 'desc',
        // 传递参数（*）
        queryParams: function (params) {
            commonLogWidget.widgetBoxShowLoading();
            commonLogFix();
            commonLogSort = params.sort;
            commonLogOrder = params.order;
            var query = {
                'sort': params.sort,
                'order': params.order,
                'pageSize': params.limit,
                'pageNum': (params.offset / params.limit) + 1,
            };
            $($('#common_log_form').serializeArray()).each(function () {
                if ($.trim(this.value) != '')
                    query[this.name] = $.trim(this.value);
            });
            return query;
        },
        onLoadSuccess: function (data) {
            commonLogWidget.widgetBoxHideLoading();
            commonLogFix();
        },
        onLoadError: function (status) {
            commonLogWidget.widgetBoxHideLoading();
            commonLogFix();
        },
        onPostBody: function (cardView) {
            commonLogFix();
        },
        onPageChange: function () {
            commonLogFix();
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
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'ip',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'action',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
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
                },
            },
            {
                field: 'method',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'select',
                    emptytext: '-',
                    source: function () {
                        var options = [];
                        options.push({value: -1, text: 'UNKNOWN'});
                        options.push({value: 0, text: 'GET'});
                        options.push({value: 1, text: 'POST'});
                        options.push({value: 2, text: 'PUT'});
                        options.push({value: 3, text: 'DELETE'});
                        options.push({value: 4, text: 'HEADER'});
                        options.push({value: 5, text: 'OPTIONS'});
                        return options;
                    }
                },
                formatter: myUpdate != undefined ? null : function (value, row, index) {
                    var html = [];
                    switch (value) {
                        case -1:
                            html.push('UNKNOWN');
                            break;
                        case 0:
                            html.push('GET');
                            break;
                        case 1:
                            html.push('POST');
                            break;
                        case 2:
                            html.push('PUT');
                            break;
                        case 3:
                            html.push('DELETE');
                            break;
                        case 4:
                            html.push('HEADER');
                            break;
                        case 5:
                            html.push('OPTIONS');
                            break;
                    }
                    return html.join('');
                }
            },
            {
                field: 'args',
                align: 'center',
                sortable: true,
                visible: false,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'func',
                align: 'center',
                sortable: true,
                visible: false,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'os',
                align: 'center',
                sortable: true,
                visible: false,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'browser',
                align: 'center',
                sortable: true,
                visible: false,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                },
            },
            {
                field: 'cost',
                align: 'center',
                sortable: true,
                visible: false,
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
                field: 'happen',
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
                    if (myGet != undefined) {
                        html.push('<button href="/common/log/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#common_log_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<button href="/common/log/edit/' + value + '" class="btn btn-xs btn-info" data-toggle="modal" data-target="#common_log_dialog" title="' + myUpdate + '">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</button>');
                    }
                    if (myRemove != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-danger" onclick="commonLogRemove(' + value + ')" title="' + myRemove + '">');
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
                    if (myGet != undefined) {
                        html.push('<li>');
                        html.push('<a href="/common/log/view/' + value + '" class="" data-toggle="modal" data-target="#common_log_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<li>');
                        html.push('<a href="/common/log/edit/' + value + '" class="" data-toggle="modal" data-target="#common_log_dialog" title="' + myUpdate + '">');
                        html.push('<span class="light-blue">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRemove != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="commonLogRemove(' + value + ')" title="' + myRemove + '">');
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
            var title = commonLogTable.find('thead').find('th[data-field="' + field + '"]').find('div.th-inner').find('span').text();
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
                    url: '/common/log',
                    type: 'PUT',
                    data: data,
                    success: function (json) {
                        var index = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            if (json.code == 0) {
                                commonLogTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditSuccess();
                            } else {
                                data[field] = myData[field + row['id']];
                                commonLogTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditFailed();
                            }
                        }
                        delete myData[field + row['id']];
                    },
                    error: function (error) {
                        var index = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            data[field] = myData[field + row['id']];
                            commonLogTable.bootstrapTable('updateRow', {index: index, row: data});
                            var $e = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
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

function commonLogReload() {
    commonLogTable.bootstrapTable('refresh');
}

function commonLogRemove(id) {
    var message = '确认要删除该[' + commonLogLabel + ']记录？';
    bootbox.confirm(message, function (result) {
        if (result) {
            commonLogWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/common/log',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    commonLogWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        commonLogTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    commonLogWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}

function commonLogBatchRemove() {
    var rows = commonLogTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        var message = '确认要删除选择的' + rows.length + '条[' + commonLogLabel + ']记录？';
        bootbox.confirm(message, function (result) {
            if (result) {
                var ids = new Array();
                $.each(rows, function (i, row) {
                    ids[i] = row['id'];
                });
                commonLogWidget.widgetBoxShowLoading();
                $.ajax({
                    url: '/common/log/batchRemove',
                    type: 'POST',
                    data: {'ids': ids},
                    success: function (json) {
                        commonLogWidget.widgetBoxHideLoading();
                        if (json.code == 0) {
                            for (var i = 0; i < ids.length; i++) {
                                commonLogTable.bootstrapTable('removeByUniqueId',  ids[i]);
                            }
                            toastr.success(json.msg);
                        }
                        else {
                            toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                        }
                    }, error: function () {
                        commonLogWidget.widgetBoxHideLoading();
                        toastr.error('Server Error');
                    }
                });
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}

function commonLogExport() {
    commonLogDialogs.find('div[name="export"]').removeClass('hidden');
    commonLogDialogs.modal('show');
}

function commonLogExportExe() {
    commonLogDialogs.modal('hide');
    var query = [];
    $($('#common_log_form').serializeArray()).each(function () {
        if ($.trim(this.value) != '')
            query.push(this.name + '=' + $.trim(this.value) + '&')
    });
    if (commonLogSort != undefined) {
        query.push('sort=' + commonLogSort + '&')
    }
    if (commonLogOrder != undefined) {
        query.push('order=' + commonLogOrder + '&')
    }
    query.push('_=' + new Date().getTime());
    var url = '/common/log/export?' + query.join('');
    window.open(encodeURI(url));
}

function commonLogImport() {
    commonLogDialogs.find('div[name="import"]').removeClass('hidden');
    commonLogDialogs.modal('show');
}

function commonLogImportExe() {
    if (commonLogExcel.length > 0) {
        commonLogExcel.data('bootstrapValidator').validate();
        if (commonLogExcel.data('bootstrapValidator').isValid()) {
            commonLogUpload();
        }
    }
}

function commonLogUpload() {
    // 隐藏之前做一次reset
    commonLogExcel.data("bootstrapValidator").resetForm();
    var dialogs = commonLogDialogs;
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
    var excel = commonLogExcel.find('input[name="file"]');
    var data = new FormData();
    data.append("file", excel.get(0).files[0]);
    $.ajax({
        type: 'POST',
        url: '/common/log/import',
        data: data,
        dataType: 'json',
        processData: false, //必须false才会自动加上正确的Content-Type
        contentType: false,//必须false才会避开jQuery对 formdata 的默认处理
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if (commonLogProgress && xhr.upload) {
                xhr.upload.addEventListener("progress", commonLogProgress, false);
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

function commonLogProgress(evt) {
    var loaded = evt.loaded;
    var total = evt.total;
    if (loaded == total) {
        commonLogDialogs.find('#tips').text('正在保存...');
    } else {
        var percent = Math.floor(100 * loaded / total);
        commonLogDialogs.find('#tips').text('正在上传[' + percent + '%]');
    }
}