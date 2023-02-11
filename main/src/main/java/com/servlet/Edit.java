package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Edit
 */
@WebServlet("/Edit")
public class Edit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Edit() {
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
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Enumeration<String> enumeration = request.getParameterNames();
			String pname = "";
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				pname = parameterName;
			}
			rs = st.executeQuery("select * from " + request.getParameter("table") + " where " + pname + "='"
					+ request.getParameter(pname) + "'");

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			out.write("<form method=\"post\" action='Update'>");
			out.write("<table>");
			for (int i = 1; i <= columnCount; i++) {
				rs.beforeFirst();
				while (rs.next()) {
					out.write("<tr>");
					if (request.getParameter(pname).equals(rs.getString(1))) {
						out.write("<td>" + rsmd.getColumnName(i) + " :</td> <td><input type='text' value='"
								+ rs.getString(i) + "' name='" + rsmd.getColumnName(i) + "'/></td>");
					}
					out.write("</tr>");
				}

			}

			out.write("<input type='hidden' value='" + request.getParameter("table") + "' name='table'/>");
			out.write("</table>");
			out.write("<input type='submit' value='Update'/>");
			out.write("</form>");
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

	}

}
