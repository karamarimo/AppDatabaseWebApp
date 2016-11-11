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
public class UserListServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String query = request.getParameter("query");
		
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='table-link.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY.openingTag);

		out.println("<h2>アカウント一覧</h2>");
		out.println("<form class='search-box' action='user_list' method='GET'>");
		out.println("<input type='search' name='query' placeholder='search for user name...'>");
		out.println("</form>");

		out.println("<table class='db-table table-link'>");
		out.println("<thead><tr><th align='right'>アカウントID</th>"
				+ "<th align='left'>名前</th>"
				+ "<th align='left'>誕生日</th>"
				+ "<th align='left'>性別</th></tr></thead>");
		out.println("<tbody>");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			if (query == null || query.isEmpty()) {
				stmt = conn.prepareStatement("SELECT uid,uname,ubirth,ugender FROM users ORDER BY uid");
			} else {
				stmt = conn.prepareStatement(
						"SELECT uid,uname,ubirth,ugender FROM users "
						+ "WHERE UPPER(uname) LIKE UPPER(?) ORDER BY uid");
				stmt.setString(1, "%" + query + "%");
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String name = rs.getString("uname");
				String birth = rs.getDate("ubirth").toString();
				String gender = rs.getBoolean("ugender") ? "女性" : "男性"; 

				out.println("<tr data-href='/user_detail?uid="+ uid + "'>");
				out.println("<td align='right'>" + uid + "</td>");
				out.println("<td align='left'>" + name + "</td>");
				out.println("<td align='left'>" + birth + "</td>");
				out.println("<td align='left'>" + gender + "</td>");
				out.println("</tr>");
			}
			rs.close();
			stmt.close();
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
		out.println("</tbody>");
		out.println("</table");
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
