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
public class UserDetailServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String uid = request.getParameter("uid");
				
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='table-popup.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY_WITH_POPUP.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT * FROM users WHERE uid = ?");
			ResultSet rs = null;
			
			out.println("<h2>アカウント詳細</h2>");
			out.println("アカウントID: " + uid);
			out.println("<br>");
			
			stmt.setInt(1, Integer.parseInt(uid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("uname");
				String birth = rs.getDate("ubirth").toString();
				String gender = rs.getBoolean("ugender") ? "女性" : "男性"; 

				
				out.println("アプリ名: " + name);
				out.println("<br>");
				out.println("誕生日: " + birth);
				out.println("<br>");
				out.println("性別: " + gender);
			}
			rs.close();
			stmt.close();
			
			out.println("<form action='user_edit' method='GET'>");
			out.println("<input type='hidden' name='uid' value='" + uid + "'>");
			out.println("<input type='submit' value='編集'>");
			out.println("</form>");
			
			// my apps
			out.println("<h2>所持アプリ</h2>");
			out.println("<table class='db-table table-popup'>");
			out.println("<thead><tr><th>アプリID</th><th>アプリ名</th></tr></thead>");
			out.println("<tbody>");
			stmt = conn.prepareStatement(
					"SELECT aid, aname "
					+ "FROM user_app_distinct_view NATURAL JOIN apps "
					+ "WHERE uid = ?");
			stmt.setInt(1, Integer.parseInt(uid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer aid = rs.getInt("aid");
				String aname = rs.getString("aname");
				out.println("<tr data-href='/app_detail?aid="+ aid + "'>");
				out.println("<td align='right'>" + aid + "</td>");
				out.println("<td align='left'>" + aname + "</td>");
				out.println("</tr>");
			}
			out.println("</tbody>");
			out.println("</table>");
			
			// my reviews
			out.println("<h2>投稿したレビュー</h2>");
			out.println("<table class='db-table table-popup'>");
			out.println("<thead><tr><th>レビューID</th><th>アプリ名</th><th>タイトル</th></tr></thead>");
			out.println("<tbody>");
			stmt = conn.prepareStatement(
					"SELECT rid, aname, rtitle "
					+ "FROM review_user NATURAL JOIN reviews NATURAL JOIN review_app NATURAL JOIN apps "
					+ "WHERE uid = ?");
			stmt.setInt(1, Integer.parseInt(uid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer rid = rs.getInt("rid");
				String aname = rs.getString("aname");
				String rtitle = rs.getString("rtitle");
				out.println("<tr data-href='/review_edit?rid="+ rid + "'>");
				out.println("<td align='right'>" + rid + "</td>");
				out.println("<td align='left'>" + aname + "</td>");
				out.println("<td align='left'>" + rtitle + "</td>");
				out.println("</tr>");
			}
			out.println("</tbody>");
			out.println("</table>");
			
		} catch (Exception e) {
			out.println("エラーが発生しました。");
			out.println("<br>");
			out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

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
