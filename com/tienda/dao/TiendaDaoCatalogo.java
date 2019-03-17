package com.tienda.dao;

import java.sql.Connection;

public interface TiendaDaoCatalogo {
	public void add();
	public void del();
	public void list();
	public void mostrar();
	public void pagar();
	public int searchInCesta(String nomprod);
	public boolean searchProducto(String nom);
	public void restarUnidad(String nomprod);
	public void sumarUnidad(String nomprod);
	public String cabeceraFactura();
	public double getTotal();
	public void setConnection(Connection conn);
	public void setComprador(String dnicli);
}
