package ca.mcgill.ecse321.MuseumManagementSystem.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.ArtworkRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.RoomRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

@Service
public class ArtworkRoomService {

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    RoomRepository RoomRepository;

    @Autowired
    LoanRequestRepository loanRequestRepository;

    /*
     * Service method that creates an artwork
     * 
     * @author Shivam Aery
     * 
     * @param name of artwork
     * 
     * @param loanPrice the price of the loan associated with artwork
     * 
     * @param artist the artist of the artwork
     * 
     * @param description the description of the artwork
     * 
     * @param year the year the artwork was made
     * 
     * @return created artwork
     */

    @Transactional
    public Artwork createArtwork(String name, double loanPrice, String artist, String description, 
    String year,boolean loanable, String imgUrl)
     {
        String error = "";

        if (name == null || name.isBlank()) {
            error += "Artwork name must be specified";
        }

        if (loanPrice < 0) {
            error += "Loan Price of Artwork cannot be negative";
        }

        if (artist == null || artist.isBlank()) {
            error += "Artist name must be specified";
        }

        if (description == null || description.isBlank()) {
            error += "Description of artwork must be specified";
        }

        if (year == null || year.isBlank()) {
            error += "Year that Artwork was made must be specified";
        }

        if (imgUrl == null || imgUrl.isBlank()) {
            error += "A valid image url must be specified";
        }

        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        List<Room> storage = RoomRepository.findByRoomType(RoomType.Storage);

        Artwork artwork = new Artwork();
        artwork.setName(name);
        artwork.setArtist(artist);
        artwork.setDescription(description);
        artwork.setLoanPrice(loanPrice);
        artwork.setYear(year);
        artwork.setLoanable(loanable);
        artwork.setImgUrl(imgUrl);
        if(storage.size()!=0) {
           artwork.setRoom(storage.get(0)); 
        }
        return artworkRepository.save(artwork);
    }

    /*
     * Service method that gets an artwork given its ID
     * 
     * @author Shivam Aery
     * 
     * @param artworkID
     * 
     * @return artwork associated with artworkID
     */

    @Transactional
    public Artwork getArtworkbyID(Long artworkID) {
        Artwork artwork = artworkRepository.findArtworkByArtworkID(artworkID);
        if ( artwork == null)
            throw new IllegalArgumentException("No artwork exists with the given artworkID");
        return artwork;

    }

    /*
     * Service method that gets all artworks in system
     * 
     * @author Shivam Aery
     * 
     * @return list of artworks in artwork repository
     */

    @Transactional
    public List<Artwork> getAllArtworks() {
        return toList(artworkRepository.findAll());
    }

    /*
     * Service method that gets all artworks in a room
     * 
     * @author Shivam Aery
     * 
     * @param room (any type or size)
     * 
     * @return all artworks in room
     */

