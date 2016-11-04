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
import utility.HtmlTag;

@SuppressWarnings("serial")
public class ReviewEditServlet extends HttpServlet {

	public void init() {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String rid = request.getParameter("rid");
		
		HtmlTag body = AppDBPage.makeBody();
		
		out.println("<html>");
		out.println(AppDBPage.makeHead().whole());
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			out.println("<h2>レビュー編集</h2>");
			out.println("<form action='review_update' method='POST'>");
			out.println("レビューID: " + rid);
			out.println("<input type='hidden' name='rid' + value='" + rid + "'>");
			out.println("<br>");
			
			stmt = conn.prepareStatement("SELECT * FROM review_user WHERE rid = ?");
			stmt.setInt(1, Integer.parseInt(rid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				out.println("投稿者ユーザID: ");
				out.println("<input type='number' name='uid' min='0' value='" + uid + "'>");
				out.println("<br>");
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
				
				out.println("タイトル: ");
				out.println("<input type='text' name='rtitle' value='" + title + "'>");
				out.println("<br>");
				out.println("レーティング: ");
				out.println("<input type='number' name='rrate' min='1' max='5' value='" + rate + "'>");
				out.println("<br>");
				out.println("内容: ");
				out.println("<input type='text' name='rcontent' value='" + content + "'>");
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
