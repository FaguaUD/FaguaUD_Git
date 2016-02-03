package co.edu.udistrital.caseTool.Persistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.udistrital.caseTool.Entidades.CtProyecto;

@Stateless
@LocalBean
public class AdminProyectos extends AdministradorPersistencia {

	public AdminProyectos() {

	}

	public void crearProyecto(CtProyecto nuevoProyecto) {
		insertar(nuevoProyecto);
	}

	public void modificarProyecto(CtProyecto proyectoOperacion) {
		actualizar(proyectoOperacion);
		
	}

	public List<CtProyecto> consultarProyectosUsuarioLider(Long ideUsuario) {
	
		return ejecutarConsulta("SELECT U FROM CtProyecto U WHERE U.liderProyecto = "
				+ ideUsuario);
	}

	public List<CtProyecto> consultarProyectosUsuario(Long ideUsuario) {		
		return ejecutarConsulta("SELECT P FROM CtProyecto P JOIN P.usuariosAsignados U WHERE U.ideUsuario = "
				+ ideUsuario);
	}

	public List<CtProyecto> consultarProyectosUsuarioFecha(Long ideUsuario,
			Date fechaInicio, Date fechaFin, boolean entrandoLider) {

		
		String queryParam = "";
		
		if (entrandoLider){
			queryParam= "SELECT P FROM CtProyecto P WHERE P.liderProyecto=?1 AND P.fechaCreacion BETWEEN ?2 and ?3";
		}else{
			queryParam= "SELECT P FROM CtProyecto P JOIN P.usuariosAsignados U WHERE U.ideUsuario = ?1 AND P.fechaCreacion BETWEEN ?2 and ?3";
		}
		ArrayList<Object> parametros = new ArrayList<Object>();
		parametros.add(ideUsuario);
		parametros.add(fechaInicio);
		parametros.add(fechaFin);
		return ejecutarConsultaParametros(queryParam, parametros, new CtProyecto());
		
	}



	

}
