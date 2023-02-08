/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;

import java.sql.Date;

// line 82 "model.ump"
// line 202 "model.ump"
@Entity
public class OrderInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //OrderInformation Attributes
  private Date orderDate;
  private int amount;
  private double totalPrice;
  @Id
  @GeneratedValue
  private Long orderID;

  //OrderInformation Associations
  @ManyToOne(optional = false)
  private Visitor visitor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public OrderInformation () {}

  public OrderInformation(Date aOrderDate, int aAmount, Visitor aVisitor)
  {
    orderDate = aOrderDate;
    amount = aAmount;
    if (!setVisitor(aVisitor))
    {
      throw new RuntimeException("Unable to create OrderInformation due to aVisitor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrderDate(Date aOrderDate)
  {
    boolean wasSet = false;
    orderDate = aOrderDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setAmount(int aAmount)
  {
    boolean wasSet = false;
    amount = aAmount;
    wasSet = true;
    return wasSet;
  }

  public boolean setOrderID(Long aOrderID)
  {
    boolean wasSet = false;
    orderID = aOrderID;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPrice(double aTotalPrice) 
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
    wasSet = true;
    return wasSet;
  }

  public Date getOrderDate()
  {
    return orderDate;
  }

  public int getAmount()
  {
    return amount;
  }

  public Long getOrderID()
  {
    return orderID;
  }

  public double getTotalPrice() 
  {
    return totalPrice;
  }
  /* Code from template association_GetOne */
  public Visitor getVisitor()
  {
    return visitor;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setVisitor(Visitor aNewVisitor)
  {
    boolean wasSet = false;
    if (aNewVisitor != null)
    {
      visitor = aNewVisitor;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    visitor = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "amount" + ":" + getAmount()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "," +
            "orderID" + ":" + getOrderID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "visitor = "+(getVisitor()!=null?Integer.toHexString(System.identityHashCode(getVisitor())):"null");
  }
}



