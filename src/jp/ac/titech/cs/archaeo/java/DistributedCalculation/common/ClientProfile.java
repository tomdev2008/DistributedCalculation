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
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClientProfile other = (ClientProfile) obj;
    if (hardwareAddrList == null) {
      if (other.hardwareAddrList != null)
        return false;
    } else if (!hardwareAddrList.equals(other.hardwareAddrList))
      return false;
    if (hostName == null) {
      if (other.hostName != null)
        return false;
    } else if (!hostName.equals(other.hostName))
      return false;
    if (inetAddr == null) {
      if (other.inetAddr != null)
        return false;
    } else if (!inetAddr.equals(other.inetAddr))
      return false;
    return true;
  }

}
