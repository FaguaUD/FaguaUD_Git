package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "\"caseTool\".\"CT_FORMULARIOS\"")
public class CtFormulario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idFormulario;
	private String nombreFormulario;
	private Date fechaCreacion = new Date();
	private Date fechaFinEstimada = new Date();
	private Date fechaFinReal = new Date();
	private String descripcionFormulario;
	private List<CtPreguntas> preguntasFormulario;
	private List<CtProyecto> proyectos; 
	private List<CtRespuesta> respuestas;
	
	
	@Id
	@Column(name = "\"IDE_FORMULARIO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdFormulario() {
		return idFormulario;
	}
	public void setIdFormulario(Long idFormulario) {
		this.idFormulario = idFormulario;
	}
	
	@Column(name = "\"NOMBRE_FORMULARIO\"")
	public String getNombreFormulario() {
		return nombreFormulario;
	}
	public void setNombreFormulario(String nombreFormulario) {
		this.nombreFormulario = nombreFormulario;
	}
	
	@Column(name = "\"FECHA_CREACION\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@Column(name = "\"DESCRIPCION_FORMULARIO\"")
	public String getDescripcionFormulario() {
		return descripcionFormulario;
	}
	public void setDescripcionFormulario(String descripcionFormulario) {
		this.descripcionFormulario = descripcionFormulario;
	}
	
	@Column(name = "\"FECHA_FINAL_ESTIMADA\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaFinEstimada() {
		return fechaFinEstimada;
	}
	
	public void setFechaFinEstimada(Date fechaFinEstimada) {
		this.fechaFinEstimada = fechaFinEstimada;
	}
	
	@Column(name = "\"FECHA_FINAL_REAL\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaFinReal() {
		return fechaFinReal;
	}
	public void setFechaFinReal(Date fechaFinReal) {
		this.fechaFinReal = fechaFinReal;
	}
	
	@ManyToMany (cascade= CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_FORMULARIOS_PREGUNTAS\"", joinColumns = { @JoinColumn(name = "\"IDE_FORMULARIO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_PREGUNTA\"") })
	public List<CtPreguntas> getPreguntasFormulario() {
		return preguntasFormulario;
	}
	public void setPreguntasFormulario(List<CtPreguntas> preguntasFormulario) {
		this.preguntasFormulario = preguntasFormulario;
	}
	
	@Transient
	public void removerPregunta(CtPreguntas pregunta) {
		this.preguntasFormulario.remove(pregunta);
		
	}
	
	@Transient
	public void adicionarPregunta(CtPreguntas preguntaNueva) {
		this.preguntasFormulario.add(preguntaNueva);
		
	}

	@Transient
	public CtFormulario clonar() throws CloneNotSupportedException {
		
		CtFormulario clonado = new CtFormulario();
		clonado.setDescripcionFormulario(this.descripcionFormulario);
		clonado.setFechaCreacion(new Date());
		clonado.setFechaFinEstimada(this.fechaFinEstimada);
		clonado.setFechaFinReal(new Date());
		clonado.setNombreFormulario(String.format("%s_copia", this.nombreFormulario));	
		
		return clonado;
	}
	
	@ManyToMany (fetch=FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_FORMULARIOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_FORMULARIO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") })
	public List<CtProyecto> getProyectos() {
		return proyectos;
	}
	public void setProyectos(List<CtProyecto> proyectos) {
		this.proyectos = proyectos;
	}
	
	@OneToMany(mappedBy="formulario")
	public List<CtRespuesta> getRespuestas() {
		return respuestas;
	}
	public void setRespuestas(List<CtRespuesta> respuestas) {
		this.respuestas = respuestas;
	}
	
	@Transient
	public void adicionarRespuesta(CtRespuesta resTmp) {
		this.respuestas.add(resTmp);
		
	}
	
	

	
}
