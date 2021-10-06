package webp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

@WebServlet(urlPatterns={"/mp"}, asyncSupported=true)
public class Manager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static String user="admin";
	final static String password="12345678";
	final static String db_url="jdbc:oracle:thin:@database-1.cfjl8y0qm7ts.us-east-2.rds.amazonaws.com:1521:ORCL";
	Statement rss1=null;
	Statement rst1=null;
	Statement rt=null;
	Connection conn=null;
	
	
    public Manager() throws ClassNotFoundException, SQLException {
        super();
        System.out.println("uue");
        Class.forName("oracle.jdbc.driver.OracleDriver");
 		this.conn=DriverManager.getConnection(db_url,user,password);
 		System.out.println("uur");
 		this.rst1=conn.createStatement();
 		this.rss1=conn.createStatement();
 		this.rt=conn.createStatement();
 		System.out.println("uua");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id=null;
    	HttpSession sess= request.getSession(false);
    	if(sess==null) {
    		response.sendRedirect("firstpg.html");
    	}else {
    		System.out.println("uu");
    		id= sess.getAttribute("id").toString();
    		ObjectMapper mapper = new ObjectMapper();
			String js =request.getReader().readLine();
			Userid u=mapper.readValue(js,Userid.class);
			System.out.println(u.pw);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();
		    String ap="approved";
		    if(u.un.equals("")) {
		    	ap="rejected";
		    }
			String q="UPDATE REB SET TSOV='"+dtf.format(now)+"', STATUS='"+ap+"', RSVER='"+id+"' where IDN='"+u.pw+"'";

			try {
				rt.executeQuery(q);

			} catch (SQLException e) {
				e.printStackTrace();
				}
			}
    	
    	
    	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	HttpSession sess= request.getSession(false);
    	if(sess==null) {
    		response.sendRedirect("firstpg.html");
    	}else {
    		System.out.println("uu");
    		ObjectMapper mapper = new ObjectMapper();
    		String value = request.getParameter("foo");
			System.out.println(value);
			String qq;
			String q;
			if(value.equals("")) {
				q="SELECT * FROM UTB";
				qq="SELECT * FROM REB";
			}else {
				q="SELECT * FROM UTB where IDN='"+value+"'";
				qq="SELECT * FROM REB where AUTHOR='"+value+"'";
			}
			System.out.println(qq);
			System.out.println(q);
			List<String> amount=new ArrayList<String>();
			List<String> tsov=new ArrayList<String>();
			List<String> tsub=new ArrayList<String>();
			List<String> dis=new ArrayList<String>();
			List<String> au=new ArrayList<String>();
			List<String> rs=new ArrayList<String>();
			List<String> status=new ArrayList<String>();
			List<String> type=new ArrayList<String>();
			List<String> idns=new ArrayList<String>();
			
			List<String> uname=new ArrayList<String>();
			List<String> utype=new ArrayList<String>();
			List<String> pw=new ArrayList<String>();
			List<String> Idn=new ArrayList<String>();
			List<String> FN=new ArrayList<String>();
			List<String> LN=new ArrayList<String>();
			List<String> EM=new ArrayList<String>();
			List<String> R=new ArrayList<String>();
			try {
				ResultSet rst=rss1.executeQuery(q);
				ResultSet rss=rst1.executeQuery(qq);
				
				while(rss.next()) {
    				amount.add(rss.getString(1));
    				tsov.add(rss.getString(2));
    				tsub.add(rss.getString(3));
    				dis.add(rss.getString(4));
    				au.add(rss.getString(5));
    				rs.add(rss.getString(6));
    				status.add(rss.getString(7));
    				type.add(rss.getString(8));
    				idns.add(rss.getString(9));
    		     }
				
				while(rst.next()) {
					uname.add(rst.getString(1));
    				utype.add(rst.getString(2));
    				pw.add(rst.getString(3));
    				Idn.add(rst.getString(4));
    				FN.add(rst.getString(5));
    				LN.add(rst.getString(6));
    				EM.add(rst.getString(7));
    				R.add(rst.getString(8));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		Map <String, List<String>> g = new HashMap<String, List<String>>();
		g.put("amount", amount);
        g.put("tsov", tsov);
        g.put("tsub",tsub);
        g.put("dis", dis);
        g.put("au",au);
        g.put("rs", rs);
        g.put("status",status);
        g.put("type", type);
        g.put("idns", idns);
        
        g.put("uname",uname);
        g.put("utype", utype);
        g.put("Idn",Idn);
        g.put("FN", FN);
        g.put("LN",LN);
        g.put("EM", EM);
        g.put("R", R);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String jsonInString = mapper.writeValueAsString(g);
		response.getWriter().write(jsonInString);
    	}
	}
}


