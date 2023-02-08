package ca.mcgill.ecse321.MuseumManagementSystem.service;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.OrderInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  @Autowired
  OrderInformationRepository orderInformationRepository;

  @Autowired
  MuseumInformationRepository museumInformationRepository;

  /**
   * @author Nathanael Lemma
   * @brief To create an order
   * @param amount
   * @param visitor
   * @param museumInformation
   * @return OrderInformation - created order object
   */
  @Transactional
  public OrderInformation createOrder(
    int amount,
    Visitor visitor
    ) {
    Date currentDate = new Date(System.currentTimeMillis());
    /* Input validition */
    String err = "";
    if (visitor == null) {
      err += "Visitor cannot be null! ";
    }

    //Retrieve museum information
    MuseumInformation museum = toList(museumInformationRepository.findAll()).get(0);

    if (amount < 0) {
      err += "Order amount cannot be negative! ";
    } else if (amount == 0) {
      err += "Order amount cannot be Zero! ";
    }
    if (!err.isEmpty()) {
      throw new IllegalArgumentException(err);
    }

    OrderInformation orderInformation = new OrderInformation();
    orderInformation.setAmount(amount);
    orderInformation.setTotalPrice(amount * museum.getVisitorFee());
    orderInformation.setOrderDate(currentDate);
    orderInformation.setVisitor(visitor);
    return orderInformationRepository.save(orderInformation);
  }

  /**
   * @author Nathanael Lemma
   * @brief To get an Order by its ID
   * @param id
   * @return OrderInformation - found order object
   */
  @Transactional
  public OrderInformation getOrderByID(long id) {
    OrderInformation orderInformation = orderInformationRepository.findOrderInformationByOrderID(
      id
    );

    if (orderInformation == null) {
      throw new IllegalArgumentException(
        "An order with the given ID cannot be found!"
      );
    }
    return orderInformation;
  }

  /**
   * @author Nathanael Lemma
   * @brief To get an Order by its ID
   * @param visitor
   * @return List<OrderInformation> - list of orders
   */
  @Transactional
  public List<OrderInformation> getOrdersByVisitor(Visitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor cannot be null!");
    }
    List<OrderInformation> orderInformationList = orderInformationRepository.findOrderInformationByVisitor(
      visitor
    );
    if (orderInformationList == null) {
      throw new IllegalArgumentException(
        "Order(s) for the given user cannot be found!"
      );
    }
    return orderInformationList;
  }

  @Transactional
  public List<OrderInformation> getAllOrders() {
    return toList(orderInformationRepository.findAll());
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


