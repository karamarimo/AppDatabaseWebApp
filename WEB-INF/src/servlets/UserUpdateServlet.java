package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;
import utility.AppDatabaseConnection;

@SuppressWarnings("serial")
public class UserUpdateServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String uid = request.getParameter("uid");
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
			
			stmt = conn.prepareStatement("UPDATE users SET "
					+ "uname = ?, ubirth = ?, ugender = CAST(? AS bit(1)) "
					+ "WHERE uid = ?");
			stmt.setInt(4, Integer.parseInt(uid));
			stmt.setString(1, uname);
			stmt.setDate(2, Date.valueOf(ubirth));;
			stmt.setString(3, ugender);
			stmt.executeUpdate();
			stmt.close();
						
			out.println("アカウント情報を更新しました。<br>");
			out.println("アカウントID: " + uid + "<br>");
		} catch (IllegalArgumentException e) {
			out.println("パラメーターの形式が正しくありません。");
			e.printStackTrace();
		} catch (SQLException e) {
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
