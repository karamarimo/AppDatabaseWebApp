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
import utility.AppDBConnection;

@SuppressWarnings("serial")
public class DevListServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String query = request.getParameter("query");
		Boolean searching = query != null && !query.isEmpty();
		
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='table-popup.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println(AppDBPage.BODY_WITH_POPUP.openingTag);

		out.println("<h2>開発者一覧</h2>");
		out.println("<form class='search-box' action='dev_list' method='GET'>");
		out.println("<i class='material-icons'>search</i>");
		if (searching) {
			out.println("<input type='search' name='query' placeholder='user name' value='"
					+ query + "'>");
		} else {
			out.println("<input type='search' name='query' placeholder='user name'>");			
		}
		out.println("</form>");

		out.println("<table class='db-table table-popup'>");
		out.println("<thead><tr><th align='right'>開発者ID</th><th align='left'>開発者名</th></tr></thead>");
		out.println("<tbody>");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDBConnection.getConnection(getServletContext());
			if (!searching) {
				stmt = conn.prepareStatement("SELECT * FROM devs ORDER BY did");
			} else {
				stmt = conn.prepareStatement(
						"SELECT * FROM devs "
						+ "WHERE UPPER(dname) LIKE UPPER(?) ORDER BY did");
				stmt.setString(1, "%" + query + "%");
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int did = rs.getInt("did");
				String name = rs.getString("dname");

				out.println("<tr data-href='/dev_edit?did="+ did + "'>");
				out.println("<td align='right'>" + did + "</td>");
				out.println("<td align='left'>" + name + "</td>");
				out.println("</tr>");
			}
			rs.close();
			stmt.close();
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
		out.println("</tbody>");
		out.println("</table");
		out.println(AppDBPage.BODY_WITH_POPUP.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
