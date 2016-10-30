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
		String updateUID = request.getParameter("uid");
		String updateTitle = request.getParameter("rtitle");
		String updateRate = request.getParameter("rrate");
		String updateContent = request.getParameter("rcontent");

		out.println("<html>");
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			conn.setAutoCommit(false);
			
			stmt = conn.prepareStatement("UPDATE reviews SET "
					+ "rtitle = ?, rrate = ?, rcontent = ? "
					+ "WHERE rid = ?");
			stmt.setInt(4, Integer.parseInt(updateRID));
			stmt.setString(1, updateTitle);
			stmt.setInt(2, Integer.parseInt(updateRate));
			stmt.setString(3, updateContent);
			stmt.executeUpdate();
			stmt.close();
			
			stmt = conn.prepareStatement(
					"UPDATE review_user SET uid = ? WHERE rid = ?");
			stmt.setInt(1, Integer.parseInt(updateUID));
			stmt.setInt(2, Integer.parseInt(updateRID));
			stmt.executeUpdate();
			stmt.close();

			conn.commit();
			
			out.println("以下のレビューを更新しました。<br/><br/>");
			out.println("レビューID: " + updateRID + "<br/>");
			out.println("レビュー名: " + updateTitle + "<br/>");
		} catch (IllegalArgumentException e) {
			out.println("パラメーターの形式が正しくありません。");
			e.printStackTrace();
			if (conn != null) {
				try {
					System.err.println("transaction is being rolled back");
					conn.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		} catch (SQLException e) {
			out.println("エラーが発生しました。");
			out.println("<br>");
			out.println(e.getMessage());
			e.printStackTrace();
			if (conn != null) {
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

		out.println("<br/>");
		out.println("<a href=\"list\">トップページに戻る</a>");

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
