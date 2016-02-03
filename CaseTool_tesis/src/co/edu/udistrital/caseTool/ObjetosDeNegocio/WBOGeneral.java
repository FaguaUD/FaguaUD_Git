package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Entidades.CtUsuario;

public class WBOGeneral {

	private CtUsuario usuarioLogueado;

	public WBOGeneral() {

	}

	public Object tomarDeSesion(String llave) {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		
		Map<String, Object> sessionMap = externalContext.getSessionMap();	
		
		
		return sessionMap.get(llave);

	}

	public void ponerEnSesion(String llave, Object objeto) {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		
		Map<String, Object> sessionMap = externalContext.getSessionMap();	
		
		
		sessionMap.put(llave, objeto);
	}

	public void quitarDeSesion(String llave) {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();	
	
		sessionMap.remove(llave);
	}

	public void matarSesion() throws IOException {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();

		externalContext.invalidateSession();
		externalContext.redirect(externalContext.getRequestContextPath()
				+ "/paginas/bienvenida.xhtml");

	}

	public void escribirErrorPantalla(String encabezado, String error,
			String componente) {

		FacesContext.getCurrentInstance()
				.addMessage(
						componente,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								encabezado, error));
	}

	public void escribirInfoPantalla(String encabezado, String error,
			String componente) {

		FacesContext.getCurrentInstance()
				.addMessage(
						componente,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								encabezado, error));
	}

	public CtUsuario getUsuarioLogueado() {
		
		if (tomarDeSesion(CConstantes.SESION_USUARIO_LOGUEADO) != null) {
			usuarioLogueado = (CtUsuario) tomarDeSesion(CConstantes.SESION_USUARIO_LOGUEADO);
		}
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(CtUsuario usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}
	
	

}
