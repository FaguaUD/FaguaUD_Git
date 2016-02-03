package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the "CT_ROLES" database table.
 * 
 */
@Entity
@Table(name="\"caseTool\".\"CT_ROLES\"")
@NamedQuery(name="CtRole.findAll", query="SELECT c FROM CtRole c")
public class CtRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long ideRol;
	private boolean visible;	
	private String descripcionRol;
	private List<CtUsuario> ctUsuarios;

	public CtRole() {
	}


	@Id
	@Column(name="\"IDE_ROL\"")
	public Long getIdeRol() {
		return this.ideRol;
	}

	public void setIdeRol(Long ideRol) {
		this.ideRol = ideRol;
	}


	@Column(name="\"DESCRIPCION_ROL\"")
	public String getDescripcionRol() {
		return this.descripcionRol;
	}

	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}

	@OneToMany(mappedBy="ctRol")
	public List<CtUsuario> getCtUsuarios() {
		return this.ctUsuarios;
	}

	public void setCtUsuarios(List<CtUsuario> ctUsuarios) {
		this.ctUsuarios = ctUsuarios;
	}

	@Column (name="\"VISIBLE\"")
	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

}