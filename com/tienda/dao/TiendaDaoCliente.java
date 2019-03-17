package com.tienda.dao;

import java.sql.Connection;

public interface TiendaDaoCliente {

	public void add();
	public void set();
	public void del();
	public void mostrar(String dni);
	public void list();
	public boolean search(String dni);
	public void setConnection(Connection conn);
}
