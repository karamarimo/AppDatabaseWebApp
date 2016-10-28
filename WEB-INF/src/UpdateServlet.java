import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UpdateServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String updateAID = request.getParameter("update_aid");
		String updateName = request.getParameter("update_name");
		String updateVersion = request.getParameter("update_version");
		String updatePrice = request.getParameter("update_price");
		String updateRelease = request.getParameter("update_release");
		String updateDescription = request.getParameter("update_description");

		out.println("<html>");
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("UPDATE apps SET "
					+ "aname = ?, aversion = ?, aprice = ?, arelease_date = ?, adescription = ? "
					+ "WHERE aid = ?");
			stmt.setInt(6, Integer.parseInt(updateAID));
			stmt.setString(1, updateName);
			stmt.setString(2, updateVersion);
			stmt.setInt(3, Integer.parseInt(updatePrice));
			stmt.setDate(4, Date.valueOf(updateRelease));
			stmt.setString(5, updateDescription);
			stmt.executeUpdate();
			stmt.close();

			out.println("以下のアプリを更新しました。<br/><br/>");
			out.println("アプリID: " + updateAID + "<br/>");
			out.println("アプリ名: " + updateName + "<br/>");
		} catch (IllegalArgumentException e) {
			out.println("パラメーターの形式が正しくありません。");
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
