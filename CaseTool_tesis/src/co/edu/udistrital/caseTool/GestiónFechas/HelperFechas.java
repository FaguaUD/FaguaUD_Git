package co.edu.udistrital.caseTool.GestiónFechas;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HelperFechas {

	public static boolean fechaMayorAHoy(Date fechaTerminacion) {		
		return compararFechas(fechaTerminacion, new Date());			
				
	}
	
	private static boolean compararFechas(Date fechaMayor, Date fechaMenor) {	
		return fechaMayor.after(fechaMenor);
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	public static Date cambiarHoraFecha(Date fechaFiltro, int hora, int minuto, int segundo) {
	
		Calendar cal = Calendar.getInstance();      
		cal.setTime(fechaFiltro);                   
		cal.set(Calendar.HOUR_OF_DAY, hora);           
		cal.set(Calendar.MINUTE, minuto);                
		cal.set(Calendar.SECOND, segundo);                
		cal.set(Calendar.MILLISECOND, 0);           
		
		return  cal.getTime(); 
	}

	public static boolean fechaIncioMayorAFin(Date fechaInicioFiltro,
			Date fechaFinFiltro) {
		
		return compararFechas(fechaFinFiltro, fechaInicioFiltro);		
	}
	
	
	
}
