$(function () {
    $('#common_log_edit_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            username: {
                validators: {
                }
            },
            ip: {
                validators: {
                }
            },
            action: {
                validators: {
                }
            },
            url: {
                validators: {
                }
            },
            method: {
                validators: {
                }
            },
            args: {
                validators: {
                }
            },
            func: {
                validators: {
                }
            },
            os: {
                validators: {
                }
            },
            browser: {
                validators: {
                }
            },
            cost: {
                validators: {
                }
            },
        }
    });
});

function commonLogEdit() {
    $('#common_log_edit_form').data('bootstrapValidator').validate();
    if ($('#common_log_edit_form').data('bootstrapValidator').isValid()) {
        commonLogDialog.modalDialogShowLoading();
        $.ajax({
            type: 'PUT',
            url: '/common/log',
            data: $('#common_log_edit_form').serialize(),
            success: function (json) {
                if (json.code == 0) {
                    var row = $('#common_log_edit_form').serializeJson();
                    var index = commonLogTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                    if (/^\d+$/.test(index)) {
                        commonLogTable.bootstrapTable('updateRow', {index: index, row: row});
                    }
                    commonLogDialog.modal('hide');
                    toastr.success(json.msg);
                }
                else {
                    commonLogDialog.modalDialogHideLoading();
                    toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                }
            }, error: function () {
                commonLogDialog.modalDialogHideLoading();
                toastr.error('Server Error');
            }
        });
    }
}