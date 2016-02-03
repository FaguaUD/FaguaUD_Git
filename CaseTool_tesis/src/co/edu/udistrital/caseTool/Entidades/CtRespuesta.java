package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "\"caseTool\".\"CT_RESPUESTAS\"")
public class CtRespuesta implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idRespuesta; 
	private Long entrevistado;
	private Date fechaRespuesta = new Date();
	private CtPreguntas pregunta;
	private CtOpcionesRespuesta opcionesRespuesta;
	private CtFormulario formulario;
	private Long idUsuarioEntrevistador;
	private String nombreEntrevistado; 
	
	@Id
	@Column(name="\"IDE_RESPUESTA\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdRespuesta() {
		return idRespuesta;
	}

	public void setIdRespuesta(Long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	@Column (name="\"IDE_ENTREVISTADO\"")
	public Long getEntrevistado() {
		return entrevistado;
	}
	
	public void setEntrevistado(Long entrevistado) {
		this.entrevistado = entrevistado;
	}

	@Column (name="\"FECHA_RESPUESTA\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(Date fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}
	 
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "\"IDE_PREGUNTA\"") })
	public CtPreguntas getPregunta() {
		return pregunta;
	}

	public void setPregunta(CtPreguntas pregunta) {
		this.pregunta = pregunta;
	}

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "\"IDE_OPCION_RESPUESTA\"") })
	public CtOpcionesRespuesta getOpcionesRespuesta() {
		return opcionesRespuesta;
	}

	public void setOpcionesRespuesta(CtOpcionesRespuesta opcionesRespuesta) {
		this.opcionesRespuesta = opcionesRespuesta;
	}

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "\"IDE_FORMULARIO\"") })
	public CtFormulario getFormulario() {
		return formulario;
	}

	public void setFormulario(CtFormulario formulario) {
		this.formulario = formulario;
	}

	@Column (name="\"IDE_USUARIO_ENTREVISTADOR\"")
	public Long getIdUsuarioEntrevistador() {
		return idUsuarioEntrevistador;
	}

	public void setIdUsuarioEntrevistador(Long idUsuarioEntrevistador) {
		this.idUsuarioEntrevistador = idUsuarioEntrevistador;
	}

	@Transient
	public String getNombreEntrevistado() {
		return nombreEntrevistado;
	}

	public void setNombreEntrevistado(String nombreEntrevistado) {
		this.nombreEntrevistado = nombreEntrevistado;
	}
	
	

}
