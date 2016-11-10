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
public class DevDeleteServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String did = request.getParameter("did");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement("SELECT dname FROM devs WHERE did = ?");
			stmt.setInt(1, Integer.parseInt(did));
			ResultSet rs = stmt.executeQuery();
			String uname = null;
			while (rs.next()) {
				uname = rs.getString("dname");
			}
			rs.close();
			stmt.close();

			stmt = conn.prepareStatement("DELETE FROM devs WHERE did = ?");
			stmt.setInt(1, Integer.parseInt(did));
			stmt.executeUpdate();
			stmt.close();
			
			out.println("以下の開発者アカウントを削除しました。<br><br>");
			out.println("開発者ID: " + did + "<br>");
			out.println("開発者名: " + uname + "<br>");

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
