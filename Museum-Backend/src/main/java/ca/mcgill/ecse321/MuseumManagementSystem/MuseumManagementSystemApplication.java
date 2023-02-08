package ca.mcgill.ecse321.MuseumManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OwnerRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.RoomRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

@SpringBootApplication
public class MuseumManagementSystemApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MuseumManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initObjects(@Autowired OwnerRepository ownerRepository, @Autowired RoomRepository roomRepository, @Autowired MuseumInformationRepository museumInformationRepository) {
		return args -> {
			if (ownerRepository.findAll().equals(List.of())) {
			Owner owner = new Owner();
			owner.setUsername("admin");
			owner.setPassword("admin2022");
			ownerRepository.save(owner);
			}

			if (museumInformationRepository.findAll().equals(List.of())) {
			MuseumInformation museumInformation = new MuseumInformation();
			museumInformation.setMondayHours("09:00-17:00");
			museumInformation.setTuesdayHours("09:00-17:00");
			museumInformation.setWednesdayHours("09:00-17:00");
			museumInformation.setThursdayHours("09:00-17:00");
			museumInformation.setFridayHours("09:00-17:00");
			museumInformation.setSaturdayHours("10:00-16:00");
			museumInformation.setSundayHours("10:00-16:00");
			museumInformation.setMuseumName("Redpath Museum");
			museumInformation.setVisitorFee(10.50);
			museumInformationRepository.save(museumInformation);
			}

			if (roomRepository.findByRoomType(RoomType.Storage).equals(List.of())) {
			List<Room> rooms = new ArrayList<Room>();
			Room storageRoom = new Room();
			storageRoom.setRoomType(RoomType.Storage);
			storageRoom.setRoomSize(RoomSize.Large);
			rooms.add(storageRoom);

			for(int i=0; i<10; i++) {
				Room displayRoom = new Room();
				displayRoom.setRoomType(RoomType.Display);
				displayRoom.setRoomSize(i<5?RoomSize.Large:RoomSize.Small);
				rooms.add(displayRoom);
			}
			roomRepository.saveAll(rooms);
		}

		};
	}

}
