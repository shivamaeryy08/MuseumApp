package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.ArtworkRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.RoomRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

@ExtendWith(MockitoExtension.class)
public class TestArtworkRoomService {
    @Mock
    private ArtworkRepository artworkDao;

    @Mock
    private RoomRepository roomDao;

    @Mock
    private LoanRequestRepository loanRequestDao;

    @InjectMocks
    private ArtworkRoomService service;

    private static final Long NON_ARTWORK_ID = -223L;
    private static final Long ARTWORK_ID = 123L;
    private static final double LOAN_PRICE = 100.00;
    private static final String NAME = "Mona Lisa";
    private static final String ARTIST = "Leonardo Da Vinci";
    private static final String DESCRIPTION = "Very famous artwork";
    private static final String imgUrl = "https://cdn.pixabay.com/photo/2021/09/28/13/14/cat-6664412_1280.jpg";
    private static final String YEAR = "2022";
    private static final Long ROOM_ID = 100L;
    private static final Long NON_ROOM_ID = 120L;
    private static final RoomType ROOM_TYPE = RoomType.Display;
    private static final RoomSize ROOM_Size = RoomSize.Large;
    private static final Artwork ARTWORK_WITH_LOAN_REQUESTS = new Artwork();
    private static final String ARTWORK_ON_LOAN = "naruto";
    private static final Room ROOM_WITH_ARTWORKS_IN_IT = new Room(RoomType.Display);

    // Mock Output Setup

