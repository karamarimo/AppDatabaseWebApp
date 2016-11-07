package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;
import utility.AppDatabaseConnection;

@SuppressWarnings("serial")
public class ReviewUpdateServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String updateRID = request.getParameter("rid");
		String updateTitle = request.getParameter("rtitle");
		String updateRate = request.getParameter("rrate");
		String updateContent = request.getParameter("rcontent");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement("UPDATE reviews SET "
					+ "rtitle = ?, rrate = ?, rcontent = ? "
					+ "WHERE rid = ?");
			stmt.setInt(4, Integer.parseInt(updateRID));
			stmt.setString(1, updateTitle);
			stmt.setInt(2, Integer.parseInt(updateRate));
			stmt.setString(3, updateContent);
			stmt.executeUpdate();
			stmt.close();
			
			out.println("レビューを更新しました。<br/><br/>");
			out.println("レビューID: " + updateRID + "<br/>");
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
