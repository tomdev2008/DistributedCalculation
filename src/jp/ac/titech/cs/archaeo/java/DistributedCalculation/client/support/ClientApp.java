package jp.ac.titech.cs.archaeo.java.DistributedCalculation.client.support;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.client.rmi.RMIRequester;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ClientProfile;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ServerInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

public final class ClientApp {

  private final class AliveSignalSender implements Runnable {

    @Override
    public void run() {
      Result<Boolean> result = server.sendAliveSignal(clientProfileUID);
      if (!result.getResult()) {
      }
    }

  }

  private Logger logger;

  private ResourceManager resourceManager;

  private ServerInterface server;

  private CommandLine cli;

  private static Options cliOptions;

  private String serverInetAddr;

  private long aliveSignalSenderInterval;

  private ClientProfile clientProfile;

  private UID clientProfileUID;

  private String hostName;

  private List<String> hardwareAddrList;

  private String inetAddr;
  
  private ExecutorService autoTaskExecutorService;

  private Future<?> autoTaskExecutorHandle;

  private ScheduledExecutorService aliveSignalSenderExecutorService;

  private Future<?> aliveSignalSenderHandle;

  static {
    cliOptions = new Options();
    cliOptions.addOption("a", "serverAddress", true,
        "input task daemon's ip address");
  }

  public ClientApp() {
    logger = Utility.getLogger(ClientApp.class);
    resourceManager = ResourceManager.getInstance();

    autoTaskExecutorService = Executors.newSingleThreadExecutor();
    aliveSignalSenderExecutorService = Executors
        .newSingleThreadScheduledExecutor();
    aliveSignalSenderInterval = Long.parseLong(resourceManager
        .getString("default.aliveSignalSenderInterval"));
  }

  public ClientApp(String[] args) {
    this();
    try {
      hostName = InetAddress.getLocalHost().getHostName();
      logger.info("hostName = " + hostName);
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
    }

    try {
      hardwareAddrList = new ArrayList<String>();
      Enumeration<NetworkInterface> nis = NetworkInterface
          .getNetworkInterfaces();
      for (; nis.hasMoreElements();) {
        NetworkInterface ni = nis.nextElement();
        byte[] rawHardwareAddr = ni.getHardwareAddress();
        if (rawHardwareAddr != null) {
          hardwareAddrList
              .add(Utility.rawHardwareAddrToString(rawHardwareAddr));
          logger.info("rawHardwareAddr = " + Arrays.toString(rawHardwareAddr));
        }
      }
    } catch (SocketException e1) {
      e1.printStackTrace();
    }

    try {
      inetAddr = InetAddress.getLocalHost().getHostAddress();
      logger.info("inetAddr = " + inetAddr);
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
    }

    clientProfile = new ClientProfile(hostName, hardwareAddrList, inetAddr);

    try {
      parseCommandLine(args);
    } catch (ParseException e) {
      logger.error("Parse error occurred. Exit program immediately...");
      e.printStackTrace();
      System.exit(1);
    }
    if (!cli.hasOption("a")) {
      logger.error("invalid arguments. Exit program immediately...");
      System.exit(1);
    }
    serverInetAddr = cli.getOptionValue("a");
    server = new RMIRequester(serverInetAddr);
    start();
  }

  public void parseCommandLine(String[] args) throws ParseException {
    CommandLineParser parser = new PosixParser();
    cli = parser.parse(cliOptions, args);
  }

  private synchronized void start() {
    {
      Result<UID> result = server.registerClient(clientProfile);
      clientProfileUID = result.getResult();
    }

    {
      // execute aliveSignalSender
      aliveSignalSenderHandle = aliveSignalSenderExecutorService
          .scheduleAtFixedRate(new AliveSignalSender(), 0L,
              aliveSignalSenderInterval, TimeUnit.MILLISECONDS);
    }

    {
      // execute autotaskexecutor
      autoTaskExecutorHandle = autoTaskExecutorService
          .submit(new AutoTaskExecutor(server, clientProfileUID));
    }
  }

  private synchronized void stop() {

    // cancel aliveSignalSender
    aliveSignalSenderHandle.cancel(true);

    // cancel autotaskexecutor
    autoTaskExecutorHandle.cancel(true);
  }

  public synchronized void shutdown() {
    stop();

    // shutdown aliveSignalSenderExecutorService
    aliveSignalSenderExecutorService.shutdown();

    // shutdown autotaskexecutorservice
    autoTaskExecutorService.shutdown();
  }

}
