import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionClientes {
	
	private Connection conn=null;
	
	public void menu(Connection conn) {
		this.conn=conn;
		int opcion=0;
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				addCliente();
				break;
			case 2:
				setCliente();
				break;
			case 3:
				delCliente();
				break;
			case 4:
				String dnicli=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea mostrar:");
				mostrarCliente(dnicli);
				break;
			case 5:
				listClientes();
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
	
	private void addCliente() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea añadir:");
		//a pesar de que en el método mostrarCliente() llamamos al searchCliente(), es imprescindible
		//llamarlo aquí también, porque en cualquier caso seguiría ejecutándose este método y no volvería
		//al menú principal; si no existe el cliente con el DNI especificado, nunca llama a mostrarCliente()
		if (searchCliente(dni)) {
			System.out.println("Ya existe un cliente con el DNI "+dni+". No puede añadir un cliente con dicho DNI.");
			return;
		}
		mostrarCliente(dni);
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

	private void setCliente() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente cuyos datos desea modificar:");
		if (!searchCliente(dni)) {
			System.out.println("No existe ningún cliente con el DNI "+dni+".");
			return;
		}
		mostrarCliente(dni);
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

	private void delCliente() {
		String dni=PedirDatos.leerCadena("Introduzca el DNI del cliente que desea eliminar:");
		if (!searchCliente(dni)) {
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
	
	/*método idéntico al mostrarProducto() de la clase Almacen, pero que recibe por argumentos el DNI del cliente;
	utilizamos este método para reutilizar código a la hora de mostrar un cliente en el método setCliente()*/
	private void mostrarCliente(String dni) {
		if (!searchCliente(dni)) {
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

	public void listClientes() {
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
	
	public boolean searchCliente(String dni) {
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

	private void mostrarMenu() {
		System.out.println("------------------");
		System.out.println("GESTIÓN DE CLIENTES");
		System.out.println("------------------");
		System.out.println("1. Añadir cliente.");
		System.out.println("2. Modificar cliente.");
		System.out.println("3. Borrar cliente.");
		System.out.println("4. Mostrar cliente.");			
		System.out.println("5. Listar todos los clientes.");
		System.out.println("0. Volver al menú principal.");		
	}
}