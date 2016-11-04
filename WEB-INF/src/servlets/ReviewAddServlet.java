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

import utility.AppDatabaseConnection;

@SuppressWarnings("serial")
public class ReviewAddServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String aid = request.getParameter("aid");
		String uid = request.getParameter("uid");
		String rtitle = request.getParameter("rtitle");
		String rrate = request.getParameter("rrate");
		String rcontent = request.getParameter("rcontent");

		out.println("<html>");
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		Boolean updaating = false;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT MAX(rid) AS max_rid FROM reviews");
			
			int max_rid = 0;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				max_rid = rs.getInt("max_rid");
			}
			stmt.close();
			rs.close();
			
			// put 2 statements into one transaction
			conn.setAutoCommit(false);
			updaating = true;
			
			// insert into reviews
			Integer rid = max_rid + 1;
			stmt = conn.prepareStatement(
					"INSERT INTO reviews (rid,rtitle,rrate,rcontent) "
					+ "VALUES (?, ?, ?, ?)");
			stmt.setInt(1, rid);
			stmt.setString(2, rtitle);
			stmt.setInt(3, Integer.parseInt(rrate));
			stmt.setString(4, rcontent);
			stmt.executeUpdate();
			
			// insert into review_app
			stmt = conn.prepareStatement(
					"INSERT INTO review_app (rid,aid)"
					+ "VALUES (?, ?)");
			stmt.setInt(1, rid);
			stmt.setInt(2, Integer.parseInt(aid));
			stmt.executeUpdate();
			
			// insert into review_user
			stmt = conn.prepareStatement(
					"INSERT INTO review_user (rid,uid)"
					+ "VALUES (?, ?)");
			stmt.setInt(1, rid);
			stmt.setInt(2, Integer.parseInt(uid));
			stmt.executeUpdate();
			
			// commit transaction
			conn.commit();
			
			out.println("レビューを投稿しました。<br>");
			out.println("レビューID: " + rid + "<br>");
			
//			response.sendRedirect("/app_list_dev");
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
