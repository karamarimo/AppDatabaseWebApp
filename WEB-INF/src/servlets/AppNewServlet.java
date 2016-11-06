package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;

@SuppressWarnings("serial")
public class AppNewServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println(AppDBPage.HEAD.whole());		
		out.println(AppDBPage.BODY.openingTag);

		out.println("<h3>アプリ追加</h3>");
		out.println("<form action='app_add' method='POST'>");
		out.println("<span>開発者ID</span>");
		out.println("<input type='text' name='add_did' required>");
		out.println("<span>アプリ名</span>");
		out.println("<input type='text' name='add_name' required>");
		out.println("<span>バージョン</span>");
		out.println("<input type='text' name='add_version' required>");
		out.println("<span>価格</span>");
		out.println("<input type='number' name='add_price' min='0' required>");
		out.println("<span>リリース日</span>");
		out.println("<input type='date' name='add_release' required>");
		out.println("<span>説明</span>");
		out.println("<textarea name='add_description'></textarea>");
		out.println("<input type='submit' value='追加'>");
		out.println("</form>");

		out.println(AppDBPage.BODY.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
