package com.blackcrystalinfo.push.main;

import java.lang.management.ManagementFactory;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.blackcrystalinfo.push.service.PushService;
import com.blackcrystalinfo.push.service.PushServiceMBean;

public class PushServiceConsole {
	public static void main(String[] args) {

		try {
			if (null != args && args.length > 0
					&& args[0].equalsIgnoreCase("stop")) {
				System.out.println("Invoke \"stop\" method");
				stop();
			} else {
				System.out.println("Invoke \"start\" method");
				start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void stop() throws Exception {
		String serviceURL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";
		JMXServiceURL url = new JMXServiceURL(serviceURL);

		JMXConnector jmxc = JMXConnectorFactory.connect(url);

		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		ObjectName objectName = new ObjectName(
				"com.blackcrystalinfo.push.service:type=PushService");
		PushServiceMBean pushServiceProxy = JMX.newMBeanProxy(mbsc, objectName,
				PushServiceMBean.class, true);

		pushServiceProxy.doStop();
	}

	private static void start() throws Exception {
		PushServiceMBean pushSerice = new PushService();

		MBeanServer server = ManagementFactory.getPlatformMBeanServer();

		ObjectName name = new ObjectName(
				"com.blackcrystalinfo.push.service:type=PushService");
		server.registerMBean(pushSerice, name);

		pushSerice.doStart();
	}
}
