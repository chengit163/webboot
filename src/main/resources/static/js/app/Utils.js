(function ($) {

    /**非空表单*/
    $.fn.isNotEmptyForm = function () {
        var flag = false;
        this.find('input, select').each(function () {
            var value = $(this).val();
            if (value != '') {
                flag = true;
                return false;
            }
        });
        return flag;
    };

    /**去掉表单前后空白*/
    $.fn.trimForm = function () {
        this.find('input, select').each(function () {
            var value = $.trim($(this).val());
            $(this).val(value);
        });
    }

    /**非空表单并去掉前后空白*/
    $.fn.isNotEmptyAndTrimForm = function () {
        var flag = false;
        this.find('input, select').each(function () {
            var value = $.trim($(this).val());
            $(this).val(value);
            if (value != '') {
                flag = true;
            }
        });
        return flag;
    };

    /**去掉表单前后空白*/
    $.fn.emptyForm = function () {
        this.find('input, select').each(function () {
            $(this).val('');
        });
    }

    $.fn.serializeJson = function () {
        var json = {};
        $(this.serializeArray()).each(function () {
            json[this.name] = $.trim(this.value);
        });
        return json;
    }

     $.fn.selectJson = function () {
         var json = {};
         this.find('option').each(function () {
             json[$(this).val()] = $(this).text();
         });
         return json;
     }

    $.fn.widgetBoxShowLoading = function () {
        if (this.hasClass('widget-box')) {
            this.addClass('position-relative');
            var height = this.find('.widget-header').height();
            var loading = [];
            loading.push('<div class="loading-overlay" style="margin-top: ' + (height + 1) + 'px;">');
            loading.push('<i class="ace-icon loading-icon fa fa-spinner fa-spin fa-2x white"></i>');
            loading.push('</div>');
            this.append(loading.join(''));
        }
    };

    $.fn.widgetBoxHideLoading = function () {
        if (this.hasClass('widget-box')) {
            this.removeClass('position-relative');
            this.find('.loading-overlay').remove();
        }
    };

    $.fn.modalDialogOnOpen = function () {
        var loading = [];
        loading.push('<div class="modal-header">');
        loading.push('<button type="button" class="close" data-dismiss="modal">×</button>');
        loading.push('<h5 class="blue bigger">');
        loading.push('<i class="ace-icon fa fa-spinner fa-spin bigger-150"></i>');
        loading.push('&nbsp;Loading...');
        loading.push(' </h5>');
        loading.push('</div>');
        this.find('.modal-content').html(loading.join(''));
    };

    $.fn.modalDialogOnClose = function () {
        this.removeData('bs.modal').find('.modal-content').empty();
    };

    $.fn.modalDialogShowLoading = function () {
        if (this.hasClass('modal')) {
            var loading = [];
            loading.push('<div class="loading-overlay">');
            loading.push('<i class="ace-icon loading-icon fa fa-spinner fa-spin fa-2x white"></i>');
            loading.push('</div>');
            this.find('.modal-body').append(loading.join(''));
        }
    };

    $.fn.modalDialogHideLoading = function () {
        if (this.hasClass('modal')) {
            this.find('.loading-overlay').remove();
        }
    };

    $.fn.tdEditLoading = function () {
        var $icon = $('<i/>').addClass('ace-icon fa fa-spinner fa-spin light-grey pull-right');
        this.find('i.ace-icon').remove();
        this.append($icon);
    };

    $.fn.tdEditSuccess = function () {
        var $icon = $('<i/>').addClass('ace-icon fa fa-check-circle light-green pull-right');
        this.find('i.ace-icon').remove();
        this.append($icon);
        setTimeout(function () {
            $icon.remove();
        }, 1500);
    };

    $.fn.tdEditFailed = function () {
        console.log('tdEditFailed');
        console.log(this);
        var $icon = $('<i/>').addClass('ace-icon fa fa-info-circle light-orange pull-right');
        this.find('i.ace-icon').remove();
        this.append($icon);
        setTimeout(function () {
            $icon.remove();
        }, 1500);
    };

    $.fn.tdEditError = function () {
        var $icon = $('<i/>').addClass('ace-icon fa fa-times-circle light-red pull-right');
        this.find('i.ace-icon').remove();
        this.append($icon);
        setTimeout(function () {
            $icon.remove();
        }, 1500);
    };

})(jQuery);