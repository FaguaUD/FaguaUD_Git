package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "\"caseTool\".\"CT_PREGUNTAS\"")
public class CtPreguntas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idPregunta;
	private String enunciado; 
	private String tipo;
	private List<CtOpcionesRespuesta> opciones;
	private List<CtFormulario> formularios;
	
	@Id
	@Column (name="\"IDE_PREGUNTA\"")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	public Long getIdPregunta() {
		return idPregunta;
	}
	public void setIdPregunta(Long idPregunta) {
		this.idPregunta = idPregunta;
	} 
	
	@Column (name="\"ENUNCIADO_PREGUNTA\"")
	public String getEnunciado() {
		return enunciado;
	}
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
	
	@Column(name="\"TIPO_PREGUNTA\"")
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@OneToMany(mappedBy="ctPregunta", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	public List<CtOpcionesRespuesta> getOpciones() {
		return opciones;
	}
	public void setOpciones(List<CtOpcionesRespuesta> opciones) {
		this.opciones = opciones;
	}
	
	@Transient
	public void removerOpcion(CtOpcionesRespuesta opcion) {
		this.opciones.remove(opcion);
		
	}
	
	@Transient
	public void adicionarOpcion(CtOpcionesRespuesta opcionNueva) {
		this.opciones.add(opcionNueva);
		
	}
	
	@ManyToMany (fetch=FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_FORMULARIOS_PREGUNTAS\"", joinColumns = { @JoinColumn(name = "\"IDE_PREGUNTA\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_FORMULARIO\"") })	
	public List<CtFormulario> getFormularios() {
		return formularios;
	}
	public void setFormularios(List<CtFormulario> formularios) {
		this.formularios = formularios;
	}
	
	@Transient
	public CtPreguntas clonar() {

		CtPreguntas clon = new CtPreguntas();
		clon.setEnunciado(this.enunciado);
		
		List<CtOpcionesRespuesta> opcionesClon = new ArrayList<CtOpcionesRespuesta>();
		for (CtOpcionesRespuesta opcion : this.opciones) {
			opcionesClon.add(opcion.clonar());
		}
		
		clon.setOpciones(opcionesClon);
		clon.setTipo(this.tipo);
		return clon;
	}
	
	
	
}
