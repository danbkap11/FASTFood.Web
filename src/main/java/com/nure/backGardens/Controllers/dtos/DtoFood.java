package com.nure.backGardens.Controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DtoFood {
  private   Double volume;
  private   String  name;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private java.util.Date Date;
  private  String tmpRejimName;

  public Double getVolume() {
    return volume;
  }

  public void setVolume(Double volume) {
    this.volume = volume;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDate() {
    return Date;
  }

  public void setDate(Date date) {
    Date = date;
  }

  public String getTmpRejimName() {
    return tmpRejimName;
  }

  public void setTmpRejimName(String tmpRejimName) {
    this.tmpRejimName = tmpRejimName;
  }
}
