$(function () {
    $('#system_role_edit_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    stringLength: {
                        min: 1,
                        max: 50,
                        message: '长度必须在1~50之间'
                    },
                }
            },
            description: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 250,
                        message: '长度必须在0~250之间'
                    },
                }
            },
            orders: {
                validators: {
                }
            },
            userIdCreate: {
                validators: {
                }
            },
        }
    });
});

function systemRoleEdit() {
    $('#system_role_edit_form').data('bootstrapValidator').validate();
    if ($('#system_role_edit_form').data('bootstrapValidator').isValid()) {
        systemRoleDialog.modalDialogShowLoading();
        $.ajax({
            type: 'PUT',
            url: '/system/role',
            data: $('#system_role_edit_form').serialize(),
            success: function (json) {
                if (json.code == 0) {
                    var row = $('#system_role_edit_form').serializeJson();
                    var index = systemRoleTable.find('tr[data-uniqueid="' + row['id'] + '"]').attr('data-index');
                    if (/^\d+$/.test(index)) {
                        systemRoleTable.bootstrapTable('updateRow', {index: index, row: row});
                    }
                    systemRoleDialog.modal('hide');
                    toastr.success(json.msg);
                }
                else {
                    systemRoleDialog.modalDialogHideLoading();
                    toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                }
            }, error: function () {
                systemRoleDialog.modalDialogHideLoading();
                toastr.error('Server Error');
            }
        });
    }
}