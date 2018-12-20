$(function () {
    var fontValue = '';
    var colorValue = '';
    var icon = $('#system_menu_add_form').find('input[name="icon"]');
    //
    var fontInput = $('#icon_font_picker');
    fontInput.fontIconPicker({
        source: fa_icons_search,
        searchSource: fa_icons_search,
        theme: 'fip-bootstrap',
        emptyIconValue: '',
    });
    fontInput.parent().find('.selected-icon').html('<i class="' + (fontValue == '' ? 'fip-icon-block' : fontValue) + ' ' + colorValue + '"></i>');
    fontInput.change(function () {
        fontValue = $(this).val();
        icon.val(fontValue + ' ' + colorValue);
        fontInput.parent().find('.selected-icon').html('<i class="' + (fontValue == '' ? 'fip-icon-block' : fontValue) + ' ' + colorValue + '"></i>');
    });
    $('#icon_color_picker').colorPicker(function (key) {
        colorValue = key;
        icon.val(fontValue + ' ' + colorValue);
        fontInput.parent().find('.selected-icon').html('<i class="' + (fontValue == '' ? 'fip-icon-block' : fontValue) + ' ' + colorValue + '"></i>');
    }).select(colorValue);

    $('#system_menu_add_form').bootstrapValidator({
        feedbackIcons: {
            valid: 'ace-icon fa fa-check-circle',
            invalid: 'ace-icon fa fa-times-circle',
            validating: 'ace-icon fa fa-info-circle'
        },
        fields: {
            pid: {
                validators: {
                    notEmpty: {message: '不能为空'},
                }
            },
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
            url: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 100,
                        message: '长度必须在0~100之间'
                    },
                }
            },
            permission: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 100,
                        message: '长度必须在0~100之间'
                    },
                }
            },
            icon: {
                validators: {
                    stringLength: {
                        min: 0,
                        max: 100,
                        message: '长度必须在0~100之间'
                    },
                }
            },
            type: {
                validators: {
                }
            },
            orders: {
                validators: {
                }
            },
        }
    });
});

function systemMenuAddReset() {
    $('#system_menu_add_form').data("bootstrapValidator").resetForm();
}

function systemMenuAddSave() {
    $('#system_menu_add_form').data('bootstrapValidator').validate();
    if ($('#system_menu_add_form').data('bootstrapValidator').isValid()) {
        systemMenuDialog.modalDialogShowLoading();
        $.ajax({
            type: 'POST',
            url: '/system/menu',
            data: $('#system_menu_add_form').serialize(),
            success: function (json) {
                if (json.code == 0) {
                    systemMenuDialog.modal('hide');
                    toastr.success(json.msg);
                }
                else {
                    systemMenuDialog.modalDialogHideLoading();
                    toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                }
            }, error: function () {
                systemMenuDialog.modalDialogHideLoading();
                toastr.error('Server Error');
            }
        });
    }
}