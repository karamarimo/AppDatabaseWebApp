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
public class DeleteServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String deleteAID = request.getParameter("delete_aid");

		out.println("<html>");
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			stmt = conn.prepareStatement("SELECT * FROM apps WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			ResultSet rs = stmt.executeQuery();
			String name = null;
			while (rs.next()) {
				name = rs.getString("aname");
			}
			rs.close();
			stmt.close();

			stmt = conn.prepareStatement("DELETE FROM apps WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(deleteAID));
			stmt.executeUpdate();
			stmt.close();
			
			out.println("以下の商品を削除しました。<br/><br/>");
			out.println("商品ID: " + deleteAID + "<br/>");
			out.println("アプリ名: " + name + "<br/>");

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
