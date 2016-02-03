package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtProyecto;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;
import co.edu.udistrital.caseTool.Persistencia.AdminProyectos;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;

@ManagedBean(name = "bAdminSolicitudes")
@ViewScoped
public class AdministrarSolicitudesWBO extends WBOGeneral {

	private List<CtUsuario> usuariosSolicitudes;

	@EJB
	AdminUsuarios manejadorPersistencia;

	@EJB
	AdminProyectos manejadorPersistenciaProyectos;

	private boolean usandoAdmin;
	private boolean usandoLider;
	private CtUsuario usuarioFiltro;
	private CtUsuario usuarioOperacion;

	private boolean conFiltro = false;

	public void configurarVista(ComponentSystemEvent event) {

		boolean preparePagina = false;
		if (tomarDeSesion(CConstantes.SESION_PREPARE_ADMIN_SOLICITUDES) != null) {
			preparePagina = true;
		}

		if (preparePagina) {
			prepararPagina();
			quitarDeSesion(CConstantes.SESION_PREPARE_ADMIN_SOLICITUDES);
		}
		RequestContext.getCurrentInstance().update("frmAdminOtros");

	}

	private void prepararPagina() {

		usuarioFiltro = new CtUsuario();

		if (getUsuarioLogueado() != null) {
			usuarioOperacion = getUsuarioLogueado().clonar();

			if (usuarioOperacion.getCtRol().getIdeRol()
					.equals(CConstantes.IDE_ADMINISTRADOR)) {

				if (!conFiltro) {
					usuariosSolicitudes = manejadorPersistencia
							.consultarUsuariosPendientesAdmin();
				}
				usandoAdmin = true;
			} else if (usuarioOperacion.getCtRol().getIdeRol()
					.equals(CConstantes.IDE_LIDER_PROYECTO)) {

				if (!conFiltro) {
					usuariosSolicitudes = manejadorPersistencia
							.consultarUsuariosPendientesLider(getUsuarioLogueado()
									.getIdeUsuario());
				}
				usandoLider = true;
			}
		}

	}

	public void btnBuscarUsuario() {

		ArrayList<Long> rolesConsulta = new ArrayList<Long>();
		if (usandoAdmin) {
			rolesConsulta.add(CConstantes.IDE_LIDER_PROYECTO);
		} else if (usandoLider) {
			rolesConsulta.add(CConstantes.IDE_REQUERIMIENTOS);
			rolesConsulta.add(CConstantes.IDE_DATAMART);
		}

		usuarioFiltro.setRolesConsulta(rolesConsulta);
		usuarioFiltro.setPadre(getUsuarioLogueado().getIdeUsuario());
		usuariosSolicitudes = manejadorPersistencia
				.buscarUsuario(usuarioFiltro);
		conFiltro = true;

	}

	public void btnNuevoUsuarioProyecto() {

		ponerEnSesion(CConstantes.SESION_USUARIO_PROYECTO, true);

	}

	
	public void btnEliminarUsuario(CtUsuario usuario) {

		List<CtProyecto> listaProy = manejadorPersistenciaProyectos
				.consultarProyectosUsuarioLider(usuario.getIdeUsuario());

		if (listaProy != null && !listaProy.isEmpty()) {
			escribirErrorPantalla("Error",
					"El usuario aún tiene proyectos asignados", "msgSolicitudes");
		} else {

			usuariosSolicitudes.remove(usuario);
			manejadorPersistencia.eliminarUsuarioPorID(usuario.getIdeUsuario());
		}

	}
	

	public void btnModificarUsuario(CtUsuario usuario) {

		ponerEnSesion(CConstantes.SESION_USUARIO_PROYECTO, true);
		ponerEnSesion(CConstantes.SESION_USUARIO_MODIFICAR, usuario);

	}

	public List<CtUsuario> getUsuariosSolicitudes() {
		return usuariosSolicitudes;
	}

	public void setUsuariosSolicitudes(List<CtUsuario> usuariosSolicitudes) {
		this.usuariosSolicitudes = usuariosSolicitudes;
	}

	public boolean isUsandoAdmin() {
		return usandoAdmin;
	}

	public void setUsandoAdmin(boolean usandoAdmin) {
		this.usandoAdmin = usandoAdmin;
	}

	public boolean isUsandoLider() {
		return usandoLider;
	}

	public void setUsandoLider(boolean usandoLider) {
		this.usandoLider = usandoLider;
	}

	public CtUsuario getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(CtUsuario usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}

}
