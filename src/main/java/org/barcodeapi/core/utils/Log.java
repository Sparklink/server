package org.barcodeapi.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Log {

	public enum LOG {
		SERVER, REQUEST, BARCODE, ERROR;
	}

	private static final String _LOGS;
	private static final SimpleDateFormat _DFORMAT;
	private static final HashMap<LOG, PrintWriter> logFiles;

	static {

		String dir = System.getenv("_LOGDIR");
		_LOGS = (dir == null) ? "logs" : dir;
		_DFORMAT = new SimpleDateFormat("YYYYMMdd HH:mm:ss");

		(new File(_LOGS)).mkdirs();
		logFiles = new HashMap<>();

		rollLogs();
	}

	// TODO call this at midnight
	public static void rollLogs() {

		for (LOG log : LOG.values()) {

			File f = new File(_LOGS, String.format("%s-%s.log", //
					getTime().substring(0, 8), log.name()));

			try {

				if (logFiles.containsKey(log)) {
					logFiles.get(log).close();
				}
				logFiles.put(log, new PrintWriter(new FileWriter(f, true), true));
				Log.out(LOG.SERVER, "Setup log file: " + f.getPath());
			} catch (Exception e) {

				Log.out(LOG.SERVER, "" + //
						"Failed to setup log: " + f.getAbsolutePath());
			}
		}
	}

	public static void out(LOG type, String message) {

		// format log line
		String output = getTime() + " : " + type.toString() + " : " + message;

		// write to log file and out
		logFiles.get(type).println(output);
		System.out.println(output);
	}

	public static String getTime() {

		return _DFORMAT.format(Calendar.getInstance().getTime());
	}
}
