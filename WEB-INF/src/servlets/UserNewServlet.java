package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;

@SuppressWarnings("serial")
public class UserNewServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println(AppDBPage.HEAD.whole());		
		out.println(AppDBPage.BODY.openingTag);

		out.println("<h3>アカウント追加</h3>");
		out.println("<form action='user_add' method='POST'>");
		out.println("<span class='label'>アカウント名</span>");
		out.println("<input type='text' name='uname' required>");
		out.println("<span class='label'>誕生日</span>");
		out.println("<input type='date' name='ubirth' required>");
		out.println("<span class='label'>性別</span>");
		out.println("<select name='ugender' required>");
		out.println("<option value='0' selected>男性</option>");
		out.println("<option value='1'>女性</option>");
		out.println("</select>");
		out.println("<input class='blue-button' type='submit' value='追加'>");
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
