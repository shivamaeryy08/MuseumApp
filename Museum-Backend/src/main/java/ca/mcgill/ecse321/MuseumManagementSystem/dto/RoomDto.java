package ca.mcgill.ecse321.MuseumManagementSystem.dto;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomSize;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room.RoomType;

public class RoomDto {
  private Long roomId;
  private RoomType roomType;
  private RoomSize roomSize;

  public RoomDto() {
  }

  public RoomDto(Long roomID, RoomSize roomSize, RoomType aRoomType) {
    roomType = aRoomType;
    this.roomSize = roomSize;
    this.roomId = roomID;
  }

  public boolean setRoomId(Long aRoomId) {
    boolean wasSet = false;
    roomId = aRoomId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomType(RoomType aRoomType) {
    boolean wasSet = false;
    roomType = aRoomType;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomSize(RoomSize aRoomSize) {
    boolean wasSet = false;
    roomSize = aRoomSize;
    wasSet = true;
    return wasSet;
  }

  public Long getRoomId() {
    return roomId;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public RoomSize getRoomSize() {
    return roomSize;
  }

}