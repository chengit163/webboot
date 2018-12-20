var commonJobContent;
var commonJobWidget;
var commonJobDialog;
var commonJobTable;
var commonJobLabel;
var commonJobDialogs;
var commonJobExcel;
var commonJobSort;
var commonJobOrder;


$(function () {
    commonJobContent = $('#common_job_content');
    commonJobWidget = $('#common_job_widget');
    commonJobDialog = $('#common_job_dialog');
    commonJobTable = $('#common_job_table');
    commonJobLabel = commonJobContent.find('input[name="hidden_label"]').val();
    // 加载数据
    commonJobLoad();
    //显示隐藏查询
    $('#common_job_query_switch').change(function () {
        if ($(this).is(":checked")) {
            $('#common_job_query_box').trigger('show.ace.widget').widget_box('show');
        } else {
            $('#common_job_query_box').trigger('hide.ace.widget').widget_box('hide');
        }
    });
    // 重置
    $('#common_job_reset').bind("click", function () {
        $('#common_job_form').emptyForm();
        commonJobReload();
    });
    // 搜索
    $('#common_job_search').bind("click", function () {
        $('#common_job_form').trimForm();
        commonJobReload();
    });
    // 对话框显示
    commonJobDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    commonJobDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });

    // Excel
    commonJobDialogs = $('#common_job_dialogs');
    commonJobExcel = $('#common_job_excel_form');

    if (commonJobExcel.length > 0) {
        commonJobExcel.bootstrapValidator({
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

    commonJobDialogs.on('hide.bs.modal', function () {
        if (commonJobExcel.length > 0) {
            commonJobExcel.emptyForm();
            commonJobExcel.data("bootstrapValidator").resetForm();
        }
        $(this).find('.modal-content').addClass('hidden');
    });

});

function commonJobFix() {
    // refresh, toggle, columns 样式冲突修改
    commonJobWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    commonJobWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    commonJobWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    commonJobWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = commonJobWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    commonJobTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function commonJobLoad() {
    var myGet = commonJobContent.find('input[name="hidden_get"]').val();
    var myUpdate = commonJobContent.find('input[name="hidden_update"]').val();
    var myRemove = commonJobContent.find('input[name="hidden_remove"]').val();
    var myRun = commonJobContent.find('input[name="hidden_run"]').val();
    var myData = {};
    var options =  $('#common_job_form select[name="jobClass"]').selectJson();
    //
    commonJobTable.bootstrapTable({
        url: '/common/job',
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
        toolbar: '#common_job_toolbar',
        uniqueId: 'id',
        idField: 'id',
        sortName: 'id',
        sortOrder: 'asc',
        // 传递参数（*）
        queryParams: function (params) {
            commonJobWidget.widgetBoxShowLoading();
            commonJobFix();
            commonJobSort = params.sort;
            commonJobOrder = params.order;
            var query = {
                'sort': params.sort,
                'order': params.order,
                'pageSize': params.limit,
                'pageNum': (params.offset / params.limit) + 1,
            };
            $($('#common_job_form').serializeArray()).each(function () {
                if ($.trim(this.value) != '')
                    query[this.name] = $.trim(this.value);
            });
            return query;
        },
        onLoadSuccess: function (data) {
            commonJobWidget.widgetBoxHideLoading();
            commonJobFix();
        },
        onLoadError: function (status) {
            commonJobWidget.widgetBoxHideLoading();
            commonJobFix();
        },
        onPostBody: function (cardView) {
            commonJobFix();
        },
        onPageChange: function () {
            commonJobFix();
        },
        columns: [
            {
                checkbox: true,
                align: 'center'
            },
            {
                field: 'jobClass',
                align: 'center',
                sortable: true,
                formatter: function (value, row, index) {
                    var html = [];
                    html.push('<span title="' + value + '">' + options[value] + '</span>');
                    return html.join('');
                }
            },
            {
                field: 'cronExpression',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'text',
                    emptytext: '-',
                    validate: function (v) {
                        if (!$.trim(v)) {return '不能为空';}
                        if ($.trim(v).length < 0 || $.trim(v).length > 100) {return '长度必须在0~100之间'}
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
                field: 'status',
                align: 'center',
                sortable: true,
                editable: myUpdate == undefined ? null :
                {
                    type: 'select',
                    emptytext: '-',
                    source: function () {
                        var options = [];
                        options.push({value: 0, text: '关闭'});
                        options.push({value: 1, text: '启动'});
                        return options;
                    }
                },
                formatter: myUpdate != undefined ? null : function (value, row, index) {
                    var html = [];
                    switch (value) {
                        case 0:
                            html.push('关闭');
                            break;
                        case 1:
                            html.push('启动');
                            break;
                    }
                    return html.join('');
                }
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
                    if (myGet != undefined) {
                        html.push('<button href="/common/job/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#common_job_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<button href="/common/job/edit/' + value + '" class="btn btn-xs btn-info" data-toggle="modal" data-target="#common_job_dialog" title="' + myUpdate + '">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</button>');
                    }
                    if (myRemove != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-danger" onclick="commonJobRemove(' + value + ')" title="' + myRemove + '">');
                        html.push('<i class="ace-icon fa fa-trash"></i>');
                        html.push('</button>');
                    }
                    if (myRun != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-purple" onclick="commonJobRun(' + value + ')" title="' + myRun + '">');
                        html.push('<i class="ace-icon fa fa-play"></i>');
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
                        html.push('<a href="/common/job/view/' + value + '" class="" data-toggle="modal" data-target="#common_job_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myUpdate != undefined) {
                        html.push('<li>');
                        html.push('<a href="/common/job/edit/' + value + '" class="" data-toggle="modal" data-target="#common_job_dialog" title="' + myUpdate + '">');
                        html.push('<span class="light-blue">');
                        html.push('<i class="ace-icon fa fa-pencil"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRemove != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="commonJobRemove(' + value + ')" title="' + myRemove + '">');
                        html.push('<span class="light-red">');
                        html.push('<i class="ace-icon fa fa-trash"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myRun != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="commonJobRun(' + value + ')" title="' + myRun + '">');
                        html.push('<span class="purple">');
                        html.push('<i class="ace-icon fa fa-play"></i>');
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
            var title = commonJobTable.find('thead').find('th[data-field="' + field + '"]').find('div.th-inner').find('span').text();
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
                    url: '/common/job',
                    type: 'PUT',
                    data: data,
                    success: function (json) {
                        var index = commonJobTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            if (json.code == 0) {
                                commonJobTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = commonJobTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditSuccess();
                            } else {
                                data[field] = myData[field + row['id']];
                                commonJobTable.bootstrapTable('updateRow', {index: index, row: data});
                                var $e = commonJobTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
                                $e.tdEditFailed();
                            }
                        }
                        delete myData[field + row['id']];
                    },
                    error: function (error) {
                        var index = commonJobTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                        if (/^\d+$/.test(index)) {
                            data[field] = myData[field + row['id']];
                            commonJobTable.bootstrapTable('updateRow', {index: index, row: data});
                            var $e = commonJobTable.find('tr[data-uniqueid="' + row['id'] + '"]').find('a[data-name="' + field + '"]');
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

function commonJobReload() {
    commonJobTable.bootstrapTable('refresh');
}

function commonJobRemove(id) {
    var message = '确认要删除该[' + commonJobLabel + ']记录？';
    bootbox.confirm(message, function (result) {
        if (result) {
            commonJobWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/common/job',
                type: 'POST',
                data: {_method: 'DELETE', 'id': id},
                success: function (json) {
                    commonJobWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        commonJobTable.bootstrapTable('removeByUniqueId', id);
                        toastr.success(json.msg);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    commonJobWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}

function commonJobBatchRemove() {
    var rows = commonJobTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        var message = '确认要删除选择的' + rows.length + '条[' + commonJobLabel + ']记录？';
        bootbox.confirm(message, function (result) {
            if (result) {
                var ids = new Array();
                $.each(rows, function (i, row) {
                    ids[i] = row['id'];
                });
                commonJobWidget.widgetBoxShowLoading();
                $.ajax({
                    url: '/common/job/batchRemove',
                    type: 'POST',
                    data: {'ids': ids},
                    success: function (json) {
                        commonJobWidget.widgetBoxHideLoading();
                        if (json.code == 0) {
                            for (var i = 0; i < ids.length; i++) {
                                commonJobTable.bootstrapTable('removeByUniqueId',  ids[i]);
                            }
                            toastr.success(json.msg);
                        }
                        else {
                            toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                        }
                    }, error: function () {
                        commonJobWidget.widgetBoxHideLoading();
                        toastr.error('Server Error');
                    }
                });
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}

function commonJobExport() {
    commonJobDialogs.find('div[name="export"]').removeClass('hidden');
    commonJobDialogs.modal('show');
}

function commonJobExportExe() {
    commonJobDialogs.modal('hide');
    var query = [];
    $($('#common_job_form').serializeArray()).each(function () {
        if ($.trim(this.value) != '')
            query.push(this.name + '=' + $.trim(this.value) + '&')
    });
    if (commonJobSort != undefined) {
        query.push('sort=' + commonJobSort + '&')
    }
    if (commonJobOrder != undefined) {
        query.push('order=' + commonJobOrder + '&')
    }
    query.push('_=' + new Date().getTime());
    var url = '/common/job/export?' + query.join('');
    window.open(encodeURI(url));
}

function commonJobImport() {
    commonJobDialogs.find('div[name="import"]').removeClass('hidden');
    commonJobDialogs.modal('show');
}

function commonJobImportExe() {
    if (commonJobExcel.length > 0) {
        commonJobExcel.data('bootstrapValidator').validate();
        if (commonJobExcel.data('bootstrapValidator').isValid()) {
            commonJobUpload();
        }
    }
}

function commonJobUpload() {
    // 隐藏之前做一次reset
    commonJobExcel.data("bootstrapValidator").resetForm();
    var dialogs = commonJobDialogs;
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
    var excel = commonJobExcel.find('input[name="file"]');
    var data = new FormData();
    data.append("file", excel.get(0).files[0]);
    $.ajax({
        type: 'POST',
        url: '/common/job/import',
        data: data,
        dataType: 'json',
        processData: false, //必须false才会自动加上正确的Content-Type
        contentType: false,//必须false才会避开jQuery对 formdata 的默认处理
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if (commonJobProgress && xhr.upload) {
                xhr.upload.addEventListener("progress", commonJobProgress, false);
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

function commonJobProgress(evt) {
    var loaded = evt.loaded;
    var total = evt.total;
    if (loaded == total) {
        commonJobDialogs.find('#tips').text('正在保存...');
    } else {
        var percent = Math.floor(100 * loaded / total);
        commonJobDialogs.find('#tips').text('正在上传[' + percent + '%]');
    }
}

function commonJobRun(id) {
    var message = '确认执行？';
    bootbox.confirm(message, function (result) {
        if (result) {
            commonJobWidget.widgetBoxShowLoading();
            $.ajax({
                url: '/common/job/run',
                type: 'POST',
                data: {'id': id},
                success: function (json) {
                    commonJobWidget.widgetBoxHideLoading();
                    if (json.code == 0) {
                        toastr.success('msg: ' + json.msg, 'code: ' + json.code);
                    }
                    else {
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    commonJobWidget.widgetBoxHideLoading();
                    toastr.error('Server Error');
                }
            });
        }
    });
}