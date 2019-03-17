package com.tienda.dao;

import java.sql.Connection;

public interface TiendaDaoFactura {
	
	public void del();
	public void list();
	public void setConnection(Connection conn);
	public void mostrarFacturaComprador(String dni);
	public void mostrarFacturaFecha();
	public boolean searchFacturaDni(String dni);
	public boolean searchFacturaFecha(String fecha);
}
