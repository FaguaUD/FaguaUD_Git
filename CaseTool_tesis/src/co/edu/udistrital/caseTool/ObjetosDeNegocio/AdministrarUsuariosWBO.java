package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtRole;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;
import co.edu.udistrital.caseTool.GestiónEmail.AdministradorEmail;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;

@ManagedBean(name = "bAdminUsuarios")
@ViewScoped
public class AdministrarUsuariosWBO extends WBOGeneral {

	private List<SelectItem> roles;
	private CtUsuario usuarioOperacion;
	private String nuevoPassword;
	private boolean modificando = false;
	private boolean permitiendoModificar = true;
	private boolean modificandoUsuariosProyecto = false;
	private boolean usandoAdmin = false;

	@EJB
	AdminUsuarios adminPersistencia;

	public void configurarVista(ComponentSystemEvent event) {

		prepararPagina();
		RequestContext.getCurrentInstance().update("frmCrearUsuario");

	}

	private void prepararPagina() {

		if (getUsuarioLogueado() != null) {
			if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.IDE_ADMINISTRADOR)) {

				usandoAdmin = true;
			}
		}

		if (tomarDeSesion(CConstantes.SESION_USUARIO_PROYECTO) != null) {
			permitiendoModificar = true;
			modificandoUsuariosProyecto = true;

			if (tomarDeSesion(CConstantes.SESION_USUARIO_MODIFICAR) != null) {

				usuarioOperacion = (CtUsuario) tomarDeSesion(CConstantes.SESION_USUARIO_MODIFICAR);
				quitarDeSesion(CConstantes.SESION_USUARIO_MODIFICAR);
				modificando = true;

			} else {

				usuarioOperacion = new CtUsuario();
				modificando = false;

			}
			cargarRoles(false);

		} else {

			if (getUsuarioLogueado() != null) {

				if (tomarDeSesion(CConstantes.SESION_MODIFICACION_PENDIENTE) != null) {
					permitiendoModificar = false;
				}

				usuarioOperacion = getUsuarioLogueado().clonar();

				if (usuarioOperacion.getEstado().equals(
						CConstantes.ESTADO_ACTIVO_EN_ESPERA)) {
					permitiendoModificar = false;
				}

				modificando = true;

			} else {
				usuarioOperacion = new CtUsuario();
			}

			cargarRoles(true);
		}

	}

	private void cargarRoles(boolean rolesVisibles) {
		roles = new ArrayList<SelectItem>();

		List<CtRole> rolesDB = adminPersistencia.consultarRoles(rolesVisibles);

		for (CtRole rol : rolesDB) {
			if (!rol.getIdeRol().equals(CConstantes.IDE_ADMINISTRADOR)) {

				if (!usandoAdmin) {
					if (!rol.getIdeRol().equals(CConstantes.IDE_LIDER_PROYECTO)) {

						roles.add(new SelectItem(rol.getIdeRol(), rol
								.getDescripcionRol()));
					}
				} else {
					if (rol.getIdeRol().equals(CConstantes.IDE_LIDER_PROYECTO)) {
						roles.add(new SelectItem(rol.getIdeRol(), rol
								.getDescripcionRol()));
					}
				}

			} else {
				if (!adminPersistencia.existeAdministrador()) {
					roles.add(new SelectItem(rol.getIdeRol(), rol
							.getDescripcionRol()));
				}
			}
		}

	}

	
	public void btnEliminarUsuario() {

		usuarioOperacion.setEstado(CConstantes.ESTADO_PENDIENTE_ELIMINACION);
		adminPersistencia.actualizarUsuario(usuarioOperacion);
		AdministradorEmail adminEmail = new AdministradorEmail();
		String mensaje = "Se creo la solicitud de eliminación, un Administrador la aprobará luego%s";

		try {
			adminEmail.enviarEmail(usuarioOperacion.getEmail(),
					"",
					"");

			escribirInfoPantalla("Info", String.format(mensaje, ""), null);

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Error",
									String.format(mensaje,
											", pero algo salió mal enviando el email, revise el log")));
			e.printStackTrace();
		}

	}
	

	public void btnCrearUsuario() {
		String componente = null;

		try {

			if (adminPersistencia.validarSiExisteUsuario(usuarioOperacion
					.getNombreUsuario())) {

				if (modificandoUsuariosProyecto) {
					componente = "msgSolicitudes";
				}

				escribirErrorPantalla("Error", String.format(
						"El usuario %s ya existe en el sistema",
						usuarioOperacion.getNombreUsuario()), componente);
			}

			else {

				AdministradorEmail adminEmail = new AdministradorEmail();
				usuarioOperacion.setPrimerIngreso(true);
				usuarioOperacion.setPadre(new Long(0));
				String mensaje = "";

				if (modificandoUsuariosProyecto) {

					usuarioOperacion.setEstado(CConstantes.ESTADO_ACTIVO);
					usuarioOperacion.setPadre(getUsuarioLogueado()
							.getIdeUsuario());
					mensaje = "Se creo el usuario del proyecto%s";
					componente = "msgSolicitudes";

				} else {

					usuarioOperacion.setEstado(CConstantes.ESTADO_ACTIVO);
					mensaje = "Se creo el usuario%s";

				}

				adminPersistencia.crearUsuario(usuarioOperacion);
				try {
					adminEmail.enviarEmail(usuarioOperacion.getEmail(),
							CConstantes.ASUNTO_NUEVO_USUARIO, String.format(
									CConstantes.MENSAJE_BIENVENIDA,
									usuarioOperacion.getNombreUsuario(),
									usuarioOperacion.getPassword()));

					escribirInfoPantalla("Info", String.format(mensaje, ""),
							componente);

				} catch (Exception e) {
					escribirErrorPantalla(
							"Error",
							String.format(mensaje,
									", pero algo salió mal enviando el email, revise el log"),
							componente);
					e.printStackTrace();
				}

			}

			if (modificandoUsuariosProyecto) {
				quitarDeSesion(CConstantes.SESION_USUARIO_PROYECTO);
				ponerEnSesion(CConstantes.SESION_PREPARE_ADMIN_SOLICITUDES,
						true);
				modificandoUsuariosProyecto = false;
			}

			RequestContext.getCurrentInstance().execute("adminUsuarios.hide()");

		} catch (Exception e) {
			escribirErrorPantalla("Error", "Algo salió mal, revise el log",
					null);
			e.printStackTrace();

		}

	}

	public void btnModificarUsuario() {

		try {

			AdministradorEmail adminEmail = new AdministradorEmail();
			String mensaje = "";
			String componente = null;

			adminPersistencia.actualizarUsuario(usuarioOperacion);

			mensaje = "Se modificó el usuario%s";

			if (modificandoUsuariosProyecto) {
				componente = "msgSolicitudes";
				modificandoUsuariosProyecto = false;
				quitarDeSesion(CConstantes.SESION_USUARIO_PROYECTO);
				ponerEnSesion(CConstantes.SESION_PREPARE_ADMIN_SOLICITUDES,
						true);
			} else {
				ponerEnSesion(CConstantes.SESION_USUARIO_LOGUEADO,
						usuarioOperacion);
			}

			try {
				adminEmail.enviarEmail(usuarioOperacion.getEmail(),
						CConstantes.ASUNTO_MODIFICACION_USUARIO,
						CConstantes.MENSAJE_MODIFICACION);

				escribirInfoPantalla("Info", String.format(mensaje, ""),
						componente);

			} catch (Exception e) {
				escribirErrorPantalla(
						"Error",
						String.format(mensaje,
								", pero algo salió mal enviando el email, revise el log"),
						componente);
				e.printStackTrace();
			}

			RequestContext.getCurrentInstance().execute("adminUsuarios.hide()");

		} catch (Exception e) {
			escribirErrorPantalla("Error", "Algo salió mal, revise el log",
					null);
			e.printStackTrace();

		}

	}

	public String btnCambiarPassword() {

		try {

			if (!usuarioOperacion.getPassword().equals(nuevoPassword)) {

				usuarioOperacion.setPrimerIngreso(false);
				usuarioOperacion.setPassword(nuevoPassword);
				adminPersistencia.actualizarUsuario(usuarioOperacion);
				quitarDeSesion(CConstantes.SESION_USUARIO_LOGUEADO);			
				escribirInfoPantalla("Info", "Se cambió el password", null);				
			}
			else {
				escribirErrorPantalla("Error", "Está intentando usar el mismo password", null);
				return "";
			}

		} catch (Exception e) {
			escribirErrorPantalla("Error", "Algo salió mal, revise el log",
					null);
			e.printStackTrace();

		}
	
		return "ingresoUsuario";

	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public String getNuevoPassword() {
		return nuevoPassword;
	}

	public void setNuevoPassword(String nuevoPassword) {
		this.nuevoPassword = nuevoPassword;
	}

	public CtUsuario getUsuarioOperacion() {
		return usuarioOperacion;
	}

	public void setUsuarioOperacion(CtUsuario usuarioOperacion) {
		this.usuarioOperacion = usuarioOperacion;
	}

	public boolean isModificando() {
		return modificando;
	}

	public void setModificando(boolean modificando) {
		this.modificando = modificando;
	}

	public boolean isPermitiendoModificar() {
		return permitiendoModificar;
	}

	public void setPermitiendoModificar(boolean permitiendoModificar) {
		this.permitiendoModificar = permitiendoModificar;
	}

	public boolean isModificandoUsuariosProyecto() {
		return modificandoUsuariosProyecto;
	}

	public void setModificandoUsuariosProyecto(
			boolean modificandoUsuariosProyecto) {
		this.modificandoUsuariosProyecto = modificandoUsuariosProyecto;
	}

}
