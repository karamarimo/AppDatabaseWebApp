$(function() {
	$('.button-delete-item').click(function(event) {
		var cart = Cookies.getJSON('apps-in-cart');
		var itemid = $(this).attr('data-aid');
		if (cart.constructor === Array && itemid != null) {
			var idx = cart.indexOf(Number(itemid));
			if (idx > -1) {
				console.log('removing item from cart...');
				cart.splice(idx, 1);
				Cookies.set('apps-in-cart', cart);
				location.reload();
			} else {
				console.log(itemid + ' is not in ' + cart);
			}
		}
	});
});