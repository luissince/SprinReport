package upla.pe.service.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import upla.pe.model.ReportesUpla;

import net.sf.jasperreports.engine.JRException;

public interface ReportePruebaApi {	
	
	ReportesUpla obtenerReportesUpla(Map<String, Object> params, String archivo) throws JRException, IOException, SQLException;

}