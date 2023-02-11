package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Delete
 */
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Delete() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();

		Connection con = null;
		Statement st = null;

		try {
			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();

			Enumeration<String> enumeration = request.getParameterNames();
			int count = 0;
			String pname = "";
			while (enumeration.hasMoreElements()) {
				if (count != 0) {
					pname = enumeration.nextElement();
				}
				count++;
			}
			String query = "delete from " + request.getParameter("table") + " where " + pname + "='"
					+ request.getParameter(pname) + "'";
			int rs = st.executeUpdate(query);

			if (rs == 1) {
				response.sendRedirect("index?tables=" + request.getParameter("table"));
			}
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
