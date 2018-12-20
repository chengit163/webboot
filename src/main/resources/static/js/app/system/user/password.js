$(function () {
    $('#system_user_password_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            username: {
                validators: {}
            },
            password: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    regexp: {
                        regexp: /^[a-zA-Z]\w{5,17}$/,
                        message: '长度在6~18之间，只能包含字母、数字和下划线'
                    },
                    different: {
                        field: 'username',
                        message: '密码不能与用户名相同'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    identical: {
                        field: 'password',
                        message: '密码不一致'
                    },
                }
            }
        }
    });
});


function systemUserPassword() {
    $('#system_user_password_form').data('bootstrapValidator').validate();
    if ($('#system_user_password_form').data('bootstrapValidator').isValid()) {
        systemUserDialog.modalDialogShowLoading();
        $.ajax({
            type: 'POST',
            url: '/system/user/password',
            data: $('#system_user_password_form :not(input[name="confirmPassword"])').serialize(),
            success: function (json) {
                console.log(json);
                if (json.code == 0) {
                    systemUserDialog.modal('hide');
                    toastr.success(json.msg);
                }
                else {
                    systemUserDialog.modalDialogHideLoading();
                    toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                }
            }, error: function () {
                systemUserDialog.modalDialogHideLoading();
                toastr.error('Server Error');
            }
        });
    }
}

