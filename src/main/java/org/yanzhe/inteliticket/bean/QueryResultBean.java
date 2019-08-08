package org.yanzhe.inteliticket.bean;

import java.util.ArrayList;
import java.util.List;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;

public class QueryResultBean {

  private int transNum;
  private String src;
  private String dst;
  private long departTime;
  private long arriveTime;
  private long duration;
  private double price;
  private List<DirectWeightedEdge<AirlineInfoBean>> details;

  public QueryResultBean() {
  }

  public QueryResultBean(
      int transNum,
      String src,
      String dst,
      long departTime,
      long arriveTime,
      long duration,
      double price,
      List<DirectWeightedEdge<AirlineInfoBean>> details) {
    this.transNum = transNum;
    this.src = src;
    this.dst = dst;
    this.departTime = departTime;
    this.arriveTime = arriveTime;
    this.duration = duration;
    this.price = price;
    this.details = details;
  }

  public int getTransNum() {
    return transNum;
  }

  public void setTransNum(int transNum) {
    this.transNum = transNum;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public String getDst() {
    return dst;
  }

  public void setDst(String dst) {
    this.dst = dst;
  }

  public long getDepartTime() {
    return departTime;
  }

  public void setDepartTime(long departTime) {
    this.departTime = departTime;
  }

  public long getArriveTime() {
    return arriveTime;
  }

  public void setArriveTime(long arriveTime) {
    this.arriveTime = arriveTime;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public List<DirectWeightedEdge<AirlineInfoBean>> getDetails() {
    return details;
  }

  public void setDetails(ArrayList<DirectWeightedEdge<AirlineInfoBean>> details) {
    this.details = details;
  }
}
