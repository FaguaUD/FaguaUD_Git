package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtAtributos;
import co.edu.udistrital.caseTool.Entidades.CtCubo;
import co.edu.udistrital.caseTool.Entidades.CtDimension;
import co.edu.udistrital.caseTool.Entidades.CtEntrevistado;
import co.edu.udistrital.caseTool.Entidades.CtFormulario;
import co.edu.udistrital.caseTool.Entidades.CtHecho;
import co.edu.udistrital.caseTool.Entidades.CtOpcionesRespuesta;
import co.edu.udistrital.caseTool.Entidades.CtPreguntas;
import co.edu.udistrital.caseTool.Entidades.CtProyecto;
import co.edu.udistrital.caseTool.Entidades.CtRespuesta;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;
import co.edu.udistrital.caseTool.GestiónFechas.HelperFechas;
import co.edu.udistrital.caseTool.Persistencia.AdminCubo;
import co.edu.udistrital.caseTool.Persistencia.AdminFormularios;
import co.edu.udistrital.caseTool.Persistencia.AdminProyectos;
import co.edu.udistrital.caseTool.Persistencia.AdminRespuesta;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;
import co.edu.udistrital.caseTool.Persistencia.AdministradorPersistencia;
import co.edu.udistrital.caseTool.TransferObject.Alertas;
import co.edu.udistrital.caseTool.TransferObject.NodoArbol;

@ManagedBean(name = "bPrincipalHerramienta")
@ViewScoped
public class IngresoUsuarioWBO extends WBOGeneral {

	private boolean cambiarPassword;

	@EJB
	AdministradorPersistencia adminPersistencia;

	@EJB
	AdminUsuarios adminPersistenciaUsuarios;

	@EJB
	AdminProyectos adminPersistenciaProyectos;

	@EJB
	AdminFormularios adminPersistenciaFormulario;

	@EJB
	AdminRespuesta adminPersistenciaRespuesta;

	@EJB
	AdminCubo adminPersistenciaCubo;

	private TreeNode arbolProyectos;

	private TreeNode nodoSeleccionado;

	private CtProyecto proyectoActual;

	private CtPreguntas preguntaActiva;

	private CtCubo cuboOperacion;

	private List<Alertas> alertas;

	private List<CtOpcionesRespuesta> opcionesMultipleSeleccionadas;

	private List<SelectItem> formulariosSelectList;

	private List<SelectItem> entrevistados;

	private CtOpcionesRespuesta opcionSingleSeleccionada;

	private CtRespuesta respuesta;

	private CtFormulario formularioCubo;

	private CtDimension dimensionNueva;

	private CtAtributos atributoNuevo;

	private boolean seleccionandoFormulario;

	private CtFormulario formularioSeleccionado;

	private int indice = 0;

	private boolean usuarioReq;

	private boolean usuarioDataMart;

	private boolean otroUsuario;

	private int totalPreguntas;

	private boolean mostrarRespuestasFormulario;

	private boolean formulariosResueltos;

	private boolean creandoCubo;

	private boolean crearHecho;

	private List<CtFormulario> formulariosProyecto;

	private List<CtRespuesta> respuestasFormularioCubo;

	private boolean mostrandoRespuestasCubo;

	private boolean crearDimension;

	private boolean crearAtributo;

	private boolean mostrarInfoDimension;

	private CtCubo cuboSeleccionado;

	private boolean mostrarDibujoCubo;

	private String cadenaCubo;

