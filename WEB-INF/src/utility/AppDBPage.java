package utility;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AppDBPage {
	static public HtmlTag makeHead() {
		HtmlTag header = new HtmlTag(
				"<head>" 
				+ "<link rel='icon' href='/icon.png'>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"
				+ "<link rel='stylesheet' type='text/css' href='appdb.css'>"
				+ "<script type='text/javascript' src='https://code.jquery.com/jquery-3.1.1.min.js'></script>", 
				"</head>");
		return header;
	}
	
	static public HtmlTag makeTable() {
		HtmlTag table = new HtmlTag("<table class=table-compact>", "</table>");
		return table;
	}
	
	static public HtmlTag makeBody() {
		HtmlTag body = new HtmlTag(
				"<body>"
				+ "<div id='popup'><div id='popup-content'></div></div>"
				+ "<div id='wrapper'>"
				+ makeNavbar().whole()
				+ "<main id='main'>"
				+ makeJSWarning().whole(),
				"</main></div></body>");
		return body;
	}
	
	static public HtmlTag makeJSWarning() {
		return new HtmlTag(
				"<noscript>"
				+ "<div style='background-color: #F99;'>"
				+ "<h3>JavaScript Required</h3>"
				+ "Javascriptを有効にしてください。", 
				"</div></noscript>");
	}
	
	static public HtmlTag makeNavbar() {
		HtmlTag navbar = new HtmlTag(
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
				+ "</ul>"
				+ "</div>", 
				"</nav>");
		return navbar;
	}
	
	static public HtmlTag makeAddForm() {
		HtmlTag form = new HtmlTag(
				"<form id='add-form'>"
				+ "", 
				"</form>");
		return form;
	}
	
	static public Date getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new java.util.Date());
        return Date.valueOf(date);
	}
}
