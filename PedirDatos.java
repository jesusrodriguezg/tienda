import java.util.InputMismatchException;
import java.util.Scanner;

public class PedirDatos {
	static private Scanner teclado=new Scanner(System.in);

	static int leerEntero(String frase){
		int entero=0;
		boolean seguir=true;
		
		do {
			try {
				System.out.println(frase);
				entero=teclado.nextInt();
				seguir=false;
				teclado.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(teclado.nextLine()+" No es un numero");
			}
		}while(seguir);
		return entero;
	}

	static double leerDouble(String frase) {
		double decimal=0;
		boolean seguir=true;
		do {
			try {
				System.out.println(frase);
				decimal=teclado.nextDouble();
				seguir=false;
				teclado.nextLine();
			} catch (InputMismatchException e) {
				teclado.nextLine();
				System.out.println("Debe introducir un numero");
			}
		} while (seguir);
		return decimal;
	}
	
	static String leerCadena(String frase) {
		String cadena="";
		
		System.out.println(frase);
		cadena=teclado.nextLine();
		
		return cadena;
	}
	
	static char leerCaracter(String frase) {
		char car;
		String cadena="";
		do {
			cadena=leerCadena(frase);
			if(cadena.length()!=1) {
				System.out.println("debe introducir un �nico caracter");
			}
		}while(cadena.length()!=1);
		
		car=cadena.charAt(0);
		return car;
	}
}
