package com.tienda.client;

import java.sql.Connection;

import com.tienda.dao.TiendaDaoClienteImpl;

public class GestionClientes extends TiendaDaoClienteImpl {
		
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
				set();
				break;
			case 3:
				del();
				break;
			case 4:
				String dnicli=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea mostrar:");
				mostrar(dnicli);
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
		System.out.println("GESTIÓN DE CLIENTES");
		System.out.println("------------------");
		System.out.println("1. Añadir cliente.");
		System.out.println("2. Modificar cliente.");
		System.out.println("3. Borrar cliente.");
		System.out.println("4. Mostrar cliente.");			
		System.out.println("5. Listar todos los clientes.");
		System.out.println("0. Volver al menú principal.");		
	}
}