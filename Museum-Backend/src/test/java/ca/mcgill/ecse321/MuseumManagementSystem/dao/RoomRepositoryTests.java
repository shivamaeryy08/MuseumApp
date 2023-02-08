package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

@SpringBootTest
public class RoomRepositoryTests {
        
	@Autowired
	private RoomRepository roomRepository;

	@AfterEach
	public void clearDatabase() {
		roomRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadRoom() {

        //Association Test Object
        RoomType roomType = RoomType.Display;
        RoomSize roomSize = RoomSize.Large;
		Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomSize(roomSize);

	    room = roomRepository.save(room);

		long id = room.getRoomId();
		
		room = null;
		room = roomRepository.findRoomByRoomId(id);

        assertNotNull(room);
		assertEquals(RoomSize.Large, room.getRoomSize());
    }
}
