package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtFormulario;
import co.edu.udistrital.caseTool.Entidades.CtOpcionesRespuesta;
import co.edu.udistrital.caseTool.Entidades.CtPreguntas;
import co.edu.udistrital.caseTool.Entidades.CtProyecto;
import co.edu.udistrital.caseTool.Persistencia.AdminFormularios;
import co.edu.udistrital.caseTool.Persistencia.AdminProyectos;

@ManagedBean(name = "bAdminFormularios")
@ViewScoped
public class AdministrarFormulariosWBO extends WBOGeneral {

	private CtFormulario formularioOperacion = new CtFormulario();
	private CtProyecto proyectoActual;
	private List<CtFormulario> formulariosBusqueda;
	private CtPreguntas preguntaNueva;
	private CtOpcionesRespuesta opcionNueva;

	@EJB
	AdminFormularios adminPersistenciaFormularios;

	@EJB
	AdminProyectos adminPersistenciaProyectos;

	private List<SelectItem> tiposPregunta;

	private boolean modificando;
	private boolean creandoNuevoFormulario;
	private boolean mostrandoFormularios;
	private boolean creandoNuevaPregunta;
	private boolean creandoNuevaOpcion;

	public AdministrarFormulariosWBO() {
		tiposPregunta = new ArrayList<SelectItem>();
		tiposPregunta.add(new SelectItem(CConstantes.TIPO_PREGUNTA_SINGLE,
				"Única respuesta"));
		tiposPregunta.add(new SelectItem(CConstantes.TIPO_PREGUNTA_MULTIPLE,
				"Múltiple respuesta"));

	}

	public void configurarVista(ComponentSystemEvent event) {

		if (tomarDeSesion(CConstantes.SESION_PREPARE_CREAR_FORMULARIO) != null
				|| tomarDeSesion(CConstantes.SESION_PREPARE_MODIFICAR_FORMULARIO) != null) {

			if (tomarDeSesion(CConstantes.SESION_PROYECTO_ACTIVO) != null) {
				proyectoActual = (CtProyecto) tomarDeSesion(CConstantes.SESION_PROYECTO_ACTIVO);
			}

			if (tomarDeSesion(CConstantes.SESION_PREPARE_MODIFICAR_FORMULARIO) != null) {
				modificando = true;
				formularioOperacion = (CtFormulario) tomarDeSesion(CConstantes.SESION_FORMULARIO_MODIFICAR);

				formularioOperacion
						.setPreguntasFormulario(adminPersistenciaFormularios
								.consultarPreguntasFormulario(formularioOperacion
										.getIdFormulario()));

				quitarDeSesion(CConstantes.SESION_PREPARE_MODIFICAR_FORMULARIO);
				quitarDeSesion(CConstantes.SESION_FORMULARIO_MODIFICAR);
				RequestContext.getCurrentInstance().update("frmCrearFormulario");
			}

		}
	}

	public String btnCrearFormulario() {

		try {
		
			proyectoActual.addFormulario(formularioOperacion);

			adminPersistenciaProyectos.modificarProyecto(proyectoActual);

			ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoActual);

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al registrar el formulario, revise el log", null);
		}

