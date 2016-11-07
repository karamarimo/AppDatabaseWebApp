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
public class ReviewEditServlet extends HttpServlet {

	public void init() {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String rid = request.getParameter("rid");
		
		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			out.println("<h2>レビュー編集</h2>");
			out.println("<form action='review_update' method='POST'>");
			out.println("<span>レビューID</span>");
			out.println("<span>" + rid + "</span>");
			out.println("<input type='hidden' name='rid' + value='" + rid + "'>");
			
			stmt = conn.prepareStatement("SELECT * FROM review_user WHERE rid = ?");
			stmt.setInt(1, Integer.parseInt(rid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				out.println("<span>投稿者ユーザID</span>");
				out.println("<span>" + uid + "</span>");
			}
			rs.close();
			stmt.close();
			
			stmt = conn.prepareStatement("SELECT * FROM reviews WHERE rid = ?");
			stmt.setInt(1, Integer.parseInt(rid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String title = rs.getString("rtitle");
				int rate = rs.getInt("rrate");
				String content = rs.getString("rcontent");
				
				out.println("<span>タイトル</span>");
				out.println("<input type='text' name='rtitle' required value='" + title + "'>");
				out.println("<span>レーティング</span>");
				out.println("<input type='number' name='rrate' required min='1' max='5' value='" + rate + "'>");
				out.println("<span>内容</span>");
				out.println("<textarea name='rcontent'>" + content + "</textarea>");
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

		out.println("<form action='review_delete' method='POST'>");
		out.println("<input type='hidden' name='rid' value='" + rid + "'>");
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
