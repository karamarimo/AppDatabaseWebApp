
public class AppDBPage {
	static public HtmlTag makeHead() {
		HtmlTag header = new HtmlTag(
				"<head>" 
				+ "<title>AppDB</title>"
				+ "<link rel='icon' href='/icon.png'>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"
				+ "<link rel='stylesheet' type='text/css' href='appdb.css'>"
				+ "<script src='https://code.jquery.com/jquery-3.1.1.min.js'></script>"
				+ "<script type='text/javascript' src='appdb.js'></script>", 
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
				+ "<nav id='nav'>"
				+ "<div id='nav-wrapper'>"
				+ "<h3>Apps</h3>"
				+ "<ul>"
				+ "<li><a href='/list'>List</a></li>"
				+ "</ul>"
				+ "</div>"
				+ "</nav>"
				+ "<main id='main'>",
				"</main></div></body>");
		return body;
	}
	
	static public HtmlTag makeAddForm() {
		HtmlTag form = new HtmlTag(
				"<form id='add-form'>"
				+ "", 
				"</form>");
		return form;
	}
}
