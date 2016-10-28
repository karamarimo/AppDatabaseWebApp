package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.AppDBPage;
import utility.AppDatabaseConnection;
import utility.HtmlTag;

@SuppressWarnings("serial")
public class AppDetailServlet extends HttpServlet {

	public void init() throws ServletException {
		
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
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT * FROM apps WHERE aid = ?");
			ResultSet rs = null;
			
			out.println("<h2>アプリ詳細</h2>");
			out.println("アプリID: " + aid);
			out.println("<br>");
			
			stmt.setInt(1, Integer.parseInt(aid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("aname");
				String version = rs.getString("aversion");
				int price = rs.getInt("aprice");
				String release = rs.getString("arelease_date");
				String desc = rs.getString("adescription");
				
				out.println("アプリ名: " + name);
				out.println("<br>");
				out.println("バージョン: " + version);
				out.println("<br>");
				out.println("価格: " + price);
				out.println("<br>");
				out.println("リリース日: " + release);
				out.println("<br>");
				out.println("説明: " + desc);
			}
			rs.close();
			stmt.close();
			
			// get average rating
			stmt = conn.prepareStatement(
					"SELECT AVG(rrate) AS avg_rate FROM reviews NATURAL JOIN review_app "
					+ "WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(aid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Double avg_rate = rs.getDouble("avg_rate");
				if (rs.wasNull()) {
					out.println("<br>");
					out.println("平均レーティング: なし");
				} else {
					DecimalFormat df = new DecimalFormat("#.0");
					String avg = df.format(avg_rate);
					
					out.println("<br>");
					out.println("平均レーティング: " + avg);
				}
			}
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

		out.println("<form action='create_review' method='GET'>");
		out.println("<input type='hidden' name='review_aid' value='" + aid + "'>");
		out.println("<input type='submit' value='レビューを書く'>");
		out.println("</form>");
		
		out.println("<form action='list_review' method='GET'>");
		out.println("<input type='hidden' name='aid' value='" + aid + "'>");
		out.println("<input type='submit' value='レビューを見る'>");
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
