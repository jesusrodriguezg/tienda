package com.tienda.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.tienda.client.PedirDatos;
import com.tienda.model.Cliente;
import com.tienda.model.Producto;

public class TiendaDaoCatalogoImpl implements TiendaDaoCatalogo {
	
	private Connection conn;
	private ArrayList<Producto> cesta = new ArrayList<Producto>();
	private String dnicli;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
	private Timestamp ts = new Timestamp(System.currentTimeMillis());
	
	@Override
	public void add() {
		String nomprod = PedirDatos.leerCadena("Introduzca el nombre del producto que desea añadir a la cesta:")
				.toUpperCase();
		if (!searchProducto(nomprod)) {
			System.out.println("En el almacén no hay existencias de " + nomprod + ".");
			return;
		}
		Producto p = new Producto();
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT IDPROD, NOMPROD, PRECIO FROM PRODUCTO WHERE NOMPROD LIKE '" + nomprod + "'";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			p.setIdprod(rs.getInt("IDPROD"));
			p.setNomprod(rs.getString("NOMPROD"));
			p.setPrecio(rs.getDouble("PRECIO"));
			this.cesta.add(p);
			restarUnidad(nomprod);
			System.out.println("Se ha añadido una unidad de " + nomprod + " a la cesta.");
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public void del() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		String nomprod = PedirDatos.leerCadena("Introduzca el nombre del producto que desea quitar de la cesta:")
				.toUpperCase();
		int pos = searchInCesta(nomprod);
		if (pos == -1) {
			System.out.println("No existe ninguna unidad de " + nomprod + " en la cesta.");
			return;
		}
		this.cesta.remove(pos);
		sumarUnidad(nomprod);
		System.out.println("Se ha eliminado una unidad de " + nomprod + " de la cesta.");
	}

	@Override
	public void list() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		int pos = 0;
		for (Producto prod : this.cesta) {
			pos++;
			System.out.println(pos + "- " + prod.getNomprod() + " - " + prod.getPrecio() + "€");
		}
	}

	@Override
	public void mostrar() {
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT IDPROD, NOMPROD, PRECIO, CANTIDAD FROM PRODUCTO";
			ResultSet rs = st.executeQuery(sql);
			Producto p = new Producto();
			while (rs.next()) {
				p.setIdprod(rs.getInt("IDPROD"));
				p.setNomprod(rs.getString("NOMPROD"));
				p.setPrecio(rs.getDouble("PRECIO"));
				p.setCantidad(rs.getInt("CANTIDAD"));
				System.out.println(p);
				System.out.println("--------------------------");
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public void pagar() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		String fecha = sdf.format(ts);
		String codfact = "";
		try {
			Statement st = conn.createStatement();
			String sqlcount = "SELECT COUNT(*) FROM FACTURA";
			ResultSet rs = st.executeQuery(sqlcount);
			codfact = (rs.getInt(1))+1 + "-" + this.dnicli + "-" + fecha.replaceAll("/", "");
			FileWriter fw = new FileWriter("Factura " + codfact + ".txt");
			fw.write(cabeceraFactura());
			int pos = 0;
			for (Producto prod : this.cesta) {
				pos++;
				fw.write(pos + " - " + prod.getNomprod() + " - " + prod.getPrecio() + "€\n");
			}
			fw.write("TOTAL               " + getTotal() + "€\n" + "\n" + "Fecha de la compra: " + fecha + "\n\n"
					+ "¡Gracias por su visita!");
			fw.flush();
			fw.close();
			String sql = "INSERT INTO FACTURA (DNICLI,FECHA,IMPORTE,CODFACT)" + "VALUES('" + this.dnicli + "','" + fecha
					+ "'," + getTotal() + ",'" + codfact + "')";
			st.executeUpdate(sql);
			
			sql = "Select id from FACTURA where CODFACT = '"+codfact+"'";
			rs = st.executeQuery(sql);
			int idFactura = rs.getInt(1);
			String query = "";
			sql = "INSERT INTO linea_factura (cantidad, id_factura, id_producto, precio) VALUES";
			for (Producto prod : this.cesta) {
			     query = "(1,"+idFactura+","+prod.getIdprod()+","+prod.getPrecio()+")";
			     query = sql+query;
			     st.executeUpdate(query);
			}
			rs.close();
			st.close();
		} catch (IOException ioe) {
			System.out.println("Error de I/O.");
			System.out.println(ioe.getMessage());
			return;
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
			return;
		}
		System.out.println("Se ha generado la factura de su compra, con código " + codfact + ".");		
	}

	@Override
	public int searchInCesta(String nomprod) {
		for (int i = 0; i < this.cesta.size(); i++) {
			if (this.cesta.get(i).getNomprod().equals(nomprod)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean searchProducto(String nom) {
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM PRODUCTO WHERE NOMPROD LIKE '" + nom + "'";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			if (rs.getInt(1) != 0) {
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
	public void restarUnidad(String nomprod) {
		try {
			Statement st = conn.createStatement();
			String sql = "UPDATE PRODUCTO SET CANTIDAD=(CANTIDAD-1) WHERE NOMPROD LIKE '" + nomprod + "'";
			st.executeUpdate(sql);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
	}

	@Override
	public void sumarUnidad(String nomprod) {
		try {
			Statement st = conn.createStatement();
			String sql = "UPDATE PRODUCTO SET CANTIDAD=(CANTIDAD+1) WHERE NOMPROD LIKE '" + nomprod + "'";
			st.executeUpdate(sql);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			;
		}		
	}

	@Override
	public String cabeceraFactura() {
		Cliente c = new Cliente();
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM CLIENTE WHERE DNI LIKE '" + this.dnicli + "'";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			c.setDNI(this.dnicli);
			c.setNomcliente(rs.getString("NOMCLIENTE"));
			c.setTelefono(rs.getString("TELEFONO"));
			c.setCorreo(rs.getString("CORREO"));
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			return "";
		}
		return "          *** EL DESAVÍO ***\n\nC/ Esclava del Señor 2, 41013, Sevilla\n"
				+ "--------------------------------------\n\nDATOS DEL COMPRADOR\n\n" + "DNI: " + c.getDNI()
				+ "\nNombre: " + c.getNomcliente() + "\nTeléfono: " + "" + c.getTelefono() + "\nCorreo-e: "
				+ c.getCorreo().toLowerCase() + "\n\n" + "--------------------------------------\n";
	}

	@Override
	public double getTotal() {
		double total = 0;
		for (Producto prod : this.cesta) {
			total += prod.getPrecio();
		}
		return total;
	}

	@Override
	public void setConnection(Connection conn) {
		this.conn=conn;
	}
	
	@Override
	public void setComprador(String dnicli) {
		this.dnicli=dnicli;
	}

}
