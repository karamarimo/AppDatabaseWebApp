package utility;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class AppDatabaseConnection {
	
	static public Connection getConnection(ServletContext ctx) throws SQLException {
		String _hostname = null;
		String _dbname = null;
		String _username = null;
		String _password = null;
		String iniFilePath = ctx.getRealPath("WEB-INF/le4db.ini");
		
		try {
			FileInputStream fis = new FileInputStream(iniFilePath);
			Properties prop = new Properties();
			prop.load(fis);
			_hostname = prop.getProperty("hostname");
			_dbname = prop.getProperty("dbname");
			_username = prop.getProperty("username");
			_password = prop.getProperty("password");
			
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connection conn = DriverManager.getConnection("jdbc:postgresql://" + _hostname
				+ ":5432/" + _dbname, _username, _password);		
		return conn;
	}
	
}
