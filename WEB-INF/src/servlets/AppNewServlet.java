package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;
import utility.HtmlTag;

@SuppressWarnings("serial")
public class AppNewServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		
		out.println(AppDBPage.makeHead().whole());
		
		HtmlTag body = AppDBPage.makeBody();
		
		out.println(body.openingTag);

		out.println("<h3>アプリ追加</h3>");
		out.println("<form action='app_add' method='POST'>");
		out.println("開発者ID: ");
		out.println("<input type='text' name='add_did'>");
		out.println("<br>");
		out.println("アプリ名: ");
		out.println("<input type='text' name='add_name'>");
		out.println("<br>");
		out.println("バージョン: ");
		out.println("<input type='text' name='add_version'>");
		out.println("<br>");
		out.println("価格: ");
		out.println("<input type='number' name='add_price' min='0'>");
		out.println("<br>");
		out.println("リリース日: ");
		out.println("<input type='date' name='add_release'>");
		out.println("<br>");
		out.println("説明: ");
		out.println("<input type='text' name='add_description'>");
		out.println("<br>");
		out.println("<input type='submit' value='追加'>");
		out.println("</form>");

		out.println(body.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
