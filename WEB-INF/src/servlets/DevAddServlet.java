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
public class DevAddServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String dname = request.getParameter("dname");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT MAX(did) AS max_did FROM devs");
			
			int max_did = 0;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				max_did = rs.getInt("max_did");
			}
			stmt.close();
			rs.close();
			
			int did = max_did + 1;
			stmt = conn.prepareStatement(
					"INSERT INTO devs (did,dname) "
					+ "VALUES (?, ?)");
			stmt.setInt(1, did);
			stmt.setString(2, dname);
			stmt.executeUpdate();
			stmt.close();
			
			out.println("以下の開発者アカウントを追加しました。<br>");
			out.println("開発者ID: " + did + "<br>");
			out.println("開発者名: " + dname + "<br>");
			
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
