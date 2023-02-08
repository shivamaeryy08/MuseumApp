package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.ArtworkDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.RoomDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.service.ArtworkRoomService;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRoomRestController {

	@Autowired
	private ArtworkRoomService service;

	/*-----------------------------------------------------Artwork Controller Methods------------------------------------------------------------------*/

	/*
	 * Controller method that gets all artworks
	 * 
	 * @author Shivam Aery
	 * 
	 * @return all artworks in system
	 */

	@GetMapping(value = { "/artworks", "/artworks/" })
	public ResponseEntity<?> getAllArtworks() {

		try {
			List<Artwork> artworks = service.getAllArtworks();
			return new ResponseEntity<>(artworks.stream()
					.map(p -> convertToDto(p)).collect(Collectors.toList()), HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that gets all artworks in a room
	 * 
	 * @author Shivam Aery
	 * 
	 * @param roomID
	 * 
	 * @return all artworks in the room associated with the given roomID
	 */

	@GetMapping(value = { "/room/{roomID}/artworks", "/room/{roomID}/artworks/" })
	public ResponseEntity<?> getAllArtworksInRoom(
			@PathVariable("roomID") Long roomID) {
		try {
			Room room = service.getRoombyID(roomID);
			List<Artwork> artworks = service.getAllArtworksInRoom(room);
			return new ResponseEntity<List<ArtworkDto>>(artworks.stream()
					.map(p -> convertToDto(p)).collect(Collectors.toList()), HttpStatus.OK);

		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that creates a artwork
	 * 
	 * @author Shivam Aery
	 * 
	 * @param loanPrice of artwork
	 * 
	 * @param name of artwork
	 * 
	 * @param artist who created artwork
	 * 
	 * @param description of artwork
	 * 
	 * @param year artwork was made
	 * 
	 * @return ResponseEntity<ArtworkDto>
	 */

	@PostMapping(value = { "/artwork", "/artwork" })
	public ResponseEntity<?> createArtwork(@RequestBody ArtworkDto request) {
		try {
			Artwork artwork = service.createArtwork(request.getName(), request.getLoanPrice(), request.getArtist(),
					request.getDescription(),
					request.getYear(), request.getLoanable(),request.getImgUrl());
			return new ResponseEntity<ArtworkDto>(convertToDto(artwork), HttpStatus.CREATED);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that gets an artwork by its ID
	 * 
	 * @author Shivam Aery
	 * 
	 * @param artworkID
	 * 
	 * @return ResponseEntity<ArtworkDto>
	 */

	@GetMapping(value = { "/artwork/{artworkID}", "/artwork/{artworkID}/" })
	public ResponseEntity<?> getArtworkByID(
			@PathVariable("artworkID") Long artworkID) {
		try {
			Artwork artwork = service.getArtworkbyID(artworkID);
			return new ResponseEntity<ArtworkDto>(convertToDto(artwork), HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that adds artwork to room
	 * 
	 * @author Shivam Aery
	 * 
	 * @param artworkID
	 * 
	 * @param roomID
	 * 
	 * @return ResponseEntity<ArtworkDto>
	 */

	@PostMapping(value = { "/artwork/{artworkID}/room/add", "/artwork/{artworkID}/room/add/" })
	public ResponseEntity<?> addArtworkToRoom(
			@RequestBody RoomDto request,
			@PathVariable("artworkID") Long artworkID) {
		try {
			Artwork artwork = service.getArtworkbyID(artworkID);
			Room newRoom = service.getRoombyID(request.getRoomId());
			artwork = service.addArtworkToRoom(artwork, newRoom);
			return new ResponseEntity<>(convertToDto(artwork), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Controller method for deleting artwork
	 * 
	 * @author Shivam Aery
	 * 
	 * @param artworkID
	 * 
	 */

	@DeleteMapping(value = { "/artwork/{artworkID}", "/artwork/{artworkID}/" })
	public ResponseEntity<?> deleteArtwork(@PathVariable("artworkID") Long artworkID) {
		try {
		Artwork artwork = service.getArtworkbyID(artworkID);
		service.deleteArtwork(artwork);
		return new ResponseEntity<>("Artwork has been deleted", HttpStatus.OK);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		
	}

	@GetMapping(value = {"/artwork/getDisplay", "/artwork/getDisplay/" }) 
	public ResponseEntity<?> getAllArtworksInDisplayRooms()
	{
		try {
			List<Artwork> artworks = service.getAllArtworksInDisplay();
			return new ResponseEntity<>(artworks.stream()
					.map(p -> convertToDto(p)).collect(Collectors.toList()), HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/*-----------------------------------------------------Room Controller Methods------------------------------------------------------------------*/

	/*
	 * Controller method that gets room by its ID
	 * 
	 * @author Shivam Aery
	 * 
	 * @param roomID
	 * 
	 * @return roomDto
	 */

	@GetMapping(value = { "/room/{roomID}", "/room/{roomID}/" })
	public ResponseEntity<?> getRoomByID(
			@PathVariable("roomID") Long roomID) {
		try {
			Room room = service.getRoombyID(roomID);
			return new ResponseEntity<RoomDto>(convertToDto(room), HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that updates a artwork
	 * 
	 * @author Shivam Aery
	 * 
	 * @param loanPrice of artwork
	 * 
	 * @param name of artwork
	 * 
	 * @param artist who created artwork
	 * 
	 * @param description of artwork
	 * 
	 * @param year artwork was made
	 * 
	 * @return ResponseEntity<ArtworkDto>
	 */

	@PostMapping(value = { "/artwork/{artworkID}/update", "/artwork/{artworkID}/update/" })
	public ResponseEntity<?> updateArtwork(
			@RequestBody ArtworkDto request,
			@PathVariable("artworkID") Long artworkID) {
		try {
			Artwork artwork = service.getArtworkbyID(artworkID);
			artwork = service.updateArtwork(artwork, request.getName(), request.getLoanPrice(), request.getArtist(),
					request.getDescription(),
					request.getYear(), request.getLoanable(),request.getImgUrl());

			return new ResponseEntity<ArtworkDto>(convertToDto(artwork), HttpStatus.OK);

		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that gets all rooms of system
	 * 
	 * @author Shivam Aery
	 * 
	 * @return list of roomDto
	 */

	@GetMapping(value = { "/rooms", "/rooms/" })
	public ResponseEntity<?> getAllRooms() {
		try {
			return new ResponseEntity<List<RoomDto>>(service.getAllRooms().stream()
					.map(p -> convertToDto(p)).collect(Collectors.toList()), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that gets how capacity of room given its roomID
	 * 
	 * @author Shivam Aery
	 * 
	 * @param roomID
	 * 
	 * @return capacity of room
	 */

	@GetMapping(value = { "/room/{roomID}/capacity", "/room/{roomID}/capacity/" })
	public ResponseEntity<?> getCapacity(
			@PathVariable("roomID") Long roomID) {
		try {
			Room room = service.getRoombyID(roomID);
			Integer capacity = Integer.valueOf(service.getCapacity(room));
			return new ResponseEntity<Integer>(capacity, HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Controller method that removes artwork from room given its artworkID
	 * 
	 * @author Shivam Aery
	 * 
	 * @param artworkID
	 * 
	 * @return ResponseEntity<ArtworkDto>
	 */

	@PostMapping(value = { "/artwork/room/remove", "/artwork/room/remove/" })
	public ResponseEntity<?> removeArtworkFromRoom(
			@RequestBody ArtworkDto request) {
		try {
			Artwork artwork = service.getArtworkbyID(request.getArtworkID());
			artwork = service.removeArtworkFromRoom(artwork);
			return new ResponseEntity<ArtworkDto>(convertToDto(artwork), HttpStatus.OK);
		}

		catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	private ArtworkDto convertToDto(Artwork artwork) {
		if (artwork == null) {
			return null;
		}

		ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(),
				artwork.getArtist(),
				artwork.getDescription(), artwork.getYear(), artwork.getLoanable(), artwork.getImgUrl(),convertToDto(artwork.getRoom()));

		artworkDto.setRoom(convertToDto(artwork.getRoom()));
		return artworkDto;

	}

	private RoomDto convertToDto(Room room) {
		if (room == null) {
			return null;
		}

		return new RoomDto(room.getRoomId(), room.getRoomSize(), room.getRoomType());

	}

}