		return "inicio";
	}

	public String btnModificarFormulario() {

		try {

			adminPersistenciaFormularios
					.actualizarFormulario(formularioOperacion);

			ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoActual);

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al registrar el formulario, revise el log", null);
		}

		return "inicio";

	}

	public void btnBuscarFormulario() {

		try {
			formulariosBusqueda = adminPersistenciaFormularios
					.consultarFormularioNombre(formularioOperacion
							.getNombreFormulario());

			creandoNuevoFormulario = false;
			if (formulariosBusqueda != null && !formulariosBusqueda.isEmpty()) {
				mostrandoFormularios = true;
			} else {
				escribirErrorPantalla("No hay resultados", "No hay resultados",
						null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al consultar el formulario, revise el log", null);

		}

	}

	public void btnNuevoFormulario() {
		formularioOperacion = new CtFormulario();
		creandoNuevoFormulario = true;
	}

	public String btnAsignarFormulario() {

		try {

			if (proyectoActual.getFormulariosProyecto() == null) {
				proyectoActual
						.setFormulariosProyecto(new ArrayList<CtFormulario>());
			} else {
				if (proyectoActual.existeFormulario(formularioOperacion
						.getIdFormulario())) {
					escribirErrorPantalla("Error",
							"El formulario ya existe en el proyecto", null);
					return "";
				}
			}
			
			

			CtFormulario formularioNuevo = formularioOperacion.clonar();
			List<CtPreguntas> preguntas = adminPersistenciaFormularios
			.consultarPreguntasFormulario(formularioOperacion
					.getIdFormulario());
						
			formularioNuevo.setPreguntasFormulario(preguntas);		
		
			proyectoActual.addFormulario(formularioNuevo);			
			adminPersistenciaProyectos.modificarProyecto(proyectoActual);
		} catch (Exception e) {

			e.printStackTrace();
			escribirErrorPantalla(
					"Error",
					"Error al registrar el formularios para el proyecto, revise el log",
					null);

		}
		return "inicio";
	}

	public void eliminarPregunta(CtPreguntas pregunta) {

		try {
			formularioOperacion.removerPregunta(pregunta);
		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al eliminar pregunta del formulario, revise el log",
					null);
		}

	}

	public void eliminarOpcion(CtOpcionesRespuesta opcion) {

		try {
			preguntaNueva.removerOpcion(opcion);
		} catch (Exception e) {
			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al eliminar opción de la pregunta, revise el log",
					null);
		}

	}

	public void btnGuardarPregunta() {

		try {

			if (formularioOperacion.getPreguntasFormulario() == null) {
				formularioOperacion
						.setPreguntasFormulario(new ArrayList<CtPreguntas>());
			}
			formularioOperacion.adicionarPregunta(preguntaNueva);
			creandoNuevaPregunta = false;
		} catch (Exception e) {

			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al agregar opción de la pregunta, revise el log",
					null);

		}

	}

	public void adicionarOpcion() {
		try {

			opcionNueva.setCtPregunta(preguntaNueva);

			if (preguntaNueva.getOpciones() == null) {
				preguntaNueva.setOpciones(new ArrayList<CtOpcionesRespuesta>());
			}
			preguntaNueva.adicionarOpcion(opcionNueva);
			creandoNuevaOpcion = false;
		} catch (Exception e) {

			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al agregar opción de la pregunta, revise el log",
					null);

		}
	}

	public void preparaNuevaPregunta() {
		preguntaNueva = new CtPreguntas();
		setCreandoNuevaPregunta(true);
	}

	public void preparaNuevaOpcion() {
		opcionNueva = new CtOpcionesRespuesta();
		setCreandoNuevaOpcion(true);
	}

	public boolean isModificando() {
		return modificando;
	}

	public void setModificando(boolean modificando) {
		this.modificando = modificando;
	}

	public CtFormulario getFormularioOperacion() {
		return formularioOperacion;
	}

	public void setFormularioOperacion(CtFormulario formularioOperacion) {
		this.formularioOperacion = formularioOperacion;
	}

	public boolean isCreandoNuevoFormulario() {
		return creandoNuevoFormulario;
	}

	public void setCreandoNuevoFormulario(boolean creandoNuevoFormulario) {
		this.creandoNuevoFormulario = creandoNuevoFormulario;
	}

	public boolean isMostrandoFormularios() {
		return mostrandoFormularios;
	}

	public void setMostrandoFormularios(boolean mostrandoFormularios) {
		this.mostrandoFormularios = mostrandoFormularios;
	}

	public List<CtFormulario> getFormulariosBusqueda() {
		return formulariosBusqueda;
	}

	public void setFormulariosBusqueda(List<CtFormulario> formulariosBusqueda) {
		this.formulariosBusqueda = formulariosBusqueda;
	}

	public boolean isCreandoNuevaPregunta() {
		return creandoNuevaPregunta;
	}

	public void setCreandoNuevaPregunta(boolean creandoNuevaPregunta) {
		this.creandoNuevaPregunta = creandoNuevaPregunta;
	}

	public CtPreguntas getPreguntaNueva() {
		return preguntaNueva;
	}

	public void setPreguntaNueva(CtPreguntas preguntaNueva) {
		this.preguntaNueva = preguntaNueva;
	}

	public List<SelectItem> getTiposPregunta() {
		return tiposPregunta;
	}

	public void setTiposPregunta(List<SelectItem> tiposPregunta) {
		this.tiposPregunta = tiposPregunta;
	}

	public boolean isCreandoNuevaOpcion() {
		return creandoNuevaOpcion;
	}

	public void setCreandoNuevaOpcion(boolean creandoNuevaOpcion) {
		this.creandoNuevaOpcion = creandoNuevaOpcion;
	}

	public CtOpcionesRespuesta getOpcionNueva() {
		return opcionNueva;
	}

	public void setOpcionNueva(CtOpcionesRespuesta opcionNueva) {
		this.opcionNueva = opcionNueva;
	}

}
