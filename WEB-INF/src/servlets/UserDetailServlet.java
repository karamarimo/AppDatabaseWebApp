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
			conn = AppDBConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT * FROM users WHERE uid = ?");
			ResultSet rs = null;
			
			out.println("<h2>アカウント詳細</h2>");
			out.println("<span class='label'>アカウントID</span>");
			out.println("<span class='value'>" + uid + "</span>");
			
			stmt.setInt(1, Integer.parseInt(uid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("uname");
				String birth = rs.getDate("ubirth").toString();
				String gender = rs.getBoolean("ugender") ? "女性" : "男性"; 
				
				out.println("<span class='label'>アプリ名</span>");
				out.println("<span class='value'>" + name + "</span>");
				out.println("<span class='label'>誕生日</span>");
				out.println("<span class='value'>" + birth + "</span>");
				out.println("<span class='label'>性別</span>");
				out.println("<span class='value'>" + gender + "</span>");
			}
			rs.close();
			stmt.close();
			
			// edit button
			out.println("<form action='user_edit' method='GET'>");
			out.println("<input type='hidden' name='uid' value='" + uid + "'>");
			out.println("<input class='blue-button' type='submit' value='編集'>");
			out.println("</form>");
			// delete button
			out.println("<form action='user_delete' method='POST'>");
			out.println("<input type='hidden' name='uid' value='" + uid + "'>");
			out.println("<input class='red-button' type='submit' value='削除'>");
			out.println("</form>");
			
			// my apps
			out.println("<h2>所持アプリ</h2>");
			out.println("<table class='db-table table-popup'>");
			out.println("<thead><tr><th align='right'>アプリID</th><th align='left'>アプリ名</th></tr></thead>");
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
			out.println("<thead><tr><th align='right'>レビューID</th><th align='left'>アプリ名</th><th align='left'>タイトル</th></tr></thead>");
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
