package org.yanzhe.inteliticket.bean;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class AirlineInfoBean {

  public static final AirlineInfoBean defaultInstance = new AirlineInfoBean();
  @NotNull
  private static ArrayList<String> companyCodesTable = new ArrayList<>();
  private double price;
  private long startTime;
  private long duration;
  private int company;

  public AirlineInfoBean() {
  }

  public AirlineInfoBean(double price, long startTime, long duration, String company) {
    this.price = price;
    this.startTime = startTime;
    this.duration = duration;
    this.company = getCompanyCode(company);
  }


  public boolean equals(@NotNull AirlineInfoBean obj) {
    return price == obj.getPrice()
        && startTime == obj.startTime
        && duration == obj.getStartTime()
        && duration == obj.getDuration()
        && company == getCompanyCode(obj.getCompany());
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public String getCompany() {
    return companyCodesTable.get(company);
  }

  public void setCompany(String company) {
    this.company = getCompanyCode(company);
  }

  protected int getCompanyCode(String name) {
    int code = companyCodesTable.indexOf(name);
    if (code == -1) {
      companyCodesTable.add(name);
      return companyCodesTable.size() - 1;
    } else {
      return code;
    }
  }

  @NotNull
  @Override
  public String toString() {
    return "AirlineInfoBean{"
        + "price="
        + price
        + ", startTime="
        + startTime
        + ", duration="
        + duration
        + ", company=\'"
        + companyCodesTable.get(company)
        + "\'"
        + '}';
  }
}
