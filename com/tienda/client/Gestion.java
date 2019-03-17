package com.tienda.client;

import java.sql.Connection;

import com.tienda.dao.TiendaDaoGestionImpl;

public class Gestion extends TiendaDaoGestionImpl{
	
	private Almacen ga=new Almacen();
	private GestionClientes gc=new GestionClientes();
	private Catalogo cat=new Catalogo();
	private GestionFacturas gf=new GestionFacturas();
	private Connection conn;
	
	public void menu() {
		int opcion=0;
		this.conn=connect();
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				ga.menu(this.conn);
				break;
			case 2:
				gc.menu(this.conn);
				break;
			case 3:
				String dni=PedirDatos.leerCadena("Por favor, introduzca su DNI para identificarse como cliente:");
				if (!identificarCliente(dni)) {
					System.out.println("No existe ningún cliente con el DNI "+dni+".");
					opcion=-1;
					break;
				}
				cat.menu(this.conn, dni);
				break;
			case 4:
				gf.menu(conn);
				break;
			case 0:
				System.out.println("Adiós.");
				disconnect(this.conn);
				break;
			default:
				System.out.println("Debe introducir una opción entre 0 y 4.");
				break;
			}
		} while (opcion!=0);
	}
	
	private void mostrarMenu() {
		System.out.println("----------");
		System.out.println("EL DESAVÍO");
		System.out.println("----------");
		System.out.println("1. Gestionar almacén.");
		System.out.println("2. Gestionar clientes.");
		System.out.println("3. Comprar.");
		System.out.println("4. Gestionar facturas.");
		System.out.println("0. Salir.");
	}
}