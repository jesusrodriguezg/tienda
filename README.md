# Proyecto Tienda

### Aplicación Java para gestionar productos, clientes y compras de forma interactiva mediante consola. Versión 0.2.0.

Esta aplicación ha sido desarrollada como un ejercicio práctico de clase dentro del módulo de Programación de primer curso del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Web, en el **[I.E.S. Polígono Sur de Sevilla](http://iespoligonosur.org/ "I.E.S. Polígono Sur")**, bajo la tutela del profesor **[Paulino Huerta](https://github.com/paulinohuerta/ "GitHub de Paulino Huerta")**.

El objetivo de este trabajo práctico es el de crear un programa de gestión de compras para ensayar el acceso a datos y la grabación de éstos en un formato elegido por el alumno, con las siguientes características:

* Una persona compra productos, cada producto tiene características y precio.
* La compra que realiza una persona puede contener más de un producto.
* Persistencia de datos (método a elección del alumno).
* Usar más de una implementación para resolver el nivel de acceso a datos.
* Conseguir un diseño flexible.

Las distintas versiones irán incorporando mejoras y nuevas funcionalidades.

1. [Estructura del programa](#1)
2. [Antes de empezar](#2)
3. [Creación de la base de datos y las tablas](#3)
4. [Ejecución](#4)
5. [Funcionamiento](#5)
6. [Funciones en desarrollo](#6)
7. [Lista de cambios](#7)

### <a name="1"></a>Estructura del programa

En la versión 0.2.0 cambia por completo el modelo, ya que se aplica un patrón de diseño DAO. A continuación se explica la estructura de paquetes y el funcionamiento y propósito de cada una de las clases:

> NOTA: El uso detallado y las funciones que permite la aplicación se explican en el apartado [5. Funcionamiento](#5).

* **com.tienda.model** - Paquete que incluye cuatro POJO (clases básicas de objetos), con sus respectivas propiedades privadas y métodos de acceso (_getters_) y establecimiento (_setters_) públicos.

| Clase | Propósito |
| ----- | ------- |
| Producto() | POJO para crear productos. |
| Cliente() | POJO para crear clientes. |
| Factura() | POJO para crear facturas. |
| LineaFactura() | POJO para crear las líneas que corresponde a cada producto adquirido en cada factura; la finalidad es poder almacenar toda esta información en la base de datos para poder gestionar de manera eficiente la facturación. |

* **com.tienda.client** - Paquete que contiene las clases que permiten la gestión de los productos, los clientes, las facturas y las propias compras. Únicamente contienen datos puramente utilitarios, caso del menu(), que sólo sirve para mostrar un menú de opciones que ayuda al usuario a interactuar con el programa. El resto de funciones se heredan de las clases incluidas en el paquete **com.tienda.dao**.

| Clase | Propósito |
| ----- | ------- |
| Almacen() | Permite la gestión de productos: añadir, modificar precio, borrar, mostrar un producto y listar todos los productos. |
| GestionClientes() | Permite la gestión de clientes: añadir, modificar, borrar, mostrar un cliente y listar todos los clientes. |
| GestionFacturas() | Permite la gestión de facturas: mostrar todas las facturas, mostrar sólo las de un cliente o fecha concretos, o borrar facturas. |
| Catalogo() | Permite la compra de productos siempre que haya existencias en el almacén. Al entrar, el cliente se identifica con su DNI y puede añadir o quitar productos de la cesta, mostrar el contenido de ésta, consultar las existencias del almacén y pagar. Al pagar se genera una factura con los datos de la tienda, del comprador y de los productos comprados, así como el importe total de la compra. |
| Gestion() | Muestra el menú principal de la aplicación y llama al método para establecer la conexión con la base de datos. |
| Tienda() | Clase ejecutable que contiene el método _main_ para iniciar la aplicación. |
| PedirDatos() | Clase accesoria que permite al usuario interactuar con el programa introduciendo datos de distinto tipo mediante Scanner() y controlando diversas excepciones. |

* **com.tienda.dao** - Paquete que almacena las interfaces y clases donde que las implementan, a fin de aislar los componentes de la aplicación. Existe una interfaz y su respectiva implementación por cada una de las clases de **com.tienda.client**, excepto Tienda() y PedirDatos(). Las interfaces definen los métodos y las clases _Impl_ desarrollan el código para que funcionen.

| Clase | Propósito |
| ----- | --------- |
| TiendaDaoAlmacen() | Define los métodos para la clase Almacen(). |
| TiendaDaoAlmacenImpl() | Implementa los métodos de TiendaDaoAlmacen() y los desarrolla. |
| TiendaDaoCliente() | Define los métodos para la clase GestionClientes(). |
| TiendaDaoClienteImpl() | Implementa los métodos de TiendaDaoCliente() y los desarrolla. |
| TiendaDaoFactura() | Define los métodos para la clase GestionFacturas(). |
| TiendaDaoFacturaImpl() | Implementa los métodos de TiendaDaoFactura() y los desarrolla. |
| TiendaDaoCatalogo() | Define los métodos para la clase Catalogo(). |
| TiendaDaoCatalogoImpl() | Implementa los métodos de TiendaDaoCatalogo() y los desarrolla. |
| TiendaDaoGestion() | Define los métodos para la clase Gestion(), entre ellos el que permite conectarse y desconectarse de la base de datos. |
| TiendaDaoGestionImpl() | Implementa los métodos de TiendaDaoGestion() y los desarrolla. |

##### Persistencia de datos

La aplicación emplea tres métodos de persistencia de datos. 

* Una base de datos SQLite en la que existen cuatro tablas: PRODUCTO, CLIENTE, FACTURA y LINEA_FACTURA. A través de las clases Almacen(), GestionClientes() y Catalogo() vamos almacenando en cada una de las tablas los datos de los objetos de cada tipo. Las facturas se consultan y gestionan a través de GestionFacturas(). La información de la base de datos se mantendrá aunque finalicemos la ejecución del programa.
> Las instrucciones para la creación de la base de datos y de las tablas, con la explicación de los campos y de las restricciones de cada una, se explican en el apartado [2. Antes de empezar](#2).
* Ficheros de texto plano .TXT en los que se almacena la información de cada compra, y que se generan al seleccionar la opción de pagar en la clase Catalogo().
* Un ArrayList() de objetos Producto() que la clase Catalogo() usa para almacenar en memoria la información sobre la compra. Si en mitad del proceso de compra salimos de Catalogo() y volvemos al menú principal se perderá la información sobre la compra y tendremos que volver a comenzar.

### <a name="2"></a>Antes de empezar

Para ejecutar la aplicación, lo primero que debemos tener instalado en nuestro sistema es Java. Puedes instalar la versión más reciente del JDK de dos maneras:

* [Descarga desde la web de Oracle](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html "Java SE Development Kit 8 Downloads").
* Si usas Linux, puede instalarlo a través de consola ejecutando los siguientes comandos:

```sh
~$ sudo add-apt-repository ppa:webupd8team/java
~$ sudo apt-get update
~$ sudo apt-get install oracle-java8-installer
```

El siguiente paso es descargar los archivos de este repositorio o clonarlo mediante consola con Git o Subversion. Para ello es necesario el siguiente código:

```sh
~$ sudo apt-get install git
~$ git clone https://github.com/jesusrodriguezg/tienda.git
```
```sh
~$ sudo apt-get install subversion
~$ svn checkout https://github.com/jesusrodriguezg/tienda.git
```

A continuación, necesitamos descargar el driver JDBC para controlar la base de datos SQLite a través de la aplicación, desde el siguiente enlace:

**[https://bitbucket.org/xerial/sqlite-jdbc/downloads/](https://bitbucket.org/xerial/sqlite-jdbc/downloads/ "Descargar JDBC SQLite")**

También necesitaremos instalar SQLite para crear la base de datos con las tablas. En el sitio oficial de SQLite encontramos enlaces de descarga para todas las plataformas, así como documentación sobre la instalación.

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

CREATE TABLE FACTURA (
	ID			INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	DNICLI		VARCHAR(9),
	FECHA		VARCHAR(10),
	IMPORTE		NUMERIC,
	CODFACT		VARCHAR(26),
	foreign key (DNICLI) references CLIENTE (DNI)
);

CREATE TABLE LINEA_FACTURA(
	ID		INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	ID_FACTURA INTEGER NOT NULL,
	ID_PRODUCTO INTEGER NOT NULL,
	CANTIDAD INTEGER NOT NULL,
	PRECIO		NUMERIC NOT NULL,
	foreign key (ID_FACTURA) references FACTURA(ID),
	foreign key (ID_PRODUCTO) references PRODUCTO(IDPROD)
);
```

A continuación debemos configurar la clase **TiendaDaoGestionImpl()** en **com.tienda.dao** para indicarle al método **_Conn()_** el directorio en el que tenemos alojada la base de datos. En este caso debemos modificar el contenido de la variable **_String dirdb_** indicándole el directorio donde se encuentra la base de datos y el nombre de ésta (extensión .db incluida):

```java
public Connection connect() {
	this.conn=null;
	try {
		Class.forName("org.sqlite.JDBC");
		String dirdb="jdbc:sqlite:carpeta-de-destino/mibasededatos.db";
		this.conn = DriverManager.getConnection(dirdb);
	} catch (ClassNotFoundException cnfe) {
		System.out.println("ERROR: driver JDBC no encontrado.");
		return null;
	} catch (SQLException sqle) {
		System.out.println("ERROR: no se encuentra la base de datos o el usuario y la contraseña no son correctos.");
		return null;
	}
	System.out.println("Ha conectado correctamente con la base de datos.");
	return this.conn;
}
```

### <a name="4"></a>Ejecución

Para una correcta y más fácil ejecución de la aplicación contamos con dos opciones: el uso de un entorno de desarrollo integrado o IDE, que es el que recomiendo por comodidad, o la compilación a través de consola.

###### Ejecución a través de un IDE

Para ejecutar el programa a través de un IDE, primero tenemos que descargarlo e instalarlo en el sistema. Por su facilidad de uso y por ser aquél en el que está desarrollado el programa, recomiendo usar **_Eclipse_**. Está disponible para Windows, Linux y MacOS en el siguiente enlace:

**[https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/ "Descargar Eclipse IDE")**

Tras la instalación, es preciso crear un nuevo proyecto Java (_New_ > _Java project_) que podemos llamar _Tienda_. Dentro de la carpeta del proyecto se creará una esta carpeta _src_ donde copiamos directamente la carpeta _com_ (y todo el contenido de ésta) de este repositorio. La estructura en _Eclipse_ debería quedar así:

![alt text][logo]

[logo]: http://i67.tinypic.com/2efl5cx.png "Estructura de carpetas en Eclipse"

Para la ejecución es necesario importar el driver JDBC en el proyecto, que recomiendo esté en el directorio raíz del proyecto Java. Para añadirlo hay que hacer clic derecho sobre el proyecto _Compra_ > _Build path_ > _Configure build path_ > _Libraries_ > _Add JARs_.

Por último, sólo tendremos que ejecutar la clase ejecutable **Tienda()**, que es la que contiene el método _main_ que llama al resto de clases.

###### Ejecución a través de consola

Dentro de la carpeta en la que hemos clonado o descargado los archivos del repositorio, primero tenemos que ejecutar el compilador de Java (```javac```), indicándole la ubicación del driver JDBC (preferentemente en la misma carpeta del proyecto); luego tendremos que ejecutar con el comando ```java```:

* Windows Powershell:
```pwsh
javac -cp <directorio del jar JDBC>;. \com\tienda\client\Tienda.java
java -cp <directorio del jar JDBC>;. \com\tienda\client\Tienda
```

* Shell de Linux:
```sh
~/tienda $ javac -cp <directorio del jar JDBC>:. com/tienda/*/*.java
~/tienda $ java -cp <directorio del jar JDBC>:. com/tienda/client/Tienda
```

### <a name="5"></a>Funcionamiento

Al ejecutar la aplicación aparecerá en la consola el menú principal, que ofrecerá al usuario cinco opciones, incluida la de salir, que termina la ejecución:

1. Gestión de almacén.
2. Gestión de clientes.
3. Gestión de facturas.
4. Comprar.
0. Salir.

##### Gestión de almacén

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

##### Gestión de clientes

En este apartado se gestionan los clientes que están registrados en la tienda. Para poder comprar es necesario que exista al menos un cliente registrado. Las opciones del menú son las siguientes:

1. Añadir producto.
2. Modificar cliente.
3. Borrar producto.
4. Mostrar un producto.
5. Listar todos los productos.
0. Volver al menú principal.

La funcionalidad de cada opción es idéntica a la de la gestión del almacén, con la salvedad de que en este caso se pueden modificar todos los datos de los clientes (nombre, teléfono y correo electrónico), excepto el DNI, que es la clave primaria y única.

##### Gestión de facturas

En este apartado se gestionan las facturas existentes, generadas por las distintas compras. Se ofrecen las siguientes opciones:

1. Mostrar facturas por comprador.
2. Mostrar facturas por fecha.
3. Borrar factura.
4. Listar todas las facturas.
0. Volver al menú principal.

La primera opción pide al usuario un DNI y muestra todas las facturas generadas para dicho comprador. La segunda opción pide una fecha con formato DD/MM/AAAA y muestra todas las facturas emitidas en dicha fecha.

La tercera opción permite borrar facturas una a una. Primero se pide al usuario el DNI del comprador, a continuación muestra todas las facturas de dicho comprador y luego se pide al usuario que especifique el ID de la factura que desea borrar.

La última opción muestra la lista de todas las facturas almacenadas en la base de datos.

##### Comprar

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

* Mejoras en la gestión de facturas, como borrar archivos .txt cuando se borran facturas en la BD o realizar consultas más detalladas.
* Inclusión de precio de coste de los productos, diferente al precio de venta al público (PVP), para calcular el beneficio.
* Corrección de errores, optimización de código y perfeccionamiento de funciones.

Aunque esto es un proyecto de clase y su funcionamiento, diseño y alcance es limitado y básico, cualquier comentario, sugerencia, ayuda o mejora son bienvenidos.

### <a name="7"></a>Cambios

**Versión 0.2.0**

* Aplicación de un patrón de diseño DAO (Data Access Object) para crear un modelo flexible y con componentes aislados mediante interfaces.
* Facturas únicas: al contrario que en la versión 0.1.0, con cada nueva compra el programa genera una factura distinta con código único, que incluye el código de la factura, el DNI del comprador y la fecha.
* Creación de tablas para facturas en la BD: se crean las tablas FACTURA y LINEA_FACTURA; en la primera se guardan registros únicos para cada factura que se genera; en la segunda se van guardando registros con cada producto comprado que exista en cada factura.
* Gestión de facturas: creación de un nuevo apartado que permite consultar todas las facturas y discriminar por DNI de comprador y por fecha, así como borrar facturas.

**Versión 0.1.0**

* Diseño original del modelo, que no cumple todos los requisitos, ya que no se ha diseñado siguiendo el patrón DAO, sino que se incluye todo en el mismo paquete, sin aislamiento ni interfaces.
* Funcionalidades principales de la aplicación: creación, modificación, eliminación y listado de productos y clientes; compra de productos y generación de facturas.
* Persistencia de datos: base de datos para almacenar productos y clientes; archivo .TXT para almacenar facturas al pagar (siempre con el mismo nombre "Factura.txt", por lo que el archivo se sobreescribe con cada nueva compra).