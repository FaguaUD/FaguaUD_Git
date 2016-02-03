package co.edu.udistrital.caseTool.Persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class AdministradorPersistencia {

	@PersistenceContext(name = "CaseToolPersistence")
	private EntityManager manejador;

	
	@SuppressWarnings("unchecked")
	protected <Entidad> List<Entidad> buscarTodo(Entidad entidad) {

		String nombreEntidad = entidad.getClass().getSimpleName();
		Query consulta = manejador.createQuery("Select t from " + nombreEntidad
				+ " t");
		return consulta.getResultList();

	}

	@SuppressWarnings("unchecked")
	protected <Entidad> List<Entidad> ejecutarConsulta(String consulta) {
		
		return manejador.createQuery(consulta).getResultList();
	}

	protected <Entidad> void insertar(Entidad entidad) {

		manejador.persist(entidad);
		manejador.flush();
	}
	

	protected <Entidad> void actualizar(Entidad entidad) {
		manejador.merge(entidad);
		manejador.flush();
	}
	
	@SuppressWarnings("unchecked")
	protected <Entidad> List<Entidad> ejecutarConsultaParametros(String queryParam, ArrayList<Object> parametros, Entidad entidad) {
		Query query = manejador.createQuery(queryParam);
		int i = 1; 
		for (Object object : parametros) {
			query.setParameter(i, object);
			i++;
		}
		return query.getResultList();
	}


	protected <Entidad> void eliminar(Entidad entidad) {
		manejador.remove(entidad);
		manejador.flush();
		
	}
	
	


}
