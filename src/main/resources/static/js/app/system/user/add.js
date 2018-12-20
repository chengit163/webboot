$(function () {
    $('#system_user_add_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            username: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    regexp: {
                        regexp: /^[a-zA-Z][a-zA-Z0-9_-]{1,17}$/,
                        message: '必须以字母开头，长度在2~18之间，只能包含字母、数字、下划线、减号'
                    },
                    remote: {
                        url: '/system/user/validateUsername',
                        type: 'POST',
                        delay :  2000,
                        message: '用户已存在'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    regexp: {
                        regexp: /^[a-zA-Z]\w{5,17}$/,
                        message: '必须以字母开头，长度在6~18之间，只能包含字符、数字、下划线'
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
            },
            nickname: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 50,
                        message: '长度必须在0~50之间'
                    },
                }
            },
            phone: {
                validators: {
                    regexp: {
                        regexp: /^1[3|4|5|7|8]\d{9}$/,
                        message: '格式不合法'
                    },
                }
            },
            email: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 150,
                        message: '长度必须在0~150之间'
                    },
                    regexp: {
                        regexp: /^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\.)+[A-Za-z]{2,4}$/,
                        message: '格式不合法'
                    },
                }
            },
        }
    });
});

function systemUserAddReset() {
    $('#system_user_add_form').data("bootstrapValidator").resetForm();
}

function systemUserAddSave() {
    $('#system_user_add_form').data('bootstrapValidator').validate();
    if ($('#system_user_add_form').data('bootstrapValidator').isValid()) {
        systemUserDialog.modalDialogShowLoading();
        $.ajax({
            type: 'POST',
            url: '/system/user',
            data: $('#system_user_add_form :not(input[name="confirmPassword"])').serialize(),
            success: function (json) {
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