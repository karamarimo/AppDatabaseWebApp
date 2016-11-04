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
public class AppPurchaseServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String apps[] = request.getParameterValues("aid");
		String uid = request.getParameter("uid");
		
		if (apps == null) {
			out.println("<html>"
					+ AppDBPage.BODY.openingTag
					+ "不正なパラメーターです。"
					+ AppDBPage.BODY.closingTag
					+ "</html>");
			return;
		}

		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='js.cookie.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		String anames[] = new String[apps.length];
		Integer aprices[] = new Integer[apps.length];
		Boolean updaating = false;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			ResultSet rs = null;
			
			stmt = conn.prepareStatement("SELECT MAX(pid) AS max_pid FROM purchases");
			Integer max_pid = 0;
			rs = stmt.executeQuery();
			while (rs.next()) {
				max_pid = rs.getInt("max_pid");
			}
			rs.close();
			stmt.close();

			Integer pid = max_pid + 1;

			stmt = conn.prepareStatement("SELECT aname,aprice FROM apps WHERE aid = ?");
			for (int i = 0; i < apps.length; i++) {
				String aid = apps[i];
				stmt.setInt(1, Integer.parseInt(aid));
				rs = stmt.executeQuery();
				while (rs.next()) {
					anames[i] = rs.getString("aname");
					aprices[i] = rs.getInt("aprice");
				}
				rs.close();
			}
			stmt.close();
			
			conn.setAutoCommit(false);
			updaating = true;
			
			// insert into purchases
			stmt = conn.prepareStatement(
					"INSERT INTO purchases (pid,ptotal,pdate) "
					+ "VALUES (?, ?, ?)");
			Integer total = 0;
			for (Integer price : aprices) {
				total += price;
			}
			stmt.setInt(1, pid);
			stmt.setInt(2, total);
			stmt.setDate(3, AppDBPage.getCurrentDate());
			stmt.executeUpdate();
			stmt.close();
			
			// insert into purchase_app
			stmt = conn.prepareStatement(
					"INSERT INTO purchase_app (pid,aid) "
					+ "VALUES (?, ?)");
			for (int i = 0; i < apps.length; i++) {
				stmt.setInt(1, pid);
				stmt.setInt(2, Integer.parseInt(apps[i]));
				stmt.executeUpdate();				
			}
			stmt.close();
			
			// insert into purchase_user
			stmt = conn.prepareStatement(
					"INSERT INTO purchase_user (pid,uid) "
					+ "VALUES (?, ?)");
			stmt.setInt(1, pid);
			stmt.setInt(2, Integer.parseInt(uid));
			stmt.executeUpdate();
			stmt.close();
			
			// commit transaction
			conn.commit();
			
			out.println("アプリを購入しました。<br>");
			out.println("購入ID: " + pid + "<br>");
			out.println("合計金額: " + total + "<br>");
			out.println("アカウントID: " + uid + "<br>");
			
			// clear cookie only if the transaction finished successfully
			out.println("<script type='text/javascript' src='clear-cookie.js'></script>");
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
