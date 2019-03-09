import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Almacen {
	
	private Connection conn=null;
	
	public void menu(Connection conn) {
		this.conn=conn;
		int opcion=0;
		do {
			mostrarMenu();
			opcion=PedirDatos.leerEntero("Elija una opción:");
			switch (opcion) {
			case 1:
				addProducto();
				break;
			case 2:
				setPrecioProducto();
				break;
			case 3:
				delProducto();
				break;
			case 4:
				mostrarProducto();
				break;
			case 5:
				listProductos();
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

	private void addProducto() {
		//pedimos el número de serie del producto
		int id=PedirDatos.leerEntero("Introduzca el código del producto que desea añadir:");
		//comprobamos que no existe en la BD un producto con el mismo ID
		if (searchProductoID(id)) {
			char opcion=' ';
			boolean seguir=true;
			//Si existe, damos la opción de añadir más unidades
			do {
				opcion=PedirDatos.leerCaracter("El producto con el ID "+id+" ya existe. ¿Quiere añadir unidades al almacén? (S/N)");
				//mediante un bucle y un switch case controlamos la opción decidida por el usuario como si fuera un menú
				switch (opcion) {
				case 'S':
					//pedimos la cantidad que se desea introducir
					int cant=PedirDatos.leerEntero("Introduzca la cantidad de unidades que desea añadir:");
					try {
						//conectamos con la BD y ejecutamos el UPDATE para añadir la cantidad introducida por el usuario
						Statement st=conn.createStatement();
						String sql="UPDATE PRODUCTO SET CANTIDAD=CANTIDAD+"+cant+" WHERE IDPROD="+id;
						//guardamos en una variable int FIL el número de filas modificadas tras la ejecución del update
						int fil=st.executeUpdate(sql);
						st.close();
						//si el valor de FIL es uno, se muestra un mensaje de confirmación
						if (fil==1) {
							System.out.println("Se han añadido "+cant+" unidades del producto con el ID "+id+".");
						}
					} catch (SQLException sqle) {
						System.out.println("Error al conectar con con la base de datos.");
						System.out.println(sqle.getMessage());
					}
					seguir=false;
					break;
				case 'N':
					seguir=false;
					break;
				default:
					//en caso de que se introduzca una letra que no sea S ó N
					System.out.println("Debe responder [S]í o [N]o. Pruebe de nuevo.");
					break;
				}				
			} while (seguir);	//controlamos que el bucle se repite si SEGUIR es igual a TRUE
			return;
		}
		//pedimos el nombre del producto
		String nom=PedirDatos.leerCadena("Introduzca el nombre de producto que desea añadir:");
		//comprobamos que no existe en la BD un producto con el mismo nombre
		if (searchProductoNombre(nom)) {
			//si existe, se repite el mismo proceso que en la comprobación por ID
			char opcion=' ';
			boolean seguir=true;
			do {
				opcion=PedirDatos.leerCaracter("El producto con el nombre "+nom+" ya existe. ¿Quiere añadir unidades al almacén? (S/N)");
				switch (opcion) {
				case 'S':
					int cant=PedirDatos.leerEntero("Introduzca la cantidad de unidades que desea añadir:");
					try {
						Statement st=conn.createStatement();
						String sql="UPDATE PRODUCTO SET CANTIDAD=CANTIDAD+"+cant+" WHERE NOMPROD LIKE '"+nom+"'";
						int fil=st.executeUpdate(sql);
						st.close();
						if (fil==1) {
							System.out.println("Se han añadido "+cant+" unidades de "+nom+".");
						}
					} catch (SQLException sqle) {
						System.out.println("Error al conectar con con la base de datos.");
						System.out.println(sqle.getMessage());
					}
					seguir=false;
					break;
				case 'N':
					seguir=false;
					break;
				default:
					System.out.println("Debe responder [S]í o [N]o. Pruebe de nuevo.");
					break;
				}				
			} while (seguir);
			return;
		}
		//pedimos el precio del producto que deseamos añadir
		double pr=PedirDatos.leerDouble("Introduzca el precio del producto que desea añadir:");
		//pedimos la cantidad de unidades del producto que deseamos añadir; si es 0, el constructor las convierte a 1
		int cant=PedirDatos.leerEntero("Introduzca la cantidad de unidades que desea añadir:");
		//construimos un nuevo objeto Producto() con los datos introducidos y ejecutamos el INSERT para añadirlo a la BD
		Producto p=new Producto(id, nom, pr, cant);
		try {
			Statement st=conn.createStatement();
			String sql="INSERT INTO PRODUCTO (IDPROD, NOMPROD, PRECIO, CANTIDAD) VALUES ("+p.getIdprod()+", "
					+ "'"+p.getNomprod()+"', "+p.getPrecio()+", "+p.getCantidad()+")";
			//guardamos en una variable int FIL el número de filas modificadas tras la ejecución del update
			int fil=st.executeUpdate(sql);
			st.close();
			//si el valor de FIL es uno, se muestra un mensaje de confirmación
			if (fil==1) {
				System.out.println("Se han añadido al almacén "+p.getCantidad()+" unidad(es) de "+p.getNomprod()+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}
	
	//método que sustituye al modificarProducto(), ya que el IDPROD y el NOMPROD son campos que no deben modificarse
	private void setPrecioProducto() {
		//pedimos el IDPROD del producto cuyo precio deseamos modificar
		int id=PedirDatos.leerEntero("Introduzca el código del producto cuyo precio desea modificar:");
		//comprobamos si existe dicho producto en la BD
		if (!searchProductoID(id)) {
			System.out.println("No existe ningún producto con el código "+id+".");
			return;
		}
		//hacemos una select del precio del producto y lo guardamos en una variable RES para mostrarlo
		double res=0;
		try {
			Statement st=conn.createStatement();
			String sql="SELECT PRECIO FROM PRODUCTO WHERE IDPROD="+id;
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			res=rs.getDouble(1);
			rs.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		//mostramos el precio actual a la vez que pedimos el nuevo precio
		double nuevopr=PedirDatos.leerDouble("El precio del producto con el código "+id+" es de "+res+"€.\nIntroduzca el nuevo precio:");
		//ejecutamos el UPDATE en la BD
		try {
			Statement st=conn.createStatement();
			String sql="UPDATE PRODUCTO SET PRECIO="+nuevopr+" WHERE IDPROD="+id;
			//guardamos en una variable int FIL el número de filas modificadas tras la ejecución del update
			int fil=st.executeUpdate(sql);
			st.close();
			//si el valor de FIL es uno, se muestra un mensaje de confirmación de los cambios
			if (fil==1) {
				System.out.println("Se ha modificado el precio del producto con el código "+id+". Ahora es de "+nuevopr+"€.");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	private void delProducto() {
		//pedimos el código del producto que deseamos eliminar
		int id=PedirDatos.leerEntero("Introduzca el código del producto que desea borrar:");
		//comprobamos que exite el producto con dicho IDPROD
		if (!searchProductoID(id)) {
			System.out.println("No existe ningún producto con el código "+id+".");
			return;
		}
		//ejecutamos el DELETE en la BD
		try {
			Statement st=conn.createStatement();
			String sql="DELETE FROM PRODUCTO WHERE IDPROD="+id;
			//guardamos en una variable int FIL el número de filas modificadas tras la ejecución del update
			int fil=st.executeUpdate(sql);
			st.close();
			//si el valor de FIL es uno, se muestra un mensaje de confirmación
			if (fil==1) {
				System.out.println("Se ha eliminado del almacén el producto con el código "+id+".");
			}
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}
	
	private void mostrarProducto() {
		//pedimos el IDPROD del producto que queremos mostrar
		int id=PedirDatos.leerEntero("Introduzca el código del producto que desea mostrar:");
		//comprobamos que exite el producto con dicho IDPROD
		if (!searchProductoID(id)) {
			System.out.println("No existe ningún producto con el código "+id+".");
			return;
		}
		//ejecutamos la SELECT, construimos un Producto() para y mostramos el producto
		System.out.println("Los datos del producto con el ID "+id+" son:");
		try {
			Statement st=conn.createStatement();
			String sql="SELECT IDPROD, NOMPROD, PRECIO, CANTIDAD FROM PRODUCTO WHERE IDPROD="+id;
			ResultSet rs=st.executeQuery(sql);
			Producto p=new Producto();
			System.out.println("--------------------------");
			p.setIdprod(rs.getInt("IDPROD"));
			p.setNomprod(rs.getString("NOMPROD"));
			p.setPrecio(rs.getDouble("PRECIO"));
			p.setCantidad(rs.getInt("CANTIDAD"));
			System.out.println(p);
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}

	//método que hace una SELECT de todos los datos de la tabla y los muestra uno tras otro gracias a un bucle
	private void listProductos() {
		ResultSet rs;
		try {
			Statement st=conn.createStatement();
			String sql1="SELECT COUNT(*) FROM PRODUCTO";
			rs=st.executeQuery(sql1);
			rs.next();
			if (rs.getInt(1)==0) {
				System.out.println("No hay productos en el almacén.");
				return;
			}
			String sql2="SELECT IDPROD, NOMPROD, PRECIO, CANTIDAD FROM PRODUCTO";
			rs=st.executeQuery(sql2);
			Producto p=new Producto();
			while (rs.next()) {
				System.out.println("--------------------------");
				p.setIdprod(rs.getInt("IDPROD"));
				p.setNomprod(rs.getString("NOMPROD"));
				p.setPrecio(rs.getDouble("PRECIO"));
				p.setCantidad(rs.getInt("CANTIDAD"));
				System.out.println(p);
			}
			rs.close();
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Error al conectar con con la base de datos.");
			System.out.println(sqle.getMessage());
		}
	}
	
	//método que busca un producto por su IDPROD con un COUNT; si la select devuelve al menos una fila, el método devuelve TRUE
	private boolean searchProductoID(int id) {
		try {
			Statement st=conn.createStatement();
			String sql="SELECT COUNT(*) FROM PRODUCTO WHERE IDPROD="+id;
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
	
	//método idéntico al anterior, pero que hace la búsqueda por el campo NOMPROD
	private boolean searchProductoNombre(String nom) {
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
	
	private void mostrarMenu() {
		System.out.println("------------------");
		System.out.println("GESTIÓN DE ALMACÉN");
		System.out.println("------------------");
		System.out.println("1. Añadir producto.");
		System.out.println("2. Modificar precio de un producto.");
		System.out.println("3. Borrar producto.");
		System.out.println("4. Mostrar un producto.");
		System.out.println("5. Listar todos los productos.");
		System.out.println("0. Volver al menú principal.");		
	}
}
