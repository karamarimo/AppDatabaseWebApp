function closePopup() {
	$('#popup').fadeOut(300);
	$('body').css('overflow', 'initial');
}

function addCart(appid) {
	appid = parseInt(appid);
	var cart = Cookies.getJSON('apps-in-cart');
	if (cart == null) {
		// empty cart
		cart = [appid];
		Cookies.set('apps-in-cart', cart);
		updateCartButton(cart.length);
		closePopup();
	} else if (cart.indexOf(appid) >= 0) {
		// item already in cart
		alert('このアプリはすでにカート内に存在します。');
		return;
	} else {
		// item not in cart
		cart.push(appid);
		Cookies.set('apps-in-cart', cart);
		updateCartButton(cart.length);
		closePopup();
	}
}

function updateCartButton(count) {
	$('.cart-count').text(count);
}

$(function() {
	$('.form-add-to-cart').submit(function(event) {
		var param = ($(this).serializeArray())[0];
		if (param != null && param.name == 'aid') {
			addCart(param.value);
		}
		return false;
	});
});