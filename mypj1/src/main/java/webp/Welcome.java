package webp;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Welcome
 */
@WebServlet(urlPatterns={"/asyncservlet"}, asyncSupported=true)
public class Welcome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	String type="nologin";
	String id="fail";
	String name="null";

	final static String user="admin";
	final static String password="12345678";
	final static String db_url="jdbc:oracle:thin:@database-1.cfjl8y0qm7ts.us-east-2.rds.amazonaws.com:1521:ORCL";

	public Welcome() {
		super();

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//RequestDispatcher rd= request.getRequestDispatcher("welcome");
		//rd.forward(request, response);
		//rd.include(request, response);
		Connection conn = null;
		Statement st= null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			String js =request.getReader().readLine();
			Userid u=mapper.readValue(js,Userid.class);
			System.out.println(u.pw + "  " + u.un);

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection(db_url,user,password);
			st=conn.createStatement();
			String q="SELECT IDN, UTYPE, FN FROM UTB WHERE UNAME='"+u.un+"' and PW='"+u.pw+"'";
			ResultSet rs=st.executeQuery(q);
			while(rs.next()) {
				id=rs.getString(1);
				type=rs.getString(2);
				name=rs.getString(3);
				System.out.println(id);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Writer out = response.getWriter();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if(type.equals("nologin")) {
			out.write("noo");
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("id", id);
			session.setAttribute("name", name);
			if(type.equals("a")) {
				response.sendRedirect("Adm.html");
				type="nologin";
				name="null";
			}else {
				response.sendRedirect("Emp.html");
				type="nologin";
				name="null";
			}
		}
	}

}
