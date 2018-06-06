package Modelo.DTO;

import java.io.Serializable;

/**
 * 
 * @author DESARROLLO1
 *
 */
public class ImagenFrutoPesada implements Serializable{

	private String linea;
	private String num_ticket;
	private String dato1;
	private String dato2;
	private String dato3;
	private String dato4;
	private String dato5;
	private String dato6;
	private String dato7;
	private String dato8;
	private String dato9;
	private String dato10;
	private String dato11;
	private String dato12;
	
	
	public ImagenFrutoPesada() {
		super();
	}


	public ImagenFrutoPesada(String linea, String num_ticket, String dato1, String dato2, String dato3, String dato4, String dato5, String dato6,
			String dato7, String dato8, String dato9, String dato10, String dato11, String dato12) {
		super();
		this.linea= linea;
		this.num_ticket=num_ticket;
		this.dato1 = dato1;
		this.dato2 = dato2;
		this.dato3 = dato3;
		this.dato4 = dato4;
		this.dato5 = dato5;
		this.dato6 = dato6;
		this.dato7 = dato7;
		this.dato8 = dato8;
		this.dato9 = dato9;
		this.dato10 = dato10;
		this.dato11 = dato11;
		this.dato12 = dato12;
	}


	public String getDato1() {
		return dato1;
	}
	
	


	public String getLinea() {
		return linea;
	}


	public void setLinea(String linea) {
		this.linea = linea;
	}


	public String getNum_ticket() {
		return num_ticket;
	}


	public void setNum_ticket(String num_ticket) {
		this.num_ticket = num_ticket;
	}


	public void setDato1(String dato1) {
		this.dato1 = dato1;
	}


	public String getDato2() {
		return dato2;
	}


	public void setDato2(String dato2) {
		this.dato2 = dato2;
	}


	public String getDato3() {
		return dato3;
	}


	public void setDato3(String dato3) {
		this.dato3 = dato3;
	}


	public String getDato4() {
		return dato4;
	}


	public void setDato4(String dato4) {
		this.dato4 = dato4;
	}


	public String getDato5() {
		return dato5;
	}


	public void setDato5(String dato5) {
		this.dato5 = dato5;
	}


	public String getDato6() {
		return dato6;
	}


	public void setDato6(String dato6) {
		this.dato6 = dato6;
	}


	public String getDato7() {
		return dato7;
	}


	public void setDato7(String dato7) {
		this.dato7 = dato7;
	}


	public String getDato8() {
		return dato8;
	}


	public void setDato8(String dato8) {
		this.dato8 = dato8;
	}


	public String getDato9() {
		return dato9;
	}


	public void setDato9(String dato9) {
		this.dato9 = dato9;
	}


	public String getDato10() {
		return dato10;
	}


	public void setDato10(String dato10) {
		this.dato10 = dato10;
	}


	public String getDato11() {
		return dato11;
	}


	public void setDato11(String dato11) {
		this.dato11 = dato11;
	}


	public String getDato12() {
		return dato12;
	}


	public void setDato12(String dato12) {
		this.dato12 = dato12;
	}


	@Override
	public String toString() {
		return "ImagenFrutoPesada [linea=" + linea + ", dato1=" + dato1 + ", dato2="
				+ dato2 + ", dato3=" + dato3 + ", dato4=" + dato4 + ", dato5=" + dato5 + ", dato6=" + dato6 + ", dato7="
				+ dato7 + ", dato8=" + dato8 + ", dato9=" + dato9 + ", dato10=" + dato10 + ", dato11=" + dato11
				+ ", dato12=" + dato12 + "]";
	}



	
	
	
	
	
}
