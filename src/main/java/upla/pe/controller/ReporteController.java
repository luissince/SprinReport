package upla.pe.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upla.pe.enums.TipoReporteEnum;
import upla.pe.model.Reporte;
import upla.pe.model.ReportesUpla;
import upla.pe.service.api.ReportePruebaApi;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

	@Autowired
	private ReportePruebaApi reportePruebaApi;

	@GetMapping(path = "/gyt")
	public ResponseEntity<Resource> obtenerRecurso(@RequestParam Map<String, Object> params)
			throws JRException, IOException, SQLException {

		ReportesUpla dto = reportePruebaApi.obtenerReportesUpla(params, "rpt_constancia");

		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
		MediaType mediaType = null;
		if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			mediaType = MediaType.APPLICATION_PDF;
		}

		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}

	@GetMapping(path = "/gyto")
	public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params) {

		try {
			ReportesUpla dto = reportePruebaApi.obtenerReportesUpla(params, "rpt_constancia");

			InputStreamResource streamResource = new InputStreamResource(dto.getStream());
			MediaType mediaType = null;
			if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
				mediaType = MediaType.APPLICATION_OCTET_STREAM;
			} else {
				mediaType = MediaType.APPLICATION_PDF;
				// mediaType = MediaType.parseMediaType("application/octet-stream");
			}
			return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
					.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
			/*
			 * return ResponseEntity.ok().header("application/pdf",
			 * dto.getFileName()).contentLength(dto.getLength())
			 * .contentType(mediaType).body(streamResource);
			 */
		} catch (JRException | IOException | SQLException a) {
			System.out.println(a.getMessage());
			return null;
			// TODO: handle exception
		}
		/*
		 * return ResponseEntity.ok().header("Content-Disposition",
		 * "inline; filename=\"" + dto.getFileName() + "\"")
		 * .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
		 */
	}
	@PostAuthorize("hasRole('Administrador')")
	@GetMapping(path = "/listar")
	public Reporte reportres(@RequestParam Map<String, Object> params) {
		String pdfBase64 = "";

		try {
			
			var dto = reportePruebaApi.obtenerReportesUpla(params, "rpt_constancia");
			InputStreamResource streamResource = new InputStreamResource(dto.getStream());
			MediaType mediaType = null;
			if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
				mediaType = MediaType.APPLICATION_OCTET_STREAM;
			} else {
				mediaType = MediaType.APPLICATION_PDF;				
			}
			
			byte[] bytes = streamResource.getInputStream().readAllBytes();
			pdfBase64 = java.util.Base64.getEncoder().encodeToString(bytes);			
		} catch (JRException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Reporte("rpt_constancia",params.get("tipo").toString(),pdfBase64);
	}

}