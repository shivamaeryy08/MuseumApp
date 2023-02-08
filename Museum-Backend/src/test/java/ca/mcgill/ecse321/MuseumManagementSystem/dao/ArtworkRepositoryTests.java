package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

@SpringBootTest
public class ArtworkRepositoryTests {
    
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private ArtworkRepository artworkRepository;
	@Autowired
	private LoanRequestRepository requestRepository;

	@AfterEach
	public void clearDatabase() {
		requestRepository.deleteAll();
		artworkRepository.deleteAll();
		roomRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadArtwork() {

        //Association Test Object
        RoomType roomType = RoomType.Display;
        RoomSize roomSize = RoomSize.Large;
		Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomSize(roomSize);

	    room = roomRepository.save(room);

	    //Test object
	    double loanPrice = 500.1;
        boolean loanable = false;
        String name = "art";
        String artist = "picasso";
        String description = "paint";
        String year = "2000";
		Artwork artwork = new Artwork();

		artwork.setArtist(artist);
        artwork.setDescription(description);
        artwork.setLoanPrice(loanPrice);
        artwork.setLoanable(loanable);
        artwork.setName(name);
        artwork.setYear(year);
		artwork.setRoom(room);

		artworkRepository.save(artwork);

		long id = artwork.getArtworkID();
		
		artwork = null;
		artwork = artworkRepository.findArtworkByArtworkID(id);

        assertNotNull(artwork);
		assertEquals(year, artwork.getYear());

		assertNotNull(artwork.getRoom());
		assertEquals(RoomSize.Large, artwork.getRoom().getRoomSize());
    }

	/*
	 * Testing crud method FindArtworkByRoom
	 * @author Shivam Aery
	 */

	@Test
	public void testFindArtworkByRoom () {
		Room room = new Room();
		room.setRoomSize(RoomSize.Large);
		room.setRoomType(RoomType.Display);
		room = roomRepository.save(room);

		double loanPrice = 500.1;
        boolean loanable = false;
        String name = "art";
        String artist = "picasso";
        String description = "paint";
        String year = "2000";
		double loanPrice2 = 200.20;
        boolean loanable2 = false;
        String name2 = "artwork of century";
        String artist2 = "leonardo";
        String description2 = "mona";
        String year2 = "1920";
		Artwork artwork = new Artwork();
		artwork.setArtist(artist);
        artwork.setDescription(description);
        artwork.setLoanPrice(loanPrice);
        artwork.setLoanable(loanable);
        artwork.setName(name);
        artwork.setYear(year);
		artwork.setRoom(room);
		artwork = artworkRepository.save(artwork);

		Artwork artwork2 = new Artwork();

		artwork2.setArtist(artist2);
        artwork2.setDescription(description2);
        artwork2.setLoanPrice(loanPrice2);
        artwork2.setLoanable(loanable2);
        artwork2.setName(name2);
        artwork2.setYear(year2);
		artwork2.setRoom(room);
		artwork2 = artworkRepository.save(artwork2);
		assertNotNull(artworkRepository.findArtworkByRoom(room));
		assertEquals(room.getRoomId(), artworkRepository.findArtworkByRoom(room).get(0).getRoom().getRoomId());
		assertEquals(room.getRoomId(), artworkRepository.findArtworkByRoom(room).get(1).getRoom().getRoomId());
	}
}
