package ca.mcgill.ecse321.repairsystem.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.repairsystem.model.*;
import ca.mcgill.ecse321.repairsystem.dao.AppointmentRepository;
import ca.mcgill.ecse321.repairsystem.dao.CarRepository;
import ca.mcgill.ecse321.repairsystem.dao.CustomerRepository;


/**
 * Service methods to handle login and registration of customers
 *
 */
@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private CarRepository carRepository;

	////////////////////SERVICE CUSTOMER METHODS //////////////////// 

	/**
	 * Creates a customer and saves new customer object in the database
	 * @param aName
	 * @param aPassword
	 * @param aPhone
	 * @param aEmail
	 * @param credit
	 * @param debit
	 * @param add
	 * @return
	 */
	@Transactional
	public Customer createCustomer(String aName, String aPassword, int aPhone, String aEmail, String credit, String debit, String add) {
		
		if(aName == null || aName.trim().length() == 0)
		{
			throw new IllegalArgumentException("Customer name cannot be empty!");
				
		}else if (aPassword == null || aPassword.trim().length() == 0)
		{
			throw new IllegalArgumentException("Customer password cannot be empty!");
		}else if(credit == null || credit.trim().length()==0) {
			throw new IllegalArgumentException("Customer credit card number cannot be empty!");
		}else if(debit == null || debit.trim().length()==0) {
			throw new IllegalArgumentException("Customer debit card number cannot be empty!");
		}else if (aEmail == null || aEmail.trim().length() == 0)
		{
			throw new IllegalArgumentException("Customer email cannot be empty!");
		}else if (add == null || add.trim().length() == 0)
		{
			throw new IllegalArgumentException("Customer address cannot be empty!");
		}else if(customerRepository.findByEmail(aEmail) != null)
		{
			throw new IllegalArgumentException("Email is already taken!");
		}else if(customerRepository.findByPhone(aPhone) != null)
		{
			throw new IllegalArgumentException("Phone number is already taken!");
		}
		else if (customerRepository.findByName(aName) != null && customerRepository.findByAddress(add) != null && customerRepository.findByEmail(aEmail) != null ) {
			throw new IllegalArgumentException("a user with the same credentials already exists!");
		}
		int id = aEmail.hashCode() ;
		Customer customer = new Customer(aName, id, aPassword, aPhone, aEmail,credit, debit, add);
		customerRepository.save(customer);
		return customer;
	}

	@Transactional 
	public Customer getCustomerById(int id) 
	{
		Customer customer = customerRepository.findById(id);
		return customer;
	}

	@Transactional 
	public List<Customer> getCustomersByName(String name) {
		List<Customer> customers = toList(customerRepository.findByName(name));
		return customers;
	}

	@Transactional 
	public Customer getCustomerByNumber(int number) {
		Customer customer = customerRepository.findByPhone(number);
		return customer;
	}

	@Transactional 
	public Customer getCustomerByEmail(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer;
	}

	@Transactional 
	public List<Customer> getCustomersByAddress(String address) {
		List<Customer> customers = toList(customerRepository.findByAddress(address));
		return customers;
	}

	@Transactional
	public List<Customer> getAllCustomers() {
		return toList(customerRepository.findAll());
	}
	
   public void updateAllCredentials(Customer customer, String newEmail, String newPassword, String newPhone, String newCredit, String newDebit, String newAddress )
   {
	   updateCustomerEmail(customer, newEmail);
	   resetPassword(customer, newPassword);
	   updatePhoneNumber(customer, newPhone);
	   updateCredit(customer, newCredit);
	   updateDebit(customer, newDebit);
	   updateAddress(customer, newAddress);
   }
	/**
	 * Updates the address of the customer
	 * @param customerId
	 * @param newAddress
	 */
	public void updateAddress(Customer customer, String newAddress)
	{
		
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(newAddress== null || newAddress.trim().length() == 0)
		{
			throw new IllegalArgumentException("New address cannot be null or empty");
		}
		if(newAddress.matches(customer.getAddress()))
		{
			throw new IllegalArgumentException("New  address cannot be the same as the old address");
		}
		
		customer.setAddress(newAddress);
		customerRepository.save(customer);
	}
	/**
	 * Updates the debit card information of the customer
	 * @param customerId
	 * @param newDebit
	 */
	public void updateDebit(Customer customer, String newDebit)
	{
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(newDebit== null || newDebit.trim().length() == 0)
		{
			throw new IllegalArgumentException("New debit card number cannot be null or empty");
		}
		if(newDebit.matches(customer.getDebitHash()))
		{
			throw new IllegalArgumentException("New debit card number cannot be the same as the old debit card number");
		}
		
		customer.setDebitHash(newDebit);
		customerRepository.save(customer);
	}
	
	/**
	 * Removes the debit card information from the customer
	 * @param customerId
	 * @param debit
	 */
	public void removeDebit(Customer customer, String debit)
	{
		
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(debit== null || debit.trim().length() == 0)
		{
			throw new IllegalArgumentException("debit card number cannot be null or empty");
		}
		customer.setDebitHash(null);
		customerRepository.save(customer);
		
	}
	/**
	 * Updates the credit card information of the customer
	 * @param customerId
	 * @param newCredit
	 */
	public void updateCredit(Customer customer, String newCredit)
	{
		
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(newCredit== null || newCredit.trim().length() == 0)
		{
			throw new IllegalArgumentException("New credit card number cannot be null or empty");
		}
		if(newCredit.matches(customer.getCreditHash()))
		{
			throw new IllegalArgumentException("New credit card number cannot be the same as the old credit card number");
		}
		
		customer.setCreditHash(newCredit);
		customerRepository.save(customer);
	}
	
	/**
	 * Removes the credit card information from the customer
	 * @param customerId
	 * @param credit
	 */
	public void removeCredit(Customer customer, String credit)
	{
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(credit== null || credit.trim().length() == 0)
		{
			throw new IllegalArgumentException("debit card number cannot be null or empty");
		}
		customer.setCreditHash(null);
		customerRepository.save(customer);
		
	}
	/**
	 * Update the customer's phone number
	 * @param customerId
	 * @param newPhoneNumber
	 */
	public void updatePhoneNumber(Customer customer, String newPhoneNumber)
	{
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(newPhoneNumber == null || newPhoneNumber.trim().length() == 0)
		{
			throw new IllegalArgumentException("New phone number cannot be null or empty");
		}
		if(newPhoneNumber.matches(String.valueOf(customer.getPhone())))
		{
			throw new IllegalArgumentException("New phone number cannot be the same as the old one");
		};
		
		customer.setPhone(Integer.parseInt(newPhoneNumber));
		customerRepository.save(customer);
	}
	/**
	 * Updates the customer email
	 * @param customerId
	 * @param newEmail
	 */
	public void updateCustomerEmail(Customer customer, String newEmail)
	{
		if(newEmail == null || newEmail.trim().length() == 0)
		{
			throw new IllegalArgumentException("email cannot be empty or null");
		}
		
		if(customer == null)
		{
			throw new IllegalArgumentException("Customer cannot be null");
		}
		if(newEmail.matches(customer.getEmail()))
		{
			throw new IllegalArgumentException("New email cannot be the same as the old one");
		}
		
		customer.setEmail(newEmail);
		customerRepository.save(customer);
	}
	
	/**
	 * Edit Profile : Resetting or modifying the password
	 * @param customerId
	 * @param newPassword
	 * @throws NullPointerException
	 */
	public void resetPassword(Customer customer, String newPassword)
	{
		
		if(customer == null )
		{
			throw new NullPointerException("Customer does not exist!");
		}
		
		if(newPassword.matches(customer.getPassword()))
		{
			throw new IllegalArgumentException("new password cannot be the same as the old one");
		}
		
		customer.setPassword(newPassword);
		customerRepository.save(customer);
		
	}
	/**
	 * Deletes the customer object as well as its attributes and associations
	 * @param customerId
	 * @return true once everything is deleted
	 */
	public boolean deleteCustomer(int customerId)
	{
		Customer customer = customerRepository.findById(customerId);
		if(customer == null)
		{
			throw new NullPointerException("Customer does not exist!");
		}
	
		List<Appointment> appointments = appointmentRepository.findByCustomer(customer);
		for(Appointment a : appointments)
		{
			a.setCustomer(null);
			appointmentRepository.delete(a);
		}
		List<Car> cars = carRepository.findByCustomer(customer);
		for(Car c : cars)
		{
			c.setCustomer(null);
			carRepository.delete(c);
		}
		
		customerRepository.deleteById(String.valueOf(customerId));
		return true;
	}

	/**
	 * 	Edit the cars associated to a customer 
	 * @param customerId
	 * @param carId
	 * @param addRemove
	 */
	public void editCars(String customerId, String carId, String addRemove)
	{
		if(customerId == null || getCustomerById(Integer.parseInt(customerId)) == null)
		{
			throw new NullPointerException("Customer does not exist!");
		}
		
		Car car = carRepository.findById(Integer.parseInt(carId));
		if(carId == null || car == null)
		{
			throw new NullPointerException("Car does not exist");
		}		
		Customer customer = getCustomerById(Integer.parseInt(customerId));
		if(addRemove.matches("add"))
		{
			customer.addCar(car);
			car.setCustomer(customer);
			customerRepository.save(customer);
			carRepository.save(car);
		}else 
		{
			customer.removeCar(car);
			car.setCustomer(null);
			carRepository.delete(car);
			customerRepository.save(customer);
		}
	}
	
	/**
	 * Add or remove the appointments for a customer
	 * @param customerId
	 * @param appointmentId
	 * @param addRemove
	 */
	public void editAppointments(String customerId, String appointmentId, String addRemove)
	{

		if(customerId == null || getCustomerById(Integer.parseInt(customerId)) == null)
		{
			throw new NullPointerException("Customer does not exist!");
		}
		
		Appointment appointment = appointmentRepository.findById(Integer.parseInt(appointmentId));
		if(appointment == null)
		{
			throw new NullPointerException("Car does not exist");
		}		
		Customer customer = getCustomerById(Integer.parseInt(customerId));
		if(addRemove.matches("add"))
		{
			customer.addAppointment( appointment);
			appointment.setCustomer(customer);
			customerRepository.save(customer);
			appointmentRepository.save(appointment);
		}
		
		/*else 
		{
			customer.removeAppointment(appointment);
			appointment.setCustomer(null);
			appointmentRepository.delete(appointment);
			customerRepository.save(customer);
		}*/
	}
	/* 
	 * helper method
	 */
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}