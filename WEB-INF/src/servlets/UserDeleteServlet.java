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
public class UserDeleteServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String uid = request.getParameter("uid");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement("SELECT uname FROM users WHERE uid = ?");
			stmt.setInt(1, Integer.parseInt(uid));
			ResultSet rs = stmt.executeQuery();
			String uname = null;
			while (rs.next()) {
				uname = rs.getString("uname");
			}
			rs.close();
			stmt.close();

			stmt = conn.prepareStatement("DELETE FROM users WHERE uid = ?");
			stmt.setInt(1, Integer.parseInt(uid));
			stmt.executeUpdate();
			stmt.close();
			
			out.println("以下のアカウントを削除しました。<br><br>");
			out.println("アカウントID: " + uid + "<br>");
			out.println("アカウント名: " + uname + "<br>");

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
