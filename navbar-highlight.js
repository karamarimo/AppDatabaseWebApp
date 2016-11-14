$(function() {
	$('#nav li').each(function(i) {
		if ($(this).find('a').attr('href') === window.location.pathname) {
			$(this).addClass('active-link');
		}
	});
});