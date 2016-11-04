$(function() {
	$('.button-clear-cart').click(function(event) {
		Cookies.set('apps-in-cart', []);
		location.href = '/app_list_user';
	});
});