package com.tienda.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tienda.client.PedirDatos;
import com.tienda.model.Cliente;

public class TiendaDaoClienteImpl implements TiendaDaoCliente {
	
	private Connection conn;

	@Override
	public void add() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea añadir:");
		//a pesar de que en el método mostrarCliente() llamamos al searchCliente(), es imprescindible
		//llamarlo aquí también, porque en cualquier caso seguiría ejecutándose este método y no volvería
		//al menú principal; si no existe el cliente con el DNI especificado, nunca llama a mostrarCliente()
		if (search(dni)) {
			System.out.println("Ya existe un cliente con el DNI "+dni+". No puede añadir un cliente con dicho DNI.");
			return;
		}
 		String nomcliente=PedirDatos.leerCadena("Introduzca el nombre del cliente que desea añadir:");
		String telefono=PedirDatos.leerCadena("Introduzca el teléfono del cliente que desea añadir:");
		String correo=PedirDatos.leerCadena("Introduzca el correo electrónico del cliente que desea añadir:");
		Cliente c=new Cliente(dni, nomcliente, telefono, correo);
		try {
			Statement st=conn.createStatement();
			String sql="INSERT INTO CLIENTE (DNI, NOMCLIENTE, TELEFONO, CORREO) VALUES ('"+c.getDNI()+"','"+c.getNomcliente()+"',"
					+ "'"+c.getTelefono()+"','"+c.getCorreo()+"')";
			int fil=st.executeUpdate(sql);
			st.close();
			if (fil==1) {
				System.out.println("Se ha añadido el cliente con el DNI "+c.getDNI()+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}		
	}

	@Override
	public void set() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente cuyos datos desea modificar:");
		if (!search(dni)) {
			System.out.println("No existe ningún cliente con el DNI "+dni+".");
			return;
		}
		mostrar(dni);
		String nomcliente=PedirDatos.leerCadena("Introduzca el nuevo nombre del cliente con el DNI "+dni+".");
		String telefono=PedirDatos.leerCadena("Introduzca el nuevo teléfono del cliente con el DNI "+dni+".");
		String correo=PedirDatos.leerCadena("Introduzca el nuevo correo electrónico del cliente con el DNI "+dni+".");
		try {
			Statement st=conn.createStatement();
			String sql="UPDATE CLIENTE SET NOMCLIENTE='"+nomcliente.toUpperCase()+"', TELEFONO='"+telefono.toUpperCase()+"',"
					+ "CORREO='"+correo.toUpperCase()+"' WHERE DNI LIKE '"+dni+"'";
			int fil=st.executeUpdate(sql);
			if (fil==1) {
				System.out.println("Se han modificado correctamente los datos del cliente con el DNI "+dni+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}		
	}

	@Override
	public void del() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea eliminar:");
		if (!search(dni)) {
			System.out.println("No existe ningún cliente con el DNI "+dni+".");
			return;
		}
		try {
			Statement st=conn.createStatement();
			String sql="DELETE FROM CLIENTE WHERE DNI LIKE '"+dni+"'";
			int fil=st.executeUpdate(sql);
			st.close();
			if (fil==1) {
				System.out.println("Se ha eliminado el cliente con el DNI "+dni+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}		
	}

	@Override
	public void mostrar(String dni) {
		if (!search(dni)) {
			System.out.println("No existe ningún cliente con el DNI "+dni+".");
			return;
		}
		System.out.println("Los datos del cliente con el DNI "+dni+" son:");
		try {
			Statement st=conn.createStatement();
			String sql="SELECT DNI, NOMCLIENTE, TELEFONO, CORREO FROM CLIENTE WHERE DNI LIKE '"+dni+"'";
			ResultSet rs=st.executeQuery(sql);
			Cliente c=new Cliente();
			System.out.println("--------------------------");
			c.setDNI(rs.getString("DNI"));
			c.setNomcliente(rs.getString("NOMCLIENTE"));
			c.setTelefono(rs.getString("TELEFONO"));
			c.setCorreo(rs.getString("CORREO"));
			System.out.println(c);
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}		
	}

	@Override
	public void list() {
		ResultSet rs;
		try {
			Statement st=conn.createStatement();
			String sql1="SELECT COUNT(*) FROM CLIENTE";
			rs=st.executeQuery(sql1);
			rs.next();
			if (rs.getInt(1)==0) {
				System.out.println("No hay clientes registrados.");
				return;
			}
			String sql2="SELECT DNI, NOMCLIENTE, TELEFONO, CORREO FROM CLIENTE";
			rs=st.executeQuery(sql2);
			Cliente c=new Cliente();
			while (rs.next()) {
				System.out.println("--------------------------");
				c.setDNI(rs.getString("DNI"));
				c.setNomcliente(rs.getString("NOMCLIENTE"));
				c.setTelefono(rs.getString("TELEFONO"));
				c.setCorreo(rs.getString("CORREO"));
				System.out.println(c);
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}		
	}

	@Override
	public boolean search(String dni) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM CLIENTE WHERE DNI LIKE '"+dni+"'";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			int res=rs.getInt(1);
			if (res!=0) {
				rs.close();
				st.close();
				return true;
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		return false;
	}

	@Override
	public void setConnection(Connection conn) {
		this.conn=conn;
	}
}
