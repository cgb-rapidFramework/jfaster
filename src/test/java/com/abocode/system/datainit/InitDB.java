package com.abocode.system.datainit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abocode.jfaster.web.system.domain.entity.*;
import com.abocode.jfaster.core.common.util.LogUtils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;



/**
 * @Description
 * @ClassName: JeecgInitDB
 * @author tanghan
 * @date 2013-7-19 下午04:24:51
 */

public class InitDB {

	private static Connection con=null;


	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		if(con == null){
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jeecg", "root","root");
		}
		return con;
	}

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration();
		String sql2="select * from t_s_base_user";
		String sql3="select * from t_s_depart";
		String sql4="select * from t_s_role";
		String sql6="select * from t_s_typegroup";
		String sql7 = "select * from t_s_function";
		String sql8 = "select * from t_s_type";
		String sql9 = "select * from t_s_log limit 100";
		Statement st=null;
		ResultSet rs=null;
		try {
			cfg.setDirectoryForTemplateLoading(new File("src/test"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template temp = cfg.getTemplate("init.ftl","UTF-8");
			con=getConnection();
			st=con.createStatement();

			Map root=new HashMap();
			rs=st.executeQuery(sql2);
			int i=1;
			List user = new ArrayList();
			while(rs.next())
			{
				User baseUser = new User();
				baseUser.setId(i+"");
				baseUser.setUserKey(rs.getString("userkey"));
				baseUser.setStatus(rs.getShort("status"));
				baseUser.setRealName(rs.getString("realname"));
				baseUser.setUserName(rs.getString("username"));
				baseUser.setPassword(rs.getString("password"));
				baseUser.setMobilePhone(rs.getString("mobilephone"));
				baseUser.setOfficePhone(rs.getString("officephone"));
				baseUser.setEmail(rs.getString("email"));
				user.add(baseUser);
				i++;
			}

			root.put("baseuser", user);


			rs=st.executeQuery(sql3);
			List dep = new ArrayList();
			i=1;
			while(rs.next())
			{
				Depart tsDepart = new Depart();
				tsDepart.setId(i+"");
				tsDepart.setDepartname(rs.getString("departname"));
				tsDepart.setDescription(rs.getString("description"));
				dep.add(tsDepart);
				i++;
			}
			root.put("depart", dep);


			rs=st.executeQuery(sql4);
			List role = new ArrayList();
			i=1;
			while(rs.next())
			{
				Role tsRole = new Role();
				tsRole.setId(i+"");
				tsRole.setRoleName(rs.getString("rolename"));
				tsRole.setRoleCode(rs.getString("rolecode"));
				role.add(tsRole);
				i++;
			}
			root.put("role", role);


			rs=st.executeQuery(sql6);
			List typegroup = new ArrayList();
			i=1;
			while(rs.next())
			{
				TypeGroup tsTypegroup = new TypeGroup();
				tsTypegroup.setId(i+"");
				tsTypegroup.setTypegroupname(rs.getString("typegroupname"));
				tsTypegroup.setTypegroupcode(rs.getString("typegroupcode"));
				typegroup.add(tsTypegroup);
				i++;
			}
			root.put("typegroup", typegroup);

			rs=st.executeQuery(sql7);
			List function = new ArrayList();
			i=1;
			while(rs.next())
			{
				Function tsFunction = new Function();
				tsFunction.setId(i+"");
				tsFunction.setFunctionName(rs.getString("functionName"));
				tsFunction.setFunctionUrl(rs.getString("functionUrl"));
				tsFunction.setFunctionLevel(rs.getShort("functionLevel"));
				tsFunction.setFunctionOrder(rs.getString("functionOrder"));
				function.add(tsFunction);
				i++;
			}
			root.put("menu", function);

			rs=st.executeQuery(sql8);
			List type = new ArrayList();
			i=1;
			while(rs.next())
			{
				Type tsType = new Type();
				tsType.setId(i+"");
				tsType.setTypename(rs.getString("typename"));
				tsType.setTypecode(rs.getString("typecode"));
				type.add(tsType);
				i++;
			}
			root.put("type", type);

			rs=st.executeQuery(sql9);
			List log = new ArrayList();
			i=1;
			while(rs.next())
			{
				Log slog = new Log();
				slog.setId(i+"");
				slog.setId(i+"");
				slog.setLogcontent(rs.getString("logcontent"));
				slog.setLoglevel(rs.getShort("loglevel"));
				slog.setBroswer(rs.getString("broswer"));
				slog.setNote(rs.getString("note"));
				slog.setOperatetime(rs.getTimestamp("operatetime"));
				slog.setOperatetype(rs.getShort("operatetype"));
				log.add(slog);
				i++;
			}
			root.put("log", log);
			Writer out = new OutputStreamWriter(new FileOutputStream("RepairServiceImpl.java"), "UTF-8");
			temp.process(root, out);
			out.flush();
			out.close();
			LogUtils.info("Successfull................");
		} catch (IOException e) {
			LogUtils.error(e.getMessage());
		}
	}
}
