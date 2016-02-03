package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtProyecto;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;
import co.edu.udistrital.caseTool.GestiónFechas.HelperFechas;
import co.edu.udistrital.caseTool.Persistencia.AdminProyectos;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;

@ManagedBean(name = "bAdminProyecto")
@ViewScoped
public class AdministrarProyectosWBO extends WBOGeneral {

	private CtProyecto proyectoOperacion = new CtProyecto();
	private List<CtUsuario> usuariosLider;
	private List<CtProyecto> proyectosUsuario;
	private Date fechaInicioFiltro;
	private Date fechaFinFiltro;

	@EJB
	AdminUsuarios adminPersistenciaUsuarios;

	@EJB
	AdminProyectos adminPersistenciaProyectos;
	private boolean modificando;
	private boolean entrandoLider;

	public void configurarVista(ComponentSystemEvent event) {

		if (tomarDeSesion(CConstantes.SESION_PREPARE_ADMIN_PROYECTOS) != null) {

			if (tomarDeSesion(CConstantes.SESION_PROYECTO_MODIFICAR) != null) {
				proyectoOperacion = (CtProyecto) tomarDeSesion(CConstantes.SESION_PROYECTO_MODIFICAR);

				List<CtUsuario> usuariosProyecto = adminPersistenciaUsuarios
						.consultarUsuariosProyecto(proyectoOperacion
								.getIdeProyecto());

				proyectoOperacion.setUsuariosAsignados(usuariosProyecto);

				quitarDeSesion(CConstantes.SESION_PROYECTO_MODIFICAR);
				setModificando(true);
			}

			if (getUsuarioLogueado() != null) {
				usuariosLider = adminPersistenciaUsuarios
						.consultarUsuariosPendientesLider(getUsuarioLogueado()
								.getIdeUsuario());
			}
			quitarDeSesion(CConstantes.SESION_PREPARE_ADMIN_PROYECTOS);
			RequestContext.getCurrentInstance().update("frmCrearProyecto");
		}

		else if (tomarDeSesion(CConstantes.SESION_PREPARE_ABRIR_PROYECTO) != null) {

			if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.IDE_LIDER_PROYECTO)) {

				entrandoLider = true;
				proyectosUsuario = adminPersistenciaProyectos
						.consultarProyectosUsuarioLider(getUsuarioLogueado()
								.getIdeUsuario());
			} else if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.IDE_DATAMART)
					|| getUsuarioLogueado().getCtRol().getIdeRol()
							.equals(CConstantes.IDE_REQUERIMIENTOS)
					|| getUsuarioLogueado().getCtRol().getIdeRol()
							.equals(CConstantes.AMBOS)) {
				entrandoLider = false;
				proyectosUsuario = adminPersistenciaProyectos
						.consultarProyectosUsuario(getUsuarioLogueado()
								.getIdeUsuario());
			}

			quitarDeSesion(CConstantes.SESION_PREPARE_ABRIR_PROYECTO);
			RequestContext.getCurrentInstance().update("frmAbrirProyecto");
		}
	}

	public void btnFiltrarProyectos() {

		if (HelperFechas.fechaIncioMayorAFin(fechaInicioFiltro, fechaFinFiltro)) {

			Date fechaInicio = HelperFechas.cambiarHoraFecha(fechaInicioFiltro,
					0, 0, 0);
			Date fechaFin = HelperFechas.cambiarHoraFecha(fechaFinFiltro, 0, 0,
					0);

			proyectosUsuario = adminPersistenciaProyectos
					.consultarProyectosUsuarioFecha(getUsuarioLogueado()
							.getIdeUsuario(), fechaInicio, fechaFin, entrandoLider);

		} else {
			escribirErrorPantalla(
					"La fecha fin debe ser mayor a la fecha inicio", "",
					"mensajeAbrirProyecto");
		}

	}

	public CtProyecto getProyectoOperacion() {
		return proyectoOperacion;
	}

	public void setProyectoOperacion(CtProyecto proyectoOperacion) {
		this.proyectoOperacion = proyectoOperacion;
	}

	public String btnCrearProyecto() {

		try {

			if (!HelperFechas.fechaMayorAHoy(proyectoOperacion
					.getFechaTerminacion())) {
				escribirErrorPantalla("La fecha debe ser mayor a hoy", "",
						"mensajeProyecto");
				return "";
			}

			proyectoOperacion.setLiderProyecto(getUsuarioLogueado()
					.getIdeUsuario());
			proyectoOperacion.setFase(0);
			adminPersistenciaProyectos.crearProyecto(proyectoOperacion);

			ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoOperacion);
			escribirInfoPantalla("info", "Se creó con exito el proyecto", null);
			RequestContext.getCurrentInstance().execute("adminProyecto.hide()");

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al registrar proyecto, revise el log", null);
		}

		return "inicio";
	}

	public String btnModificarProyecto() {

		try {
			proyectoOperacion.setLiderProyecto(getUsuarioLogueado()
					.getIdeUsuario());
			adminPersistenciaProyectos.modificarProyecto(proyectoOperacion);

			ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoOperacion);
			escribirInfoPantalla("info", "Se creó con exito el proyecto", null);
			RequestContext.getCurrentInstance().execute("adminProyecto.hide()");

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al registrar proyecto, revise el log", null);
		}

		return "inicio";

	}

	public String btnAbrirProyecto() {

		ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoOperacion);
		return "inicio";

	}

	public List<CtUsuario> getUsuariosLider() {
		return usuariosLider;
	}

	public void setUsuariosLider(List<CtUsuario> usuariosLider) {
		this.usuariosLider = usuariosLider;
	}

	public boolean isModificando() {
		return modificando;
	}

	public void setModificando(boolean modificando) {
		this.modificando = modificando;
	}

	public List<CtProyecto> getProyectosUsuario() {
		return proyectosUsuario;
	}

	public void setProyectosUsuario(List<CtProyecto> proyectosUsuario) {
		this.proyectosUsuario = proyectosUsuario;
	}

	public Date getFechaInicioFiltro() {
		return fechaInicioFiltro;
	}

	public void setFechaInicioFiltro(Date fechaInicioFiltro) {
		this.fechaInicioFiltro = fechaInicioFiltro;
	}

	public Date getFechaFinFiltro() {
		return fechaFinFiltro;
	}

	public void setFechaFinFiltro(Date fechaFinFiltro) {
		this.fechaFinFiltro = fechaFinFiltro;
	}

}