    @BeforeEach
    public void setMockOutput() {
        // When findArtworkByArtworkID is called, a random artwork object will be
        // returned unless the
        // id does not match the defined constant above (ARTWORK_ID)

        lenient().when(artworkDao.findArtworkByArtworkID(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ARTWORK_ID)) {
                Artwork artwork = new Artwork();
                artwork.setArtworkID(ARTWORK_ID);
                artwork.setArtist(ARTIST);
                artwork.setDescription(DESCRIPTION);
                artwork.setLoanPrice(LOAN_PRICE);
                artwork.setName(NAME);
                artwork.setYear(YEAR);
                return artwork;
            }

            else {
                return null;
            }

        });

        // When findRoomByRoomId is called, a random room object will be returned unless
        // the
        // id does not match the defined constant above (ROOM_ID)

        lenient().when(roomDao.findRoomByRoomId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ROOM_ID)) {
                Room room = new Room();
                room.setRoomId(ROOM_ID);
                room.setRoomType(ROOM_TYPE);
                room.setRoomSize(ROOM_Size);
                return room;
            }

            else {
                return null;
            }

        });

        lenient().when(roomDao.findByRoomType(any(RoomType.class))).thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(ROOM_TYPE)) {
            List<Room> displayRooms = new ArrayList<Room>();    
            ROOM_WITH_ARTWORKS_IN_IT.setRoomType(RoomType.Display);
            displayRooms.add(ROOM_WITH_ARTWORKS_IN_IT);
                return displayRooms;
          }
          else {
            return null;
          }

            

        });

        // When findArtworkbyRoom is called, a list of one artwork will be returned
        // Unless the room ROOM_WITH_ARTWORKS_IN_IT is not specified as argument, in
        // which case null is returned

        lenient().when(artworkDao.findArtworkByRoom(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ROOM_WITH_ARTWORKS_IN_IT)) {
                List<Artwork> artworkList = new ArrayList<Artwork>();

                Artwork artwork = new Artwork();
                artwork.setRoom(ROOM_WITH_ARTWORKS_IN_IT);
                artwork.setArtworkID(ARTWORK_ID);
                artwork.setArtist(ARTIST);
                artwork.setDescription(DESCRIPTION);
                artwork.setLoanPrice(LOAN_PRICE);
                artwork.setName(NAME);
                artwork.setYear(YEAR);
                artworkList.add(artwork);
                return artworkList;
            } else {
                return List.of();
            }
        }

        );

        // When findLoanRequestByArtwork is called, a list of one loan request will be
        // returned
        // Unless the artwork ARTWORK_WITH_LOAN_REQUESTS is not specified as argument,
        // in which case null is returned

        lenient().when(loanRequestDao.findLoanRequestByArtwork(any(Artwork.class)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(ARTWORK_WITH_LOAN_REQUESTS)) {
                        List<LoanRequest> loanRequestList = new ArrayList<LoanRequest>();
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setArtwork(ARTWORK_WITH_LOAN_REQUESTS);
                        loanRequestList.add(loanRequest);
                        return loanRequestList;
                    } else {
                        return null;
                    }
                }

                );
        // When
        // findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual
        // is called
        // A list of one loan request is returned if the argument is ARTWORK_ON_LOAN,
        // else the method returns null

        lenient()
                .when(loanRequestDao
                        .findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(
                                any(LoanRequest.Status.class), any(Artwork.class),
                                any(Date.class), any(Date.class)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    Object objectArtwork = invocation.getArgument(1);
                    Artwork artwork = (Artwork) objectArtwork;

                    if (artwork.getName().equals(ARTWORK_ON_LOAN)) {
                        List<LoanRequest> loanRequestList = new ArrayList<LoanRequest>();
                        LoanRequest loanRequest = new LoanRequest();
                        loanRequest.setArtwork(artwork);
                        loanRequestList.add(loanRequest);
                        return loanRequestList;
                    } else {
                        return List.of();

                    }

                }

                );

        // When findAll is called, list of one artwork is returned

        lenient().when(artworkDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Artwork> artworkList = new ArrayList<Artwork>();
            Artwork artwork = new Artwork();
            artwork.setArtworkID(ARTWORK_ID);
            artwork.setArtist(ARTIST);
            artwork.setDescription(DESCRIPTION);
            artwork.setLoanPrice(LOAN_PRICE);
            artwork.setName(NAME);
            artwork.setYear(YEAR);
            artworkList.add(artwork);
            return artworkList;

        }

        );

        // When findAll is called, list of one room is returned

        lenient().when(roomDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Room> roomList = new ArrayList<Room>();
            Room room = new Room();
            room.setRoomId(ROOM_ID);
            room.setRoomSize(ROOM_Size);
            room.setRoomType(ROOM_TYPE);
            roomList.add(room);
            return roomList;

        }

        );

        lenient().when(roomDao.findByRoomType(any(Room.RoomType.class))).thenAnswer((InvocationOnMock invocation) -> {
            List<Room> roomList = new ArrayList<Room>();
            Room room = new Room();
            room.setRoomId(ROOM_ID);
            room.setRoomSize(RoomSize.Large);
            room.setRoomType(RoomType.Storage);
            roomList.add(room);
            return roomList;
        }

        );

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        // When saving a object, return the orignal object instance
        lenient().when(artworkDao.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);

        lenient().when(roomDao.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);

    }

    /*-------------------------------------------------- Artwork Tests ---------------------------------------------------------------------------- */

    // Test if we can create a artwork with valid inputs

    @Test
    public void testCreateArtwork() {
        String name = "Mona Lisa";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        boolean loanable = false;

        Artwork artwork = null;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);
        }
        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertNotNull(artwork);
        assertEquals(description, artwork.getDescription());
        assertEquals(name, artwork.getName());
        assertEquals(year, artwork.getYear());
        assertEquals(artist, artwork.getArtist());
        assertEquals(loanPrice, artwork.getLoanPrice());

    }

    // Test if we can create a artwork with no name

    @Test
    public void testCreateArtworkNameNull() {
        String name = null;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artwork name must be specified", error);

    }

    // Test if we can create a artwork with a negative loan price

    @Test
    public void testCreateArtworkNegativeLoanPrice() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = -1;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Loan Price of Artwork cannot be negative", error);

    }

    // Test if we can create a artwork with no description

    @Test
    public void testCreateArtworkDescriptionNull() {
        String name = NAME;
        String description = null;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Description of artwork must be specified", error);

    }

    // Test if we can create a artwork with no artist
    @Test
    public void testCreateArtworkArtistNull() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = null;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;
        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artist name must be specified", error);

    }

    // Test if we can create a artwork with no valid year
    @Test
    public void testCreateArtworkYearNull() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = null;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Year that Artwork was made must be specified", error);

    }

    // Test if we can create a artwork with a empty name (i.e. "")
    @Test
    public void testCreateArtworkEmptyName() {
        String name = "";
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artwork name must be specified", error);

    }

    // Test if we can create a artwork with a empty description (i.e. "")

    @Test
    public void testCreateArtworkEmptyDescription() {
        String name = NAME;
        String description = "";
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Description of artwork must be specified", error);

    }

    // Test if we can create a artwork with a empty artist (i.e. "")

    @Test
    public void testCreateArtworkEmptyArtist() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = "";
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artist name must be specified", error);

    }

    // Test if we can create a artwork with a name = " "

    @Test
    public void testCreateArtworkNameSpaces() {
        String name = " ";
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artwork name must be specified", error);

    }

    // Test if we can create a artwork with a description = " "

    @Test
    public void testCreateArtworkDescriptionSpaces() {
        String name = NAME;
        String description = " ";
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Description of artwork must be specified", error);

    }

    // Test if we can create a artwork with a artist = " "

    @Test
    public void testCreateArtworkArtistSpaces() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = " ";
        String year = YEAR;
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Artist name must be specified", error);

    }

    // Test if we can create a artwork with a year = " "

    @Test
    public void testCreateArtworkYearSpaces() {
        String name = NAME;
        String description = DESCRIPTION;
        double loanPrice = LOAN_PRICE;
        String artist = ARTIST;
        String year = " ";
        Artwork artwork = new Artwork();
        String error = null;
        boolean loanable = false;

        try {
            artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(artwork.getName());
        assertEquals(0.0, artwork.getLoanPrice());
        assertNull(artwork.getArtist());
        assertNull(artwork.getDescription());
        assertNull(artwork.getYear());
        assertEquals("Year that Artwork was made must be specified", error);

    }

    // Test if we can update artwork with valid inputs

    @Test
    public void testUpdateArtwork() {
        String name = "Mona Lisa";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = "Mona";
        String newDescription = "Really really famous artwork";
        double newLoanPrice = 200.00;
        String newArtist = "Michael Di angelo";
        String newYear = "2022";
        boolean loanable = false;
        boolean newLoanable = false;
        Artwork artwork = service.createArtwork(name, loanPrice, artist, description, year, loanable,imgUrl);

        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());

        }

        assertEquals(newName, artwork.getName());
        assertEquals(newLoanPrice, artwork.getLoanPrice());
        assertEquals(newArtist, artwork.getArtist());
        assertEquals(newDescription, artwork.getDescription());
        assertEquals(newYear, artwork.getYear());

    }

    // Test if we can update artwork with no name

    @Test
    public void testUpdateArtworkNoName() {
        String error = "";
        String name = "Mona";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = null;
        String newDescription = "Really really famous artwork";
        double newLoanPrice = 200.00;
        String newArtist = "Michael Di angelo";
        String newYear = "2022";
        Artwork artwork = new Artwork(loanPrice, name, artist, description, year,imgUrl);
        boolean newLoanable = false;
        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);
            ;

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }

        assertEquals("Artwork name must be specified", error);

    }

    // Test if we can update artwork with no description

    @Test
    public void testUpdateArtworkNoDescription() {
        String error = "";
        String name = "Mona";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = "Naruto";
        String newDescription = null;
        double newLoanPrice = 200.00;
        String newArtist = "Michael Di angelo";
        String newYear = "2022";
        Artwork artwork = new Artwork(loanPrice, name, artist, description, year,imgUrl);
        boolean newLoanable = false;
        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }

        assertEquals("Description of artwork must be specified", error);

    }

    // Test if we can update artwork with no year

    @Test
    public void testUpdateArtworkNoYear() {
        String error = "";
        String name = "Mona";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = "Naruto";
        String newDescription = "great artwork";
        double newLoanPrice = 200.00;
        String newArtist = "Michael Di angelo";
        String newYear = null;
        Artwork artwork = new Artwork(loanPrice, name, artist, description, year,imgUrl);
        boolean newLoanable = false;

        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }

        assertEquals("Year that Artwork was made must be specified", error);

    }

    // Test if we can update artwork with no artist

    @Test
    public void testUpdateArtworkNoArtist() {
        String error = "";
        String name = "Mona";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = "Naruto";
        String newDescription = "great artwork";
        double newLoanPrice = 200.00;
        String newArtist = null;
        String newYear = "2009";
        Artwork artwork = new Artwork(loanPrice, name, artist, description, year,imgUrl);
        boolean newLoanable = false;
        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }

        assertEquals("Artist name must be specified", error);

    }

    // Test if we can update artwork with a negative loan price

    @Test
    public void testUpdateArtworkNegativeLoanPrice() {
        String error = "";
        String name = "Mona";
        String description = "Very famous artwork";
        double loanPrice = 100.00;
        String artist = "Leonardo Da Vinci";
        String year = "2022";
        String newName = "Naruto";
        String newDescription = "great artwork";
        double newLoanPrice = -200.00;
        String newArtist = "Michael";
        String newYear = "2009";
        Artwork artwork = new Artwork(loanPrice, name, artist, description, year,imgUrl);
        boolean newLoanable = false;
        try {
            artwork = service.updateArtwork(artwork, newName, newLoanPrice, newArtist, newDescription, newYear,
                    newLoanable,imgUrl);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }

        assertEquals("Loan Price of Artwork cannot be negative", error);

    }

    // Test if we can delete a artwork with all associated its loan requests

    @Test
    public void deleteArtwork() {
        boolean success = false;
        try {
            success = service.deleteArtwork(ARTWORK_WITH_LOAN_REQUESTS);

        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertTrue(success);

    }

    // Test if we can get all loan requests associated with a artwork

    @Test
    public void getLoanRequestsByArtwork() {
        List<LoanRequest> loanRequests = null;
        try {
            loanRequests = service.getLoanRequestsByArtwork(ARTWORK_WITH_LOAN_REQUESTS);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(loanRequests);
        assertEquals(1, loanRequests.size());
    }

    // Test if we can get all artworks in the system

    @Test
    public void getAllArtworks() {

        List<Artwork> artworks = null;
        try {
            artworks = service.getAllArtworks();
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(artworks);
        assertEquals(1, artworks.size());
    }

    // Test if we can get a existing artwork

    @Test
    public void testGetExistingArtwork() {
        Long artwork_id = 0L;
        try {
            artwork_id = service.getArtworkbyID(ARTWORK_ID).getArtworkID();
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertEquals(ARTWORK_ID, artwork_id);
    }

    // Test if we can get a non-existing artwork

    @Test
    public void testGetNonExistingArtwork() {
        String error = "";
        try {
            service.getArtworkbyID(NON_ARTWORK_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("No artwork exists with the given artworkID", error);
    }

    // Test if we can add artwork to display room

    @Test
    public void testAddArtworkInDisplayRoom() {
        Artwork artwork = new Artwork();
        artwork.setName("one piece");

        try {
            service.addArtworkToRoom(artwork, ROOM_WITH_ARTWORKS_IN_IT);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals(RoomType.Display, artwork.getRoom().getRoomType());

    }

    // Test if we can add artwork that is on loan to display room

    @Test
    public void testAddArtworkOnLoanInDisplayRoom() {
        Artwork artwork = new Artwork();
        artwork.setName(ARTWORK_ON_LOAN);
        String error = "";

        try {
            service.addArtworkToRoom(artwork, ROOM_WITH_ARTWORKS_IN_IT);
        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();
        }
        assertEquals("Artwork is already out on loan", error);

    }

    // Test if we can add artwork that to the storage room

    @Test
    public void testAddArtworkInStorageRoom() {
        Artwork artwork = new Artwork();
        artwork.setName("one piece");
        Room room = new Room();
        room.setRoomType(RoomType.Storage);
        try {
            artwork = service.addArtworkToRoom(artwork, room);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertEquals(RoomType.Storage, artwork.getRoom().getRoomType());

    }

    // Test if we can add artwork that is on loan to the storage room

    @Test
    public void testSetArtworkOnLoanInStorageRoom() {
        Artwork artwork = new Artwork();
        artwork.setName(ARTWORK_ON_LOAN);
        Room room = new Room();
        room.setRoomType(RoomType.Storage);
        String error = "";

        try {
            service.addArtworkToRoom(artwork, room);
        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();
        }
        assertEquals("Artwork is already out on loan", error);

    }

    // Test if we can check if a artwork is on loan

    @Test
    public void testGetOnLoan() {
        boolean loanFound = false;
        try {

            Artwork artwork = new Artwork();
            artwork.setName("naruto");
            Visitor visitor = new Visitor();
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            Calendar c3 = Calendar.getInstance();
            c1.set(2022, Calendar.JANUARY, 16, 9, 00, 0);
            c2.set(2022, Calendar.FEBRUARY, 16, 9, 00, 0);
            c3.set(2021, Calendar.DECEMBER, 16, 9, 00, 0);
            Date requestedStartDate = new Date(c1.getTimeInMillis());
            Date requestedEndDate = new Date(c2.getTimeInMillis());
            LoanRequest loanRequest = new LoanRequest(requestedStartDate, requestedEndDate, visitor,
                    artwork);
            loanRequest.setArtwork(artwork);
            loanFound = service.getOnLoan(artwork);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertEquals(true, loanFound);

    }



    /*-------------------------------------------------- Room Tests ---------------------------------------------------------------------------- */

    // Test if we can create a display room

    @Test
    public void testCreateDisplayRoom() {
        RoomType roomType = RoomType.Display;
        RoomSize roomSize = RoomSize.Large;

        Room room = null;
        try {
            room = service.createRoom(roomType, roomSize);

        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());

        }

        assertNotNull(room);
        assertEquals(roomType, room.getRoomType());
        assertEquals(roomSize, room.getRoomSize());

    }

    // Test if we can create a room with no specified roomType (Display or Storage)

    @Test
    public void testCreateRoomNoRoomType() {
        RoomType roomType = null;
        RoomSize roomSize = RoomSize.Large;
        String error = "";
        Room room = null;
        try {
            room = service.createRoom(roomType, roomSize);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }
        assertNull(room);
        assertEquals("Enter a valid room type (Display or Storage)", error);

    }

    // Test if we can create a room with no specified roomSize (Large or Small)
    @Test
    public void testCreateRoomNoRoomSize() {
        RoomType roomType = RoomType.Display;
        RoomSize roomSize = null;
        String error = "";
        Room room = null;
        try {
            room = service.createRoom(roomType, roomSize);

        }

        catch (IllegalArgumentException e) {
            error += e.getMessage();

        }
        assertNull(room);
        assertEquals("Enter a valid room size (Large or small)", error);

    }

    // Test if we can get all rooms of the system

    @Test
    public void getAllRooms() {
        List<Room> rooms = new ArrayList<Room>();
        try {
            rooms = service.getAllRooms();
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertNotNull(rooms);
        assertEquals(1, rooms.size());
    }

    // Test if we can get the number of artworks in a room

    @Test
    public void getCapacity() {
        int capacity = 0;

        try {
            capacity = service.getCapacity(ROOM_WITH_ARTWORKS_IN_IT);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertEquals(1, capacity);
    }

    // Test if we can find a existing room

    @Test
    public void testGetExistingRoom() {
        Long room_id = 0L;
        try {
            room_id = service.getRoombyID(ROOM_ID).getRoomId();
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertEquals(ROOM_ID, room_id);
    }

    // Test if we can find a non-existing room

    @Test
    public void testGetNonExistingRoom() {
        String error = "";
        try {
            service.getRoombyID(NON_ROOM_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("No room exists with the given roomID", error);

    }

    // Test if we can create a room with no roomType && roomSize
    @Test
    public void testCreateRoomNull() {
        String error = "";
        RoomType roomType = null;
        RoomSize roomSize = null;
        Room room = null;

        try {
            room = service.createRoom(roomType, roomSize);

        }

        catch (IllegalArgumentException e) {
            error = e.getMessage();

        }

        assertNull(room);
        assertEquals("Enter a valid room type (Display or Storage)", error);

    }

    // Test if we can remove a artwork from a room

    @Test
    public void removeArtworkFromRoom() {
        Artwork artwork = new Artwork();
        Room room = new Room();
        artwork.setRoom(room);
        try {
            artwork = service.removeArtworkFromRoom(artwork);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());

        }
        assertNull(artwork.getRoom());
    }

    // Test if we can get all the artworks in a room

    @Test
    public void getAllArtworksInRoom() {
        List<Artwork> artworks = new ArrayList<Artwork>();
        try {
            artworks = service.getAllArtworksInRoom(ROOM_WITH_ARTWORKS_IN_IT);
        }

        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertNotNull(artworks);
        assertEquals(1, artworks.size());

    }

}
