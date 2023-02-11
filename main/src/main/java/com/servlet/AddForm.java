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

/**
 * Servlet implementation class Add
 */
public class AddForm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddForm() {
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

		try {
			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();

			String table = request.getParameter("table");

			rs = st.executeQuery("select * from " + table);
			ResultSetMetaData rsmd = rs.getMetaData();

			out.write("<form method='get' action='Add'>");
			out.write("<table>");
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				out.write("<tr>");
				out.write("<td>" + rsmd.getColumnName(i) + " : </td><td><input type='text' name = '"
						+ rsmd.getColumnName(i) + "'/></td>");
				out.write("</tr>");
			}
			out.write("<input type='hidden' name='table' value='" + request.getParameter("table") + "'/>");
			out.write("</table>");
			out.write("<input type='submit' value='Add Data'/>");
			out.write("</form>");

		} catch (Exception e) {
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
