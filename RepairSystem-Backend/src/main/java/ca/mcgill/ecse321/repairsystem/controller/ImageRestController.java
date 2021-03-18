package ca.mcgill.ecse321.repairsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.repairsystem.dto.*;
import ca.mcgill.ecse321.repairsystem.model.*;
import ca.mcgill.ecse321.repairsystem.service.*;

@CrossOrigin(origins = "*")
@RestController
public class ImageRestController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private ImageService imageService;
	
	@GetMapping(value = { "/image/{url}", "/image/{url}/"})
	public ImageDto getImageByUrl(String url) {
		return Converter.convertToDto(imageService.getImageByUrl(url));
	}

	@PostMapping(value = { "/image/{url}", "/Mechanics/{url}/" })
	public ImageDto createImage(@PathVariable("url") String url, @RequestParam String appointmentId) throws IllegalArgumentException {
		Appointment appointment = appointmentService.getAppointmentById(Integer.parseInt(appointmentId));
		Image image = imageService.createImage(url, appointment);
		return Converter.convertToDto(image);
	}

}


