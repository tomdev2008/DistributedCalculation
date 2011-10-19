package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common;

import java.io.Serializable;
import java.util.List;

public final class ClientProfile implements Serializable {

  private static final long serialVersionUID = 1925099750325618623L;

  private String hostName;

  private List<String> hardwareAddrList;

  private String inetAddr;

  private long loadCapability;

  public ClientProfile(String hostName, List<String> hardwareAddrList,
      String inetAddr) {
    super();
    this.hostName = hostName;
    this.hardwareAddrList = hardwareAddrList;
    this.inetAddr = inetAddr;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getInetAddr() {
    return inetAddr;
  }

  public void setInetAddr(String inetAddr) {
    this.inetAddr = inetAddr;
  }

  public long getLoadCapability() {
    return loadCapability;
  }

  public void setLoadCapability(long loadCapability) {
    this.loadCapability = loadCapability;
  }

  public void incrLoadCapability() {
    ++this.loadCapability;
  }

  public void incrLoadCapability(long value) {
    this.loadCapability += value;
  }

  public void decrLoadCapability() {
    --this.loadCapability;
  }

  public void decrLoadCapability(long value) {
    this.loadCapability -= value;
  }

  public List<String> getHardwareAddrList() {
    return hardwareAddrList;
  }

  public void setHardwareAddrList(List<String> hardwareAddrList) {
    this.hardwareAddrList = hardwareAddrList;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    for (String hardwareAddr : hardwareAddrList) {
      result = prime * result
          + ((hardwareAddr == null) ? 0 : hardwareAddr.hashCode());
    }
    result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
    result = prime * result + ((inetAddr == null) ? 0 : inetAddr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object otherClientObj) {
    if (this == otherClientObj)
      return true;
    if (otherClientObj == null)
      return false;
    if (getClass() != otherClientObj.getClass())
      return false;

    ClientProfile otherClient = (ClientProfile) otherClientObj;

    if (hostName.compareToIgnoreCase(otherClient.hostName) != 0)
      return false;

    if (inetAddr.compareToIgnoreCase(otherClient.inetAddr) != 0)
      return false;

    for (String hardwareAddr : hardwareAddrList) {
      for (String otherClientHardwareAddr : otherClient.hardwareAddrList) {
        if (hardwareAddr.compareToIgnoreCase(otherClientHardwareAddr) == 0) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "ClientProfile [hostName=" + hostName + ", hardwareAddrList="
        + hardwareAddrList + ", inetAddr=" + inetAddr + ", loadCapability="
        + loadCapability + "]";
  }

}
