package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;
import utility.AppDatabaseConnection;

@SuppressWarnings("serial")
public class AppCartServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String apps[] = request.getParameterValues("aid");
				
		if (apps == null) {
			out.println("<html>"
					+ AppDBPage.HEAD
					+ AppDBPage.BODY.openingTag
					+ "カートは空です。"
					+ AppDBPage.BODY.closingTag
					+ "</html>");
			return;
		}
		
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='js.cookie.js'></script>");
		out.println("<script type='text/javascript' src='cart-clear-button.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY.openingTag);
		
		out.println("<h2>カート内のアプリ</h2>");
		out.println("<table class='db-table'>");
		out.println("<thead><tr><th>アプリID</th><th>名前</th><th>価格</th></tr></thead>");
		out.println("<tbody>");
		
		Integer total = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT aid,aname,aprice FROM apps WHERE aid = ?");
			ResultSet rs = null;
			
			for (String aid : apps) {
				stmt.setInt(1, Integer.parseInt(aid));
				rs = stmt.executeQuery();
				while (rs.next()) {
					String name = rs.getString("aname");
					Integer price = rs.getInt("aprice");
					
					out.println("<tr>");
					out.println("<td align='right'>" + aid + "</td>");
					out.println("<td align='left'>" + name + "</td>");
					out.println("<td align='right'>" + price + "</td>");
					out.println("</tr>");
					
					total += price;
				}
				rs.close();
			}
			
		} catch (Exception e) {
			out.println("エラーが発生しました。");
			out.println("<br>");
			out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("</tbody>");
		out.println("</table>");
		
		out.println("<div>合計: ¥" + total + "</div>");
		
		// purchase form
		out.println("<form action='app_purchase' method='POST'>");
		out.println("<div>購入者のアカウントID"
				+ "<input type='number' name='uid' min='0' required></div>");
		for (String aid: apps) {
			out.println("<input type='hidden' name='aid' value='" + aid + "'>");			
		}
		out.println("<input type='submit' value='購入'>");
		out.println("</form>");
		
		// clear button
		out.println("<button class='button-clear-cart'>カートを空にする</button>");
		
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
