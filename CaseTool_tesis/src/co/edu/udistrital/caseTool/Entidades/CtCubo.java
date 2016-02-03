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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "\"caseTool\".\"CT_CUBOS\"")
public class CtCubo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ideCubo;
	private String nombre;
	private CtHecho hecho;
	private List<CtDimension> dimensiones;
	private Date fechaCreacion = new Date(); 
	private Long ideUsuarioCreacion;
	private List<CtProyecto> proyecto;

	@Id
	@Column(name = "\"IDE_CUBO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdeCubo() {
		return ideCubo;
	}

	public void setIdeCubo(Long ideCubo) {
		this.ideCubo = ideCubo;
	}

	@Column(name="\"NOMBRE_CUBO\"")	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "\"IDE_HECHO\"") })
	public CtHecho getHecho() {
		return hecho;
	}

	public void setHecho(CtHecho hecho) {
		this.hecho = hecho;
	}

	@ManyToMany (cascade=CascadeType.ALL)
	@JoinTable(name = "\"caseTool\".\"CT_DIMENSIONES_CUBO\"", joinColumns = { @JoinColumn(name = "\"IDE_CUBO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_DIMENSION\"") })
	public List<CtDimension> getDimensiones() {
		return dimensiones;
	}

	public void setDimensiones(List<CtDimension> dimensiones) {
		this.dimensiones = dimensiones;
	}
	
	@Column(name = "\"IDE_USUARIO_CREACION\"")
	public Long getIdeUsuarioCreacion() {
		return ideUsuarioCreacion;
	}

	public void setIdeUsuarioCreacion(Long ideUsuarioCreacion) {
		this.ideUsuarioCreacion = ideUsuarioCreacion;
	}

	@Column(name="\"FECHA_CREACION\"")
	@Temporal (TemporalType.TIMESTAMP)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_CUBOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_CUBO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") })
	public List<CtProyecto> getProyecto() {
		return proyecto;
	}

	public void setProyecto(List<CtProyecto> proyecto) {
		this.proyecto = proyecto;
	}

	@Transient
	public void adicionarDimension(CtDimension dimensionNueva) {
		this.dimensiones.add(dimensionNueva);

	}

	

}
