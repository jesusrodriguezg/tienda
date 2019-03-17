package com.tienda.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tienda.dao.TiendaDaoFactura;
import com.tienda.client.PedirDatos;

public class TiendaDaoFacturaImpl implements TiendaDaoFactura{
	Connection conn;
	@Override
	public void mostrarFacturaComprador(String dni) {
		if (!searchFacturaDni(dni)) {
			System.out.println("No hay facturas para el cliente con el DNI "+dni+" en la base de datos.");
			return;
		}
		try {
			Statement st=conn.createStatement();
			String sql="SELECT * FROM FACTURA WHERE DNICLI LIKE '"+dni+"'";
			ResultSet rs=st.executeQuery(sql);
			System.out.println("Las facturas correspondientes al cliente con el DNI "+dni+" son:");
			while (rs.next()) {
				System.out.println("--------------------------");
				System.out.println("ID Factura: "+rs.getInt("ID"));
				System.out.println("DNI cliente: "+rs.getString("DNICLI"));
				System.out.println("Fecha: "+rs.getString("FECHA"));
				System.out.println("Importe: "+rs.getDouble("IMPORTE")+"€");
				System.out.println("Código de factura: "+rs.getString("CODFACT"));
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public boolean searchFacturaDni(String dni) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM FACTURA WHERE DNICLI LIKE '"+dni+"'";
			ResultSet rs = st.executeQuery(sql);
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
			System.out.println(sqle.getMessage());
		}
		return false;
	
	}

	@Override
	public void del() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea eliminar:");
		if (!searchFacturaDni(dni)) {
			System.out.println("No existe ninguna factura del cliente con el DNI "+dni+".");
			return;
		}
		System.out.println("Las facturas del cliente con el DNI "+dni+" son:");
		mostrarFacturaComprador(dni);
		int id=PedirDatos.leerEntero("Introduzca el ID de la factura que de sea eliminar:");
		try {
			Statement st=conn.createStatement();
			String sql="DELETE FROM FACTURA WHERE ID="+id+"";
			int fil=st.executeUpdate(sql);
			st.close();
			if (fil==1) {
				System.out.println("Se ha eliminado el cliente con el DNI "+dni+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public void list() {
		ResultSet rs;
		try {
			Statement st=conn.createStatement();
			String sql1="SELECT COUNT(*) FROM FACTURA";
			rs=st.executeQuery(sql1);
			rs.next();
			if (rs.getInt(1)==0) {
				System.out.println("No hay registros de facturas en la base de datos.");
				return;
			}
			String sql2="SELECT * FROM FACTURA";
			rs=st.executeQuery(sql2);
			while (rs.next()) {
				System.out.println("--------------------------");
				System.out.println("ID Factura: "+rs.getInt("ID"));
				System.out.println("DNI cliente: "+rs.getString("DNICLI"));
				System.out.println("Fecha: "+rs.getString("FECHA"));
				System.out.println("Importe: "+rs.getDouble("IMPORTE")+"€");
				System.out.println("Código de factura: "+rs.getString("CODFACT"));
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public void mostrarFacturaFecha() {
		String fecha=PedirDatos.leerCadena("Introduzca la fecha (DD/MM/AAAA) de las facturas que desea mostrar:");
		if (!searchFacturaFecha(fecha)) {
			System.out.println("No hay facturas con la fecha "+fecha+" en la base de datos.");
			return;
		}
		try {
			Statement st=conn.createStatement();
			String sql="SELECT * FROM FACTURA WHERE FECHA LIKE '"+fecha+"'";
			ResultSet rs=st.executeQuery(sql);
			System.out.println("Las facturas correspondientes a la fecha "+fecha+" son:");
			while (rs.next()) {
				System.out.println("--------------------------");
				System.out.println("ID Factura: "+rs.getInt("ID"));
				System.out.println("DNI cliente: "+rs.getString("DNICLI"));
				System.out.println("Fecha: "+rs.getString("FECHA"));
				System.out.println("Importe: "+rs.getDouble("IMPORTE")+"€");
				System.out.println("Código de factura: "+rs.getString("CODFACT"));
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public boolean searchFacturaFecha(String fecha) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM FACTURA WHERE FECHA LIKE '"+fecha+"'";
			ResultSet rs = st.executeQuery(sql);
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
			System.out.println(sqle.getMessage());
		}
		return false;
	}
	
	@Override
	public void setConnection(Connection conn)	{
		this.conn = conn;	
	}	
}