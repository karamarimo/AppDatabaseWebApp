import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ItemServlet extends HttpServlet {

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
		
		String aid = request.getParameter("aid");
		
		HtmlTag body = AppDBPage.makeBody();
		
		out.println("<html>");
		out.println(AppDBPage.makeHead().whole());
		out.println(body.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
					+ ":5432/" + _dbname, _username, _password);
			stmt = conn.prepareStatement("SELECT * FROM apps WHERE aid = ?");
			
			out.println("<form action='update' method='GET'>");
			out.println("アプリID： " + aid);
			out.println("<input type='hidden' name='update_pid' + value='" + aid + "'>");
			out.println("<br/>");
			
			stmt.setInt(1, Integer.parseInt(aid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("aname");
				String version = rs.getString("aversion");
				int price = rs.getInt("aprice");
				String release = rs.getString("arelease_date");
				String desc = rs.getString("adescription");
				
				out.println("アプリ名: ");
				out.println("<input type='text' name='update_name' value='" + name + "'>");
				out.println("<br>");
				out.println("バージョン: ");
				out.println("<input type='text' name='update_version' value='" + version + "'>");
				out.println("<br>");
				out.println("価格: ");
				out.println("<input type='number' name='update_price' min='0' value='" + price + "'>");
				out.println("<br>");
				out.println("リリース日: ");
				out.println("<input type='date' name='update_release' value='" + release + "'>");
				out.println("<br>");
				out.println("説明: ");
				out.println("<input type='text' name='update_description' value='" + desc + "'>");
				out.println("<br>");				
			}
			rs.close();
			
			out.println("<input type='submit' value='更新'>");
			out.println("</form>");

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

		out.println("<form action='delete' method='GET'>");
		out.println("<input type='hidden' name='delete_pid' value='" + aid + "'>");
		out.println("<input type='submit' value='削除'>");
		out.println("</form>");

//		out.println("<br/>");
//		out.println("<a href='list'>トップページに戻る</a>");

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
