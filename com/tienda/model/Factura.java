package com.tienda.model;

import java.sql.Connection;
import java.util.ArrayList;

public class Factura {
	public String fecha;
	public Cliente cliente;
	public ArrayList<LineaFactura> lineas;
	public float total;
	public String codFactura;

	private Connection conn;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<LineaFactura> getLineas() {
		return lineas;
	}

	public void setLineas(ArrayList<LineaFactura> lineas) {
		this.lineas = lineas;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getCodFactura() {
		return codFactura;
	}

	public void setCodFactura(String codFactura) {
		this.codFactura = codFactura;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((codFactura == null) ? 0 : codFactura.hashCode());
		result = prime * result + ((conn == null) ? 0 : conn.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((lineas == null) ? 0 : lineas.hashCode());
		result = prime * result + Float.floatToIntBits(total);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Factura other = (Factura) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (codFactura == null) {
			if (other.codFactura != null)
				return false;
		} else if (!codFactura.equals(other.codFactura))
			return false;
		if (conn == null) {
			if (other.conn != null)
				return false;
		} else if (!conn.equals(other.conn))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (lineas == null) {
			if (other.lineas != null)
				return false;
		} else if (!lineas.equals(other.lineas))
			return false;
		if (Float.floatToIntBits(total) != Float.floatToIntBits(other.total))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Factura [fecha=" + fecha + ", cliente=" + cliente + ", lineas=" + lineas + ", total=" + total
				+ ", codFactura=" + codFactura + ", conn=" + conn + "]";
	}
}