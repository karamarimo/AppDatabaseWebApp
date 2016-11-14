package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
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

@SuppressWarnings("serial")
public class AppDetailServlet extends HttpServlet {

	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String aid = request.getParameter("aid");
				
		out.println("<html>");
		out.println(AppDBPage.HEAD.openingTag);
		out.println("<script type='text/javascript' src='js.cookie.js'></script>");
		out.println("<script type='text/javascript' src='add-to-cart.js'></script>");
		out.println(AppDBPage.HEAD.closingTag);
		out.println("<body>");

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = AppDatabaseConnection.getConnection(getServletContext());
			stmt = conn.prepareStatement("SELECT * FROM apps WHERE aid = ?");
			ResultSet rs = null;
			
			out.println("<h2>アプリ詳細</h2>");
			out.println("<span class='label'>アプリID</span>");
			out.println("<span class='value'>" + aid + "</span>");
			
			stmt.setInt(1, Integer.parseInt(aid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("aname");
				String version = rs.getString("aversion");
				int price = rs.getInt("aprice");
				String release = rs.getString("arelease_date");
				String desc = rs.getString("adescription");
				
				out.println("<span class='label'>アプリ名</span>");
				out.println("<span class='value'>" + name + "</span>");
				out.println("<span class='label'>バージョン</span>");
				out.println("<span class='value'>" + version + "</span>");
				out.println("<span class='label'>価格</span>");
				out.println("<span class='value'>" + price + "</span>");
				out.println("<span class='label'>リリース日</span>");
				out.println("<span class='value'>" + release + "</span>");
				out.println("<span class='label'>説明</span>");
				out.println("<span class='value'>" + desc + "</span>");
			}
			rs.close();
			stmt.close();
			
			// get average rating
			stmt = conn.prepareStatement(
					"SELECT AVG(rrate) AS avg_rate, COUNT(*) AS rcount "
					+ "FROM reviews NATURAL JOIN review_app "
					+ "WHERE aid = ?");
			stmt.setInt(1, Integer.parseInt(aid));
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer rcount = rs.getInt("rcount");
				Double avg_rate = rs.getDouble("avg_rate");
				
				// average rating
				out.println("<span class='label'>平均レーティング</span>");
				if (rs.wasNull()) {
					out.println("<span class='value'>なし</span>");
				} else {
					DecimalFormat df = new DecimalFormat("#.0");
					df.setRoundingMode(RoundingMode.HALF_UP);
					String avg = df.format(avg_rate);
					
					out.println("<span class='value'>" + avg + "</span>");
				}
				
				// see-all-reviews button showing review count
				out.println("<form action='review_list' method='GET'>");
				out.println("<input type='hidden' name='aid' value='" + aid + "'>");
				if (rcount == 0) {
					out.println("<input class='blue-button' type='submit' value='レビューなし' disabled>");
				} else {
					out.println("<input class='blue-button' type='submit' value='レビュー一覧("
							+ rcount + "件)'>");
				}
				out.println("</form>");
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

		out.println("<form action='review_new' method='GET'>");
		out.println("<input type='hidden' name='aid' value='" + aid + "'>");
		out.println("<input class='blue-button' type='submit' value='レビューを書く'>");
		out.println("</form>");
		
		out.println("<form class='form-add-to-cart'>");
		out.println("<input type='hidden' name='aid' value='" + aid + "'>");
		out.println("<input class='blue-button' type='submit' value='カートに入れる'>");
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
