package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;
import co.edu.udistrital.caseTool.GestiónEmail.AdministradorEmail;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;

@ManagedBean(name = "bLoginUsuario")
@RequestScoped
public class LoginUsuarioWBO extends WBOGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4645209907714564147L;
	private CtUsuario usuario;

	@EJB
	AdminUsuarios adminPersistencia;

	@PostConstruct
	public void inicializar() {

		usuario = new CtUsuario();
	}

	public String btnEntrarAlSistema() {

		usuario = adminPersistencia.verificarUsuario(usuario);

		if (usuario != null) {

			ponerEnSesion(CConstantes.SESION_USUARIO_LOGUEADO, usuario);

			if (usuario.getPrimerIngreso()) {
				return "nuevoPassword";

			} else {

				return "inicio";
			}
		} else {
			escribirErrorPantalla("Error de acceso",
					"nombre de usuario o contraseña incorrecta", null);
		}

		return "";

	}

	public void btnRecordarPassword() {

		try {

			if (usuario.getNombreUsuario() != null
					&& !usuario.getNombreUsuario().isEmpty()) {

				if (adminPersistencia.validarSiExisteUsuario(usuario
						.getNombreUsuario())) {

					CtUsuario userRecuperacion = adminPersistencia
							.buscarUsuario(usuario).get(0);
					AdministradorEmail adminEmail = new AdministradorEmail();

					adminEmail.enviarEmail(userRecuperacion.getEmail(),
							CConstantes.ASUNTO_RECUPERACION_PASSWORD, String
									.format(CConstantes.MENSAJE_PASSWORD,
											userRecuperacion.getPassword()));
					escribirInfoPantalla("Info",
							"Se envió un email con la información de ingreso",
							null);

				} else {

					escribirErrorPantalla(
							"Error",
							String.format("El usuario %s no existe",
									usuario.getNombreUsuario()), null);

				}

			} else {

				escribirErrorPantalla("Error", "Digite un nombre de usuario",
						null);

			}

		} catch (Exception e) {

			e.printStackTrace();
			escribirErrorPantalla("Error",
					"Error al enviar el email, revise el log", null);
		}

	}

	public CtUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(CtUsuario usuario) {
		this.usuario = usuario;
	}

}
