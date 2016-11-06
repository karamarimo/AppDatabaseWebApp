$(function() {
	$('.table-popup tr').click(function (event) {
		var ref = $(this).attr('data-href');
		if (ref) {
			// window.location.href = ref;
			// var popup = $('#popup-content').get(0);
			// if (popup) {
			// 	popup.load(ref, function () {});
			// }
			$('#popup-content').load(ref, function() {
				$('#popup').fadeIn(300).css('display', 'flex');
				$('body').css('overflow', 'hidden');
			});
		}
	});	

	// close popup
	$('#popup').click(function(event) {
		if (event.target == this) {
			$(this).fadeOut(300);
			$('body').css('overflow', 'initial');
		}
	});

	// $('form').submit(function(event) {
	// 	history.pushstate({}, '', '');
	// 	return false;
	// });
});