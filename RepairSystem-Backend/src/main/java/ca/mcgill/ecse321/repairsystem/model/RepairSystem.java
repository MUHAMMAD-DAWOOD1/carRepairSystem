package ca.mcgill.ecse321.repairsystem.model;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RepairSystem
{


  private String shopName;
  @Id
  public String getShopName()
  {
    return this.shopName;
  }
  
  public void setShopName(String aShopName)
  {
    this.shopName = aShopName;
  }

  private String address;
  
  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String aAddress)
  {
    this.address = aAddress;
  }
  
  private int spaceAvailable;
  
  public int getSpaceAvailable()
  {
    return this.spaceAvailable;
  }

  public void setSpaceAvailable(int aSpaceAvailable)
  {
    this.spaceAvailable=aSpaceAvailable;
  }
  
  private int numOfEmployees;
  
  public int getNumOfEmployees()
  {
    return this.numOfEmployees;
  }
  
  public void setNumOfEmployees(int aNumOfEmployees)
  {
    this.numOfEmployees=aNumOfEmployees;
  }

  //RepairSystem Associations
  
  private List<User> user;
  @OneToMany(cascade={CascadeType.ALL})
  public List<User> getUsers()
  {
    return this.user;
  }

 public void setUsers(List<User> users) {
	  this.user = users;
  }

  private List<Service> service;
  
  @OneToMany(cascade={CascadeType.ALL})
  public List<Service> getServices()
  {
    return this.service;
  }
  
  public void setServices(List<Service> services) {
	  this.service = services;
  }


  private List<Appointment> appointment;
  
  @OneToMany(cascade={CascadeType.ALL})
  public List<Appointment> getAppointments()
  {
    return this.appointment;
  }
  
  public void setAppointments(List<Appointment> appointments) {
	  this.appointment=appointments;
  }
  
  
  private List<TimeSlot> timeSlot;
  @OneToMany(targetEntity=TimeSlot.class, mappedBy = "repairSystem", cascade={CascadeType.ALL})
  public List<TimeSlot> getTimeSlots()
  {
    return timeSlot;
  }

  public void setTimeSlots(List<TimeSlot> timeSlots) {
	  this.timeSlot=timeSlots;
  }

  @Override
  public String toString()
  {
    return super.toString() + "["+
            "shopName" + ":" + getShopName()+ "," +
            "address" + ":" + getAddress()+ "," +
            "spaceAvailable" + ":" + getSpaceAvailable()+ "," +
            "numOfEmployees" + ":" + getNumOfEmployees()+ "]";
  }
}
