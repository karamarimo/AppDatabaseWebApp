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
public class AppDeleteServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String deleteAID = request.getParameter("delete_aid");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		Boolean updaating = false;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement("SELECT aname FROM apps WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			ResultSet rs = stmt.executeQuery();
			String name = null;
			while (rs.next()) {
				name = rs.getString("aname");
			}
			rs.close();
			stmt.close();

			conn.setAutoCommit(false);
			updaating = true;
			
			// DO THIS UPDATE FIRST
			// delete the app's reviews from reviews
			stmt = conn.prepareStatement("DELETE FROM reviews WHERE rid IN "
					+ "(SELECT rid FROM review_app WHERE aid = ?)");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			stmt.executeUpdate();
			stmt.close();
			
			// delete purchases that includes the app
			stmt = conn.prepareStatement("DELETE FROM purchase_app WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			stmt.executeUpdate();
			stmt.close();
			
			// delete from apps
			stmt = conn.prepareStatement("DELETE FROM apps WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			stmt.executeUpdate();
			stmt.close();
			
			// commit transaction
			conn.commit();

			out.println("以下のアプリを削除しました。<br><br>");
			out.println("アプリID: " + deleteAID + "<br>");
			out.println("アプリ名: " + name + "<br>");

		} catch (Exception e) {
			out.println("エラーが発生しました。");
			out.println("<br>");
			out.println(e.getMessage());
			e.printStackTrace();
			// if error thrown during transaction, roll it back
			if (updaating && conn != null) {
				try {
					System.err.println("transaction is being rolled back");
					conn.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.setAutoCommit(true);
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
