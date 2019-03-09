import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tienda {
	
	private Almacen ga=new Almacen();
	private GestionClientes gc=new GestionClientes();
	private Catalogo cat=new Catalogo();
	private Connection conn;
	
	private Connection conectar() {
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
			String dirdb="jdbc:sqlite:BD/compra.db";
			conn = DriverManager.getConnection(dirdb);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ERROR: driver JDBC no encontrado.");
			return null;
		} catch (SQLException sqle) {
			System.out.println("ERROR: no se encuentra la base de datos o el usuario y la contraseña no son correctos.");
			return null;
		}
		System.out.println("Ha conectado correctamente con la base de datos.");
		return conn;
	}
	
	private void desconectar(Connection conn) {
		try {
			conn.close();
			System.out.println("Se ha desconectado de la base de datos.");
		} catch (SQLException sqle) {
			System.out.println("Error al desconectar de la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}
	
	public void menu() {
		int opcion=0;
		this.conn=conectar();
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				ga.menu(this.conn);
				break;
			case 2:
				gc.menu(this.conn);
				break;
			case 3:
				String dni=PedirDatos.leerCadena("Por favor, introduzca su DNI para identificarse como cliente:");
				if (!identificarCliente(dni)) {
					System.out.println("No existe ningún cliente con el DNI "+dni+".");
					opcion=-1;
					break;
				}
				cat.menu(this.conn, dni);
				break;				
			case 0:
				System.out.println("Adiós.");
				desconectar(this.conn);
				break;
			default:
				System.out.println("Debe introducir una opción entre 0 y 3.");
				break;
			}
		} while (opcion!=0);
	}
	
	private void mostrarMenu() {
		System.out.println("----------");
		System.out.println("EL DESAVÍO");
		System.out.println("----------");
		System.out.println("1. Gestionar almacén.");
		System.out.println("2. Gestionar clientes.");
		System.out.println("3. Comprar.");
		System.out.println("0. Salir.");
	}
	
	private boolean identificarCliente(String dni) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM CLIENTE WHERE DNI LIKE '"+dni+"'";
			ResultSet rs=st.executeQuery(sql);
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
			System.out.println("Error al conectar con la base de datos.");
			System.out.println(sqle.getMessage());
		}
		return false;		
	}
}