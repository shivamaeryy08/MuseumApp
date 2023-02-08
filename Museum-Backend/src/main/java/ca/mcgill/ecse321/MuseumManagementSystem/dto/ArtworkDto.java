package ca.mcgill.ecse321.MuseumManagementSystem.dto;

public class ArtworkDto {

  private double loanPrice;
  private boolean loanable;
  private String name;
  private String artist;
  private String description;
  private String year;
  private Long artworkID;
  private RoomDto room;
  private String imgUrl;

  public ArtworkDto() {
  }

  public ArtworkDto(Long artworkID, double loanPrice, String name, String artist, String description, String year,
      boolean loanable, String imgUrl,RoomDto roomDto) {
    this.loanable = loanable;
    this.loanPrice = loanPrice;
    this.name = name;
    this.artist = artist;
    this.description = description;
    this.year = year;
    this.artworkID = artworkID;
    this.room = roomDto;
    this.imgUrl = imgUrl;
  }

  public ArtworkDto(double loanPrice, String name, String artist, String description, String year, boolean loanable,
  String imgUrl,RoomDto roomDto) {
    this.loanable = loanable;
    this.loanPrice = loanPrice;
    this.name = name;
    this.artist = artist;
    this.description = description;
    this.year = year;
    this.room = roomDto;
    this.imgUrl = imgUrl;
  }

  public double getLoanPrice() {
    return loanPrice;
  }

  public boolean getLoanable() {
    return loanable;
  }

  public boolean getDefaultLoanable() {
    return false;
  }

  public String getName() {
    return name;
  }

  public String getArtist() {
    return artist;
  }

  public String getDescription() {
    return description;
  }

  public String getYear() {
    return year;
  }

  public Long getArtworkID() {
    return artworkID;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }


  public RoomDto getRoom() {
    return room;
  }

  public void setRoom(RoomDto room) {
    this.room = room;
  }

}