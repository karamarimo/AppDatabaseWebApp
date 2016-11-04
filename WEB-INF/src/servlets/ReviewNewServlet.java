package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;

@SuppressWarnings("serial")
public class ReviewNewServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String aid = request.getParameter("aid");

		out.println("<html>");
		out.println(AppDBPage.HEAD);		
		out.println(AppDBPage.BODY.openingTag);

		out.println("<h3>新規レビュー</h3>");
		out.println("<form action='review_add' method='POST'>");
		out.println("アプリID: " + aid);
		out.println("<input type='hidden' name='aid' value='" + aid + "'>");
		out.println("<br>");
		out.println("ユーザID: ");
		out.println("<input type='text' name='uid'>");
		out.println("<br>");
		out.println("タイトル: ");
		out.println("<input type='text' name='rtitle'>");
		out.println("<br>");
		out.println("レーティング: ");
		out.println("<input type='number' name='rrate' min='1' max='5' value='5'>");
		out.println("<br>");
		out.println("内容: ");
		out.println("<input type='text' name='rcontent'>");
		out.println("<br>");
		out.println("<input type='submit' value='投稿'>");
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
