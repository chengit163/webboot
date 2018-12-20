$(function () {
    $('#common_job_add_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            jobClass: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    stringLength: {
                        min: 0,
                        max: 100,
                        message: '长度必须在0~100之间'
                    },
                }
            },
            cronExpression: {
                validators: {
                    notEmpty: {message: '不能为空'},
                    stringLength: {
                        min: 0,
                        max: 100,
                        message: '长度必须在0~100之间'
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
            status: {
                validators: {
                }
            },
        }
    });
});

function commonJobAddReset() {
    $('#common_job_add_form').data("bootstrapValidator").resetForm();
}

function commonJobAddSave() {
    $('#common_job_add_form').data('bootstrapValidator').validate();
    if ($('#common_job_add_form').data('bootstrapValidator').isValid()) {
        commonJobDialog.modalDialogShowLoading();
        $.ajax({
            type: 'POST',
            url: '/common/job',
            data: $('#common_job_add_form').serialize(),
            success: function (json) {
                if (json.code == 0) {
                    commonJobDialog.modal('hide');
                    toastr.success(json.msg);
                }
                else {
                    commonJobDialog.modalDialogHideLoading();
                    toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                }
            }, error: function () {
                commonJobDialog.modalDialogHideLoading();
                toastr.error('Server Error');
            }
        });
    }
}