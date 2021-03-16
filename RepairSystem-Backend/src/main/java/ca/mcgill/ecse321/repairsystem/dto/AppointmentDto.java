package ca.mcgill.ecse321.repairsystem.dto;

import java.util.List;
import ca.mcgill.ecse321.repairsystem.model.Appointment.AppointmentStatus;

public class AppointmentDto {
	private CustomerDto customer;
	private int id;
	private AppointmentStatus status;
	private String note;
	private TimeSlotDto timeslot;
	private List<MechanicDto> mechanics;
	private CarDto car;
	private List<ImageDto> images;
	private List<ServiceDto> services;
	
	public AppointmentDto(CustomerDto customer, int id, TimeSlotDto time, List<MechanicDto> mechanics, CarDto car, List<ImageDto> images, List<ServiceDto> services, String note, AppointmentStatus status) {
		this.customer = customer;
		this.id = id;
		this.status = status;
		this.note = note;
		this.timeslot = time;
		this.mechanics = mechanics;
		this.customer = customer;
		this.images = images;
		this.services = services;
		this.car = car;
	}
	
	public AppointmentDto(int Id) {
		this.id = Id;
	}
	
	public AppointmentDto() {
		
	}
	
	public CustomerDto getCustomer() {
		return customer;
	}
	
	public int getId() {
		return id;
	}
	
	public AppointmentStatus getStatus() {
		return status;
	}
	
	public String getNote() {
		return note;
	}

	public TimeSlotDto getTimeSlot() {
		return timeslot;
	}
	
	public List<MechanicDto> getMechanics() {
		return mechanics;
	}
	
	public void setMechanics(List<MechanicDto> mechanics)
	{
		this.mechanics = mechanics;
	}
	public CarDto getCar() {
		return car;
	}
	
	public void setCar(CarDto car)
	{
		this.car = car;
	}
	
	public List<ImageDto> getImages() {
		return images;
	}
	
	public void setImages(List<ImageDto> images)
	{
		this.images = images;
	}
	public List<ServiceDto> getServices() {
		return services;
	}
	
	public void setServices(List<ServiceDto> services)
	{
		this.services = services;
	}
}
