package org.barcodeapi.server.api;

import org.barcodeapi.server.cache.CachedBarcode;
import org.barcodeapi.server.core.GenerationException;
import org.barcodeapi.server.core.Log;
import org.barcodeapi.server.core.Log.LOG;
import org.barcodeapi.server.core.RestHandler;
import org.barcodeapi.server.gen.BarcodeGenerator;
import org.barcodeapi.server.gen.BarcodeRequest;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BarcodeAPIHandler extends RestHandler {

	private final CachedBarcode ERR;

	public BarcodeAPIHandler() {
		super();

		try {

			ERR = BarcodeGenerator.requestBarcode(BarcodeRequest.fromUri(//
					"/api/128/$$@E$$@R$$@R$$@O$$@R$$@"));
		} catch (GenerationException e) {
			throw new RuntimeException("init failed", e);
		}
	}

	@Override
	protected void onRequest(HttpServletRequest request, HttpServletResponse response)
			throws JSONException, IOException {

		CachedBarcode barcode;
		String uri = request.getRequestURI();

		try {

			// generate user requested barcode
			BarcodeRequest barcodeRequest = BarcodeRequest.fromUri(uri);
			barcode = BarcodeGenerator.requestBarcode(barcodeRequest);

		} catch (GenerationException e) {

			Log.out(LOG.ERROR, "" + //
					"Failed [ " + uri + " ] " + //
					"reason [ " + e.getMessage() + " ]");

			switch (e.getExceptionType()) {
			case EMPTY:
			case FAILED:
			default:
				// serve error code
				barcode = ERR;
				break;
			}

			// set HTTP response code and add message to headers
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setHeader("X-Error-Message", e.getMessage());
		}

		// additional properties
		String type = barcode.getProperties().getProperty("type");
		String nice = barcode.getProperties().getProperty("nice");
		String encd = barcode.getProperties().getProperty("encd");

		// add cache headers
		response.setHeader("Cache-Control", "max-age=86400, public");

		// add content headers
		response.setHeader("Content-Type", "image/png");
		response.setHeader("Content-Length", Long.toString(barcode.getDataSize()));

		// file name when clicking save
		response.setHeader("Content-Disposition", "filename=" + nice + ".png");

		// barcode details
		response.setHeader("X-Barcode-Type", type);
		response.setHeader("X-Barcode-Content", encd);

		// print image to stream
		response.getOutputStream().write(barcode.getData());
	}
}
