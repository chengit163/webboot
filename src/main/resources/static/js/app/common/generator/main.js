var commonGeneratorContent;
var commonGeneratorWidget;
var commonGeneratorDialog;
var commonGeneratorTable;

$(function () {
    commonGeneratorContent = $('#common_generator_content');
    commonGeneratorWidget = $('#common_generator_widget');
    commonGeneratorDialog = $('#common_generator_dialog');
    commonGeneratorTable = $('#common_generator_table');
    // 加载数据
    commonGeneratorLoad();
    // 对话框显示
    commonGeneratorDialog.on('show.bs.modal', function () {
        //'shown.bs.modal'
        $(this).modalDialogOnOpen();
    });
    // 对话框隐藏
    commonGeneratorDialog.on('hidden.bs.modal', function () {
        //'hide.bs.modal'
        $(this).modalDialogOnClose();
    });
});

function commonGeneratorFix() {
    // refresh, toggle, columns 样式冲突修改
    commonGeneratorWidget.find('button[aria-label="refresh"]').removeClass('btn-default').addClass('btn-grey');
    commonGeneratorWidget.find('button[aria-label="toggle"]').removeClass('btn-default').addClass('btn-grey');
    commonGeneratorWidget.find('button[aria-label="columns"]').removeClass('btn-default').addClass('btn-grey').css({'margin-left': '1px'});
    commonGeneratorWidget.find('button[aria-label="columns"]').find('.caret').remove();
    // 页码
    var num = commonGeneratorWidget.find('.fixed-table-pagination').find('.pull-left').find('button');
    if (num.hasClass('btn-default')) {
        num.removeClass('btn-default').addClass('btn-sm btn-white dropdown-toggle');
        num.find('.caret').remove();
        num.append('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
    }
    // checkbox居中
    commonGeneratorTable.find('.bs-checkbox').css({'text-align': 'center', 'vertical-align': 'middle'});
}

function commonGeneratorLoad() {
    var myGet = commonGeneratorContent.find('input[name="hidden_get"]').val();
    var myCode = commonGeneratorContent.find('input[name="hidden_code"]').val();
    //
    commonGeneratorTable.bootstrapTable({
        url: '/common/generator',
        method: 'GET',
        dataType: 'json',
        sidePagination: 'server',
        cache: false,
        pagination: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [5, 10, 25, 50, 100],
        striped: true,
        showRefresh: true,
        showToggle: true,
        showColumns: true,
        toolbar: '#common_generator_toolbar',
        uniqueId: 'tableName',
        idField: 'tableName',
        // 传递参数（*）
        queryParams: function (params) {
            commonGeneratorWidget.widgetBoxShowLoading();
            commonGeneratorFix();
            var query = {
                'pageSize': params.limit,
                'pageNum': (params.offset / params.limit) + 1,
            };
            $($('#common_generator_form').serializeArray()).each(function () {
                if ($.trim(this.value) != '')
                    query[this.name] = $.trim(this.value);
            });
            return query;
        },
        onLoadSuccess: function (data) {
            commonGeneratorWidget.widgetBoxHideLoading();
            commonGeneratorFix();
        },
        onLoadError: function (status) {
            commonGeneratorWidget.widgetBoxHideLoading();
            commonGeneratorFix();
        },
        onPostBody: function (cardView) {
            commonGeneratorFix();
        },
        onPageChange: function () {
            commonGeneratorFix();
        },
        columns: [
            {
                checkbox: true,
                align: 'center'
            },
            {
                field: 'tableName',
                align: 'center',
            },
            {
                field: 'tableComment',
                align: 'center',
            },
            {
                field: 'createTime',
                align: 'center',
            },
            {
                field: 'tableName',
                align: 'center',
                switchable: false,
                formatter: function (value, row, index) {
                    var html = [];

                    html.push('<div class="hidden-sm hidden-xs btn-group">');
                    if (myGet != undefined) {
                        html.push('<button href="/common/generator/view/' + value + '" class="btn btn-xs btn-success" data-toggle="modal" data-target="#common_generator_dialog" title="' + myGet + '">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</button>');
                    }
                    if (myCode != undefined) {
                        html.push('<button href="#" class="btn btn-xs btn-primary" onclick="commonGeneratorCode(\'' + value + '\')" title="' + myCode + '">');
                        html.push('<i class="ace-icon fa fa-code"></i>');
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
                        html.push('<a href="/common/generator/view/' + value + '" class="" data-toggle="modal" data-target="#common_generator_dialog" title="' + myGet + '">');
                        html.push('<span class="light-green">');
                        html.push('<i class="ace-icon fa fa-eye"></i>');
                        html.push('</span>');
                        html.push('</a>');
                        html.push('</li>');
                    }
                    if (myCode != undefined) {
                        html.push('<li>');
                        html.push('<a href="#" class="" onclick="commonGeneratorCode(\'' + value + '\')" title="' + myCode + '">');
                        html.push('<span class="blue2">');
                        html.push('<i class="ace-icon fa fa-code"></i>');
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
        ]
    });
}

function commonGeneratorReload() {
    commonGeneratorTable.bootstrapTable('refresh');
}

function commonGeneratorCode(tableName) {
    bootbox.prompt("模块名称(空或小写字母)", function (result) {
        if (result != null) {
            if (/^[a-z]*$/.test($.trim(result))) {
                var url = '/common/generator/code?tableName=' + tableName + '&module=' + $.trim(result);
                window.open(encodeURI(url));
            } else {
                toastr.warning("空或小写字母");
            }
        }
    });
}

function commonGeneratorBatchCode() {
    var rows = commonGeneratorTable.bootstrapTable('getSelections');
    if (rows.length > 0) {
        bootbox.prompt("模块名称(空或小写字母)", function (result) {
            if (result != null) {
                if (/^[a-z]*$/.test($.trim(result))) {
                    var tableNames = new Array();
                    $.each(rows, function (i, row) {
                        tableNames[i] = row['tableName'];
                    });
                    var url = '/common/generator/batchCode?tableNames=' + JSON.stringify(tableNames) + '&module=' + $.trim(result);
                    window.open(encodeURI(url));
                } else {
                    toastr.warning("空或小写字母");
                }
            }
        });
    } else {
        toastr.warning("请至少选择一条记录!");
    }
}