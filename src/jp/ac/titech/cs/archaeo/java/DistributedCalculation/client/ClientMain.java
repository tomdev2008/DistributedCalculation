package jp.ac.titech.cs.archaeo.java.DistributedCalculation.client;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.client.support.ClientApp;

public final class ClientMain implements Runnable {

  private ClientApp client;

  /**
   * @param args
   */
  public static void main(String[] args) {
    ClientMain clientMain = new ClientMain(args);
    Runtime.getRuntime().addShutdownHook(new Thread(clientMain));
  }

  public ClientMain(String[] args) {
    client = new ClientApp(args);
  }

  @Override
  public void run() {
    client.shutdown();
  }

}
