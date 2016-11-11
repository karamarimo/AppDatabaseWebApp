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
public class DevEditServlet extends HttpServlet {

	public void init() {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String did = request.getParameter("did");
		
		// no sidebar		
		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			out.println("<h2>開発者情報編集</h2>");
			out.println("<form action='dev_update' method='POST'>");
			out.println("<span class='label'>開発者ID</span>");
			out.println("<span class='value'>" + did + "</span>");
			out.println("<input type='hidden' name='did' + value='" + did + "'>");
			
			stmt = conn.prepareStatement("SELECT * FROM devs WHERE did = ?");
			stmt.setInt(1, Integer.parseInt(did));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("dname");
				
				out.println("<span class='label'>開発者名</span>");
				out.println("<input type='text' name='dname' required value='" + name + "'>");
			}
			rs.close();
			stmt.close();
			
			out.println("<input class='blue-button' type='submit' value='更新'>");
			out.println("</form>");
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
		
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
