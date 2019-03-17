package com.tienda.model;

public class LineaFactura {
	
	Producto producto;
	int cantidad;
	float precio;
	Factura factura;
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantidad;
		result = prime * result + ((factura == null) ? 0 : factura.hashCode());
		result = prime * result + Float.floatToIntBits(precio);
		result = prime * result + ((producto == null) ? 0 : producto.hashCode());
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
		LineaFactura other = (LineaFactura) obj;
		if (cantidad != other.cantidad)
			return false;
		if (factura == null) {
			if (other.factura != null)
				return false;
		} else if (!factura.equals(other.factura))
			return false;
		if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
			return false;
		if (producto == null) {
			if (other.producto != null)
				return false;
		} else if (!producto.equals(other.producto))
			return false;
		return true;
	}
}
