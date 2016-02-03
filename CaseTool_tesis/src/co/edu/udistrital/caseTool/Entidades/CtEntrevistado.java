package co.edu.udistrital.caseTool.Entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "\"caseTool\".\"CT_ENTREVISTADO\"")
public class CtEntrevistado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long ideEntrevistado;
	private String nombre; 
	private String area; 
	private String cargo;
	private String nivelJerarquico;
	private Long ideUsuario; 
	
	@Id
	@Column(name = "\"IDE_ENTREVISTADO\"")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdeEntrevistado() {
		return ideEntrevistado;
	}
	public void setIdeEntrevistado(Long ideEntrevistado) {
		this.ideEntrevistado = ideEntrevistado;
	}
	
	@Column(name="\"NOMBRE_ENTREVISTADO\"")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="\"AREA\"")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	@Column(name="\"CARGO\"")
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	@Column(name="\"IDE_USUARIO\"")
	public Long getIdeUsuario() {
		return ideUsuario;
	}
	public void setIdeUsuario(Long ideUsuario) {
		this.ideUsuario = ideUsuario;
	}
	
	@Column(name="\"NIVEL_JERARQUICO\"")
	public String getNivelJerarquico() {
		return nivelJerarquico;
	}
	public void setNivelJerarquico(String nivelJerarquico) {
		this.nivelJerarquico = nivelJerarquico;
	} 
	
	

	

}
