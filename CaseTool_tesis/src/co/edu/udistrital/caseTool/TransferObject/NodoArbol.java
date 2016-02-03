package co.edu.udistrital.caseTool.TransferObject;

import co.edu.udistrital.caseTool.Entidades.CtCubo;
import co.edu.udistrital.caseTool.Entidades.CtFormulario;

public class NodoArbol {

	private String nombreNodo;
	private CtFormulario formulario;
	private CtCubo cubo;

	public NodoArbol(String nombre) {
		setNombreNodo(nombre);
	}

	public NodoArbol(String nombreFormulario, CtFormulario formulario) {
		this.nombreNodo = nombreFormulario;	
		this.setFormulario(formulario);
	}

	public NodoArbol(String nombre, CtCubo ctCubo) {
		this.nombreNodo = nombre; 
		this.setCubo(ctCubo);
	}

	public String getNombreNodo() {
		return nombreNodo;
	}

	public void setNombreNodo(String nombreNodo) {
		this.nombreNodo = nombreNodo;
	}

	public CtFormulario getFormulario() {
		return formulario;
	}

	public void setFormulario(CtFormulario formulario) {
		this.formulario = formulario;
	}

	public CtCubo getCubo() {
		return cubo;
	}

	public void setCubo(CtCubo cubo) {
		this.cubo = cubo;
	}


}
