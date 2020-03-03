package org.barcodeapi.server.tasks;

import org.barcodeapi.core.utils.Log;
import org.barcodeapi.core.utils.Log.LOG;
import org.barcodeapi.server.core.BackgroundTask;
import org.barcodeapi.server.core.ObjectCache;
import org.barcodeapi.server.session.SessionCache;

public class SessionCleanupTask extends BackgroundTask {

	public SessionCleanupTask() {
		super();
	}

	@Override
	public void onRun() {

		ObjectCache sessions = SessionCache.getCache();
		int removed = sessions.expireOldObjects();
		int active = sessions.count();

		Log.out(LOG.SERVER, "SESSIONS : Stale [ " + removed + " ] Active [ " + active + " ]");
	}
}
