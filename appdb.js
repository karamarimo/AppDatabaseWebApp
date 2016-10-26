$(function() {
	$('.table-compact tr').click(function (event) {
		var ref = $(this).attr('data-href');
		console.log(ref + " is clicked!");
		if (ref) {
			// window.location.href = ref;
			// var popup = $('#popup-content').get(0);
			// if (popup) {
			// 	popup.load(ref, function () {});
			// }
			$('#popup-content').load(ref + ' #main', function() {
				$('#popup').fadeIn(300);
			});
			
		}
	});	

	$('#popup').click(function(event) {
		if (event.target == this) {
			$(this).fadeOut(300);
		}
	});
});