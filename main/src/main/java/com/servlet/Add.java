package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Add
 */
public class Add extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Add() {
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
		ResultSet rs = null;
		try {
			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();

			String table = request.getParameter("table");

			rs = st.executeQuery("select * from " + table);
			ResultSetMetaData rsmd = rs.getMetaData();

			List dates = new ArrayList();
			List numbers = new ArrayList();
			int columnCount = rsmd.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				if (rsmd.getColumnTypeName(i).equalsIgnoreCase("date")) {
					dates.add(i);
				}
				if (rsmd.getColumnTypeName(i).equalsIgnoreCase("number")) {
					numbers.add(i);
				}
			}
			int index = 0;
			int nindex = 0;
			StringBuffer sb = new StringBuffer();
			sb.append("insert into " + request.getParameter("table") + " values (");
			for (int i = 1; i <= columnCount; i++) {
				boolean flag = false;
//				  System.out.println(i+"  "+dates.get(index));

				if (i != columnCount) {
					if (dates.size() != 0) {
						if (i == (int) dates.get(index)) {
							flag = true;
							sb.append("TO_DATE('" + request.getParameter(rsmd.getColumnName(i))
									+ "','YYYY-MM-DD:HH24:MI:SS'),");
							if (dates.size() > 1) {
								index++;
							}
						}

					}
					if (numbers.size() != 0) {
						if (i == (int) numbers.get(nindex) && i == columnCount) {
							System.out.println("here");
							flag = true;
							sb.append(request.getParameter(rsmd.getColumnName(i)) + ")");
							if (numbers.size() > 1) {
								nindex++;
							}
						} else if (i == (int) numbers.get(nindex)) {
							flag = true;
							sb.append(request.getParameter(rsmd.getColumnName(i)) + ",");
							if (numbers.size() > 1) {
								nindex++;
							}
						}
					}
					if (flag == false) {
						sb.append("'" + request.getParameter(rsmd.getColumnName(i)) + "',");
					}
				} else {

					sb.append("'" + request.getParameter(rsmd.getColumnName(i)) + "')");
				}
			}
			int rs1 = st.executeUpdate(sb.toString());
			if (rs1 == 1) {
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
