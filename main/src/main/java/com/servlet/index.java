package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class index
 */
public class index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public index() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		ServletContext context = getServletContext();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {

			out.write("<style>\r\n" + ".pagination {\r\n" + "  display: inline-block;\r\n" + "}\r\n" + "\r\n"
					+ ".pagination a {\r\n" + "  color: black;\r\n" + "  float: left;\r\n" + "  padding: 8px 16px;\r\n"
					+ "  text-decoration: none;\r\n" + "  transition: background-color .3s;\r\n"
					+ "  border: 1px solid #ddd;\r\n" + "  font-size: 22px;\r\n" + "}\r\n" + "\r\n"
					+ ".pagination a.active {\r\n" + "  background-color: #4CAF50;\r\n" + "  color: white;\r\n"
					+ "  border: 1px solid #4CAF50;\r\n" + "}\r\n" + "\r\n"
					+ ".pagination a:hover:not(.active) {background-color: #ddd;}\r\n" + "table {\r\n"
					+ "    border-collapse: collapse;\r\n" + "  }\r\n" + "  th, td {\r\n"
					+ "    border: 1px solid black;\r\n" + "    padding: 10px;\r\n" + "    text-align: left;\r\n"
					+ "  }" + ".GFG {\r\n" + "	   margin-top:10px;" + "    display: block;\r\n"
					+ "    width: 115px;\r\n" + "    height: 25px;\r\n" + "    background: #4CAF50;\r\n"
					+ "    padding: 10px;\r\n" + "    text-align: center;\r\n" + "    border-radius: 5px;\r\n"
					+ "    color: white;\r\n" + "    font-weight: bold;\r\n" + "    line-height: 25px;\r\n" + "}"
					+ "</style>");

			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();
			rs = st.executeQuery("select * from editabletables");
			out.write("<div style='\r\n" + "    display: flex;\r\n" + "    flex-direction: column;\r\n"
					+ "align-items: center;" + "'>");
			out.write("<form method=\"get\" action='index'>\r\n" + "Select Table : " + "<select name=\"tables\">\r\n");

			if (request.getParameter("tables") == null) {
				out.write("<option value=\"none\" selected disabled hidden>Select an Option</option>");
			}
			while (rs.next()) {
				if (request.getParameter("tables") != null) {
					if (request.getParameter("tables").equalsIgnoreCase(rs.getString(1))) {
						out.write("<option value=" + rs.getString(1) + " selected>" + rs.getString(1) + "</option>");
					} else {
						out.write("<option value=" + rs.getString(1) + ">" + rs.getString(1) + "</option>");
					}
				} else {
					out.write("<option value=" + rs.getString(1) + ">" + rs.getString(1) + "</option>");
				}
			}
			out.write("<input type=\"submit\" value=\"show table\"/>\r\n" + "</select>\r\n" + "</form>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
		}

		try {
			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();
			String table = request.getParameter("tables");

			if (table != null) {
				int total = 0;
				ResultSet rs3 = st.executeQuery("select count(*) from " + table);
				if (rs3.next()) {
					total = rs3.getInt(1);
				}

				int noOfPages = total / 50;
				if (noOfPages % total > 0) {

					noOfPages++;
				}

				String query = "";
				int start = 0;
				int end = start + 50;
				;

				if (noOfPages > 1) {

					if (request.getParameter("page") != null) {
						if (Integer.parseInt(request.getParameter("page")) > 1) {
							end = 50 * Integer.parseInt(request.getParameter("page"));
							start = 50 * (Integer.parseInt(request.getParameter("page")) - 1);
						}
					}
					query = "SELECT * FROM (\r\n" + "    SELECT\r\n" + "      ord.*,\r\n"
							+ "      row_number() over (ORDER BY ord.EMPNO ASC) line_number\r\n"
							+ "    FROM emp ord\r\n" + " \r\n" + "  ) WHERE line_number BETWEEN " + start + " AND "
							+ end + "  ORDER BY line_number";
				} else {
					query = "select * from " + table;
				}

				rs = st.executeQuery(query);

				rsmd = rs.getMetaData();

				int columnCount = rsmd.getColumnCount();

				out.write("<form method='get' action='/Edit'>");
				out.write("<table>");

				if (noOfPages > 1) {
					columnCount = columnCount - 1;
				}

				out.write("<tr>");

				for (int i = 1; i <= columnCount; i++) {
					out.write("<td>" + rsmd.getColumnName(i) + "</td>");

				}
				out.write("</tr>");
				while (rs.next()) {
					out.write("<tr>");
					for (int i = 1; i <= columnCount; i++) {
						out.write("<td>" + rs.getString(i) + "</td>");
					}
					String edit = "<a href='Edit?table=\"" + table + "\"&val=\"" + rs.getString(1) + "\"'>Edit</a>";

					out.write("<td><a href='Edit?table=" + table + "&" + rsmd.getColumnName(1) + "=" + rs.getString(1)
							+ "'>Edit</a></td>");
					out.write("<td><a href='Delete?table=" + table + "&" + rsmd.getColumnName(1) + "=" + rs.getString(1)
							+ "'>Delete</a></td>");

					out.write("</tr>");
				}

				out.write("</table>");
				if (request.getParameter("tables") != null) {
					out.write("<input type='hidden' name='table' value='" + request.getParameter("tables") + "'/>");
				}
				out.write("<a class='GFG' href='AddForm?table=" + request.getParameter("tables") + "'>Add Data</a>");

				out.write("<br>");

				out.write("<div class=\"pagination\">\r\n");
				for (int i = 1; i <= noOfPages; i++) {
					if (request.getParameter("page") != null) {
						if (Integer.parseInt(request.getParameter("page")) == i) {
							out.write("<a href='index?tables=" + table + "&page=" + i + "' class=\"active\">" + i
									+ "</a>");
						} else {
							out.write("<a href='index?tables=" + table + "&page=" + i + "'>" + i + "</a>");
						}
					} else {
						if (i == 1) {
							out.write("<a href='index?tables=" + table + "&page=" + i + "' class=\"active\">" + i
									+ "</a>");
						} else {
							out.write("<a href='index?tables=" + table + "&page=" + i + "'>" + i + "</a>");
						}
					}
				}
				out.write("</div>");
				out.write("</form>");
				out.write("</div>");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
			Statement st = con.createStatement();
			String table = request.getParameter("tables");

			if (table != null) {
				String query = "select * from " + table;
				ResultSet rs = st.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();

				int columnCount = rsmd.getColumnCount();
				out.write("<form method='get' action='/main/Edit'>");
				out.write("<table>");
				out.write("<tr>");

				for (int i = 1; i <= columnCount; i++) {
					out.write("<td>" + rsmd.getColumnName(i) + "</td>");

				}
				out.write("</tr>");

				while (rs.next()) {
					out.write("<tr>");
					for (int i = 1; i <= columnCount; i++) {
						out.write("<td>" + rs.getString(i) + "</td>");

					}
					String edit = "<a href='/main/Edit?table=\"" + table + "\"&val=\"" + rs.getString(1)
							+ "\"'>Edit</a>";

					out.write("<td><a href='/main/Edit?table=" + table + "&" + rsmd.getColumnName(1) + "="
							+ rs.getString(1) + "'>Edit</a></td>");
					out.write("<td><input type='submit' value='Delete'></td>");
					out.write("</tr>");
				}

				out.write("</table>");
				out.write("<input type=\"submit\" value='Add Data'>");
				out.write("</form>");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
