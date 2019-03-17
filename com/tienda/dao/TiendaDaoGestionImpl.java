package com.tienda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TiendaDaoGestionImpl implements TiendaDaoGestion {
	Connection conn;
	@Override
	public Connection connect() {
		this.conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
			String dirdb="jdbc:sqlite:BD/tienda.db";
			this.conn = DriverManager.getConnection(dirdb);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ERROR: driver JDBC no encontrado.");
			return null;
		} catch (SQLException sqle) {
			System.out.println("ERROR: no se encuentra la base de datos o el usuario y la contraseña no son correctos.");
			return null;
		}
		System.out.println("Ha conectado correctamente con la base de datos.");
		return this.conn;
	}

	@Override
	public void disconnect(Connection conn) {
		try {
			conn.close();
			System.out.println("Se ha desconectado de la base de datos.");
		} catch (SQLException sqle) {
			System.out.println("Error al desconectar de la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public boolean identificarCliente(String dni) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM CLIENTE WHERE DNI LIKE '"+dni+"'";
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			int res=rs.getInt(1);
			if (res!=0) {
				rs.close();
				st.close();
				return true;
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
		return false;
	}
}
