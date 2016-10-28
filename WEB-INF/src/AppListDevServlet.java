import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AppListDevServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		
		HtmlTag head = AppDBPage.makeHead();
		HtmlTag body = AppDBPage.makeBody();
		HtmlTag table = AppDBPage.makeTable();
		
		out.println(head.openingTag);
		out.println("<script type='text/javascript' src='table-popup.js'></script>");
		out.println(head.closingTag);
		out.println(body.openingTag);

		out.println("<h2>アプリ一覧</h2>");
		out.println("<form class='search-box' action='search' method='GET'>");
		out.println("<input type='search' name='search_name' placeholder='search for app...'>");
		out.println("<input type='submit' value='GO'>");
		out.println("</form>");

		out.println(table.openingTag);
		out.println("<thead><tr><th>アプリID</th><th>名前</th><th>価格</th></tr></thead>");
		out.println("<tbody>");
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM apps ORDER BY aid");
			while (rs.next()) {
				int aid = rs.getInt("aid");
				String name = rs.getString("aname");
				int price = rs.getInt("aprice");

				out.println("<tr data-href='/app_edit?aid="+ aid + "'>");
				out.println("<td align='right'>" + aid + "</td>");
				out.println("<td align='left'>" + name + "</td>");
				out.println("<td align='right'>" + price + "</td>");
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
		out.println(table.closingTag);
		out.println(body.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
