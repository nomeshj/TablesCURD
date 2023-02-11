<%@ page import="java.sql.*" %>
<html>
<body>

<%
Class.forName("oracle.jdbc.driver.OracleDriver");  
 
Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");  

Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("select * from editabletables");  
%>
<form method="get">
<select name="tables">
<option value="none" selected disabled hidden>Select an Option</option>
<%
	while(rs.next())
	{
%>
	<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
<%
	}
%>
<input type="submit" value="show table"/>
</select>
</form>
</body>
</html>
