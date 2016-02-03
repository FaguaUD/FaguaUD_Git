package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "\"caseTool\".\"CT_OPCIONES_RESPUESTA\"")
public class CtOpcionesRespuesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idOpcionRespuesta;
	private String enunciadoOpcion;
	private CtPreguntas ctPregunta;
	
	@Id
	@Column (name="\"IDE_OPCION_RESPUESTA\"")
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	public Long getIdOpcionRespuesta() {
		return idOpcionRespuesta;
	}
	public void setIdOpcionRespuesta(Long idOpcionRespuesta) {
		this.idOpcionRespuesta = idOpcionRespuesta;
	}
	
	@Column (name="\"ENUNCIADO_OPCION\"")
	public String getEnunciadoOpcion() {
		return enunciadoOpcion;
	}
	public void setEnunciadoOpcion(String enunciadoOpcion) {
		this.enunciadoOpcion = enunciadoOpcion;
	}
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "\"IDE_PREGUNTA\"") })
	public CtPreguntas getCtPregunta() {
		return ctPregunta;
	}
	public void setCtPregunta(CtPreguntas ctPregunta) {
		this.ctPregunta = ctPregunta;
	}
	public CtOpcionesRespuesta clonar() {
		CtOpcionesRespuesta clon = new CtOpcionesRespuesta();
		clon.setEnunciadoOpcion(this.enunciadoOpcion);
		return clon;
	}
	
	

}
