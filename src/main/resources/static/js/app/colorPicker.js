var ALL_COLORS = [
    {key: 'red', color: '#DD5A43'},
    {key: 'red2', color: '#E08374'},
    {key: 'light-red', color: '#FF7777'},
    {key: 'blue', color: '#478FCA'},
    {key: 'blue2', color: '#3983C2'},
    {key: 'light-blue', color: '#93CBF9'},
    {key: 'green', color: '#69AA46'},
    {key: 'green2', color: '#2E8965'},
    {key: 'light-green', color: '#B0D877'},
    {key: 'orange', color: '#FF892A'},
    {key: 'orange2', color: '#FEB902'},
    {key: 'light-orange', color: '#FCAC6F'},
    {key: 'purple', color: '#A069C3'},
    {key: 'purple2', color: '#5F47B0'},
    {key: 'pink', color: '#C6699F'},
    {key: 'pink2', color: '#D6487E'},
    {key: 'grey', color: '#777'},
    {key: 'light-grey', color: '#BBB'},
    {key: 'dark', color: '#333'}
];
(function ($) {
    $.fn.colorPicker = function (callback) {
        // 所有颜色
        var colors = ALL_COLORS;
        //
        var colorPicker = this;
        var template = [];
        template.push('<div class="btn-group">');
        template.push('<div class="dropdown dropdown-colorpicker pull-left">');
        template.push('<button data-toggle="dropdown" class="btn btn-primary btn-white dropdown-toggle" aria-expanded="false" >');
        template.push('<span class="btn-colorpicker" style="text-align: center; background-color: transparent;" data-color="" title="Default Color">');
        template.push('<i class="ace-icon glyphicon glyphicon-dashboard dark"></i>');
        template.push('</span>');
        template.push('<i class="ace-icon fa fa-angle-down icon-on-right"></i>');
        template.push('</button>');
        template.push('<ul class="dropdown-menu dropdown-caret dropdown-menu-right" style="margin-top: 10px;">');
        template.push('<li><a class="colorpick-btn" style="text-align: center; background-color: transparent;" data-color="" title="Default Color"><i class="ace-icon glyphicon glyphicon-dashboard dark"></i></a></li>');
        for (var i = 0; i < colors.length; i++) {
            template.push('<li><a class="colorpick-btn" style="background-color:' + colors[i].color + ';" data-color="' + colors[i].key + '" title="' + colors[i].key + '"></a></li>');
        }
        template.push('</ul>');
        template.push('</div>');
        template.push('</div>');


        var methods = {
            init: function () {
                colorPicker.html(template.join(''));
                methods.listen();
            },
            listen: function () {
                event.onSelect();
            },
            select: function (key) {
                var obj = colorPicker.find('a[data-color="' + key + '"]').addClass('selected');
                if (obj.length > 0) {
                    colorPicker.find('.colorpick-btn').removeClass('selected');
                    var color = obj.css('background-color');
                    colorPicker.find('span').css('background-color', color).attr('data-color', key).attr('title', key).html('');
                    if (key != '') {
                        obj.addClass('selected');
                    } else {
                        colorPicker.find('span').attr('title', 'Default Color').html('<i class="ace-icon glyphicon glyphicon-dashboard dark"></i>');
                    }
                }
            },
            // getSelectKey: function () {
            //    var key = colorPicker.find('span').attr('data-color');
            // 	return key;
            // },
            // getSelectColor :function()
            // {
            // 	var color = colorPicker.find('span').css('background-color');
            // 	return color;
            // }
            getColors: function () {
                return colors;
            }
        };

        var event = {
            onSelect: function () {
                colorPicker.on("click", '.colorpick-btn', function () {
                    var key = $(this).attr('data-color');
                    methods.select(key);
                    //
                    callback(key);
                });
                return event;
            },
        };

        methods.init();
        return methods;
    }
})(jQuery);
