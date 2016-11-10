package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;

@SuppressWarnings("serial")
public class DevNewServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println(AppDBPage.HEAD.whole());		
		out.println(AppDBPage.BODY.openingTag);

		out.println("<h3>開発者追加</h3>");
		out.println("<form action='dev_add' method='POST'>");
		out.println("<span>開発者名</span>");
		out.println("<input type='text' name='dname' required>");
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
