package com.tienda.client;

import java.sql.Connection;

import com.tienda.dao.TiendaDaoFacturaImpl;

public class GestionFacturas extends TiendaDaoFacturaImpl {
	
	public void menu(Connection conn) {
		int opcion = 0;
		super.setConnection(conn);
		do {
			mostrarMenu();
			opcion = PedirDatos.leerEntero("Introduzca una opción:");
			switch (opcion) {
			case 1:
				String dni = PedirDatos.leerCadena("Introduzca el DNI del comprador cuyas facturas desea mostrar:");
				mostrarFacturaComprador(dni);
				break;
			case 2:
				mostrarFacturaFecha();
				break;
			case 3:
				del();
				break;
			case 4:
				list();
				break;
			case 0:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Debe introducir un número entre 0 y 3.");
				break;
			}
		} while (opcion != 0);
	}

	// método que se usa en el delFactura() y que no sólo muestra las facturas con
	// un dni que se le pasa por argumentos
	// sino que además devuelve el número de filas que hay en la base de datos, para
	// mejorar las opciones del método delFactura()

	/*
	 * private int mostrarFacturaDni(String dni) { try { Statement
	 * st=conn.createStatement(); String
	 * sql="SELECT * FROM COMPRA WHERE DNICLI LIKE '"+dni+"'"; ResultSet
	 * rs=st.executeQuery(sql);
	 * System.out.println("Las facturas correspondientes al cliente con el DNI "
	 * +dni+" son:"); int cont=0; while (rs.next()) { cont++;
	 * System.out.println("--------------------------");
	 * System.out.println("ID Factura: "+rs.getInt("ID"));
	 * System.out.println("DNI cliente: "+rs.getString("DNICLI"));
	 * System.out.println("Fecha: "+rs.getString("FECHA"));
	 * System.out.println("Importe: "+rs.getDouble("IMPORTE")+"€");
	 * System.out.println("Código de factura: "+rs.getString("CODFACT")); }
	 * rs.close(); st.close(); return cont; } catch (SQLException sqle) {
	 * System.out.println("Error al conectar con la base de datos.");
	 * System.out.println(sqle.getMessage()); } return -1; }
	 */

	private void mostrarMenu() {
		System.out.println("-------------------");
		System.out.println("GESTIÓN DE FACTURAS");
		System.out.println("-------------------");
		System.out.println("1. Mostrar facturas por comprador.");
		System.out.println("2. Mostrar facturas por fecha.");
		System.out.println("3. Borrar factura.");
		System.out.println("4. Listar todas las facturas.");
		System.out.println("0. Volver al menú principal.");
	}
	
}