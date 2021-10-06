package webp;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;




@WebServlet(urlPatterns={"/ep"}, asyncSupported=true)
public class Employee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final static String user="admin";
	final static String password="12345678";
	final static String db_url="jdbc:oracle:thin:@database-1.cfjl8y0qm7ts.us-east-2.rds.amazonaws.com:1521:ORCL";
	Statement st=null;
	Connection conn=null;
    public Employee() throws ClassNotFoundException, SQLException {
        super();
		
        Class.forName("oracle.jdbc.driver.OracleDriver");
		this.conn=DriverManager.getConnection(db_url,user,password);
		this.st=conn.createStatement();

    }
    
    
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    	String id=null;
    	HttpSession sess= request.getSession(false);
    	if(sess==null) {
    		response.sendRedirect("firstpg.html");
    	}else {
    		id= sess.getAttribute("id").toString();
    		System.out.println(id);
    		String q="SELECT * FROM REB WHERE AUTHOR='"+id+"' order by 'TYPE'";
    		Map < String, List<String>> m = new HashMap<String, List<String>>();
    		Table table=new Table();
    		try {
    			ResultSet rs=st.executeQuery(q);
    			while(rs.next()) {
    				table.amount.add(rs.getString(1));
    				table.tsov.add(rs.getString(2));
    				table.tsub.add(rs.getString(3));
    				table.dis.add(rs.getString(4));
    				table.au.add(rs.getString(5));
    				table.rs.add(rs.getString(6));
    				table.status.add(rs.getString(7));
    				table.type.add(rs.getString(8));
    				table.idn.add(rs.getString(9));
    		     
    			}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            m.put("amount", table.amount);
	        m.put("tsov", table.tsov);
	        m.put("tsub",table.tsub);
	        m.put("dis", table.dis);
	        m.put("au",table.au);
	        m.put("rs", table.rs);
	        m.put("status",table.status);
	        m.put("type", table.type);
	        m.put("idn", table.idn);
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.enable(SerializationFeature.INDENT_OUTPUT);
    		String jsonInString = mapper.writeValueAsString(m);
    		response.getWriter().write(jsonInString);
    	}
		}
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess= request.getSession(false);
		String id= sess.getAttribute("id").toString();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	    LocalDateTime now = LocalDateTime.now();
	    
	    
	    ObjectMapper mapper = new ObjectMapper();
		String js =request.getReader().readLine();
		Newrb nrb=mapper.readValue(js,Newrb.class);
		System.out.println(nrb.amount+nrb.dis+nrb.opt);
		try {
			String sst= "INSERT INTO REB(AMOUNT, TSUB, DESCR,AUTHOR,TYPE) VALUES (?,?,?,?,?)";
			PreparedStatement pst=conn.prepareStatement(sst);{
				pst.setString(1,nrb.amount);
				pst.setString(2,dtf.format(now)); 
				pst.setString(3,nrb.dis); 
				pst.setString(4,id); 
				pst.setString(5,nrb.opt); 
				pst.executeUpdate(); 
			}}catch (SQLException e) {
				e.printStackTrace();
			}
	}

}
