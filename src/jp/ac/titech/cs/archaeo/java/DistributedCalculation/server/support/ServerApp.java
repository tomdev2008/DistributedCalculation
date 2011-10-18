package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.server.UID;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ClientProfile;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ResultCode;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ServerInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task.TaskDistributor;

import org.apache.log4j.Logger;

public final class ServerApp implements ServerInterface {

  private final Logger logger;

  private final ResourceManager resourceManager;

  private final ViewSupport viewSupport;

  private static Properties inputProperties;

  /**
   * key = client profile UID : UID<br>
   * value = client profile info : ClientProfile
   */
  private Map<UID, ClientProfile> clientsMap;

  /**
   * only one taskDistributor
   */
  private TaskDistributor<Task<?, ?>> taskDistributor;

  /**
   * key = client profile UID : UID<br>
   * value = array list of task UID : ArrayList\<UID\>
   */
  private Map<UID, List<Task<?, ?>>> currentWorkingTaskListsMap;

  public ServerApp() {
    logger = Utility.getLogger(ServerApp.class);
    resourceManager = ResourceManager.getInstance();
    viewSupport = new ViewSupport();

    clientsMap = new ConcurrentHashMap<UID, ClientProfile>();

    startup();
  }

  @Override
  public Result<UID> registerClient(ClientProfile clientProfile) {
    logger.info("Recieved: new client register request.");
    if (clientsMap.containsValue(clientProfile)) {
      UID clientProfileUID = null;
      Set<Map.Entry<UID, ClientProfile>> entrySet = clientsMap.entrySet();
      for (Map.Entry<UID, ClientProfile> entry : entrySet) {
        if (entry.getValue().equals(clientProfile)) {
          clientProfileUID = entry.getKey();
          break;
        }
      }
      logger.info("Send: the client has already registerd.");
      return new Result<UID>(clientProfileUID, ResultCode.AlreadyRegistered);
    } else {
      UID clientProfileUID = new UID();
      clientProfile.setLoadCapability(0L);
      clientsMap.put(clientProfileUID, clientProfile);
      currentWorkingTaskListsMap.put(clientProfileUID,
          new CopyOnWriteArrayList<Task<?, ?>>());
      logger.info("Send: the client is now registerd.");
      return new Result<UID>(clientProfileUID, ResultCode.Success);
    }
  }

  @Override
  public Result<Boolean> sendAliveSignal(UID clientProfileUID) {
    logger
        .info("Received: alive signal of " + clientsMap.get(clientProfileUID));
    if (clientsMap.containsKey(clientProfileUID)) {
      return new Result<Boolean>(true, ResultCode.Success);
    } else {
      return new Result<Boolean>(false, ResultCode.Failed);
    }
  }

  @Override
  public Result<Task<?, ?>> requestTask(Task<?, ?> previousTask,
      UID clientProfileUID) {
    logger.info("Received: new task request from "
        + clientsMap.get(clientProfileUID));
    synchronized (taskDistributor) {
      ResultCode resultCode = ResultCode.Success;
      if (previousTask == null)
        taskDistributor.noticeFailure(previousTask);
      Task<?, ?> task = taskDistributor.requestNextTask(previousTask,
          clientsMap.get(clientProfileUID).getLoadCapability());
      currentWorkingTaskListsMap.get(clientProfileUID).add(task);
      return new Result<Task<?, ?>>(task, resultCode);
    }
  }

  @Override
  public void quit(UID clientProfileUID) {
    logger
        .info("Received: quit request of " + clientsMap.get(clientProfileUID));
    synchronized (taskDistributor) {
      List<Task<?, ?>> taskList = currentWorkingTaskListsMap
          .get(clientProfileUID);
      for (Task<?, ?> task : taskList) {
        taskDistributor.noticeFailure(task);
      }
      currentWorkingTaskListsMap.remove(clientProfileUID);
      clientsMap.remove(clientProfileUID);
    }
  }

  public void changeNextProcess() {
    startup();
  }

  @SuppressWarnings("unchecked")
  private void startup() {
    currentWorkingTaskListsMap = new ConcurrentHashMap<UID, List<Task<?, ?>>>();

    readNewInputPorpertiesFile();

    try {
      String taskDistributorClassName = inputProperties
          .getProperty("taskDistributor.className");
      logger.info("taskDistributorClassName = " + taskDistributorClassName);
      Class<TaskDistributor<Task<?, ?>>> taskDistributorClass = (Class<TaskDistributor<Task<?, ?>>>) Class
          .forName(taskDistributorClassName);
      Constructor<TaskDistributor<Task<?, ?>>> constructor = taskDistributorClass
          .getConstructor(Properties.class);
      taskDistributor = constructor.newInstance(inputProperties);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (SecurityException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }

  private void readNewInputPorpertiesFile() {
    File newInputPropertiesFile = viewSupport.getNewInputPropertiesFile();

    // input properties configure
    try {
      if (newInputPropertiesFile.exists()) {
        inputProperties = new Properties();
        inputProperties.load(new FileReader(newInputPropertiesFile));
        for (Object key : inputProperties.keySet()) {
          logger.info("(Key, Value) = (\"" + key + "\", \""
              + inputProperties.getProperty(key.toString()) + "\")");
        }
        logger.info("input.properties has read.");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      System.exit(1);
    } catch (NullPointerException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      System.exit(1);
    }
  }

}
