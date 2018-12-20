/**
 * nth-tabs
 * author:nethuige
 * version:1.0
 * https://github.com/nethuige/nth-tabs
 */
(function ($) {
    $.fn.nthTabs = function (obj, callback) {
        //插件中的40为默认左边距
        var nthTabs = this;
        var nthDivs = $(obj);
        var defaults = {
            allowClose: true, //添加选项卡时是否允许关闭,默认值：是
            active: false, //添加选项卡时是否为活动状态,默认值：否
            rollWidth: nthTabs.width() - 120, //可滚动的区域宽度，120即3个操作按钮的宽度
        };
        var settings = $.extend({}, defaults);
        var templateTab = '<div class="page-tabs"><a href="#" class="roll-nav roll-nav-left"><span class="fa fa-backward"></span></a><div class="content-tabs"><div class="content-tabs-container"><ul class="nav nav-tabs"></ul></div></div><a href="#" class="roll-nav roll-nav-right"><span class="fa fa-forward"></span></a><div class="dropdown roll-nav right-nav-list"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="fa fa-chevron-down"></span></a><ul id="tab-dropdown-menu" class="dropdown-menu"><li><a href="#" class="tab-location">定位当前选项卡</a></li><li><a href="#" class="tab-close-current">关闭当前选项卡</a></li><li role="separator" class="divider"></li><li><a href="#" class="tab-close-other">关闭其他选项卡</a></li><li><a href="#" class="tab-close-all">关闭全部选项卡</a></li><li role="separator" class="divider"></li><li class="scrollbar-outer tab-list-scrollbar"><div class="tab-list-container"><ul class="tab-list"></ul></div></li></ul></div></div>';
        var templateDiv = '<div class="tab-content" style="border: none;"></div>';
        //各种api
        var methods = {
            //初始化
            init: function () {
                nthTabs.html(templateTab);
                nthDivs.html(templateDiv);
                methods.listen();
            },
            //事件监听
            listen: function () {
                event.onTabClose().onTabRollLeft().onTabRollRight().onTabList()
                    .onTabCloseOpt().onTabCloseAll().onTabCloseOther().onLocationTab().onTabHrefToggle();
            },
            //获取所有tab宽度
            getAllTabWidth: function () {
                var sum_width = 0;
                nthTabs.find('.nav-tabs li').each(function () {
                    sum_width += parseFloat($(this).width());
                });
                return sum_width;
            },
            //获取左右滑动步值
            getMarginStep: function () {
                return settings.rollWidth / 2;
            },
            //获取当前活动状态选项卡ID
            getActiveId: function () {
                return nthTabs.find('li[class="active"]').find("a").attr("href").replace('#', '');
            },
            //获取所有选项卡
            getTabList: function () {
                var tablist = [];
                nthTabs.find('.nav-tabs li a').each(function () {
                    tablist.push({id: $(this).attr('href'), title: $(this).children('span').html()});
                });
                return tablist;
            },
            //添加一个选项卡
            addTab: function (options) {
                // 判断id是否已经添加了
                var isAdd = true;
                var href = '#' + options.id;
                nthTabs.find('.nav-tabs li a').each(function () {
                    if ($(this).attr('href') == href) {
                        isAdd = false;
                        return false;
                    }
                });
                if (isAdd) {
                    //nav-tab
                    var tab = [];
                    var active = options.active == undefined ? settings.active : options.active;
                    var allowClose = options.allowClose == undefined ? settings.allowClose : options.allowClose;
                    active = active ? 'active' : '';
                    tab.push('<li role="presentation" class="' + active + '" id="_' + options.id + '">');
                    tab.push('<a href="#' + options.id + '" data-toggle="tab">');
                    tab.push('<span>' + options.name + '</span>');
                    allowClose ? tab.push('<i class="fa fa-close tab-close"></i>') : '';
                    tab.push('</a>');
                    tab.push('</li>');
                    nthTabs.find(".nav-tabs").append(tab.join(''));
                    //tab-content
                    var div = [];
                    div.push('<div style="border: none; width: 100%;" class="tab-pane ' + active + '" id="' + options.id + '">');
                    if (options.type == -1) {
                        // 粗略计算
                        var a = $('.page-content').offset().top;
                        var b = ($('.page-content').outerHeight() - $('.page-content').height()) * 2;
                        var c = $('.footer-content').outerHeight();
                        var d = ($('.footer-content').outerHeight() - $('.footer-content').height()) * 2;
                        var h = $(window).height() - (a + b + c + d) + 50;
                        div.push('<iframe frameborder="0" style="margin: auto; width: 100%; height: ' + h + 'px;" src="' + options.url + '">');
                        div.push('</iframe>');
                    }
                    div.push('</div>');
                    nthDivs.find(".tab-content").append(div.join(''));
                    //callback
                    if (options.type != -1) {
                        nthDivs.find("#" + options.id).load(options.url);
                    }
                }
                return methods;
            },
            //定位选项卡
            locationTab: function (tabId) {
                tabId = tabId == undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#') > -1 ? tabId : '#' + tabId;
                var navTabA = nthTabs.find("[href='" + tabId + "']"); //当前活动标签对象
                //计算存在于当前活动选项卡之前的所有同级选项卡的宽度之和
                var width = 0;
                navTabA.parent().prevAll().each(function () {
                    width += $(this).width();
                });
                //得到选项卡容器对象
                var contentTab = navTabA.parent().parent().parent();
                //情况1：之前同级选项卡宽度之和小于选项卡可视区域的70%则不做任何margin处理
                if (width <= settings.rollWidth * 0.7) {
                    margin_left_total = 40;
                }
                //情况2：之前同级选项卡宽度之和大于选项卡可视区域的70%且小于等于选项卡可视区域的则margin为可视区域的一半,使其更显眼
                else if (width <= settings.rollWidth) {
                    margin_left_total = 40 - settings.rollWidth / 2;
                }
                //情况3：算法：a:当前活动选项卡的所有前面的同级选项卡的宽度之和；b:当前活动选项卡的宽度；c:选项卡可视区域的宽度；
                //即当前活动选项卡相对选项卡可视区域宽度的(余宽) = a+b-Math.floor(a/c)*c
                else {
                    var remaining = width + navTabA.parent().width() - (Math.floor(width / settings.rollWidth) * settings.rollWidth);
                    if (remaining <= settings.rollWidth * 0.7) {
                        //余宽小于选项卡可视区域宽度70%则边距为可视区域宽度的整数倍
                        margin_left_total = 40 - Math.floor(width / settings.rollWidth) * settings.rollWidth;
                    } else {
                        //余宽大于选项卡可视区域宽度70%则边距为可视区域宽度的整数倍外加一半的边距，保证活动选项卡可视效果最佳
                        margin_left_total = 40 - Math.floor(width / settings.rollWidth) * settings.rollWidth - settings.rollWidth / 2;
                    }
                }
                contentTab.css("margin-left", margin_left_total);
                return methods;
            },
            //删除单个选项卡
            delTab: function (tabId) {
                tabId = tabId == undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#') > -1 ? tabId : '#' + tabId;
                if ('#0' != tabId) {
                    var navTabA = nthTabs.find("[href='" + tabId + "']");
                    //如果关闭的是激活状态的选项卡
                    if (navTabA.parent().attr('class') == 'active') {
                        //激活选项卡，如果后面存在激活后面，否则激活前面
                        var activeNavTab = navTabA.parent().next();
                        var activeTabContent = $(tabId).next();
                        if (activeNavTab.length < 1) {
                            activeNavTab = navTabA.parent().prev();
                            activeTabContent = $(tabId).prev();
                        }
                        activeNavTab.addClass('active');
                        activeTabContent.addClass('active');
                    }
                    //移除旧选项卡
                    navTabA.parent().remove();
                    $(tabId).remove();
                }
                return methods;
            },
            //删除其他选项卡
            delOtherTab: function () {
                nthTabs.find(".nav-tabs li").not("#_0").not('[class="active"]').remove();
                nthDivs.find(".tab-pane").not("#0").not('[class="tab-pane active"]').remove();
                return methods;
            },
            //删除全部选项卡
            delAllTab: function () {
                nthTabs.find(".nav-tabs li").not("#_0").remove();
                nthDivs.find(".tab-pane").not("#0").remove();
                //
                nthTabs.find('[href=#0]').parent().addClass('active');
                nthDivs.find('#0').addClass('active');
                return methods;
            },
            //切换活动选项卡
            setActTab: function (tabId) {
                tabId = tabId == undefined ? methods.getActiveId() : tabId;
                tabId = tabId.indexOf('#') > -1 ? tabId : '#' + tabId;
                nthTabs.find('.active').removeClass('active');
                nthTabs.find("[href='" + tabId + "']").parent().addClass('active');
                // nthTabs.find(tabId).addClass('active');
                nthDivs.find('.active').removeClass('active');
                nthDivs.find(tabId).addClass('active');
                //
                callback(tabId.replace('#', ''));
                return methods;
            },
        };
        //事件处理
        var event = {
            //定位选项卡
            onLocationTab: function () {
                nthTabs.on("click", '.tab-location', function () {
                    methods.locationTab();
                });
                return event;
            },
            //关闭选项卡按钮
            onTabClose: function () {
                nthTabs.on("click", '.tab-close', function () {
                    var tabId = $(this).parent().attr('href');
                    methods.delTab(tabId);
                });
                return event;
            },
            //关闭当前选项卡操作
            onTabCloseOpt: function () {
                nthTabs.on("click", '.tab-close-current', function () {
                    methods.delTab();
                });
                return event;
            },
            //关闭其他选项卡
            onTabCloseOther: function () {
                nthTabs.on("click", '.tab-close-other', function () {
                    methods.delOtherTab();
                });
                return event;
            },
            //关闭全部选项卡
            onTabCloseAll: function () {
                nthTabs.on("click", '.tab-close-all', function () {
                    methods.delAllTab();
                });
                return event;
            },
            //左滑选项卡
            onTabRollLeft: function () {
                nthTabs.on("click", '.roll-nav-left', function () {
                    if (methods.getAllTabWidth() <= settings.rollWidth) return false; //未超出可视区域宽度,不可滑动
                    var contentTab = $(this).parent().find('.content-tabs-container');
                    var margin_left_origin = contentTab.css('marginLeft').replace('px', '');
                    var margin_left_total = parseFloat(margin_left_origin) + methods.getMarginStep() + 40;
                    contentTab.css("margin-left", margin_left_total > 40 ? 40 : margin_left_total);
                });
                return event;
            },
            //右滑选项卡
            onTabRollRight: function () {
                nthTabs.on("click", '.roll-nav-right', function () {
                    if (methods.getAllTabWidth() <= settings.rollWidth) return false; //未超出可视区域宽度,不可滑动
                    var contentTab = $(this).parent().find('.content-tabs-container');
                    var margin_left_origin = contentTab.css('marginLeft').replace('px', '');
                    var margin_left_total = parseFloat(margin_left_origin) - methods.getMarginStep();
                    if (methods.getAllTabWidth() - Math.abs(margin_left_origin) <= settings.rollWidth) return false; //已无隐藏无需滚动
                    contentTab.css("margin-left", margin_left_total);
                });
                return event;
            },
            //选项卡清单
            onTabList: function () {
                nthTabs.on("click", '.right-nav-list', function () {
                    var tablist = methods.getTabList();
                    var html = [];
                    $.each(tablist, function (key, val) {
                        html.push('<li class="toggle-tab" data-id="' + val.id + '">' + val.title + '</li>');
                    });
                    nthTabs.find(".tab-list").html(html.join(''));
                });
                nthTabs.find(".tab-list-scrollbar").scrollbar();
                event.onTabListToggle();
                return event;
            },
            //清单下切换选项卡
            onTabListToggle: function () {
                nthTabs.on("click", '.toggle-tab', function () {
                    var tabId = $(this).data("id");
                    methods.setActTab(tabId).locationTab(tabId);
                });
                return event;
            },
            onTabHrefToggle: function () {
                nthTabs.on("click", 'a[data-toggle="tab"]', function () {
                    callback($(this).attr('href').replace('#', ''));
                });
                return event;
            }
        };
        methods.init();
        return methods;
    }
})(jQuery);