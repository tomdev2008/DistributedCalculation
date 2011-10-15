package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.rmi.RMIResponder;

import org.apache.log4j.Logger;

public final class ServerMain implements Runnable {

	private RMIResponder remoteObject = null;

	private int port;

	private ResourceManager resourceManager;

	private Logger logger;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerMain serverMain = new ServerMain(args);
		Runtime.getRuntime().addShutdownHook(new Thread(serverMain));
	}

	public ServerMain(String[] args) {
		resourceManager = ResourceManager.getInstance();
		logger = Utility.getLogger(ServerMain.class);
		port = Integer.parseInt(resourceManager.getString("default.rmiport"));
		startup();
	}

	private synchronized void startup() {
		if (remoteObject == null)
			try {
				remoteObject = RMIResponder.getInstance(port);
			} catch (RemoteException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		try {
			Registry registry = LocateRegistry
					.createRegistry(Registry.REGISTRY_PORT);
			registry.bind(RMIResponder.SERVICE_NAME, remoteObject);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private synchronized void shutdown() {
		try {
			UnicastRemoteObject.unexportObject(remoteObject, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@Override
	public void run() {
		shutdown();
	}

}
