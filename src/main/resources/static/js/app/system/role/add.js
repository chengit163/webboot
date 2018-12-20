$(function () {
    $('#system_role_add_form').bootstrapValidator({
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

function systemRoleAddReset() {
    $('#system_role_add_form').data("bootstrapValidator").resetForm();
}

function systemRoleAddSave() {
    $('#system_role_add_form').data('bootstrapValidator').validate();
    if ($('#system_role_add_form').data('bootstrapValidator').isValid()) {
        systemRoleDialog.modalDialogShowLoading();
        $.ajax({
            type: 'POST',
            url: '/system/role',
            data: $('#system_role_add_form').serialize(),
            success: function (json) {
                if (json.code == 0) {
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