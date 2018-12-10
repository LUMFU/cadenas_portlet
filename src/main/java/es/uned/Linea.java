package es.uned;

public class Linea {

	public String cadena1;
	public String cadena2;

	public String getCadena1() {
		return cadena1;
	}

	public String getCadena2() {
		return cadena2;
	}

	public void setCadena1(String cadena1) {
		this.cadena1 = cadena1;
	}

	public void setCadena2(String cadena2) {
		this.cadena2 = cadena2;
	}
        
        @Override
        public String toString() {
            return cadena1 + '\n' + cadena2;
        }

}