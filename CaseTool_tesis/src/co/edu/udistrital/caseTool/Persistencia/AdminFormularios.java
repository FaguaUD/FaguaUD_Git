package co.edu.udistrital.caseTool.Persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.udistrital.caseTool.Entidades.CtEntrevistado;
import co.edu.udistrital.caseTool.Entidades.CtFormulario;
import co.edu.udistrital.caseTool.Entidades.CtPreguntas;

@Stateless
@LocalBean
public class AdminFormularios extends AdministradorPersistencia {

	public List<CtFormulario> consultarFormularioNombre(String nombreFormulario) {
		String query = "SELECT F FROM CtFormulario F WHERE F.nombreFormulario LIKE ?1";
		ArrayList<Object> parametros = new ArrayList<Object>();

		parametros.add("%" + nombreFormulario + "%");

		return ejecutarConsultaParametros(query.toString(), parametros,
				new CtFormulario());

	}

	public void crearFormulario(CtFormulario formularioOperacion) {
		insertar(formularioOperacion);

	}

	public void actualizarFormulario(CtFormulario formularioOperacion) {
		actualizar(formularioOperacion);
	}

	public List<CtPreguntas> consultarPreguntasFormulario(Long idFormulario) {
		return ejecutarConsulta("SELECT P FROM CtPreguntas P JOIN P.formularios F WHERE F.idFormulario = "
				+ idFormulario);
	}

	public List<CtFormulario> consultarFormularioProyecto(Long ideProyecto) {
		return ejecutarConsulta("SELECT F FROM CtFormulario F JOIN F.proyectos P WHERE P.ideProyecto = "
				+ ideProyecto);
	}

	public List<CtEntrevistado> consultarEntrevistadosDisponibles(
			Long ideUsuario) {		
		return ejecutarConsulta("SELECT F FROM CtEntrevistado F WHERE F.ideUsuario = "
				+ ideUsuario);
	}

	public void crearEntrevistado(CtEntrevistado entrevistado) {
		insertar(entrevistado);
		
	}

	public void eliminarEntrevistado(CtEntrevistado entrevistado) {
		
		List<CtEntrevistado> entrevistadoDB = ejecutarConsulta("SELECT U FROM CtEntrevistado U WHERE U.ideEntrevistado = "
				+ entrevistado.getIdeEntrevistado());

		if (!entrevistadoDB.isEmpty()) {
			eliminar(entrevistadoDB.get(0));
		}	
		
	}

	public void modificarEntrevistado(CtEntrevistado entrevistado) {
		actualizar(entrevistado);
		
	}
	
}
