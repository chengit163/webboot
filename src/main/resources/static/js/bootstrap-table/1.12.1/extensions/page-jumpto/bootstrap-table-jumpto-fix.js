/**
 * @author Jay <jwang@dizsoft.com>
 */

(function ($) {
    'use strict';
    var sprintf = $.fn.bootstrapTable.utils.sprintf;

    $.extend($.fn.bootstrapTable.defaults, {
        showJumpto: false,
        exportOptions: {}
    });

    $.extend($.fn.bootstrapTable.locales, {
        formatJumpto: function () {
            return 'GO';
        }
    });
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales);

    var BootstrapTable = $.fn.bootstrapTable.Constructor,
        _initPagination = BootstrapTable.prototype.initPagination;

    BootstrapTable.prototype.initPagination = function () {
        _initPagination.apply(this, Array.prototype.slice.apply(arguments));

        if (this.options.showJumpto) {
            var that = this,
                $pageGroup = this.$pagination.find('ul.pagination'),
                $jumpto = $pageGroup.find('li.jumpto');

            if (!$jumpto.length) {
                $jumpto = $([
                    '<li class="jumpto">',
                        '<input type="text" class="form-control" style="width: 55px; height: 30px; margin-top: 2px; margin-left: 5px; margin-right: 5px; text-align: center; display: inline-block">',
                        '<button class="btn btn-xs btn-info" title="' + this.options.formatJumpto() + '" ' +
                            ' type="button" style="margin-bottom: 2px;" >&nbsp;'+this.options.formatJumpto(),
                        '&nbsp;</button>',
                    '</li>'].join('')).appendTo($pageGroup);

                $jumpto.find('button').click(function () {
                    that.selectPage(parseInt($jumpto.find('input').val()));
                });
            }
        }
    };
})(jQuery);
