package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "\"caseTool\".\"CT_ATRIBUTOS\"")
public class CtAtributos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ideAtributo;
	private String nombre;
	private List<CtRespuesta> respuestas;
	private List<CtDimension> dimensiones;

	@Id
	@Column(name = "\"IDE_ATRIBUTO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdeAtributo() {
		return ideAtributo;
	}

	public void setIdeAtributo(Long ideAtributo) {
		this.ideAtributo = ideAtributo;
	}

	@Column(name="\"NOMBRE_ATRIBUTO\"")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToMany (cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_RESPUESTAS_ATRIBUTOS\"", joinColumns = { @JoinColumn(name = "\"IDE_ATRIBUTO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_RESPUESTA\"") })	
	public List<CtRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<CtRespuesta> respuestas) {
		this.respuestas = respuestas;
	}

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_DIMENSIONES_ATRIBUTOS\"", joinColumns = { @JoinColumn(name = "\"IDE_ATRIBUTO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_DIMENSION\"") })
	public List<CtDimension> getDimensiones() {
		return dimensiones;
	}

	public void setDimensiones(List<CtDimension> dimensiones) {
		this.dimensiones = dimensiones;
	}
	
	

}
