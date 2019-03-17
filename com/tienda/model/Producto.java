package com.tienda.model;

public class Producto {
	
	private int idprod;
	private String nomprod;
	private double precio;
	private int cantidad;
	
	public Producto() {
	}
	
	public Producto(int id, String nom, double pr, int cant) {
		this.idprod=id;
		this.nomprod=nom;
		this.precio=pr;
		if (cant<=0) {
			this.cantidad=1;
		}else {
			this.cantidad=cant;
		}
	}

	public int getIdprod() {
		return idprod;
	}

	public void setIdprod(int idprod) {
		this.idprod = idprod;
	}

	public String getNomprod() {
		return nomprod;
	}

	public void setNomprod(String nomprod) {
		this.nomprod = nomprod;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad=cantidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantidad;
		result = prime * result + idprod;
		result = prime * result + ((nomprod == null) ? 0 : nomprod.hashCode());
		long temp;
		temp = Double.doubleToLongBits(precio);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Producto other = (Producto) obj;
		if (cantidad != other.cantidad)
			return false;
		if (idprod != other.idprod)
			return false;
		if (nomprod == null) {
			if (other.nomprod != null)
				return false;
		} else if (!nomprod.equals(other.nomprod))
			return false;
		if (Double.doubleToLongBits(precio) != Double.doubleToLongBits(other.precio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.idprod+" - "+this.nomprod+"; precio: "+this.precio+"€; stock disponible: "+this.cantidad+" unidades";
	}
	
}