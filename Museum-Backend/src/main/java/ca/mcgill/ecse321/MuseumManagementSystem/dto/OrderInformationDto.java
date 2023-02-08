package ca.mcgill.ecse321.MuseumManagementSystem.dto;

import java.sql.Date;

public class OrderInformationDto {
  private Date orderDate;
  private Integer amount;
  private Double totalPrice;
  private Long orderID;
  private VisitorDto visitor;

  public OrderInformationDto() {}

  //Constructor with orderID and total price
  public OrderInformationDto(Long orderID, Date orderDate, Integer amount, Double totalPrice, VisitorDto visitor){
      this.orderDate = orderDate;
      this.amount = amount;
      this.totalPrice = totalPrice;
      this.orderID = orderID; 
      this.visitor = visitor;
  }

  //Constructor without orderID
  public OrderInformationDto(Integer amount, VisitorDto visitor){
      this.amount = amount;
      this.visitor = visitor;
  }

  public Date getOrderDate(){
      return orderDate;
  }

  public Integer getAmount(){
      return amount;
  }

  public Double getTotalPrice(){
      return totalPrice;
  }

  public Long getOrderID(){
      return orderID;
  }

  public VisitorDto getVisitor(){
      return visitor;
  }

  public void setOrderDate(Date orderDate)
  {
    this.orderDate = orderDate;
  }

  public void setAmount(Integer amount)
  {
    this.amount = amount;
  }

  public void setOrderID(Long orderID)
  {
    this.orderID = orderID;
  }

  public void setTotalPrice(Double totalPrice) 
  {
    this.totalPrice = totalPrice;
  }

  public void setVisitor(VisitorDto visitor)
  {
      this.visitor = visitor;
  }
}
