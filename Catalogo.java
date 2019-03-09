import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Catalogo {
	
	private Connection conn;
	private ArrayList<Producto> cesta=new ArrayList<Producto>();
	private String dnicli;
	
	public void menu(Connection conn, String dnicli) {
		int opcion=0;
		this.conn=conn;
		this.dnicli=dnicli;
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				addCesta();
				break;
			case 2:
				delCesta();
				break;
			case 3:
				listCesta();
				break;
			case 4:
				mostrarCatalogo();
				break;
			case 5:
				pagar();
				break;
			case 0:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Debe introducir una opción entre 0 y 5.");
				break;
			}
		} while (opcion!=0);
	}

	private void addCesta() {
		String nomprod=PedirDatos.leerCadena("Introduzca el nombre del producto que desea añadir a la cesta:").toUpperCase();
		if (!searchProducto(nomprod)) {
			System.out.println("En el almacén no hay existencias de "+nomprod+".");
			return;
		}
		Producto p=new Producto();
		try {
			Statement st=conn.createStatement();
			String sql="SELECT NOMPROD, PRECIO FROM PRODUCTO WHERE NOMPROD LIKE '"+nomprod+"'";
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			p.setNomprod(rs.getString("NOMPROD"));
			p.setPrecio(rs.getDouble("PRECIO"));
			this.cesta.add(p);
			//restamos una unidad al producto en la base de datos
			restarUnidad(nomprod);
			System.out.println("Se ha añadido una unidad de "+nomprod+" a la cesta.");
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	private void delCesta() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		String nomprod=PedirDatos.leerCadena("Introduzca el nombre del producto que desea quitar de la cesta:").toUpperCase();
		int pos=searchInCesta(nomprod);
		if (pos==-1) {
			System.out.println("No existe ninguna unidad de "+nomprod+" en la cesta.");
			return;
		}
		this.cesta.remove(pos);
		//sumamos una unidad al producto en la base de datos
		sumarUnidad(nomprod);
		System.out.println("Se ha eliminado una unidad de "+nomprod+" de la cesta.");
	}

	private void listCesta() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		int pos=0;
		System.out.println("La cesta contiene actualmente los siguientes productos:\n");
		for (int i = 0; i < this.cesta.size(); i++) {
			pos++;
			System.out.println(pos+"- "+this.cesta.get(i).getNomprod()+""
					+ " - "+this.cesta.get(i).getPrecio()+"€");
		}
		/*int pos=0;
		for (Producto prod : this.cesta) {
			pos++;
			System.out.println(pos+"- "+prod.getNomprod()+" - "+prod.getPrecio()+"€");
		}*/
	}
	
	//método que muestra todos los productos de la tabla PRODUCTO, como el listarProductos() de Almacén
	private void mostrarCatalogo() {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT IDPROD, NOMPROD, PRECIO, CANTIDAD FROM PRODUCTO";
			ResultSet rs=st.executeQuery(sql);
			Producto p=new Producto();
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
	//método que genera factura pasando el ArrayList a un archivo TXT
	private void pagar() {
		if (this.cesta.isEmpty()) {
			System.out.println("No hay productos en la cesta.");
			return;
		}
		//hacemos una búsqueda en la base de datos para sacar los datos del comprador
		//para ello usamos el DNI que el usuario introduce al entrar en el menú de CATALOGO
		try {
			FileWriter fw=new FileWriter("Factura.txt");
			fw.write(cabeceraFactura());
			int pos=0;
			for (Producto prod : this.cesta) {
				pos++;
				fw.write(pos+" - "+prod.getNomprod()+" - "+prod.getPrecio()+"€\n");
			}
			fw.write("TOTAL               "+getTotal()+"€");
			fw.flush();
			fw.close();
		} catch (IOException ioe) {
			System.out.println("Error de I/O.");
			System.out.println(ioe.getMessage());
			return;
		}
		System.out.println("Se ha generado la factura de su compra.");
	}
	
	private int searchInCesta(String nomprod) {
		for (int i = 0; i < this.cesta.size(); i++) {
			if (this.cesta.get(i).getNomprod().equals(nomprod)) {
				return i;
			}
		}
		return -1;
	}
	
	private boolean searchProducto(String nom) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM PRODUCTO WHERE NOMPROD LIKE '"+nom+"'";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			if (rs.getInt(1)!=0) {
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
	
	//método que resta una unidad a un producto cada vez que se añade a la cesta
	private void restarUnidad(String nomprod) {
		//no hace falta llamar a searchProducto() porque si un método llama
		//a restarUnidad() es porque hay unidades de dicho producto en la BD
		try {
			Statement st=conn.createStatement();
			String sql="UPDATE PRODUCTO SET CANTIDAD=(CANTIDAD-1) WHERE NOMPROD LIKE '"+nomprod+"'";
			st.executeUpdate(sql);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());;
		}
	}
	
	//método que suma una unidad a un producto cada vez que se quita de la cesta
	private void sumarUnidad(String nomprod) {
		//no hace falta llamar a searchProducto() porque si un método llama
		//a restarUnidad() es porque hay unidades de dicho producto en la BD
		try {
			Statement st=conn.createStatement();
			String sql="UPDATE PRODUCTO SET CANTIDAD=(CANTIDAD+1) WHERE NOMPROD LIKE '"+nomprod+"'";
			st.executeUpdate(sql);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());;
		}
	}
	
	private String cabeceraFactura() {
		Cliente c=new Cliente();
		try {
			Statement st=conn.createStatement();
			String sql="SELECT * FROM CLIENTE WHERE DNI LIKE '"+this.dnicli+"'";
			ResultSet rs=st.executeQuery(sql);
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
				+ "--------------------------------------\n\nDATOS DEL COMPRADOR\n\n"
				+ "DNI: "+c.getDNI()+"\nNombre: "+c.getNomcliente()+"\nTeléfono: "
				+ ""+c.getTelefono()+"\nCorreo-e: "+c.getCorreo().toLowerCase()+"\n\n"
				+ "--------------------------------------\n";
	}
	
	private double getTotal() {
		double total=0;
		for (Producto prod : this.cesta) {
			total+=prod.getPrecio();
		}
		return total;
	}
	
	private void mostrarMenu() {
		System.out.println("------------------");
		System.out.println("CATÁLOGO DE COMPRA");
		System.out.println("------------------");
		System.out.println("1. Añadir producto a la cesta.");
		System.out.println("2. Quitar producto de la cesta.");
		System.out.println("3. Listar productos de la cesta.");
		System.out.println("4. Mostrar catálogo.");			
		System.out.println("5. Pagar.");
		System.out.println("0. Volver al menú principal.");		
	}
}
