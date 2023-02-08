package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;

public interface ArtworkRepository extends CrudRepository<Artwork, Long> {
    Artwork findArtworkByArtworkID(Long artworkId);
    List<Artwork> findArtworkByRoom(Room room);
}
