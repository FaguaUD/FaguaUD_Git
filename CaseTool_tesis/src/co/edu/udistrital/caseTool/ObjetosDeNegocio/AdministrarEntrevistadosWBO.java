package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtEntrevistado;
import co.edu.udistrital.caseTool.Persistencia.AdminFormularios;

@ManagedBean(name = "bAdminEntrevistados")
@ViewScoped
public class AdministrarEntrevistadosWBO extends WBOGeneral {

	private CtEntrevistado entrevistado;
	private List<SelectItem> jerarquias;
	private List<CtEntrevistado> entrevistados;

	@EJB
	AdminFormularios adminPersistenciaFormularios;
	private boolean modificando = false;

	public void configurarVista(ComponentSystemEvent event) {

		if (!modificando) {
			entrevistado = new CtEntrevistado();
		}

		jerarquias = new ArrayList<SelectItem>();

		jerarquias.add(new SelectItem("Estratégico", "Estratégico"));
		jerarquias.add(new SelectItem("Técnico", "Técnico"));
		jerarquias.add(new SelectItem("Operativo", "Operativo"));

		if (getUsuarioLogueado() != null) {
			
			Long idUsuarioConsulta = darIdUsuario();
			
			entrevistados = adminPersistenciaFormularios
					.consultarEntrevistadosDisponibles(idUsuarioConsulta);
		}

	}

	public void btnCrearEntrevistado() {

		try {
			
			Long idUsuarioRegistro = darIdUsuario();
			
			
			entrevistado.setIdeUsuario(idUsuarioRegistro);

			adminPersistenciaFormularios.crearEntrevistado(entrevistado);

			escribirInfoPantalla("Entrevistado creado con éxito", "",
					"mensajesEntrevistado");

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla(
					"Error",
					"Ocurrio un error al guardar el entrevistado, consulte el log",
					"mensajesEntrevistado");
		}
	}
	
	private Long darIdUsuario() {
		Long idUsuarioRegistro;
		if (getUsuarioLogueado().getCtRol().getIdeRol()
				.equals(CConstantes.IDE_LIDER_PROYECTO)) {
			idUsuarioRegistro=getUsuarioLogueado()
					.getIdeUsuario();
		}else{
			idUsuarioRegistro=getUsuarioLogueado()
					.getPadre();
		}
		return idUsuarioRegistro;
	}

	public void btnModificarEntrevistado() {

		try {
			

			adminPersistenciaFormularios.modificarEntrevistado(entrevistado);

			escribirInfoPantalla("Entrevistado modificado con éxito", "",
					"mensajesEntrevistado");

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla(
					"Error",
					"Ocurrio un error al guardar el entrevistado, consulte el log",
					"mensajesEntrevistado");
		}
	}

	public void btnEliminarEntrevistado(CtEntrevistado entrevistado) {

		try {
			adminPersistenciaFormularios.eliminarEntrevistado(entrevistado);
		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla(
					"Ocurrio un error al guardar el entrevistado, consulte el log",
					"", "mensajesEntrevistado");
		}

	}

	public void btnPreparaModificarEntrevistado(CtEntrevistado entrevistadoMod) {

		try {
			entrevistado = entrevistadoMod;
			modificando = true;
		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla(
					"Ocurrio un error al guardar el entrevistado, consulte el log",
					"", "mensajesEntrevistado");
		}

	}

	public CtEntrevistado getEntrevistado() {
		return entrevistado;
	}

	public void setEntrevistado(CtEntrevistado entrevistado) {
		this.entrevistado = entrevistado;
	}

	public List<SelectItem> getJerarquias() {
		return jerarquias;
	}

	public void setJerarquias(List<SelectItem> jerarquias) {
		this.jerarquias = jerarquias;
	}

	public List<CtEntrevistado> getEntrevistados() {
		return entrevistados;
	}

	public void setEntrevistados(List<CtEntrevistado> entrevistados) {
		this.entrevistados = entrevistados;
	}

	public boolean isModificando() {
		return modificando;
	}

	public void setModificando(boolean modificando) {
		this.modificando = modificando;
	}
	
	

}
