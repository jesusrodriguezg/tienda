package com.tienda.dao;

import java.sql.Connection;

public interface TiendaDaoAlmacen {
	
	public void setPrecioProducto();
	public  boolean searchProductoNombre(String nom);
	public void add();
	public void del();
	public void mostrar();
	public void list();
	public boolean searchProductoId(int id);
	public void setConnection(Connection conn);
	
}
