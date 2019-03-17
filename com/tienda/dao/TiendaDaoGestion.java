package com.tienda.dao;

import java.sql.Connection;

public interface TiendaDaoGestion {
	public Connection connect();
	public void disconnect(Connection conn);
	public boolean identificarCliente(String dni);
}
