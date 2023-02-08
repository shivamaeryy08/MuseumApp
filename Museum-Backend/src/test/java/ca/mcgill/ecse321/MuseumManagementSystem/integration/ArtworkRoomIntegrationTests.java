package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.ArtworkRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.RoomRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.ArtworkDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.RoomDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArtworkRoomIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ArtworkRepository artworkRepo;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LoanRequestRepository requestRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        requestRepository.deleteAll();
        artworkRepo.deleteAll();
    }

    /*-----------------------------------------------------------------Artwork Integration Tests-------------------------------------------------------------- */

    @Test
    public void testCreateAndGetArtwork() {
        Long id = testCreateArtwork();
        testGetArtwork(id);
    }

    private Long testCreateArtwork() {
        ArtworkDto artworkDto = new ArtworkDto(100.0, "mICHEAL", "jan", "cool artwork", "2001", false, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg", null);
        ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork", artworkDto, ArtworkDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals("mICHEAL", response.getBody().getName(), "Response has incorrect name");
        assertTrue(response.getBody().getArtworkID() > 0, "Response has invalid ID");
        return response.getBody().getArtworkID();
    }

    /**
     * @param id
     */

    private void testGetArtwork(Long id) {
        ResponseEntity<ArtworkDto> response = client.getForEntity("/artwork/" + id, ArtworkDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals("mICHEAL", response.getBody().getName(), "Response has incorrect ArtworkType");
        assertEquals(id, response.getBody().getArtworkID(), "Response has incorrect ID");
    }

    @Test
    public void testCreateInvalidArtwork() {
        ResponseEntity<String> response = client.postForEntity("/artwork",
                new ArtworkDto(-1, null, null, null, null, false, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg", null), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
    }

    @Test
    public void testGetInvalidArtwork() {
        ResponseEntity<String> response = client.getForEntity("/artwork/" + Long.MAX_VALUE, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
        assertEquals("No artwork exists with the given artworkID", response.getBody(),
                "Response has incorrect error message");
    }

    @Test
    public void testCreateAndAddArtworkToRoom() {
        Room room2 = roomRepository.findByRoomType(RoomType.Display).get(0);
        RoomDto room2Dto = new RoomDto(room2.getRoomId(), room2.getRoomSize(), room2.getRoomType());

        Long artworkID = testCreateArtwork();

        ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork/" + artworkID + "/room/add", room2Dto,
                ArtworkDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(room2.getRoomId(), response.getBody().getRoom().getRoomId(), "Response has incorrect roomID");
    }

    @Test
    public void testCreateAndAddArtworkToInvalidRoom() {
        RoomDto room2Dto = new RoomDto(null, null, null);
        Long artworkID = testCreateArtwork();

        ResponseEntity<String> response = client.postForEntity("/artwork/" + artworkID + "/room/add", room2Dto,
                String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals("No room exists with the given roomID", response.getBody(),
                "Response has incorrect error message");
    }

    @Test
    public void testGetAllArtworks() {
        Long artworkID = testCreateArtwork();
        ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks", ArtworkDto[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(artworkID, response.getBody()[0].getArtworkID(), "Response has incorrect ArtworkID");

    }

    @Test
    public void testGetAllArtworksInDisplay() {
        Room room = new Room();
        room.setRoomType(RoomType.Display);
        roomRepository.save(room);
        Artwork artwork = new Artwork();
        artwork.setRoom(room);
        artworkRepo.save(artwork);
        
        ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artwork/getDisplay", ArtworkDto[].class);
       assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(1, response.getBody().length, "Response has incorrect ArtworkID");

    }


    @Test
    public void testCreateAndUpdateArtwork() {
        Long artworkID = testCreateArtwork();
        ArtworkDto UpdateDTO = new ArtworkDto(120.0, "naruto", "onepiece", "description", "2001", true, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg",null);
        ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork/" + artworkID + "/update", UpdateDTO,
                ArtworkDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals("naruto", response.getBody().getName(), "Name of artwork updated incorrectly");
        assertEquals("onepiece", response.getBody().getArtist(), "Artist of artwork updated incorrectly");
        assertEquals("description", response.getBody().getDescription(), "Description of artwork updated incorrectly");
        assertEquals("2001", response.getBody().getYear(), "Year of artwork updated incorrectly");
        assertEquals(true, response.getBody().getLoanable(), "Loanable status of artwork updated incorrectly");

    }

    @Test
    public void testCreateAndUpdateInvalidArtwork() {
        Long artworkID = Long.MAX_VALUE;
        ArtworkDto UpdateDTO = new ArtworkDto(120.0, "naruto", "onepiece", "description", "year", true, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg", null);
        ResponseEntity<String> response = client.postForEntity("/artwork/" + artworkID + "/update", UpdateDTO,
                String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals("No artwork exists with the given artworkID", response.getBody(),
                "Response has incorrect error message");

    }

    @Test
    public void testGetAllArtworksInRoom() {
        Room room = roomRepository.findByRoomType(RoomType.Storage).get(0);
        Long artworkID = testCreateArtwork();
        Artwork artwork = artworkRepo.findArtworkByArtworkID(artworkID);
        artwork.setRoom(room);
        artworkRepo.save(artwork);
        Long roomID = room.getRoomId();
        ResponseEntity<ArtworkDto[]> response = client.getForEntity("/room/" + roomID + "/artworks",
                ArtworkDto[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(artworkID, response.getBody()[0].getArtworkID(), "Response has incorrect ArtworkID");
        assertEquals(1, response.getBody().length, "Response has length that is not equal to 1");
    }

    @Test
    public void testCreateAndDeleteArtwork() {
        Long artworkID = testCreateArtwork();
        client.delete("/artwork/" + artworkID + "/");
        assertNull(artworkRepo.findArtworkByArtworkID(artworkID));
    }

    @Test
    public void testCreateAndDeleteNullArtwork() {
            ResponseEntity<Void> resp = client.exchange("/artwork/" + Integer.MAX_VALUE + "/", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
            assertEquals(resp.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /*-----------------------------------------------------------------Room Integration Tests-------------------------------------------------------------- */
    @Test
    public void testGetAllRooms() {
        Long roomID = roomRepository.findByRoomType(RoomType.Storage).get(0).getRoomId();
        ResponseEntity<RoomDto[]> response = client.getForEntity("/rooms", RoomDto[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(roomID, response.getBody()[0].getRoomId(), "Response has incorrect RoomID");
    }

    @Test
    public void testGetCapacity() {

        Long artworkID = testCreateArtwork();
        Artwork artwork = artworkRepo.findArtworkByArtworkID(artworkID);
        Long roomID = roomRepository.findByRoomType(RoomType.Storage).get(0).getRoomId();
        artwork.setRoom(roomRepository.findRoomByRoomId(roomID));
        artworkRepo.save(artwork);
        ResponseEntity<Integer> response = client.getForEntity("/room/" + roomID + "/capacity", Integer.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(1, response.getBody(), "Response has incorrect capacity");

    }

    @Test
    public void testGetCapacityInvalid() {

        Long artworkID = testCreateArtwork();
        Artwork artwork = artworkRepo.findArtworkByArtworkID(artworkID);
        Long roomID = roomRepository.findByRoomType(RoomType.Storage).get(0).getRoomId();
        artwork.setRoom(roomRepository.findRoomByRoomId(roomID));
        artworkRepo.save(artwork);
        ResponseEntity<String> response = client.getForEntity("/room/" + Integer.MAX_VALUE + "/capacity", String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
    }

    @Test
    public void testRemoveArtworkFromRoom() {
        Long artworkID = testCreateArtwork();
        Artwork artwork = artworkRepo.findArtworkByArtworkID(artworkID);
        Long roomID = roomRepository.findByRoomType(RoomType.Storage).get(0).getRoomId();
        Room room = roomRepository.findRoomByRoomId(roomID);
        artwork.setRoom(room);
        artworkRepo.save(artwork);
        RoomDto roomDto = new RoomDto(room.getRoomId(), room.getRoomSize(), room.getRoomType());
        ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(),
                artwork.getArtist(), artwork.getDescription(), artwork.getYear(), false, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg", roomDto);
        ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork/room/remove", artworkDto,
                ArtworkDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(artworkID, response.getBody().getArtworkID(), "Artwork was not removed from room");
    }

    @Test
    public void testRemoveArtworkFromRoomInvalid() {
        Long artworkID = testCreateArtwork();
        Artwork artwork = artworkRepo.findArtworkByArtworkID(artworkID);
        Long roomID = roomRepository.findByRoomType(RoomType.Storage).get(0).getRoomId();
        Room room = roomRepository.findRoomByRoomId(roomID);
        artwork.setRoom(room);
        artwork.setArtworkID((long) Integer.MAX_VALUE);
        artworkRepo.save(artwork);
        RoomDto roomDto = new RoomDto(room.getRoomId(), room.getRoomSize(), room.getRoomType());
        ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(),
                artwork.getArtist(), artwork.getDescription(), artwork.getYear(), false, "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg", roomDto);
        ResponseEntity<String> response = client.postForEntity("/artwork/room/remove", artworkDto,
                String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
    }

    @Test
    public void testGetRoom() {
        Room room = roomRepository.findByRoomType(RoomType.Storage).get(0);
        ResponseEntity<RoomDto> response = client.getForEntity("/room/" + room.getRoomId(), RoomDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has incorrect status");
        assertNotNull(response.getBody(), "Response has no body");
        assertEquals(RoomType.Storage, response.getBody().getRoomType(), "Response has incorrect roomType");
        assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Response has incorrect ID");
    }

    @Test
    public void testGetInvalidRoom() {
        ResponseEntity<String> response = client.getForEntity("/room/" + Integer.MAX_VALUE, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has incorrect status");
    }

}