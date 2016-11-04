package utility;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AppDBPage {
	public static final HtmlTag HEAD = new HtmlTag(
				"<head>" 
				+ "<link rel='icon' href='/icon.png'>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"
				+ "<link rel='stylesheet' type='text/css' href='appdb.css'>"
				+ "<script type='text/javascript' src='https://code.jquery.com/jquery-3.1.1.min.js'></script>", 
				"</head>");

//	public static final HtmlTag TABLE = new HtmlTag("<table class=table-compact>", "</table>");
	
	public static final HtmlTag JS_WARNING_TAG = new HtmlTag(
				"<noscript>"
				+ "<div style='background-color: #F99;'>"
				+ "<h3>JavaScript Required</h3>"
				+ "Javascriptを有効にしてください。", 
				"</div></noscript>");
	
	public static final HtmlTag NAV_BAR = new HtmlTag(
				"<nav id='nav'>"
				+ "<div id='nav-wrapper'>"
				+ "<h3>開発者用</h3>"
				+ "<ul>"
				+ "<li><a href='/app_list_dev'>アプリ一覧</a></li>"
				+ "<li><a href='/app_new'>アプリ追加</a></li>"
				+ "</ul>"
				+ "<h3>一般ユーザ用</h3>"
				+ "<ul>"
				+ "<li><a href='/app_list_user'>アプリ一覧</a></li>"
				+ "<li><a href='/user_list'>アカウント一覧</a></li>"
				+ "</ul>"
				+ "</div>", 
				"</nav>");

	public static final HtmlTag BODY_WITH_POPUP = new HtmlTag(
			"<body>"
			+ "<div id='popup'><div id='popup-content'></div></div>"
			+ "<div id='wrapper'>"
			+ NAV_BAR.whole()
			+ "<main id='main'>"
			+ JS_WARNING_TAG.whole(),
			"</main></div></body>");
	
	public static final HtmlTag BODY = new HtmlTag(
			"<body>"
			+ "<div id='wrapper'>"
			+ NAV_BAR.whole()
			+ "<main id='main'>"
			+ JS_WARNING_TAG.whole(),
			"</main></div></body>");
	
	public static Date getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new java.util.Date());
        return Date.valueOf(date);
	}
}
