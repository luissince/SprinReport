package upla.pe.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upla.pe.commons.JasperReportManager;
import upla.pe.enums.TipoReporteEnum;
import upla.pe.model.ReportesUpla;
import upla.pe.service.api.ReportePruebaApi;
import net.sf.jasperreports.engine.JRException;

@Service
public class ReporteUplaServiceImpl implements ReportePruebaApi {

	@Autowired
	private JasperReportManager reportManager;

	@Autowired
	private DataSource dataSource;

	@Override
	public ReportesUpla obtenerReportesUpla(Map<String, Object> params, String archivo)
			throws JRException, IOException, SQLException {
		// String fileName = "rpt_constancia";
		String fileName = archivo;
		ReportesUpla reporte = new ReportesUpla();
		String extension = params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx"
				: ".pdf";
		reporte.setFileName(fileName + extension);
		try (Connection connection = dataSource.getConnection()) {
			ByteArrayOutputStream stream = reportManager.export(fileName, params.get("tipo").toString(), params,
					connection);
			byte[] bs = stream.toByteArray();
			reporte.setStream(new ByteArrayInputStream(bs));
			reporte.setLength(bs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reporte;
	}

}