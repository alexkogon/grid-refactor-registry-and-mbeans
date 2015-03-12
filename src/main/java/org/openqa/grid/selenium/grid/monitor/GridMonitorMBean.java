package org.openqa.grid.selenium.grid.monitor;

import java.util.List;

public interface GridMonitorMBean {
	public List<String> getNodes();
	public String getLastRegistrationString();
}
