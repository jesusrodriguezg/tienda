package com.tienda.client;

import java.sql.Connection;
import com.tienda.dao.TiendaDaoCatalogoImpl;

public class Catalogo extends TiendaDaoCatalogoImpl {

	public void menu(Connection conn, String dnicli) {
		super.setConnection(conn);
		super.setComprador(dnicli);
		int opcion = 0;
		do {
			mostrarMenu();
			opcion = PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				add();
				break;
			case 2:
				del();
				break;
			case 3:
				list();
				break;
			case 4:
				mostrar();
				break;
			case 5:
				pagar();
				break;
			case 0:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Debe introducir una opción entre 0 y 5.");
				break;
			}
		} while (opcion != 0);
	}

	private void mostrarMenu() {
		System.out.println("------------------");
		System.out.println("CATÁLOGO DE COMPRA");
		System.out.println("------------------");
		System.out.println("1. Añadir producto a la cesta.");
		System.out.println("2. Quitar producto de la cesta.");
		System.out.println("3. Listar productos de la cesta.");
		System.out.println("4. Mostrar catálogo.");
		System.out.println("5. Pagar.");
		System.out.println("0. Volver al menú principal.");
	}
}
