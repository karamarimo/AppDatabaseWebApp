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
import utility.AppDBConnection;

@SuppressWarnings("serial")
public class ReviewListServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		Integer aid = Integer.parseInt(request.getParameter("aid"));
		
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='table-popup.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY_WITH_POPUP.openingTag);

		out.println("<h3>レビュー一覧</h3>");
		out.println("<h5>アプリID: " + aid + " のすべてのレビュー</h5>");
		
		out.println("<table class='db-table table-popup'>");
		out.println("<thead><tr><th align='right'>レビューID</th>"
				+ "<th align='left'>投稿者名</th>"
				+ "<th align='left'>タイトル</th>"
				+ "<th align='right'>レーティング</th></tr></thead>");
		out.println("<tbody>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDBConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement(
					"SELECT rid,uname,rtitle,rrate "
					+ "FROM reviews NATURAL JOIN review_app NATURAL JOIN review_user NATURAL JOIN users "
					+ "WHERE aid = ?");
			stmt.setInt(1, aid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Integer rid = rs.getInt("rid");
				String uname = rs.getString("uname");
				String rtitle = rs.getString("rtitle");
				Integer rate = rs.getInt("rrate");

				out.println("<tr data-href='/review_edit?rid="+ rid + "'>");
				out.println("<td align='right'>" + rid + "</td>");
				out.println("<td align='left'>" + uname + "</td>");
				out.println("<td align='left'>" + rtitle + "</td>");
				out.println("<td align='right'>" + rate + "</td>");
				out.println("</tr>");
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
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
		out.println(AppDBPage.BODY_WITH_POPUP.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
