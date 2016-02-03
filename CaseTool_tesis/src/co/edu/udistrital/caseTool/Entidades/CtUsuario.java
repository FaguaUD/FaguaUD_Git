package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the "CT_USUARIOS" database table.
 * 
 */
@Entity
@Table(name = "\"caseTool\".\"CT_USUARIOS\"")
@NamedQuery(name = "CtUsuario.findAll", query = "SELECT c FROM CtUsuario c")
public class CtUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long ideUsuario;
	private Long padre;
	private String email;
	private String nombreUsuario;
	private String password;
	private String estado;
	private Long codigo; 
	private String nombre; 
	private String apellido; 
	private boolean primerIngreso;
	private CtRole ctRol = new CtRole();
	private List<Long> rolesConsulta;
	private List<String> estadosConsulta;
	private String nombreCompleto;
	private List<CtProyecto> proyectosAsignados;
	

	public CtUsuario() {
	}

	@Id
	@Column(name = "\"IDE_USUARIO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdeUsuario() {
		return this.ideUsuario;
	}

	public void setIdeUsuario(Long ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	@Column(name = "\"EMAIL\"")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "\"NOMBRE_USUARIO\"")
	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	@Column(name = "\"PASSWORD\"")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "\"IDE_ROL\"") })
	public CtRole getCtRol() {
		return this.ctRol;
	}

	public void setCtRol(CtRole ctRol) {
		this.ctRol = ctRol;
	}

	@Column(name = "\"PRIMER_INGRESO\"")
	public boolean getPrimerIngreso() {
		return primerIngreso;
	}

	public void setPrimerIngreso(boolean primerIngreso) {
		this.primerIngreso = primerIngreso;
	}

	@Column(name = "\"ESTADO\"")
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "\"CODIGO\"")
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	@Column(name = "\"NOMBRE\"")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name = "\"APELLIDO\"")
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@Column(name = "\"IDE_PADRE\"")
	public Long getPadre() {
		return padre;
	}

	public void setPadre(Long padre) {
		this.padre = padre;
	}
	
	@ManyToMany (fetch=FetchType.LAZY)
	@JoinTable(name = "\"caseTool\".\"CT_USUARIOS_PROYECTO\"", joinColumns = { @JoinColumn(name = "\"IDE_USUARIO\"") }, inverseJoinColumns = { @JoinColumn(name = "\"IDE_PROYECTO\"") })
	public List<CtProyecto> getProyectosAsignados() {
		return proyectosAsignados;
	}
	
	public void setProyectosAsignados(List<CtProyecto> proyectosAsignados) {
		this.proyectosAsignados = proyectosAsignados;
	}

	@Transient	
	public String getNombreCompleto() {
		nombreCompleto = String.format("%s %s", nombre, apellido);
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	@Transient
	public List<Long> getRolesConsulta() {
		return rolesConsulta;
	}

	public void setRolesConsulta(List<Long> rolesConsulta) {
		this.rolesConsulta = rolesConsulta;
	}
	
	@Transient
	public List<String> getEstadosConsulta() {
		return estadosConsulta;
	}

	public void setEstadosConsulta(List<String> estadosConsulta) {
		this.estadosConsulta = estadosConsulta;
	}

	@Transient
	public CtUsuario clonar(){
		CtUsuario clon = new CtUsuario();
		clon.setApellido(apellido);
		clon.setCodigo(codigo);
		clon.setCtRol(ctRol);
		clon.setEmail(email);
		clon.setEstado(estado);
		clon.setIdeUsuario(ideUsuario);
		clon.setNombre(nombre);
		clon.setNombreUsuario(nombreUsuario);
		clon.setPadre(padre);
		clon.setPassword(password);
		clon.setPrimerIngreso(primerIngreso);

		return clon;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CtUsuario){
			CtUsuario us = (CtUsuario)obj;
			return us.getIdeUsuario().equals(ideUsuario);			
		}
		return false;
	}
	
	

	
	
}