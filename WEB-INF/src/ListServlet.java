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
public class ListServlet extends HttpServlet {

	private String _hostname = null;
	private String _dbname = null;
	private String _username = null;
	private String _password = null;

	public void init() throws ServletException {
		// iniファイルから自分のデータベース情報を読み込む
		String iniFilePath = getServletConfig().getServletContext()
				.getRealPath("WEB-INF/le4db.ini");
		try {
			FileInputStream fis = new FileInputStream(iniFilePath);
			Properties prop = new Properties();
			prop.load(fis);
			_hostname = prop.getProperty("hostname");
			_dbname = prop.getProperty("dbname");
			_username = prop.getProperty("username");
			_password = prop.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		
		out.println(AppDBPage.makeHead().whole());
		
		HtmlTag body = AppDBPage.makeBody();
		HtmlTag table = AppDBPage.makeTable();
		
		out.println(body.openingTag);

		out.println("<h2>アプリ一覧</h2>");
		out.println("<form class='search-box' action='search' method='GET'>");
		out.println("<input type='text' name='search_name' placeholder='search for app...'>");
		out.println("<input type='submit' value='GO'>");
		out.println("</form>");

		out.println(table.openingTag);
		out.println("<thead><tr><th>アプリID</th><th>名前</th><th>価格</th></tr></thead>");
		out.println("<tbody>");
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM apps");
			while (rs.next()) {
				int aid = rs.getInt("aid");
				String name = rs.getString("aname");
				int price = rs.getInt("aprice");

				out.println("<tr data-href='/item?aid="+ aid + "'>");
				out.println("<td align='right'>" + aid + "</td>");
				out.println("<td align='left'>" + name + "</td>");
				out.println("<td align='right'>" + price + "</td>");
				out.println("</tr>");
			}
			rs.close();

		} catch (Exception e) {
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

		out.println("<h3>追加</h3>");
		out.println("<form action=\"add\" method=\"GET\">");
		out.println("商品名： ");
		out.println("<input type=\"text\" name=\"add_name\"/>");
		out.println("<br/>");
		out.println("価格： ");
		out.println("<input type=\"text\" name=\"add_price\"/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"追加\"/>");
		out.println("</form>");

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
