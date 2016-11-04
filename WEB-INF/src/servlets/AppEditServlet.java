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
			out.println("アプリID: " + aid);
			out.println("<input type='hidden' name='update_aid' + value='" + aid + "'>");
			out.println("<br>");
			
			stmt = conn.prepareStatement("SELECT * FROM app_dev WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(aid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String did = rs.getString("did");
				out.println("開発者ID: ");
				out.println("<input type='number' name='update_did' min='0' value='" + did + "'>");
				out.println("<br>");
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
			stmt.close();
			
			out.println("<input type='submit' value='更新'>");
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
		out.println("<input class='delete_button' type='submit' value='削除'>");
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
