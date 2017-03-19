package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




/** 
 * @Description 
 * @ClassName: JeecgInitDB
 * @author tanghan
 * @date 2013-7-19 下午04:24:51  
 */

public class JeecgInitDB {
   
    private static Connection con=null;
    
    
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		if(con == null){
			Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jeecg", "root","root");
		}
		return con;
	}
	

}
