//回到顶部
$(window).scroll(function () {
    //创建一个变量存储当前窗口下移的高度
    var scroTop = $(window).scrollTop();
    //判断当前窗口滚动高度
    //如果大于100，则显示顶部元素，否则隐藏顶部元素
    if (scroTop > 100) {
        $('.return_top').fadeIn(500);
    } else {
        $('.return_top').fadeOut(500);
    }
});

(function ($) {
    $.fn.textSlider = function (settings) {
        settings = jQuery.extend({
            speed: "normal",
            line: 2,
            timer: 3000
        }, settings);
        return this.each(function () {
            $.fn.textSlider.scllor($(this), settings);
        });
    };
    $.fn.textSlider.scllor = function ($this, settings) {
        var ul = $("ul:eq(0)", $this);
        var timerID;
        var li = ul.children();
        var liHight = $(li[0]).height();
        var upHeight = 0 - settings.line * liHight;//滚动的高度；
        var scrollUp = function () {
            ul.animate({marginTop: upHeight}, settings.speed, function () {
                for (i = 0; i < settings.line; i++) {
                    ul.find("li:first", $this).appendTo(ul);
                }
                ul.css({marginTop: 0});
            });
        };
        var autoPlay = function () {
            timerID = window.setInterval(scrollUp, settings.timer);
        };
        var autoStop = function () {
            window.clearInterval(timerID);
        };
        //事件绑定
        ul.hover(autoStop, autoPlay).mouseout();
    };
})(jQuery);

$(document).ready(function () {
    /*$('#banner').easyFader();*/
    if ($(".swiper-container").length) {
        var swiper = new Swiper('.swiper-container', {
            spaceBetween: 30,
            centeredSlides: true,
            loop: true,
            autoplay: {
                delay: 2500,
                disableOnInteraction: false,
            },
            pagination: {
                el: '.swiper-pagination',
                clickable: true,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        });
    }

    var Sticky = new hcSticky('.pb-sidebar', {
        stickTo: '.pb-main',
        followScroll: false,
        queries: {
            768: {
                disable: true,
                stickTo: 'body'
            }
        }
    });
    /*回到顶部点击函数*/
    $('.return_top').click(function () {
        $("html,body").animate({scrollTop: 0}, "fast");
    });

    /*3D标签云*/
    tagcloud({
        selector: "#tagcloud",  //元素选择器
        fontsize: 15,       //基本字体大小, 单位px
        radius: 50,         //滚动半径, 单位px
        mspeed: "normal",   //滚动最大速度, 取值: slow, normal(默认), fast
        ispeed: "normal",   //滚动初速度, 取值: slow, normal(默认), fast
        direction: 135,     //初始滚动方向, 取值角度(顺时针360): 0对应top, 90对应left, 135对应right-bottom(默认)...
        keep: false          //鼠标移出组件后是否继续随鼠标滚动, 取值: false, true(默认) 对应 减速至初速度滚动, 随鼠标滚动
    });


});

window.onload = function () {
    var new_scroll_position = 0;
    var last_scroll_position;
    var header = document.getElementById("header");
    window.addEventListener('scroll', function (e) {
        last_scroll_position = window.scrollY;
        if (new_scroll_position < last_scroll_position && last_scroll_position > 55) {
            header.classList.remove("slideDown");
            header.classList.add("slideUp");

        } else if (new_scroll_position > last_scroll_position) {
            header.classList.remove("slideUp");
            header.classList.add("slideDown");
        }
        new_scroll_position = last_scroll_position;
    });

}
