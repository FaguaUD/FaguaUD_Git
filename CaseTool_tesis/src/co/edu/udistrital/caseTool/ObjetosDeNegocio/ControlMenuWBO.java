package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.caseTool.Constantes.CConstantes;
import co.edu.udistrital.caseTool.Persistencia.AdminUsuarios;

@ManagedBean(name = "bMenu")
@ViewScoped
public class ControlMenuWBO extends WBOGeneral {

	private boolean usuarioAdmin = false;
	private boolean usuarioLider = false;
	private boolean otroUsuario = false;
	private boolean sinUsuario = true;
	private boolean sinAdmin = true;
	private boolean usuarioReq = false;
	
	@EJB
	AdminUsuarios adminPersistencia;

	
	@PostConstruct
	public void inicializar (){
		prepararPagina();
		RequestContext.getCurrentInstance().update("frmMenu");
	}
	
	public void configurarVista(ComponentSystemEvent event) {

		prepararPagina();
		RequestContext.getCurrentInstance().update("frmMenu");

	}

	private void prepararPagina() {
		
		sinAdmin = adminPersistencia.existeAdministrador();
		
		if (getUsuarioLogueado() != null) {
			
			if (!getUsuarioLogueado().getPrimerIngreso()) {
				sinUsuario = false;
				if (getUsuarioLogueado().getCtRol().getIdeRol()
						.equals(CConstantes.IDE_ADMINISTRADOR)) {
					usuarioAdmin = true;
				} else if (getUsuarioLogueado().getCtRol().getIdeRol()
						.equals(CConstantes.IDE_LIDER_PROYECTO)) {
					usuarioLider = true;
				} else if (getUsuarioLogueado().getCtRol().getIdeRol()
						.equals(CConstantes.IDE_REQUERIMIENTOS)){
					usuarioReq =true;
					otroUsuario = true;
				} else if (getUsuarioLogueado().getCtRol().getIdeRol()
						.equals(CConstantes.AMBOS)){
					usuarioReq =true;
					usuarioLider =true;
					otroUsuario = true;
				}
				else{
					otroUsuario = true;
				}
			}
		}

	}

	public void btnCerrarSesion() throws IOException {

		matarSesion();

	}
	
	public void prepareAdminSolicitudes(){
		ponerEnSesion(CConstantes.SESION_PREPARE_ADMIN_SOLICITUDES, true);
	}
	
	public void  prepareAdminProyectos (){
		ponerEnSesion(CConstantes.SESION_PREPARE_ADMIN_PROYECTOS, true);
	}
	
	public void prepareAbrirProyecto(){
		ponerEnSesion(CConstantes.SESION_PREPARE_ABRIR_PROYECTO, true);
	}

	public boolean isUsuarioAdmin() {
		return usuarioAdmin;
	}

	public void setUsuarioAdmin(boolean usuarioAdmin) {
		this.usuarioAdmin = usuarioAdmin;
	}	

	public boolean isUsuarioLider() {
		return usuarioLider;
	}

	public void setUsuarioLider(boolean usuarioLider) {
		this.usuarioLider = usuarioLider;
	}

	public boolean isOtroUsuario() {
		return otroUsuario;
	}

	public void setOtroUsuario(boolean otroUsuario) {
		this.otroUsuario = otroUsuario;
	}

	public boolean isSinUsuario() {
		return sinUsuario;
	}

	public void setSinUsuario(boolean sinUsuario) {
		this.sinUsuario = sinUsuario;
	}

	public boolean isSinAdmin() {
		return sinAdmin;
	}

	public void setSinAdmin(boolean sinAdmin) {
		this.sinAdmin = sinAdmin;
	}

	public boolean isUsuarioReq() {
		return usuarioReq;
	}

	public void setUsuarioReq(boolean usuarioReq) {
		this.usuarioReq = usuarioReq;
	}
	
	

}
