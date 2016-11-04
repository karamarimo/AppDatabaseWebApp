package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
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
public class AppAddServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String did = request.getParameter("add_did");
		String addName = request.getParameter("add_name");
		String addVersion = request.getParameter("add_version");
		String addPrice = request.getParameter("add_price");
		String addRelease = request.getParameter("add_release");
		String addDescription = request.getParameter("add_description");

		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement selectAID = null;
		PreparedStatement insertApp = null;
		PreparedStatement insertAppDev = null;
		Boolean updating = false;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			selectAID = conn.prepareStatement("SELECT MAX(aid) AS max_aid FROM apps");
			
			int max_aid = 0;
			ResultSet rs = selectAID.executeQuery();
			while (rs.next()) {
				max_aid = rs.getInt("max_aid");
			}
			selectAID.close();
			rs.close();
			
			// put 2 statements into one transaction
			conn.setAutoCommit(false);
			updating = true;
			
			int addAID = max_aid + 1;
			insertApp = conn.prepareStatement(
					"INSERT INTO apps (aid,aname,aversion,aprice,arelease_date,adescription) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");
			insertApp.setInt(1, addAID);
			insertApp.setString(2, addName);
			insertApp.setString(3, addVersion);
			insertApp.setInt(4, Integer.parseInt(addPrice));
			insertApp.setDate(5, Date.valueOf(addRelease));
			insertApp.setString(6, addDescription);
			insertApp.executeUpdate();
			
			insertAppDev = conn.prepareStatement(
					"INSERT INTO app_dev (aid,did)"
					+ "VALUES (?, ?)");
			insertAppDev.setInt(1, addAID);
			insertAppDev.setInt(2, Integer.parseInt(did));
			insertAppDev.executeUpdate();
			
			// commit transaction
			conn.commit();
			
			out.println("以下のアプリを追加しました。<br>");
			out.println("アプリID: " + addAID + "<br>");
			out.println("アプリ名: " + addName + "<br>");
			
//			response.sendRedirect("/app_list_dev");
		} catch (Exception e) {
			out.println("エラーが発生しました。");
			out.println("<br>");
			out.println(e.getMessage());
			e.printStackTrace();
			if (updating && conn != null) {
				try {
					System.err.println("transaction is being rolled back");
					conn.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		} finally {
			try {
				if (selectAID != null) {
					selectAID.close();
				}
				if (insertApp != null) {
					insertApp.close();
				}
				if (insertAppDev != null) {
					insertAppDev.close();
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
