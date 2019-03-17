package com.tienda.model;

public class Cliente {
	
	private String dni;
	private String nomcliente;
	private String telefono;
	private String correo;
	
	public Cliente() {
	}
	
	public Cliente(String dni, String nomcliente, String telefono, String correo) {
		/*if (dni.length()!=9) {
			System.out.println("El DNI debe constar de 9 y sólo 9 caracteres.");
			return;
		}*/
		this.dni = dni;
		this.nomcliente = nomcliente;
		/*if (telefono.length()!=9) {
			System.out.println("El teléfono debe tener 9 y sólo 9 dígitos.");
			return;
		}*/
		this.telefono = telefono;
		this.correo = correo;
	}

	public String getDNI() {
		return dni;
	}
	public void setDNI(String dni) {
		/*if (dni.length()!=9) {
			System.out.println("El DNI debe constar de 9 y sólo 9 caracteres.");
			return;
		}*/
		this.dni = dni;
	}
	public String getNomcliente() {
		return nomcliente;
	}
	public void setNomcliente(String nomcliente) {
		this.nomcliente = nomcliente;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		/*if (telefono.length()!=9) {
			System.out.println("El teléfono debe tener 9 y sólo 9 dígitos.");
			return;
		}*/
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		result = prime * result + ((nomcliente == null) ? 0 : nomcliente.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
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
		Cliente other = (Cliente) obj;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		if (nomcliente == null) {
			if (other.nomcliente != null)
				return false;
		} else if (!nomcliente.equals(other.nomcliente))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.dni + " - "+this.nomcliente+"; teléfono: "+this.telefono +"; correo-e: " +this.correo;
	}
	
}
