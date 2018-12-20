var fontValue = '';
var colorValue = '';
var icon = $('#system_menu_edit_form').find('input[name="icon"]');
var value = icon.val();
if (value.length > 0) {
    for (var i = 0; i < ALL_COLORS.length; i++) {
        if (value.endsWith(' ' + ALL_COLORS[i].key)) {
            var index = value.lastIndexOf(' ' + ALL_COLORS[i].key);
            fontValue = value.substring(0, index);
            colorValue = ALL_COLORS[i].key;
            break;
        }
    }
}
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