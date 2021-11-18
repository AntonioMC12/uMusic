package es.antoniomc.uMusic.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConexion {
//	habria que hacer una interfaz de conexion para todas las conexiones

	private static Connection con = null;

	private final static String SERVER = xmlReader.getConectionInfo("server");
	private final static String DATABASE = xmlReader.getConectionInfo("database");
	private final static String USERNAME = xmlReader.getConectionInfo("user");
	private final static String PASSWORD = xmlReader.getConectionInfo("password");

	public static Connection getConexion() {
		if (con == null) {
			try {
				con = DriverManager.getConnection(SERVER + "/" + DATABASE, USERNAME, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
				con = null;
//				Se puede no hacer null y lanzar una excepcion
			}
		}
		return con;
	}

	public static void cerrarConexion() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
