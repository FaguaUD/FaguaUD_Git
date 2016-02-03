package co.edu.udistrital.caseTool.Persistencia;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.udistrital.caseTool.Entidades.CtEntrevistado;
import co.edu.udistrital.caseTool.Entidades.CtRespuesta;

@Stateless
@LocalBean
public class AdminRespuesta extends AdministradorPersistencia {

	public void crearRespuesta(CtRespuesta respuesta) {
		insertar(respuesta);

	}

	public List<CtRespuesta> consultarRespuestaPreguna(Long idPregunta,
			Long idFormulario) {

		return ejecutarConsulta("SELECT R FROM CtRespuesta R JOIN R.pregunta P JOIN R.formulario F WHERE P.idPregunta = "
				+ idPregunta + " AND F.idFormulario = " + idFormulario);
	}

	public List<CtRespuesta> consultarRespuestasFormulario(Long idFormulario) {
		
		return ejecutarConsulta("SELECT R FROM CtRespuesta R JOIN R.formulario F WHERE F.idFormulario = " + idFormulario);
	}

	public CtEntrevistado consultarEntrevistadoRespuesta(
			Long idUsuarioEntrevistador) {
	
		List<CtEntrevistado> entrevistados = 
				ejecutarConsulta("SELECT R FROM CtEntrevistado R WHERE R.ideEntrevistado = " + idUsuarioEntrevistador);
		
		if (entrevistados != null && !entrevistados.isEmpty()){
			return entrevistados.get(0);
		}
		
		return null;
		
	}

}
