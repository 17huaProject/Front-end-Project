/**
 * 单选按钮&复选框
 * Created by Administrator on 2016-1-14.
 */

;(function ($) {
    'use strict';


    $.fn.jCheckbox = function (settings) {
        /* 默认参数 */
        var _defaults = {
            checkedClass: "z-checked", // 选中状态类名
            onChange: function (element) {} // onchange回调，返回当前选中项DOM元素组
        };

        var options = $.extend(_defaults, settings || {});
        var checkboxes = this;

        checkboxes.each(function () {
            var $checkbox = $(this);
			
            

            /*---- 添加事件 ----*/
            $checkbox.on("change", function () {
                $(this).toggleClass(options.checkedClass);
				
                options.onChange($(this));

            });
        });
		
		

	/*$('#radio1').on('change', function (e) {
			if (e.args.checked) {
				$('#submit').removeAttr("disabled");
			} else {
				$('#submit').attr({"disabled":"disabled"});
			}	
		*/
		
		
		
		
    };
})(jQuery);





