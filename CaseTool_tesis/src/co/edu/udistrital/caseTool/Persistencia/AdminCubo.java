package co.edu.udistrital.caseTool.Persistencia;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.udistrital.caseTool.Entidades.CtAtributos;
import co.edu.udistrital.caseTool.Entidades.CtCubo;
import co.edu.udistrital.caseTool.Entidades.CtDimension;

@Stateless
@LocalBean
public class AdminCubo extends AdministradorPersistencia {

	public void actualizarCubo(CtCubo cuboOperacion) {
		actualizar(cuboOperacion);
	}

	public List<CtCubo> consultarCubosProyecto(Long ideProyecto) {
	
		return ejecutarConsulta("SELECT C FROM CtCubo C JOIN C.proyecto P WHERE P.ideProyecto ="+ideProyecto);
	}

	public List<CtDimension> consultarDimensionesCubo(Long ideCubo) {
		
		return ejecutarConsulta("SELECT D FROM CtDimension D JOIN D.cubos C WHERE C.ideCubo ="+ideCubo);
	}

	public List<CtAtributos> consultarAtributosDimension(Long ideDimension) {
		return ejecutarConsulta("SELECT A FROM CtAtributos A JOIN A.dimensiones D WHERE D.ideDimension ="+ideDimension);
	}

}
