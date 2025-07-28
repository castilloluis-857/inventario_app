package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

public class Conexion {

	// conector de SQLite
	private static final String CONECTOR_SQLITE = "org.sqlite.JDBC";
	// fichero de la base de datos "biblioteca"
	private static final String FICHERO_BD = "inventario.db";
	// URL de conexi�n con la base de datos SQLite "biblioteca"
	private static final String URL_SQLITE_BD = "jdbc:sqlite:" + FICHERO_BD;

	/**
	 * Abre una conexi�n con la base de datos relacional SQLite "biblioteca".
	 * Devuelve la conexi�n creada.
	 */
	public static Connection abrirConexion() throws ClassNotFoundException, SQLException {
		Class.forName(CONECTOR_SQLITE);
		SQLiteConfig config = new SQLiteConfig();
		config.enforceForeignKeys(true);
		Connection conexion = DriverManager.getConnection(URL_SQLITE_BD, config.toProperties());
		return conexion;
	}

	/**
	 * Cierra una conexi�n con la base de datos relacional SQLite "biblioteca".
	 */
	public static void cerrarConexion(Connection conexion) throws SQLException {
		if (conexion != null && !conexion.isClosed()) {
			conexion.close();
		}
	}
}