    @Transactional
    public List<Artwork> getAllArtworksInRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException("The room must be specified");
        return artworkRepository.findArtworkByRoom(room);

    }

    /*
     * Service method that creates a room with a given room type
     * 
     * @author Shivam Aery
     * 
     * @param roomType which is either Storage or Display
     * 
     * @return created room with type roomType
     */

    @Transactional
    public Room createRoom(RoomType roomType, RoomSize roomSize) {
        if (roomType == null) {
            throw new IllegalArgumentException("Enter a valid room type (Display or Storage)");
        }

        if (roomSize == null) {
            throw new IllegalArgumentException("Enter a valid room size (Large or small)");
        }

        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomSize(roomSize);
        return RoomRepository.save(room);
    }

    /*
     * Service method that gets a room by its ID
     * 
     * @author Shivam Aery
     * 
     * @param RoomID
     * 
     * @return room associated with RoomID
     */

    @Transactional
    public Room getRoombyID(Long RoomID) {
        Room room = RoomRepository.findRoomByRoomId(RoomID);
        if (room == null)
            throw new IllegalArgumentException("No room exists with the given roomID");
        return room;

    }

    /*
     * Service method that gets all the rooms in the system
     *
     * @author Shivam Aery
     * 
     * @return list of rooms in room repository
     */

    @Transactional
    public List<Room> getAllRooms() {
        return toList(RoomRepository.findAll());
    }

    /*
     * Service method that gets number of artworks in a given room
     * 
     * @author Shivam Aery
     * 
     * @param room (can be any type or size)
     * 
     * @return number of artworks in a room
     */

    @Transactional
    public int getCapacity(Room room) {
        if (room == null)
            throw new IllegalArgumentException("The room must be specified");
        List<Artwork> artworks = artworkRepository.findArtworkByRoom(room);
        return artworks.size();
        
    }

    /*
     * Service method that updates a given artwork
     * 
     * @author Shivam Aery
     * 
     * @param name of artwork
     * 
     * @param loanPrice the price of the loan associated with artwork
     * 
     * @param artist the artist of the artwork
     * 
     * @param description the description of the artwork
     * 
     * @param year the year the artwork was made
     * 
     * @return updated artwork
     */

    @Transactional
    public Artwork updateArtwork(Artwork artwork, String name, double loanPrice, String artist, String description,
            String year, boolean loanable, String imgUrl) {

        String error = "";

        if (artwork == null) {
            error += "Artwork must be specified";
            throw new IllegalArgumentException(error);
        }

        if (name == null || name.isBlank()) {
            error += "Artwork name must be specified";
        }

        if (loanPrice < 0) {
            error += "Loan Price of Artwork cannot be negative";
        }

        if (artist == null || artist.isBlank()) {
            error += "Artist name must be specified";
        }

        if (description == null || description.isBlank()) {
            error += "Description of artwork must be specified";
        }

        if (year == null || year.isBlank()) {
            error += "Year that Artwork was made must be specified";
        }

        if (imgUrl == null || imgUrl.isBlank()) {
            error += "A valid image url must be specified";
        }

        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        artwork.setName(name);
        artwork.setArtist(artist);
        artwork.setDescription(description);
        artwork.setLoanPrice(loanPrice);
        artwork.setYear(year);
        artwork.setLoanable(loanable);
        artwork.setImgUrl(imgUrl);
        return artworkRepository.save(artwork);
    }

    /*
     * Service method that removes an artwork from its room
     * 
     * @author Shivam Aery
     * 
     * @param artwork to remove from its room
     * 
     * @return artwork removed from room
     */

    @Transactional
    public Artwork removeArtworkFromRoom(Artwork artwork) {
        if (artwork == null)
            throw new IllegalArgumentException("The artwork must be specified");
        artwork.setRoom(null);
        return artworkRepository.save(artwork);

    }


    
    /**
     * Service method that returns list of artworks that are being displayed
     *  
     * @author Shivam Aery
     * 
     * @return List<Artwork>
     */

    @Transactional
    public List<Artwork> getAllArtworksInDisplay() {
        List<Room> displayRooms = RoomRepository.findByRoomType(RoomType.Display);
        List<Artwork> displayArtworks = new ArrayList<Artwork>();
        for (Room room : displayRooms) {
            for (Artwork artwork: artworkRepository.findArtworkByRoom(room)) {
                if (artwork != null) {
                    displayArtworks.add(artwork);
                }
            }
           
        }

        return displayArtworks;
    }

    /*
     * Service method that deletes an artwork and all loan requests associated with
     * it
     * 
     * @author Shivam Aery
     * 
     * @param artwork to delete from system
     * 
     * @return artwork true
     */

    @Transactional
    public boolean deleteArtwork(Artwork artwork) {
        if (artwork == null)
            throw new IllegalArgumentException("The artwork must be specified");
        List<LoanRequest> loanRequests = loanRequestRepository.findLoanRequestByArtwork(artwork);
        for (LoanRequest loanRequest : loanRequests) {
            loanRequestRepository.delete(loanRequest);
        }
        artworkRepository.delete(artwork);
        return true;
    }

    /*
     * Service method that adds a given artwork to a given display room
     * 
     * @author Shivam Aery
     * 
     * @param artwork to add to display room
     * 
     * @param room which has to have type Display
     * 
     * @return artwork added to display room
     */

    @Transactional
    public Artwork addArtworkToRoom(Artwork artwork, Room room) {
        String error = "";
        boolean roomIsLarge = false;
        int numberOfArtworksInRoom = getCapacity(room);

        if (artwork == null) {
            error += "Artwork must be specified";
            throw new IllegalArgumentException(error);
        }

        if (room == null) {
            error += "Room must be specified";
            throw new IllegalArgumentException(error);
        }

        if (room.getRoomSize() == RoomSize.Large) {
            roomIsLarge = true;
        }

        if (room.getRoomType() == RoomType.Display && roomIsLarge && numberOfArtworksInRoom >= 300) {
            error = "Large room cannot hold any more artworks";
            throw new IllegalArgumentException(error);

        }

        if (room.getRoomType() == RoomType.Display && !roomIsLarge && numberOfArtworksInRoom >= 200) {
            error = "Small room cannot hold any more artworks";
            throw new IllegalArgumentException(error);
        }

        if (getOnLoan(artwork)) {
            error += "Artwork is already out on loan";
            throw new IllegalArgumentException(error);
        }

        artwork.setRoom(room);
        return artworkRepository.save(artwork);

    }

    /*
     * Service method that gets all loan requests that are associated with a given
     * artwork
     *
     * @author Shivam Aery
     * 
     * @param artwork
     * 
     * @return list of loan requests that are associated with artwork
     */

    @Transactional
    public List<LoanRequest> getLoanRequestsByArtwork(Artwork artwork) {
        if (artwork == null) throw new IllegalArgumentException("Artwork must be specified");
        return loanRequestRepository.findLoanRequestByArtwork(artwork);
    }

    /*
     * Service method that retuns whether artwork is currently on loan
     * 
     * @author Shivam Aery
     * 
     * @param artwork
     * 
     * @return true if artwork is on loan, false otherwise
     */

    @Transactional
    public boolean getOnLoan(Artwork artwork) {
        if (artwork == null)
            throw new IllegalArgumentException("The artwork must be specified");
        if (loanRequestRepository
                        .findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(
                                Status.Approved, artwork,
                                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    // Helper method to retrieve lists of objects obtained from repository

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}