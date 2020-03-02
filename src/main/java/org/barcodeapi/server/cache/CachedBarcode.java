package org.barcodeapi.server.cache;

import java.util.Properties;

import org.barcodeapi.core.utils.CodeUtils;
import org.barcodeapi.server.core.CachedObject;

public class CachedBarcode implements CachedObject {

	private final long timeCreated;

	private final byte[] cachedData;

	private final String checksum;

	private long lastAccess = 0;

	private Properties properties;

	public CachedBarcode(byte[] data) {

		this.timeCreated = System.currentTimeMillis();

		this.cachedData = data;

		this.checksum = CodeUtils.getMD5Sum(data);

		this.properties = new Properties();
	}

	public long getTimeCreated() {

		return timeCreated;
	}

	public byte[] getData() {

		lastAccess = System.currentTimeMillis();
		return cachedData;
	}

	public long getDataSize() {

		return cachedData == null ? 0 : cachedData.length;
	}

	public String getChecksum() {

		return checksum;
	}

	public long getLastAccess() {

		return lastAccess;
	}

	public Properties getProperties() {

		return properties;
	}
}