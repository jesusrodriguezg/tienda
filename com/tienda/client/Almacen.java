package com.tienda.client;

import com.tienda.dao.TiendaDaoAlmacenImpl;
import java.sql.Connection;


public class Almacen extends TiendaDaoAlmacenImpl{
	
	public void menu(Connection conn) {	
		super.setConnection(conn);
		int opcion=0;
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				add();
				break;
			case 2:
				setPrecioProducto();
				break;
			case 3:
				del();
				break;
			case 4:
				mostrar();
				break;
			case 5:
				list();
				break;
			case 0:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Debe introducir una opción entre 0 y 5.");
				break;
			}
		} while (opcion!=0);
	}
	
	private void mostrarMenu() {
		System.out.println("------------------");
		System.out.println("GESTIÓN DE ALMACÉN");
		System.out.println("------------------");
		System.out.println("1. Añadir producto.");
		System.out.println("2. Modificar precio de un producto.");
		System.out.println("3. Borrar producto.");
		System.out.println("4. Mostrar un producto.");
		System.out.println("5. Listar todos los productos.");
		System.out.println("0. Volver al menú principal.");		
	}
}
