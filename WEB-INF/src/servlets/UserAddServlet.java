package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
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
public class UserAddServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String uname = request.getParameter("uname");
		String ubirth = request.getParameter("ubirth");
		String ugender = request.getParameter("ugender");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT MAX(uid) AS max_uid FROM users");
			
			int max_uid = 0;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				max_uid = rs.getInt("max_uid");
			}
			stmt.close();
			rs.close();
			
			int uid = max_uid + 1;
			stmt = conn.prepareStatement(
					"INSERT INTO users (uid,uname,ubirth,ugender) "
					+ "VALUES (?, ?, ?, CAST(? AS bit(1)))");
			stmt.setInt(1, uid);
			stmt.setString(2, uname);
			stmt.setDate(3, Date.valueOf(ubirth));;
			stmt.setString(4, ugender);
			stmt.executeUpdate();
			stmt.close();
			
			out.println("以下のアカウントを追加しました。<br>");
			out.println("アカウントID: " + uid + "<br>");
			out.println("アカウント名: " + uname + "<br>");
			
//			response.sendRedirect("/app_list_dev");
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
