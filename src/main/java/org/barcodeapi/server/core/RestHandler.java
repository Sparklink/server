package org.barcodeapi.server.core;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.barcodeapi.core.utils.Log;
import org.barcodeapi.core.utils.Log.LOG;
import org.barcodeapi.server.session.SessionCache;
import org.barcodeapi.server.session.SessionObject;
import org.barcodeapi.server.statistics.StatsCollector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public abstract class RestHandler extends AbstractHandler {

	private final String serverName;

	private final StatsCollector stats;

	public RestHandler() {

		try {
			serverName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.stats = StatsCollector.getInstance();
	}

	public StatsCollector getStats() {
		return stats;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// skip if handled
		if (baseRequest.isHandled()) {
			return;
		}

		// request is handled
		baseRequest.setHandled(true);
		response.setStatus(HttpServletResponse.SC_OK);

		// extract class name
		String className = getClass().getName();
		className = className.substring(className.lastIndexOf('.') + 1);

		// log the request
		Log.out(LOG.REQUEST, className + " : " + target + " : " + baseRequest.getRemoteAddr());

		// hit the counters
		getStats().incrementCounter("request.handler." + className + ".count");
		getStats().incrementCounter("request.method." + request.getMethod() + ".count");

		// add CORS headers
		addCORS(baseRequest, response);

		// done if options request
		if (request.getMethod().equals("OPTIONS")) {
			return;
		}

		// server details
		response.setHeader("Server", "BarcodeAPI.org");
		response.setHeader("Server-Node", serverName);
		response.setHeader("Accept-Charset", "utf-8");
		response.setCharacterEncoding("UTF-8");

		// user session info
		SessionObject session = getSession(request);
		session.hit(baseRequest.getOriginalURI().toString());
		response.addHeader("Set-Cookie", "session=" + session.getKey() + ";");
	}

	protected void addCORS(HttpServletRequest request, HttpServletResponse response) {

		String origin = request.getHeader("origin");
		if (origin != null) {

			response.setHeader("Access-Control-Max-Age", "86400");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}

		if (request.getMethod().equals("OPTIONS")) {
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		}
	}

	protected SessionObject getSession(HttpServletRequest request) {

		// get existing user session
		SessionObject session = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("session")) {
					session = SessionCache.getInstance().get(cookie.getValue());
				}
			}
		}

		// new session if none existing
		if (session == null) {
			session = SessionCache.getInstance().createNewSession();
		}

		return session;
	}
}
