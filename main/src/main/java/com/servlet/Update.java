package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Update() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		PrintWriter out = response.getWriter();

		Connection con = null;
		Statement st = null;
		ResultSet rs2 = null;
		ResultSet rs1 = null;
		try {
			Class.forName(context.getInitParameter("driverName"));
			con = DriverManager.getConnection(context.getInitParameter("driverURL"),
					context.getInitParameter("username"), context.getInitParameter("password"));
			st = con.createStatement();
			rs2 = st.executeQuery("select * from " + request.getParameter("table"));

			boolean primary = false;
			boolean charr = false;
			ResultSetMetaData rsmd = rs2.getMetaData();

			List dates = new ArrayList();
			int NumOfCol = rsmd.getColumnCount();
			for (int i = 1; i <= NumOfCol; i++) {

				if (rsmd.getColumnTypeName(i).equalsIgnoreCase("date")) {
					dates.add(i - 1);
				}
			}

			if (rsmd.getColumnTypeName(1).equalsIgnoreCase("NUMBER")) {
				primary = true;
			}

			if (rsmd.getColumnTypeName(1).equalsIgnoreCase("VARCHAR2")) {
				charr = true;
			}

			Enumeration<String> enumeration = request.getParameterNames();

			StringBuffer query = new StringBuffer("UPDATE " + request.getParameter("table") + " SET ");
			int index = 0;
			int count = 0;
			Map<String, String> m = new LinkedHashMap<String, String>();
			List l = new ArrayList();
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				String date = "";
				boolean flag = false;

				if (!parameterName.equals("table")) {

					if (!(primary == true && count == 0) || charr == true) {

						if (dates.size() != 0) {
							if ((int) dates.get(index) == count) {
								flag = true;
								date = "TO_DATE('" + request.getParameter(parameterName) + "','YYYY-MM-DD:HH24:MI:SS')";
								if (dates.size() > 1) {
									index++;
								}
							}
						}

						if (count == 0 || (count == 1 && primary == true)) {
							if (flag) {
								query.append(parameterName + "=" + date + " ");
							} else {
								if (rsmd.getColumnTypeName(count + 1).equalsIgnoreCase("NUMBER")) {
									query.append(parameterName + "=" + request.getParameter(parameterName) + "");
								} else {
									query.append(parameterName + "='" + request.getParameter(parameterName) + "' ");
								}
							}
						} else {
							if (flag) {
								query.append("," + parameterName + "=" + date + " ");
							} else {
								query.append("," + parameterName + "='" + request.getParameter(parameterName) + "' ");
							}
						}
					}

					if (count == 0 && primary == true || charr == true) {
						m.put(parameterName, request.getParameter(parameterName));
						l.add(parameterName);
						l.add(request.getParameter(parameterName));
					}
					if (primary == false) {
						System.out.println("count=" + count);
						if (count <= 2) {
							String query1 = "select " + parameterName + " from " + request.getParameter("table")
									+ " where " + l.get(0) + "='" + l.get(1) + "'";
							System.out.println("query1=" + query1);
							rs1 = st.executeQuery(query1);
							if (rs1.next()) {
								m.put(parameterName, rs1.getString(1));
							}
						}
					}
					count++;
				}
			}
			count = 0;
			query.append("WHERE");
			for (Map.Entry<String, String> entry : m.entrySet()) {
				if (count == 0) {
					query.append(" " + entry.getKey() + "='" + entry.getValue() + "'");
				} else {
					query.append(" AND " + entry.getKey() + "='" + entry.getValue() + "'");
				}
				count++;
			}
			System.out.println("FinalQuery=" + query);
			int rs = st.executeUpdate(query.toString());

			if (rs == 1) {
				response.sendRedirect("index?tables=" + request.getParameter("table"));
			} else {
				System.out.println("not updated");
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
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (Exception e) {
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (Exception e) {
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
