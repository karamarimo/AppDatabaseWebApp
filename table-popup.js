$(function() {
	var fading = false;
	$('.table-popup tr').click(function (event) {
		var ref = $(this).attr('data-href');
		if (ref && !fading) {
			// window.location.href = ref;
			// var popup = $('#popup-content').get(0);
			// if (popup) {
			// 	popup.load(ref, function () {});
			// }
			fading = true;
			$('#popup-content').load(ref, function() {
				$('#popup').fadeIn(300, function() {
					fading = false;
				}).scrollTop(0).css('display', 'flex');
				$('body').css('overflow', 'hidden');
			});
		}
	});	

	// close popup
	$('#popup').click(function(event) {
		if (event.target == this && !fading) {
			fading = true;
			$(this).fadeOut(300, function(){
				fading = false;
			});
			$('body').css('overflow', 'initial');
		}
	});

	// $('form').submit(function(event) {
	// 	history.pushstate({}, '', '');
	// 	return false;
	// });
});