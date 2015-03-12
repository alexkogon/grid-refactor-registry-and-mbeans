package org.openqa.grid.selenium.grid.monitor;

import java.util.ArrayList;
import java.util.List;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class GridMonitor extends NotificationBroadcasterSupport implements
		GridMonitorMBean {
	public enum EventTypeKeys {
		LastDriverString, LastRegistrationString,LastProxyStatus, LastHubStatus;
	}

	private static final String KEY_DRIVER_EVENT = EventTypeKeys.LastDriverString.toString();
	private static final String KEY_REGISTRATION_EVENT = EventTypeKeys.LastRegistrationString.toString();
	private long theRegistrationNotificationSequenceNumber = 1;
	private long theDriverNotificationSequenceNumber = 1;
	private long theProxyNotificationSequenceNumber = 1;
	private long theHubStatusNotificationSequenceNumber = 1;
	private volatile String theLastRegistrationString = "";
	private final ArrayList<String> theArrayList = new ArrayList<String>();
	private String theDriverRequestString;
	private String theProxyRequestString;

	@Override
	public List<String> getNodes() {
		ArrayList<String> arrayList = theArrayList;
		return arrayList;
	}

	@Override
	public String getLastRegistrationString() {
		return theLastRegistrationString;
	}

	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };

		String name = AttributeChangeNotification.class.getName();
		String description = "An attribute of this MBean has changed";
		MBeanNotificationInfo info = new MBeanNotificationInfo(types, name,
				description);
		return new MBeanNotificationInfo[] { info };
	}

	public void handleRegistrationString(String aRequest, String aNewRegistrationString) {
		synchronized (theArrayList) {
			theArrayList.add(aNewRegistrationString);
		}
		Notification n = new AttributeChangeNotification(this,
				theRegistrationNotificationSequenceNumber++,
				System.currentTimeMillis(), "Registration Event",
				KEY_REGISTRATION_EVENT, "String", aRequest,
				aNewRegistrationString);
		sendNotification(n);
	}

	public void handleDriverRequest(String aRequest, String aDriverRequestString) {
		Notification n = new AttributeChangeNotification(this,
				theDriverNotificationSequenceNumber++,
				System.currentTimeMillis(), "Driver Event", KEY_DRIVER_EVENT,
				"String", aRequest, aDriverRequestString);
		sendNotification(n);
	}

	public void handleProxyRequest(String aRequest, String aProxyMessage) {
		Notification n = new AttributeChangeNotification(this,
				theProxyNotificationSequenceNumber++,
				System.currentTimeMillis(), "Proxy Event", EventTypeKeys.LastProxyStatus.toString(),
				"String", aRequest, aProxyMessage);
		sendNotification(n);
	}

	public void handleHubStatusRequest(String aRequest, String aJson) {
		Notification n = new AttributeChangeNotification(this,
				theHubStatusNotificationSequenceNumber++,
				System.currentTimeMillis(), "Proxy Event", EventTypeKeys.LastHubStatus.toString(),
				"String", aRequest, aJson);
		sendNotification(n);
	}

}
