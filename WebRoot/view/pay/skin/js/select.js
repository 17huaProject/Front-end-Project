// JavaScript Document
$(function () {
	var opt = {
			'select': {
				preset: 'select'
			},
		}
	$("#bank").mobiscroll($.extend(opt['select'],  {
		theme: 'ios7',
		mode: 'scroller',
		display: 'bottom',
		animate: ''
	}));
});
	
$(function () {
	var opt = {
			'select': {
				preset: 'select'
			},
		}
	$("#package").mobiscroll($.extend(opt['select'],  {
		theme: 'ios7',
		mode: 'scroller',
		display: 'bottom',
		animate: ''
	}));
});	

$(function () {
	var opt = {
			'select': {
				preset: 'select'
			},
		}
	$("#sex").mobiscroll($.extend(opt['select'],  {
		theme: 'ios7',
		mode: 'scroller',
		display: 'bottom',
		animate: ''
	}));
});	


$(function () {
	var opt = {
			'select': {
				preset: 'select'
			},
		}
	$("#pro").mobiscroll($.extend(opt['select'],  {
		theme: 'ios7',
		mode: 'scroller',
		display: 'bottom',
		animate: ''
	}));
});	
$(function () {
	var opt = {
			'select': {
				preset: 'select'
			},
		}
	$("#card").mobiscroll($.extend(opt['select'],  {
		theme: 'ios7',
		mode: 'scroller',
		display: 'bottom',
		animate: ''
	}));
});	
	
$(function () {
            var curr = new Date().getFullYear();
            var opt = {
                'date': {
                    preset: 'date',
                    invalid: { daysOfWeek: [0, 6], daysOfMonth: ['5/1', '12/24', '12/25'] }
                },
                'datetime': {
                    preset: 'datetime',
                    minDate: new Date(2012, 3, 10, 9, 22),
                    maxDate: new Date(2014, 7, 30, 15, 44),
                    stepMinute: 5
                },
                'time': {
                    preset: 'time'
                },
                'select': {
                    preset: 'select'
                },
                'select-opt': {
                    preset: 'select',
                    group: true,
                    width: 50
                }
            }

            $('.demo-test-date').bind('change', function() {
                var demo = "date";
                $('.demo-test-' + demo).scroller('destroy').scroller($.extend(opt[demo], {
                    theme: 'ios7',
					mode: 'scroller',
					display: 'bottom',
					animate: ''
                }));
            });

            $('.demo-test-date').trigger('change');
			

			$(".content").css({minHeight:$(window).height()-80});
        });