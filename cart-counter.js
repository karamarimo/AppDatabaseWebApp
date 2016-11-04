$(function() {
	var cart = Cookies.getJSON('apps-in-cart');
	var count = 0;
	if (cart != null) {
		count = cart.length;
	}
	$('.cart-count').text(count);

	$('.button-cart').click(function(event) {
		var cart = Cookies.getJSON('apps-in-cart');
		if (cart != null) {
			var query = "?" + cart.map(function(aid, i) {
				return 'aid=' + aid;
			}).join('&');
			window.location.href = '/app_cart' + query;
		} else {
			alert('カートは空です。')
		}
	});
});