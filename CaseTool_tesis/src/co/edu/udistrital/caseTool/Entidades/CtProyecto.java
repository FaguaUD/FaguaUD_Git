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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the "CT_PROYECTO" database table.
 * 
 */
@Entity
@Table(name = "\"caseTool\".\"CT_PROYECTO\"")
@NamedQuery(name = "CtProyecto.findAll", query = "SELECT c FROM CtProyecto c")
public class CtProyecto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long ideProyecto;
	private String descripcionProyecto;
	private Date fechaCreacion = new Date();
	private Date fechaTerminacion = new Date();
	private Long liderProyecto;
	private String nombreProyecto;
	private List<CtUsuario> usuariosAsignados;
	private List<CtFormulario> formulariosProyecto;
	private List<CtCubo> cubosProyecto;
	private Integer fase;

	public CtProyecto() {
	}

	@Id
	@Column(name = "\"IDE_PROYECTO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdeProyecto() {
		return this.ideProyecto;
	}

	public void setIdeProyecto(Long ideProyecto) {
		this.ideProyecto = ideProyecto;
	}

	@Column(name = "\"DESCRIPCION_PROYECTO\"")
	public String getDescripcionProyecto() {
		return this.descripcionProyecto;
	}

	public void setDescripcionProyecto(String descripcionProyecto) {
		this.descripcionProyecto = descripcionProyecto;
	}

	@Column(name = "\"FECHA_CREACION\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@Column(name = "\"FECHA_TERMINACION\"")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}


	@Column(name = "\"NOMBRE_PROYECTO\"")
	public String getNombreProyecto() {
		return this.nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	@Column(name = "\"IDE_LIDER_PROYECTO\"")
	public Long getLiderProyecto() {
		return this.liderProyecto;
	}

	public void setLiderProyecto(Long liderProyecto) {
		this.liderProyecto = liderProyecto;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_USUARIOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_USUARIO\"") })
	public List<CtUsuario> getUsuariosAsignados() {
		return usuariosAsignados;
	}

	public void setUsuariosAsignados(List<CtUsuario> usuariosAsignados) {
		this.usuariosAsignados = usuariosAsignados;
	}

	@Column(name = "\"FASE\"")
	public Integer getFase() {
		return fase;
	}

	public void setFase(Integer fase) {
		this.fase = fase;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_FORMULARIOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_FORMULARIO\"") })
	public List<CtFormulario> getFormulariosProyecto() {
		return formulariosProyecto;
	}

	public void setFormulariosProyecto(List<CtFormulario> formulariosProyecto) {
		this.formulariosProyecto = formulariosProyecto;
	}
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_CUBOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_CUBO\"") })
	public List<CtCubo> getCubosProyecto() {
		return cubosProyecto;
	}

	public void setCubosProyecto(List<CtCubo> cubosProyecto) {
		this.cubosProyecto = cubosProyecto;
	}

	@Transient
	public void addFormulario(CtFormulario formularioNuevo) {
		formulariosProyecto.add(formularioNuevo);
	}

	@Transient
	public void removeFormularioXId(Long idFormulario) {
		for (CtFormulario formulario : this.formulariosProyecto) {
			if (formulario.getIdFormulario().equals(idFormulario)) {
				this.formulariosProyecto.remove(formulario);
				break;
			}
		}

	}

	@Transient
	public boolean existeFormulario(Long idFormulario) {
		for (CtFormulario formulario : this.formulariosProyecto) {
			if (formulario.getIdFormulario().equals(idFormulario)) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public void addCubo(CtCubo cuboOperacion) {
		this.cubosProyecto.add(cuboOperacion);

	}


	

}