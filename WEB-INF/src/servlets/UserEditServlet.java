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
public class UserEditServlet extends HttpServlet {

	public void init() {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String uid = request.getParameter("uid");
		
		// no sidebar		
		out.println("<html>");
		out.println(AppDBPage.HEAD);
		out.println(AppDBPage.BODY.openingTag);

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			
			out.println("<h2>アカウント情報編集</h2>");
			out.println("<form action='user_update' method='POST'>");
			out.println("<span>アカウントID</span>");
			out.println("<span>" + uid + "</span>");
			out.println("<input type='hidden' name='uid' + value='" + uid + "'>");
			
			stmt = conn.prepareStatement("SELECT * FROM users WHERE uid = ?");
			stmt.setInt(1, Integer.parseInt(uid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("uname");
				String birth = rs.getString("ubirth");
				Boolean gender = rs.getBoolean("ugender");
				
				out.println("<span>アカウント名</span>");
				out.println("<input type='text' name='uname' required value='" + name + "'>");
				out.println("<span>誕生日</span>");
				out.println("<input type='date' name='ubirth' required value='" + birth + "'>");
				out.println("<span>性別</span>");
				out.println("<select name='ugender' required>");
				out.println("<option value='0'" + (gender ? "" : " selected") + ">男性</option>");
				out.println("<option value='1'" + (gender ? " selected" : "") + ">女性</option>");
				out.println("</select>");
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

		out.println(AppDBPage.BODY.closingTag);
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
