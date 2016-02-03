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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "\"caseTool\".\"CT_DIMENSIONES\"")
public class CtDimension implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -776415481919515742L;
	private Long ideDimension; 
	private String nombre;
	private List<CtRespuesta> respuestas;
	private List<CtAtributos> atributos; 
	private List<CtCubo> cubos;
	private Date fechaCreacion = new Date();
	private Long usuarioCreacion; 
	
	@Id
	@Column(name="\"IDE_DIMENSION\"")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getIdeDimension() {
		return ideDimension;
	}

	public void setIdeDimension(Long ideDimension) {
		this.ideDimension = ideDimension;
	}

	@Column(name="\"NOMBRE_DIMENSION\"")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToMany (cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_RESPUESTAS_DIMENSIONES\"", joinColumns = { @JoinColumn(name = "\"IDE_DIMENSION\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_RESPUESTA\"") })	
	public List<CtRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<CtRespuesta> respuestas) {
		this.respuestas = respuestas;
	}

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_DIMENSIONES_ATRIBUTOS\"", joinColumns = { @JoinColumn(name = "\"IDE_DIMENSION\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_ATRIBUTO\"") })
	public List<CtAtributos> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<CtAtributos> atributos) {
		this.atributos = atributos;
	}
	
	@Column(name="\"FECHA_CREACION\"")
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@ManyToMany (cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_DIMENSIONES_CUBO\"", joinColumns = { @JoinColumn(name = "\"IDE_DIMENSION\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_CUBO\"") })
	public List<CtCubo> getCubos() {
		return cubos;
	}

	public void setCubos(List<CtCubo> cubos) {
		this.cubos = cubos;
	}

	@Transient
	public void adicionarAtributo(CtAtributos atributoNuevo) {
		this.atributos.add(atributoNuevo);
		
	}


	
	

}
