package webp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebServlet(urlPatterns={"/if"}, asyncSupported=true)
public class Info extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	final static String user="admin";
	final static String password="12345678";
	final static String db_url="jdbc:oracle:thin:@database-1.cfjl8y0qm7ts.us-east-2.rds.amazonaws.com:1521:ORCL";
	Statement st=null;
	Connection conn=null;
    public Info() throws ClassNotFoundException, SQLException {
        super();
        Class.forName("oracle.jdbc.driver.OracleDriver");
		this.conn=DriverManager.getConnection(db_url,user,password);
		this.st=conn.createStatement();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession sess= request.getSession(false);
    	if(sess==null) {
    		response.sendRedirect("firstpg.html");
    	}else {
    		String id= sess.getAttribute("id").toString();
    		String q="SELECT * FROM UTB WHERE IDN='"+id+"'";
    		Map < String, String> m = new HashMap<String, String>();
    		try {
    			ResultSet rs=st.executeQuery(q);
    			while(rs.next()) {
    				m.put("user",rs.getString(1));
    				m.put("type",rs.getString(2));
    				m.put("pw",rs.getString(3));
    				m.put("id",rs.getString(4));
    				m.put("fn",rs.getString(5));
    				m.put("ln",rs.getString(6));
    				m.put("email",rs.getString(7));
    				m.put("role",rs.getString(8));
    		     }
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.enable(SerializationFeature.INDENT_OUTPUT);
    		String jsonInString = mapper.writeValueAsString(m);
    		response.getWriter().write(jsonInString);
    	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess= request.getSession(false);
		if(sess==null) {
    		response.sendRedirect("firstpg.html");
    	}else {
    		String id= sess.getAttribute("id").toString();
    		ObjectMapper mapper = new ObjectMapper();
			String js =request.getReader().readLine();
			Userid u=mapper.readValue(js,Userid.class);
			System.out.println(u.pw + "  " + u.un);
			String q="UPDATE UTB SET "+u.pw+"='"+u.un+"' where IDN='"+id+"'";
			try {
				st.executeQuery(q);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	}

}
