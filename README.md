# Proyecto Tienda

### Aplicación Java para gestionar productos, clientes y compras de forma interactiva mediante consola.

Esta aplicación ha sido desarrollada como un ejercicio práctico de clase dentro del módulo de Programación de primer curso del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Web, en el **[I.E.S. Polígono Sur de Sevilla](http://iespoligonosur.org/ "I.E.S. Polígono Sur")**, bajo la tutela del profesor **[Paulino Huerta](https://github.com/paulinohuerta/ "GitHub de Paulino Huerta")**.

El objetivo de este trabajo práctico es el de crear un programa que escenifique la gestión de compras para ensayar el acceso a datos y la grabación de éstos en un formato elegido por el alumno, con las siguientes características:

* Una persona compra productos, cada producto tiene características y precio.
* La compra que realiza una persona puede contener más de un producto.
* Persistencia de datos (método a elección del alumno).
* Usar más de una implementación para resolver el nivel de acceso a datos.
* Conseguir un diseño flexible.

La versión inicial que se presenta (0.1.0) contiene el diseño original del modelo, que no cumple todos los requisitos citados más arriba. En concreto, se contempla aplicar al diseño de la aplicación el patrón DAO (Data Access Object) para crear un modelo con componentes aislados para favorecer la flexibilidad en el acceso a los datos y frente a cambios que se harán con posterioridad para mejorar y ampliar la funcionalidad.

1. [Estructura del programa](#1)
2. [Antes de empezar](#2)
3. [Creación de la base de datos y las tablas](#3)
4. [Ejecución](#4)
5. [Funcionamiento](#5)
6. [Funciones en desarrollo](#6)
7. [Lista de cambios](#7)

### <a name="1"></a>Estructura del programa

En la versión 0.1.0 se incluyen las siguientes clases, cuyo funcionamiento y propósito se explica a continuación:

* **Producto()** - Clase _value object_ o básica, donde se definen las propiedades de los objetos de tipo "producto" que se venderán en la tienda, así como los métodos de acceso (_getters_) y establecimiento (_setters_) de dichos objetos.
* **Cliente()** - Clase _value object_ para los objetos cliente, con sus correspondientes propiedades y métodos _getters_ y _setters_.
* **Almacen()** - Clase que permite la gestión de objetos de tipo Producto() en la tienda, a fin de crearlos/añadirlos, borrarlos, modificar su precio, mostrar un producto o listar todas las existencias.
* **GestionClientes()** - Clase que permite la gestión de los objetos Cliente() registrados en la tienda, con el mismo propósito y funciones que la clase Almacen() respecto a los objetos de tipo Producto().
* **Catalogo()** - Clase en la que un cliente se identifica con su DNI y puede comprar productos siempre que estén disponibles en el almacén. Se pueden añadir y quitar productos de la cesta, mostrar el contenido de ésta, consultar las existencias del almacén y pagar. Al pagar se genera una factura con los datos de la tienda, del comprador y de los productos comprados, así como el importe total de la compra.
* **Tienda()** - Clase que muestra el menú principal de la aplicación y crea la conexión con la base de datos.
* **Compra()** - Clase ejecutable, que contiene el método _main_ para la ejecución del programa.
* **PedirDatos()** - Clase accesoria que permite al usuario interactuar con el programa introduciendo datos de distinto tipo y controlando diversas excepciones.

La aplicación emplea dos métodos de persistencia de datos. En primer lugar, una base de datos SQLite en la que existen dos tablas, PRODUCTO y CLIENTE. A través de las clases Almacen() y GestionClientes(), respectivamente, vamos almacenando en ambas tablas los datos de los objetos de cada tipo. La información de la base de datos permanecerá aunque finalicemos la ejecución del programa.

_(Las instrucciones para la creación de la base de datos y de las tablas, con la explicación de los campos y de las restricciones de cada una, se explican más abajo, en el apartado "Antes de empezar")_

Por otro lado, en la clase Catalogo(), que es la que gestiona la compra en sí, utilizamos un ArrayList para almacenar en memoria la información sobre la compra. Si en mitad del proceso de compra salimos de Catalogo() y volvemos al menú principal se perderá la información sobre la compra y tendremos que volver a comenzar. Si, en cambio, finalizamos la compra mediante el método pagar, se generará una factura en un archivo de texto plano (.TXT), y el ArrayList quedará igualmente vacío.

### <a name="2"></a>Antes de empezar

El primer paso para la ejecución es descargar los archivos de este repositorio o clonarlo con el siguiente comando en consola:

`~$ mkdir tienda && cd tienda && git clone https://github.com/jesusrodriguezg/tienda`

En segundo lugar, necesitamos el driver JDBC para controlar la base de datos SQLite a través de la aplicación. El driver se puede descargar en el siguiente enlace:

**[https://bitbucket.org/xerial/sqlite-jdbc/downloads/](https://bitbucket.org/xerial/sqlite-jdbc/downloads/ "Descargar JDBC SQLite")**

También necesitaremos instalar SQLite para crear la base de datos con las tablas PRODUCTO y CLIENTE. En el sitio oficial de SQLite encontramos enlaces de descarga para todas las plataformas, así como documentación sobre la instalación.


**[https://www.sqlite.org/](https://www.sqlite.org/index.html "Descargar SQLite")**

Es opcional, aunque recomendable, gestionar la base de datos a través de un entorno gráfico. Para este proyecto me he valido de dos: DB Browser for SQLite y SQLite Studio. Ambos están disponibles para Windows, Linux y MacOS y se pueden descargar en los siguientes enlaces:

* DB Browser for SQLite - **[https://sqlitebrowser.org](https://sqlitebrowser.org/ "Descargar DB Browser for SQLite")**
* SQLite Studio - **[https://sqlitestudio.pl](https://sqlitestudio.pl/index.rvt "Descargar SQLite Studio")**

### <a name="3"></a>Creación de la base de datos y de las tablas

Antes de compilar y/o ejecutar el programa debemos crear la base de datos COMPRA y guardarla en la carpeta que elijamos. Entre los archivos de este repositorio se incluyen un fichero .SQL con los scripts para crear las tablas PRODUCTO y CLIENTE en la base de datos, con los respectivos campos, tipos de datos y restricciones. El código es el siguiente:

```sql
CREATE TABLE PRODUCTO (
	IDPROD 		INTEGER 	NOT NULL
							UNIQUE,
	NOMPROD 	VARCHAR(10) NOT NULL
							UNIQUE,
	PRECIO 		DECIMAL(4,2),
	CANTIDAD 	NUMERIC(2),
	PRIMARY KEY(IDPROD,NOMPROD)
);

CREATE TABLE CLIENTE (
    DNI        VARCHAR (9)  NOT NULL
    						UNIQUE,
    NOMCLIENTE VARCHAR (25) NOT NULL,
    TELEFONO   VARCHAR (9),
    CORREO     VARCHAR (30)
    PRIMARY KEY(DNI,CLIENTE)
);
```

A continuación debemos configurar la clase **Tienda()** para indicarle al método **_Conectar()_** el directorio en el que tenemos alojada la base de datos. En este caso debemos modificar el contenido de la variable **_String dirdb_** indicándole el directorio donde se encuentra la base de datos y el nombre de ésta (extensión .db incluida):

```java
private Connection conectar() {
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
			String dirdb="jdbc:sqlite:carpeta-de-destino/mibasededatos.db";
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
```

### <a name="4"></a>Ejecución

Para una correcta y más fácil ejecución de la aplicación contamos con dos opciones: el uso de un entorno de desarrollo integrado o IDE, que es el que recomiendo por comodidad, o la compilación a través de consola.

###### Ejecución a través de un IDE

Para ejecutar el programa a través de un IDE, primero tenemos que descargarlo e instalarlo en el sistema. Por su facilidad de uso y por ser aquél en el que está desarrollado el programa, recomiendo usar **_Eclipse_**. Está disponible para descarga para los principales sistemas (Windows, Linux, MacOS) en el siguiente enlace:

**[https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/ "Descargar Eclipse IDE")**

Tras la instalación, es preciso crear un nuevo proyecto Java (_New_ > _Java project_) que podemos llamar _Compra_, y dentro de éste un nuevo paquete (_New_ > _package_), con idéntico nombre _compra_. Dentro de la carpeta _src_ copiamos las clases de este proyecto.

![alt text][logo]

[logo]: http://i63.tinypic.com/15gb5o9.png "Estructura de carpetas en Eclipse"

Para la ejecución es necesario importar el driver JDBC en el proyecto (clic derecho sobre el proyecto _Compra_ > _Build path_ > _Configure build path_ > _Libraries_ > _Add JARs_). También será necesario declarar el nombre del paquete (en este caso _compra_ o el que hayamos elegido) en la cabecera de cada clase.

```java
package compra;
```

Por último, sólo tendremos que ejecutar la clase ejecutable **Compra()**, que es la que contiene el método _main_ que llama al resto de clases y métodos.

###### Ejecución a través de consola

Dentro de la carpeta en la que hemos clonado o descargado los archivos del repositorio, ejecutamos el compilador de Java (javac):

`~/tienda $ javac *.java`

A continuación ejecutamos la clase Compra() con el comando `java`. En el mismo comando debemos indicar que el programa necesita el driver JDBC SQLite cuya descarga indicamos más arriba (y que está alojado preferentemnte en la misma carpeta que el resto del proyecto):

`~/tienda $ java Compra sqlite-jdbc-3.23.1`

### <a name="5"></a>Funcionamiento

Al ejecutar la aplicación aparecerá en la consola el menú principal, que ofrecerá al usuario cuatro opciones:

1. Gestión de almacén.
2. Gestión de clientes.
3. Comprar.
0. Salir.

###### Gestión de almacén

Aquí es donde se gestionan las existencias de productos en el almacén de la tienda. Si no hay productos, lógicamente, no se puede comprar. Al seleccionar esta opción se despliega un nuevo menú con las siguientes opciones:

1. Añadir producto.
2. Modificar precio de un producto.
3. Borrar producto.
4. Mostrar un producto.
5. Listar todos los productos.
0. Volver al menú principal.

Al añadir producto se nos piden los cuatro datos del producto: ID, nombre, precio (con decimales; si se introduce un entero, se añaden los decimales .00) y cantidad (si la cantidad es 0, el programa hace que sea al menos una unidad) y se añade a la tabla. Si en la base de datos ya hay un producto cuyo ID o nombre coincide con los especificados, el programa permite elegir si se quieren añadir más unidades de dicho producto.

Además de la cantidad de unidades, solamente se puede modificar el precio de los productos. Tanto el ID como el nombre son claves primarias y únicas y no pueden repetirse ni deben modificarse. Al modificar el precio el programa pide un ID y muestra los datos del producto que se va a modificar. Luego confirma los cambios con el precio anterior y el actual.

Al borrar un producto, se pide el ID del producto y, si existe, se borra de la base de datos.

La cuarta opción pide un ID y muestra dicho producto, si existe. La quinta muestra la lista con todos los productos del almacén.

###### Gestión de clientes

En este apartado se gestionan los clientes que están registrados en la tienda. Para poder comprar es necesario que exista al menos un cliente registrado. Las opciones del menú son las siguientes:

1. Añadir producto.
2. Modificar cliente.
3. Borrar producto.
4. Mostrar un producto.
5. Listar todos los productos.
0. Volver al menú principal.

La funcionalidad de cada opción es idéntica a la de la gestión del almacén, con la salvedad de que en este caso se pueden modificar todos los datos de los clientes (nombre, teléfono y correo electrónico), excepto el DNI, que es la clave primaria y única.

###### Comprar

En este apartado un cliente realiza la compra de productos. Cuando el usuario elige esta opción, el programa le pide que se identifique como comprador introduciendo su DNI; si éste no existe, no puede comprar. Una vez identificado como comprador, se despliega el menú de compra:

1. Añadir producto a la cesta.
2. Quitar producto de la cesta.
3. Listar productos de la cesta.
4. Mostrar catálogo.
5. Pagar.
0. Volver al menú principal.

La primera opción pide el nombre de un producto y añade a la cesta una unidad de dicho producto. Al añadir un producto a la cesta se resta una unidad de dicho producto en la tabla de la base de datos.

La segunda opción pide el nombre de un producto y elimina una unidad de la cesta. Al hacerlo, se suma uno al número de unidades de dicho producto en la base de datos.

La opción 3 muestra todos los productos que hay en ese momento en la cesta, mientras que la 4 muestra los productos que quedan en el almacén (con los cambios que haya podido haber tras añadir o quitar productos de la cesta).

Al pagar se genera un fichero "Factura" en formato de texto plano (.TXT) con los datos de la tienda, del comprador, de los productos que ha comprado (nombre y precio) y la suma del total de la compra.

### <a name="6"></a>Funciones en desarrollo / mejoras

Algunas funciones que se encuentran en desarrollo para futuras versiones son:

* Diseño flexible basado en el patrón de diseño DAO, mediante interfaces
* Mejora del diseño de la base de datos para incluir una tabla de compras realizadas por cada cliente.
* Inclusión de precio de coste de los productos, diferente al precio de venta al público (PVP), para calcular el beneficio.
* Corrección de errores, optimización de código y perfeccionamiento de funciones.

Aunque esto es un proyecto de clase y su funcionamiento, diseño y alcance es limitado y básico, cualquier comentario, sugerencia, ayuda o mejora son bienvenidos.