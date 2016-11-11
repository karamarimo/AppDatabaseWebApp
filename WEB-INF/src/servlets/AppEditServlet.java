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
public class AppEditServlet extends HttpServlet {

	public void init() {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String aid = request.getParameter("aid");
		
		// no sidebar		
		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			out.println("<h2>アプリ編集</h2>");
			out.println("<form action='app_update' method='POST'>");
			out.println("<span class='label'>アプリID</span>");
			out.println("<span class='value'>" + aid + "</span>");
			out.println("<input type='hidden' name='update_aid' + value='" + aid + "'>");
			
			stmt = conn.prepareStatement("SELECT * FROM app_dev WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(aid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String did = rs.getString("did");
				out.println("<span class='label'>開発者ID</span>");
				out.println("<span class='value'>" + did + "</span>");
			}
			rs.close();
			stmt.close();
			
			stmt = conn.prepareStatement("SELECT * FROM apps WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(aid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("aname");
				String version = rs.getString("aversion");
				Integer price = rs.getInt("aprice");
				String release = rs.getString("arelease_date");
				String desc = rs.getString("adescription");
				
				out.println("<span class='label'>アプリ名</span>");
				out.println("<input type='text' name='update_name' required value='" + name + "'>");
				out.println("<span class='label'>バージョン</span>");
				out.println("<input type='text' name='update_version' required value='" + version + "'>");
				out.println("<span class='label'>価格</span>");
				out.println("<input type='number' name='update_price' required min='0' value='" + price + "'>");
				out.println("<span class='label'>リリース日</span>");
				out.println("<input type='date' name='update_release' required value='" + release + "'>");
				out.println("<span class='label'>説明</span>");
				out.println("<textarea name='update_description'>" + desc + "</textarea>");
			}
			rs.close();
			stmt.close();
			
			out.println("<input class='blue-button' type='submit' value='更新'>");
			out.println("</form>");

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

		out.println("<form action='app_delete' method='POST'>");
		out.println("<input type='hidden' name='delete_aid' value='" + aid + "'>");
		out.println("<input class='red-button' type='submit' value='削除'>");
		out.println("</form>");

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
