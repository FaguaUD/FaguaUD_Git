package co.edu.udistrital.caseTool.Persistencia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtRole;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;

@Stateless
@LocalBean
public class AdminUsuarios extends AdministradorPersistencia {

	public AdminUsuarios() {

	}

	public void crearUsuario(CtUsuario nuevoUsuario) {
		insertar(nuevoUsuario);
	}

	public void actualizarUsuario(CtUsuario usuario) {
		actualizar(usuario);
	}

	public void eliminarUsuarioPorID(Long idUsuario) {

		List<CtUsuario> usuarioPadre = ejecutarConsulta("SELECT U FROM CtUsuario U WHERE U.ideUsuario = "
				+ idUsuario);

		if (!usuarioPadre.isEmpty()) {
			eliminar(usuarioPadre.get(0));
		}

	}

	public boolean existeAdministrador() {

		CtUsuario usuarioFiltro = new CtUsuario();

		ArrayList<Long> rolesConsulta = new ArrayList<Long>();

		rolesConsulta.add(CConstantes.IDE_ADMINISTRADOR);

		usuarioFiltro.setRolesConsulta(rolesConsulta);

		return !buscarUsuario(usuarioFiltro).isEmpty();

	}

	public boolean validarSiExisteUsuario(String nombreUsuario) {

		CtUsuario usuarioFiltro = new CtUsuario();
		ArrayList<String> estadosConsulta = new ArrayList<String>();
		estadosConsulta.add(CConstantes.ESTADO_ACTIVO);

		usuarioFiltro.setEstadosConsulta(estadosConsulta);
		usuarioFiltro.setNombreUsuario(nombreUsuario);

		return !buscarUsuario(usuarioFiltro).isEmpty();
	}

	public CtUsuario verificarUsuario(CtUsuario usuario) {

		ArrayList<String> estadosConsulta = new ArrayList<String>();
		estadosConsulta.add(CConstantes.ESTADO_ACTIVO);
		estadosConsulta.add(CConstantes.ESTADO_ACTIVO_EN_ESPERA);

		usuario.setEstadosConsulta(estadosConsulta);

		List<CtUsuario> usuarios = buscarUsuario(usuario);
		return usuarios.isEmpty() ? null : usuarios.get(0);
	}

	public List<CtUsuario> consultarUsuariosPendientesAdmin() {

		CtUsuario usuarioFiltro = new CtUsuario();
		ArrayList<Long> rolesConsulta = new ArrayList<Long>();

		rolesConsulta.add(CConstantes.IDE_LIDER_PROYECTO);
		
		usuarioFiltro.setRolesConsulta(rolesConsulta);

		return buscarUsuario(usuarioFiltro);

	}

	public List<CtUsuario> consultarUsuariosPendientesLider(Long padre) {

		CtUsuario usuarioFiltro = new CtUsuario();
		ArrayList<Long> rolesConsulta = new ArrayList<Long>();

		rolesConsulta.add(CConstantes.IDE_REQUERIMIENTOS);
		rolesConsulta.add(CConstantes.IDE_DATAMART);
		rolesConsulta.add(CConstantes.AMBOS);

		usuarioFiltro.setPadre(padre);
		usuarioFiltro.setRolesConsulta(rolesConsulta);

		return buscarUsuario(usuarioFiltro);

	}

	public List<CtRole> consultarRoles(boolean rolesVisibles) {
		return ejecutarConsulta("SELECT r FROM CtRole r WHERE r.visible = "
				+ rolesVisibles);
	}

	public List<CtUsuario> buscarUsuario(CtUsuario usuarioFiltro) {
		StringBuffer query = new StringBuffer("SELECT U FROM CtUsuario U ");
		StringBuffer condiciones = new StringBuffer(" WHERE ");
		ArrayList<Object> parametros = new ArrayList<Object>();
		boolean conCondiciones = false;
		int posicionParametro = 1;

		if (usuarioFiltro.getNombre() != null
				&& !usuarioFiltro.getNombre().isEmpty()) {

			condiciones.append(" U.nombre like ?" + posicionParametro);
			conCondiciones = true;
			parametros.add("%" + usuarioFiltro.getNombre() + "%");
			posicionParametro++;

		}

		if (usuarioFiltro.getApellido() != null
				&& !usuarioFiltro.getApellido().isEmpty()) {

			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.apellido like ?" + posicionParametro);
			parametros.add("%" + usuarioFiltro.getApellido() + "%");
			conCondiciones = true;
			posicionParametro++;
		}

		if (usuarioFiltro.getCodigo() != null
				&& !usuarioFiltro.getCodigo().equals(new Long(0))) {

			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.codigo = ?" + posicionParametro);
			parametros.add(usuarioFiltro.getCodigo());
			conCondiciones = true;
			posicionParametro++;
		}

		if (usuarioFiltro.getRolesConsulta() != null
				&& !usuarioFiltro.getRolesConsulta().isEmpty()) {

			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.ctRol.ideRol in (");

			Iterator<Long> it = usuarioFiltro.getRolesConsulta().iterator();
			while (it.hasNext()) {

				Long rol = it.next();
				condiciones.append(rol);
				if (it.hasNext()) {
					condiciones.append(",");
				}

			}

			condiciones.append(")");
			conCondiciones = true;
		}

		if (usuarioFiltro.getEstadosConsulta() != null
				&& !usuarioFiltro.getEstadosConsulta().isEmpty()) {

			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.estado in (");

			Iterator<String> it = usuarioFiltro.getEstadosConsulta().iterator();
			while (it.hasNext()) {

				String estado = it.next();
				condiciones.append("'" + estado + "'");
				if (it.hasNext()) {
					condiciones.append(",");
				}

			}

			condiciones.append(")");
			conCondiciones = true;
		}

		if (usuarioFiltro.getNombreUsuario() != null
				&& !usuarioFiltro.getNombreUsuario().isEmpty()) {
			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.nombreUsuario = ?" + posicionParametro);
			parametros.add(usuarioFiltro.getNombreUsuario());
			conCondiciones = true;
			posicionParametro++;
		}

		if (usuarioFiltro.getPassword() != null
				&& !usuarioFiltro.getPassword().isEmpty()) {
			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.password = ?" + posicionParametro);
			parametros.add(usuarioFiltro.getPassword());
			conCondiciones = true;
			posicionParametro++;
		}

		if (usuarioFiltro.getPadre() != null
				&& !usuarioFiltro.getPadre().equals(new Long(0))) {			

			if (conCondiciones) {
				condiciones.append(" AND ");
			}

			condiciones.append(" U.padre = ?" + posicionParametro);
			parametros.add(usuarioFiltro.getPadre());
			conCondiciones = true;
			posicionParametro++;

		}

		if (conCondiciones) {
			query.append(condiciones);
		}

		return ejecutarConsultaParametros(query.toString(), parametros,
				new CtUsuario());

	}

	public List<CtUsuario> consultarUsuariosProyecto(Long ideProyecto) {
		return ejecutarConsulta("SELECT U FROM CtUsuario U JOIN U.proyectosAsignados P WHERE P.ideProyecto = "+ideProyecto);
	}

}
