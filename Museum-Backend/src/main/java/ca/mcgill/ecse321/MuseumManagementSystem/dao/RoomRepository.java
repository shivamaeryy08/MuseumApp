package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findRoomByRoomId(Long RoomId);
    List<Room> findByRoomType(RoomType roomType);
}