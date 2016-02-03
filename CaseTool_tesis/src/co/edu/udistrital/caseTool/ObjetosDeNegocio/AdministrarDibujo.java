package co.edu.udistrital.caseTool.ObjetosDeNegocio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import co.edu.udistrital.caseTool.Constantes.CConstantes;

@ManagedBean(name = "bAdminDibujo")
@ViewScoped
public class AdministrarDibujo extends WBOGeneral {

	private static final String RUTA_FILE = "/opt/jboss-as-7.1.1.Final/fagua/archivosScript";
	private String cubo;
	private String nombreCubo;
	private int lenguajeScript;
	private StreamedContent file;
	private List<SelectItem> lenguajes;

	public AdministrarDibujo() {

		lenguajes = new ArrayList<SelectItem>();
		lenguajes.add(new SelectItem(0, CConstantes.LENGUAJE_ORACLE));
		lenguajes.add(new SelectItem(1, CConstantes.LENGUAJE_POSTGRESS));
		lenguajes.add(new SelectItem(2, CConstantes.LENGUAJE_SQLSERVER));

		if (tomarDeSesion(CConstantes.SESION_CADENA_CUBO) != null) {
			cubo = (String) tomarDeSesion(CConstantes.SESION_CADENA_CUBO);

			if (tomarDeSesion(CConstantes.SESION_NOMBRE_CUBO) != null) {
				nombreCubo = (String) tomarDeSesion(CConstantes.SESION_NOMBRE_CUBO);
			}

		}
	}

	private InputStream escribirScript(String cadenaCubo) {
		try {

			String content = cuboAScript(cadenaCubo);

			File file = new File(RUTA_FILE);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			return new FileInputStream(file);

		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;
	}

	private String cuboAScript(String cadenaCubo) {

		String[] arregloCubo = cadenaCubo.split("\\|");
		String[] arregloDim = arregloCubo[1].split(":");

		String nombreHecho = arregloCubo[0];
		String scriptHecho = "CREATE TABLE \"#NOMBRE_CUBO\" (\"ID_CUBO\" #TIPO_NUMERO NOT NULL,\n #IDE_DIMENSIONES,\n CONSTRAINT PK_CUBO PRIMARY KEY (\"ID_CUBO\"),\n #FK_DIMENSIONES);";
		String scriptDIM = "CREATE TABLE \"#DIM\" (#ID_DIM #TIPO_NUMERO NOT NULL,\n #IDE_ATRIBUTOS,\n CONSTRAINT \"PK_#DIM\" PRIMARY KEY (#ID_DIM)\n);\n";
		String scriptFKDIM = "CONSTRAINT \"FK_#DIM\" FOREIGN KEY (#ID_DIM) REFERENCES \"#DIM\" (#ID_DIM) MATCH SIMPLE,";
		StringBuffer idsDimensiones = new StringBuffer();
		StringBuffer fkDimensiones = new StringBuffer();
		StringBuffer tablaDimensiones = new StringBuffer();
		StringBuffer script = new StringBuffer();

		for (String dimension : arregloDim) {

			String[] arregloAtr = dimension.split("\\(");
			String[] atributos = arregloAtr[1].split(";");

			StringBuffer nomAtributos = new StringBuffer();
			for (String atr : atributos) {

				if (!atr.equals(")")) {
					nomAtributos.append("\"");
					nomAtributos.append(atr);
					nomAtributos.append("\" #TIPO_TEXTO ");
					nomAtributos.append(",");
				}

			}

			String nomDim = arregloAtr[0];
			String idDIm = "\"ID_" + nomDim + "\"";
			idsDimensiones.append(idDIm + " #TIPO_NUMERO,");

			String atributosNom = "";
			try {
				atributosNom = nomAtributos.toString().substring(0,
					nomAtributos.toString().length() - 1);
			}
			catch(Exception e){
				atributosNom = "";
			}
			
			fkDimensiones.append(scriptFKDIM.replace("#DIM", nomDim).replace(
					"#ID_DIM", idDIm));
			tablaDimensiones
					.append(scriptDIM
							.replace("#DIM", nomDim)
							.replace("#ID_DIM", idDIm)
							.replace("#IDE_ATRIBUTOS",
									atributosNom.replace(",", ",\n")));
		}

		String dimensionesId = idsDimensiones.toString().substring(0,
				idsDimensiones.toString().length() - 1);
		String dimensionesFk = fkDimensiones.toString().substring(0,
				fkDimensiones.toString().length() - 1);
		scriptHecho = scriptHecho.replace("#NOMBRE_CUBO", nombreHecho);
		scriptHecho = scriptHecho.replace("#IDE_DIMENSIONES",
				dimensionesId.replace(",", ",\n"));
		scriptHecho = scriptHecho.replace("#FK_DIMENSIONES",
				dimensionesFk.replace(",", ",\n"));

		script.append(tablaDimensiones.toString());
		script.append("\n");
		script.append(scriptHecho);

		String scriptFinal = script.toString();
		
		switch (lenguajeScript) {
		case 0:
			scriptFinal = scriptFinal.replace("#TIPO_NUMERO", "number(10)");
			scriptFinal = scriptFinal.replace("#TIPO_TEXTO", "varchar2(200)");
			break;
		case 1:
			scriptFinal = scriptFinal.replace("#TIPO_NUMERO", "numeric");
			scriptFinal = scriptFinal.replace("#TIPO_TEXTO", "text");
			break;
		case 2:
			scriptFinal = scriptFinal.replace("#TIPO_NUMERO", "numeric(10,0)");
			scriptFinal = scriptFinal.replace("#TIPO_TEXTO", "varchar(200)");
			break;

		}
		
		
		return scriptFinal;
	}

	public void crearArchivo() {

		String nombreFile = "";

		switch (lenguajeScript) {
		case 0:
			nombreFile = String.format("%s_script.sql",
					CConstantes.LENGUAJE_ORACLE);
			break;
		case 1:
			nombreFile = String.format("%s_script.sql",
					CConstantes.LENGUAJE_POSTGRESS);
			break;
		case 2:
			nombreFile = String.format("%s_script.sql",
					CConstantes.LENGUAJE_SQLSERVER);
			break;

		}

		
		
		InputStream stream = escribirScript(cubo);
		file = new DefaultStreamedContent(stream, "text/plain", nombreFile);

	}

	public String getCubo() {
		return cubo;
	}

	public void setCubo(String cubo) {
		this.cubo = cubo;
	}

	public String getNombreCubo() {
		return nombreCubo;
	}

	public void setNombreCubo(String nombreCubo) {
		this.nombreCubo = nombreCubo;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public int getLenguajeScript() {
		return lenguajeScript;
	}

	public void setLenguajeScript(int lenguajeScript) {
		this.lenguajeScript = lenguajeScript;
	}

	public List<SelectItem> getLenguajes() {
		return lenguajes;
	}

	public void setLenguajes(List<SelectItem> lenguajes) {
		this.lenguajes = lenguajes;
	}

}
