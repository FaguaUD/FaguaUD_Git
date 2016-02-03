package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "\"caseTool\".\"CT_HECHOS\"")
public class CtHecho implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ideHecho;
	private String nombre;
	private List<CtRespuesta> respuestas;
	private Date fechaCreacion = new Date();
	private Long usuarioCreacion; 

	@Id
	@Column(name="\"IDE_HECHO\"")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getIdeHecho() {
		return ideHecho;
	}

	public void setIdeHecho(Long ideHecho) {
		this.ideHecho = ideHecho;
	}

	@Column(name="\"FECHA_CREACION\"")
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name="\"IDE_USUARIO_CREACION\"")
	public Long getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(Long usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	@Column(name="\"NOMBRE_HECHO\"")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToMany (cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_RESPUESTAS_HECHOS\"", joinColumns = { @JoinColumn(name = "\"IDE_HECHO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_RESPUESTA\"") })	
	public List<CtRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<CtRespuesta> respuestas) {
		this.respuestas = respuestas;
	}



	
	
	

}
