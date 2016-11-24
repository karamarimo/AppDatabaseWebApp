$(function() {
	$('.table-link tr').click(function (event) {
		var ref = $(this).attr('data-href');
		if (ref) {
			location.href = ref;
		}
	});
});