	public void configurarVista(ComponentSystemEvent event) {

		alertas = new ArrayList<Alertas>();

		if (tomarDeSesion(CConstantes.SESION_PROYECTO_ACTIVO) != null) {

			if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.IDE_REQUERIMIENTOS)) {
				usuarioReq = true;
			} else if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.IDE_DATAMART)) {
				usuarioDataMart = true;
			} else if (getUsuarioLogueado().getCtRol().getIdeRol()
					.equals(CConstantes.AMBOS)) {
				usuarioReq = true;
				usuarioDataMart = true;
			}

			else {
				setOtroUsuario(true);
			}

			proyectoActual = (CtProyecto) tomarDeSesion(CConstantes.SESION_PROYECTO_ACTIVO);
			List<CtUsuario> usuariosProyecto = adminPersistenciaUsuarios
					.consultarUsuariosProyecto(proyectoActual.getIdeProyecto());

			formulariosProyecto = adminPersistenciaFormulario
					.consultarFormularioProyecto(proyectoActual
							.getIdeProyecto());

			List<CtCubo> cubosProyecto = adminPersistenciaCubo
					.consultarCubosProyecto(proyectoActual.getIdeProyecto());

			proyectoActual.setFormulariosProyecto(formulariosProyecto);
			proyectoActual.setCubosProyecto(cubosProyecto);
			proyectoActual.setUsuariosAsignados(usuariosProyecto);

			if (usuariosProyecto == null || usuariosProyecto.isEmpty()) {
				alertas.add(new Alertas(CConstantes.ALERTA_SIN_USUARIO));
			}
			
			if (cubosProyecto.isEmpty()){
				alertas.add(new Alertas(CConstantes.ALERTA_SIN_CUBOS));
			}
			
			if (formulariosProyecto.isEmpty()){
				alertas.add(new Alertas(CConstantes.ALERTA_SIN_FORMULARIOS));
			}
			
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");			
			
			if (proyectoActual.getFechaTerminacion() == null){
				proyectoActual.setFechaTerminacion(new Date());
			}
			
			Date fechaActualSinHora = new Date();
			try {
				fechaActualSinHora = formatter.parse(formatter.format(new Date()));
			} catch (ParseException e) {				
				e.printStackTrace();
			}			
			
			long diasRestantes = HelperFechas.getDateDiff(fechaActualSinHora, proyectoActual.getFechaTerminacion(), TimeUnit.DAYS);
			if (diasRestantes > 0){
				alertas.add(new Alertas(String.format(CConstantes.ALERTA_DIAS_RESTANTES, diasRestantes)));
			} else if (diasRestantes < 0){
				alertas.add(new Alertas(String.format(CConstantes.ALERTA_DIAS_RETRASO, diasRestantes*-1)));
			}
			else if (diasRestantes == 0){
				alertas.add(new Alertas(CConstantes.ALERTA_TERMINAR_HOY));
			}
			

			arbolProyectos = new DefaultTreeNode(new NodoArbol("arbol"), null);
			
			
			String tipoNodoRaiz = "";
			String tipoNodoReq = "";
			if (usuarioReq){
				tipoNodoRaiz = "raizSinPermisos"; 
				tipoNodoReq = "fasereq";
			}
			else if (usuarioDataMart){
				tipoNodoRaiz = "raizSinPermisos";	
				tipoNodoReq = "fasereqSinPermisos";
			}
			else {
				tipoNodoRaiz = "raizLider";	
				tipoNodoReq = "fasereq";
			}

			TreeNode proyecto = new DefaultTreeNode(tipoNodoRaiz, new NodoArbol(
					proyectoActual.getNombreProyecto()), arbolProyectos);	
			
			TreeNode faseRequerimientos = new DefaultTreeNode(tipoNodoReq,
					new NodoArbol("Fase Requerimientos"), proyecto);

			for (CtFormulario formulario : proyectoActual
				.getFormulariosProyecto()) {

			if (!usuarioReq && !usuarioDataMart) {

				@SuppressWarnings("unused")
				TreeNode formularioNodo = new DefaultTreeNode("formulario",
						new NodoArbol(formulario.getNombreFormulario(),
								formulario), faseRequerimientos);
				} else {

					@SuppressWarnings("unused")
					TreeNode formularioNodo = new DefaultTreeNode(
							"formSinPermisos", new NodoArbol(
									formulario.getNombreFormulario(),
									formulario), faseRequerimientos);

				}
			}

			formulariosResueltos = todosFormulariosResueltos(formulariosProyecto);

			if (formulariosResueltos) {

				if (usuarioDataMart) {
					TreeNode faseAnalisis = new DefaultTreeNode("faseanalisis",
							new NodoArbol("Fase Análisis"), proyecto);

				for (CtCubo ctCubo : cubosProyecto) {

						@SuppressWarnings("unused")
						TreeNode cuboAnalisis = new DefaultTreeNode(
								"cuboAnalisis", new NodoArbol(
										ctCubo.getNombre(), ctCubo),
								faseAnalisis);

					}

				} else if (otroUsuario) {
					TreeNode faseAnalisis = new DefaultTreeNode(
							"faseanalisisLider",
							new NodoArbol("Fase Análisis"), proyecto);

					for (CtCubo ctCubo : cubosProyecto) {

						@SuppressWarnings("unused")
						TreeNode cuboAnalisis = new DefaultTreeNode(
								"cuboMostrar", new NodoArbol(
										ctCubo.getNombre(), ctCubo),
								faseAnalisis);

					}
				}
			}

			else {
				alertas.add(new Alertas(
						"Aun no se resuelven todos los formularios"));
			}

		}
	}

	private boolean todosFormulariosResueltos(
			List<CtFormulario> formulariosProyecto) {

		int preguntasResueltas = 0;
		int formulariosResueltos = 0;

		for (CtFormulario ctFormulario : formulariosProyecto) {

			List<CtPreguntas> preguntasFormulario = adminPersistenciaFormulario
					.consultarPreguntasFormulario(ctFormulario
							.getIdFormulario());

			List<CtRespuesta> respuestasFormulario = adminPersistenciaRespuesta
					.consultarRespuestasFormulario(ctFormulario
							.getIdFormulario());

			for (CtPreguntas pregunta : preguntasFormulario) {

				for (CtRespuesta ctRespuesta : respuestasFormulario) {

					if (ctRespuesta.getPregunta().getIdPregunta()
							.equals(pregunta.getIdPregunta())) {
						preguntasResueltas++;
						break;
					}
				}
			}

			if (preguntasResueltas == preguntasFormulario.size()) {
				formulariosResueltos++;
				preguntasResueltas = 0;
			}
		}

		if (formulariosResueltos == formulariosProyecto.size()) {
			return true;
		}

		return false;
	}

	public void seleccionNodo() {

		mostrarRespuestasFormulario = false;
		NodoArbol nodo = (NodoArbol) nodoSeleccionado.getData();
		formularioSeleccionado = nodo.getFormulario();
		setCuboSeleccionado(nodo.getCubo());

		if (formularioSeleccionado != null) {

			entrevistados = new ArrayList<SelectItem>();
			List<CtEntrevistado> entrevistadosUsuario = adminPersistenciaFormulario
					.consultarEntrevistadosDisponibles(proyectoActual
							.getLiderProyecto());

			for (CtEntrevistado ctEntrevistado : entrevistadosUsuario) {

				entrevistados.add(new SelectItem(ctEntrevistado
						.getIdeEntrevistado(), ctEntrevistado.getNombre()));

			}

			List<CtPreguntas> preguntasFormulario = adminPersistenciaFormulario
					.consultarPreguntasFormulario(formularioSeleccionado
							.getIdFormulario());

			List<CtRespuesta> respuestasFormulario = adminPersistenciaRespuesta
					.consultarRespuestasFormulario(formularioSeleccionado
							.getIdFormulario());

			for (CtRespuesta ctRespuesta : respuestasFormulario) {

				CtEntrevistado entrevistadoRespuesta = adminPersistenciaRespuesta
						.consultarEntrevistadoRespuesta(ctRespuesta
								.getEntrevistado());

				if (entrevistadoRespuesta != null) {
					ctRespuesta.setNombreEntrevistado(entrevistadoRespuesta
							.getNombre());
				} else {
					ctRespuesta.setNombreEntrevistado("Desconocido");
				}

			}

			formularioSeleccionado.setPreguntasFormulario(preguntasFormulario);
			formularioSeleccionado.setRespuestas(respuestasFormulario);

			setTotalPreguntas(formularioSeleccionado.getPreguntasFormulario()
					.size());
			setSeleccionandoFormulario(true);

			boolean formularioResuelto = preguntasFormulario.size() == respuestasFormulario.size();
			
			if (formularioResuelto) {
				mostrarRespuestasFormulario = true;
			} else if (!formularioResuelto && usuarioReq) {
				respuesta = new CtRespuesta();
				cargaPregunta(indice);
			}
		} else if (cuboSeleccionado != null) {

			StringBuffer cadenaCuboBf = new StringBuffer();

			cadenaCuboBf.append(cuboSeleccionado.getHecho().getNombre());
			cadenaCuboBf.append("|");

			List<CtDimension> dimensionesCubo = adminPersistenciaCubo
					.consultarDimensionesCubo(cuboSeleccionado.getIdeCubo());

			for (CtDimension ctDimension : dimensionesCubo) {
				cadenaCuboBf.append(ctDimension.getNombre());
				cadenaCuboBf.append("(");
				List<CtAtributos> atributosDimension = adminPersistenciaCubo
						.consultarAtributosDimension(ctDimension
								.getIdeDimension());
				for (CtAtributos ctAtributos : atributosDimension) {
					cadenaCuboBf.append(ctAtributos.getNombre());
					cadenaCuboBf.append(";");
				}
				cadenaCuboBf.append(")");
				cadenaCuboBf.append(":");
			}

			ponerEnSesion(CConstantes.SESION_CADENA_CUBO,
					cadenaCuboBf.toString());
			ponerEnSesion(CConstantes.SESION_NOMBRE_CUBO,
					cuboSeleccionado.getNombre());
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath()
						+ "/paginas/pintarTest.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void cargaPregunta(int indice) {

		if (indice < formularioSeleccionado.getPreguntasFormulario().size()) {

			preguntaActiva = formularioSeleccionado.getPreguntasFormulario()
					.get(indice);

			if (tieneRespuestaPregunta(preguntaActiva.getIdPregunta(),
					formularioSeleccionado.getIdFormulario())) {
				indice++;
				cargaPregunta(indice);
			}

		} else {

			formularioSeleccionado.setFechaFinReal(new Date());
			adminPersistenciaFormulario
					.actualizarFormulario(formularioSeleccionado);
			escribirInfoPantalla("Info", "Se ha completado el formulario", null);
			setMostrarRespuestasFormulario(true);
		}

	}

	private boolean tieneRespuestaPregunta(Long idPregunta, Long idFormulario) {

		List<CtRespuesta> respuestas = adminPersistenciaRespuesta
				.consultarRespuestaPreguna(idPregunta, idFormulario);
		return !respuestas.isEmpty();
	}

	public void cargarSiguientePregunta() {

		if (formularioSeleccionado.getRespuestas() == null) {
			formularioSeleccionado.setRespuestas(new ArrayList<CtRespuesta>());
		}

		if (opcionesMultipleSeleccionadas != null
				&& !opcionesMultipleSeleccionadas.isEmpty()) {
			for (CtOpcionesRespuesta opcion : opcionesMultipleSeleccionadas) {

				CtRespuesta resTmp = new CtRespuesta();
				resTmp.setEntrevistado(respuesta.getEntrevistado());
				resTmp.setPregunta(preguntaActiva);
				resTmp.setFormulario(formularioSeleccionado);
				resTmp.setIdUsuarioEntrevistador(getUsuarioLogueado()
						.getIdeUsuario());
				resTmp.setOpcionesRespuesta(opcion);
				resTmp.setNombreEntrevistado(darNombreEntrevistado(resTmp));
				adminPersistenciaRespuesta.crearRespuesta(resTmp);			

				formularioSeleccionado.adicionarRespuesta(resTmp);
			}

			opcionesMultipleSeleccionadas = new ArrayList<CtOpcionesRespuesta>();
		} else {

			CtRespuesta resTmp = new CtRespuesta();
			resTmp.setEntrevistado(respuesta.getEntrevistado());
			resTmp.setPregunta(preguntaActiva);
			resTmp.setFormulario(formularioSeleccionado);
			resTmp.setIdUsuarioEntrevistador(getUsuarioLogueado()
					.getIdeUsuario());
			resTmp.setOpcionesRespuesta(opcionSingleSeleccionada);
			resTmp.setNombreEntrevistado(darNombreEntrevistado(resTmp));
			adminPersistenciaRespuesta.crearRespuesta(resTmp);
			formularioSeleccionado.adicionarRespuesta(resTmp);
		}

		indice++;
		cargaPregunta(indice);
	}

	private String darNombreEntrevistado(CtRespuesta resTmp) {
		
		CtEntrevistado entrevistadoRespuesta = adminPersistenciaRespuesta
				.consultarEntrevistadoRespuesta(resTmp
						.getEntrevistado());

		if (entrevistadoRespuesta != null) {
			return entrevistadoRespuesta
					.getNombre();
		} else {
			return "Desconocido";
		}	
	
		
	}

	public void prepararModificacion() {

		ponerEnSesion(CConstantes.SESION_PREPARE_ADMIN_PROYECTOS, true);
		ponerEnSesion(CConstantes.SESION_PROYECTO_MODIFICAR, proyectoActual);

		RequestContext.getCurrentInstance().execute("adminProyecto.show()");
	}

	public void prepararCrearFormulario() {

		ponerEnSesion(CConstantes.SESION_PREPARE_CREAR_FORMULARIO, true);
		ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoActual);
		RequestContext.getCurrentInstance().execute("adminFormularios.show()");
	}

	public void preparaCrearCubo() {

		respuestasFormularioCubo = new ArrayList<CtRespuesta>();
		formularioSeleccionado = new CtFormulario();
		cuboOperacion = new CtCubo();

		for (CtFormulario formProyecto : formulariosProyecto) {

			List<CtRespuesta> respuestasFormulario = adminPersistenciaRespuesta
					.consultarRespuestasFormulario(formProyecto
							.getIdFormulario());
			
			for (CtRespuesta ctRespuesta : respuestasFormulario) {

				CtEntrevistado entrevistadoRespuesta = adminPersistenciaRespuesta
						.consultarEntrevistadoRespuesta(ctRespuesta
								.getEntrevistado());

				if (entrevistadoRespuesta != null) {
					ctRespuesta.setNombreEntrevistado(entrevistadoRespuesta
							.getNombre());
				} else {
					ctRespuesta.setNombreEntrevistado("Desconocido");
				}

			}
			

			respuestasFormularioCubo.addAll(respuestasFormulario);
		}

		formularioSeleccionado.setRespuestas(respuestasFormularioCubo);

		setCreandoCubo(true);
		RequestContext.getCurrentInstance().update("frmCoreApp");
	}

	public void preparaCrearHecho() {

		CtHecho hecho = new CtHecho();
		hecho.setRespuestas(new ArrayList<CtRespuesta>());
		cuboOperacion.setHecho(hecho);

		setCrearHecho(true);
	}

	public void preparaCrearDimension() {

		dimensionNueva = new CtDimension();
		if (cuboOperacion.getDimensiones() == null) {
			cuboOperacion.setDimensiones(new ArrayList<CtDimension>());
		}

		setCrearDimension(true);
	}

	public void preparaCrearAtributo() {

		atributoNuevo = new CtAtributos();
		if (dimensionNueva.getAtributos() == null) {
			dimensionNueva.setAtributos(new ArrayList<CtAtributos>());
		}

		setCrearAtributo(true);

	}

	public void preparaVerDetalleDimension(CtDimension dimension) {
		dimensionNueva = dimension;
		setMostrarInfoDimension(true);
	}

	public void prepararModificarFormulario() {

		NodoArbol nodo = (NodoArbol) nodoSeleccionado.getData();
		CtFormulario formularioModificar = nodo.getFormulario();

		ponerEnSesion(CConstantes.SESION_PREPARE_MODIFICAR_FORMULARIO, true);
		ponerEnSesion(CConstantes.SESION_FORMULARIO_MODIFICAR,
				formularioModificar);
		RequestContext.getCurrentInstance().execute("adminFormularios.show()");
	}

	public void volverVerDetalle() {

		mostrarInfoDimension = false;
		dimensionNueva = null;
	}

	public void guardarCubo() {

		try {

			cuboOperacion.setIdeUsuarioCreacion(getUsuarioLogueado()
					.getIdeUsuario());

			proyectoActual.addCubo(cuboOperacion);

			adminPersistenciaProyectos.modificarProyecto(proyectoActual);

			creandoCubo = false;
			RequestContext.getCurrentInstance().update("frmCoreApp");
		} catch (Exception e) {

			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al crear el cubo, revise el log", null);

		}

	}

	public void guardarHecho() {

		if (cuboOperacion.getHecho().getRespuestas() == null
				|| cuboOperacion.getHecho().getRespuestas().isEmpty()) {
			escribirErrorPantalla("Error",
					"Debe seleccionar al menos una respuesta para el Hecho",
					null);
		} else {
			cuboOperacion.getHecho().setUsuarioCreacion(
					getUsuarioLogueado().getIdeUsuario());
			crearHecho = false;
		}
	}

	public void guardarDimension() {

		if (dimensionNueva.getRespuestas() == null
				|| dimensionNueva.getRespuestas().isEmpty()) {
			escribirErrorPantalla(
					"Error",
					"Debe seleccionar al menos una respuesta para la dimensión",
					null);
		} else {

			dimensionNueva.setUsuarioCreacion(getUsuarioLogueado()
					.getIdeUsuario());
			cuboOperacion.adicionarDimension(dimensionNueva);
			crearDimension = false;
		}

	}

	public void guardarAtributo() {

		if (atributoNuevo.getRespuestas() == null
				|| atributoNuevo.getRespuestas().isEmpty()) {
			escribirErrorPantalla("Error",
					"Debe seleccionar al menos una respuesta para el atributo",
					null);
		} else {

			dimensionNueva.adicionarAtributo(atributoNuevo);
			crearAtributo = false;
		}
	}

	public void eliminarFormulario() {

		nodoSeleccionado.getChildren().clear();
		nodoSeleccionado.getParent().getChildren().remove(nodoSeleccionado);
		nodoSeleccionado.setParent(null);

		NodoArbol formularioEliminar = (NodoArbol) nodoSeleccionado.getData();

		proyectoActual.removeFormularioXId(formularioEliminar.getFormulario()
				.getIdFormulario());
		adminPersistenciaProyectos.modificarProyecto(proyectoActual);

		ponerEnSesion(CConstantes.SESION_PROYECTO_ACTIVO, proyectoActual);

		nodoSeleccionado = null;
		RequestContext.getCurrentInstance().update("frmCoreApp");

	}

	public void eliminarProyecto() {

	}

	public boolean isCambiarPassword() {
		return cambiarPassword;
	}

	public void setCambiarPassword(boolean cambiarPassword) {
		this.cambiarPassword = cambiarPassword;
	}

	public TreeNode getArbolProyectos() {
		return arbolProyectos;
	}

	public void setArbolProyectos(TreeNode arbolProyectos) {
		this.arbolProyectos = arbolProyectos;
	}

	public TreeNode getNodoSeleccionado() {
		return nodoSeleccionado;
	}

	public void setNodoSeleccionado(TreeNode nodoSeleccionado) {
		this.nodoSeleccionado = nodoSeleccionado;
	}

	public List<Alertas> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<Alertas> alertas) {
		this.alertas = alertas;
	}

	public boolean isSeleccionandoFormulario() {
		return seleccionandoFormulario;
	}

	public void setSeleccionandoFormulario(boolean seleccionandoFormulario) {
		this.seleccionandoFormulario = seleccionandoFormulario;
	}

	public boolean isOtroUsuario() {
		return otroUsuario;
	}

	public void setOtroUsuario(boolean otroUsuario) {
		this.otroUsuario = otroUsuario;
	}

	public boolean isUsuarioReq() {
		return usuarioReq;
	}

	public void setUsuarioReq(boolean usuarioReq) {
		this.usuarioReq = usuarioReq;
	}

	public boolean isUsuarioDataMart() {
		return usuarioDataMart;
	}

	public void setUsuarioDataMart(boolean usuarioDataMart) {
		this.usuarioDataMart = usuarioDataMart;
	}

	public CtFormulario getFormularioSeleccionado() {
		return formularioSeleccionado;
	}

	public void setFormularioSeleccionado(CtFormulario formularioSeleccionado) {
		this.formularioSeleccionado = formularioSeleccionado;
	}

	public CtRespuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(CtRespuesta respuesta) {
		this.respuesta = respuesta;
	}

	public CtPreguntas getPreguntaActiva() {
		return preguntaActiva;
	}

	public void setPreguntaActiva(CtPreguntas preguntaActiva) {
		this.preguntaActiva = preguntaActiva;
	}

	public List<CtOpcionesRespuesta> getOpcionesMultipleSeleccionadas() {
		return opcionesMultipleSeleccionadas;
	}

	public void setOpcionesMultipleSeleccionadas(
			List<CtOpcionesRespuesta> opcionesMultipleSeleccionadas) {
		this.opcionesMultipleSeleccionadas = opcionesMultipleSeleccionadas;
	}

	public CtOpcionesRespuesta getOpcionSingleSeleccionada() {
		return opcionSingleSeleccionada;
	}

	public void setOpcionSingleSeleccionada(
			CtOpcionesRespuesta opcionSingleSeleccionada) {
		this.opcionSingleSeleccionada = opcionSingleSeleccionada;
	}

	public int getTotalPreguntas() {
		return totalPreguntas;
	}

	public void setTotalPreguntas(int totalPreguntas) {
		this.totalPreguntas = totalPreguntas;
	}

	public boolean isMostrarRespuestasFormulario() {
		return mostrarRespuestasFormulario;
	}

	public void setMostrarRespuestasFormulario(
			boolean mostrarRespuestasFormulario) {
		this.mostrarRespuestasFormulario = mostrarRespuestasFormulario;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public boolean isCreandoCubo() {
		return creandoCubo;
	}

	public void setCreandoCubo(boolean creandoCubo) {
		this.creandoCubo = creandoCubo;
	}

	public CtCubo getCuboOperacion() {
		return cuboOperacion;
	}

	public void setCuboOperacion(CtCubo cuboOperacion) {
		this.cuboOperacion = cuboOperacion;
	}

	public boolean isCrearHecho() {
		return crearHecho;
	}

	public void setCrearHecho(boolean crearHecho) {
		this.crearHecho = crearHecho;
	}

	public CtFormulario getFormularioCubo() {
		return formularioCubo;
	}

	public void setFormularioCubo(CtFormulario formularioCubo) {
		this.formularioCubo = formularioCubo;
	}

	public List<SelectItem> getFormulariosSelectList() {
		return formulariosSelectList;
	}

	public void setFormulariosSelectList(List<SelectItem> formulariosSelectList) {
		this.formulariosSelectList = formulariosSelectList;
	}

	public List<CtRespuesta> getRespuestasFormularioCubo() {
		return respuestasFormularioCubo;
	}

	public void setRespuestasFormularioCubo(
			List<CtRespuesta> respuestasFormularioCubo) {
		this.respuestasFormularioCubo = respuestasFormularioCubo;
	}

	public boolean isMostrandoRespuestasCubo() {
		return mostrandoRespuestasCubo;
	}

	public void setMostrandoRespuestasCubo(boolean mostrandoRespuestasCubo) {
		this.mostrandoRespuestasCubo = mostrandoRespuestasCubo;
	}

	public boolean isCrearDimension() {
		return crearDimension;
	}

	public void setCrearDimension(boolean crearDimension) {
		this.crearDimension = crearDimension;
	}

	public CtDimension getDimensionNueva() {
		return dimensionNueva;
	}

	public void setDimensionNueva(CtDimension dimensionNueva) {
		this.dimensionNueva = dimensionNueva;
	}

	public boolean isCrearAtributo() {
		return crearAtributo;
	}

	public void setCrearAtributo(boolean crearAtributo) {
		this.crearAtributo = crearAtributo;
	}

	public CtAtributos getAtributoNuevo() {
		return atributoNuevo;
	}

	public void setAtributoNuevo(CtAtributos atributoNuevo) {
		this.atributoNuevo = atributoNuevo;
	}

	public boolean isMostrarInfoDimension() {
		return mostrarInfoDimension;
	}

	public void setMostrarInfoDimension(boolean mostrarInfoDimension) {
		this.mostrarInfoDimension = mostrarInfoDimension;
	}

	public CtCubo getCuboSeleccionado() {
		return cuboSeleccionado;
	}

	public void setCuboSeleccionado(CtCubo cuboSeleccionado) {
		this.cuboSeleccionado = cuboSeleccionado;
	}

	public boolean isMostrarDibujoCubo() {
		return mostrarDibujoCubo;
	}

	public void setMostrarDibujoCubo(boolean mostrarDibujoCubo) {
		this.mostrarDibujoCubo = mostrarDibujoCubo;
	}

	public String getCadenaCubo() {
		return cadenaCubo;
	}

	public void setCadenaCubo(String cadenaCubo) {
		this.cadenaCubo = cadenaCubo;
	}

	public List<SelectItem> getEntrevistados() {
		return entrevistados;
	}

	public void setEntrevistados(List<SelectItem> entrevistados) {
		this.entrevistados = entrevistados;
	}

}
