/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.MappedSuperclass;

// line 14 "model.ump"
// line 133 "model.ump"
@MappedSuperclass
public abstract class Staff extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Staff() {}
  
  public Staff(String aUsername, String aPassword)
  {
    super(aUsername, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}



