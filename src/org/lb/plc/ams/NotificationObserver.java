package org.lb.plc.ams;

import java.util.Map;

public interface NotificationObserver {
	// If called with null, the connection has been closed
	void notificationReceived(final Map<Long, byte[]> newValues);
}
