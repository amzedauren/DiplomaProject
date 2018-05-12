package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.time.LocalDate;

public class Review implements Serializable {

  private Long id = null;
  private String name;
  private LocalDate date;
  private String type;

  public Review() {
    reset();
  }

  public Review(String name, LocalDate date, String type) {
    this.name = name;
    this.date = date;
    this.type = type;
  }

  public Review(Review other) {
    this(other.getName(), other.getDate(), other.getType());
    this.id = other.getId();
  }

  public void reset() {
    this.id = null;
    this.name = "";
    this.date = LocalDate.now();
    this.type = "";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "Review{" + "id=" + getId() + ", name="
      + getName() + ", date="
      + getDate() + ", type=" + getType() + '}';
  }

  public String getType() {
    return type;
  }
}
