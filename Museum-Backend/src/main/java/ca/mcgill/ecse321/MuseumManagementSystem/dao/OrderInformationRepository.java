package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.OrderInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

public interface OrderInformationRepository extends CrudRepository<OrderInformation, Long> {
  OrderInformation findOrderInformationByOrderID(Long orderID);
  List<OrderInformation> findByVisitor(Visitor visitor);
  void deleteAllByVisitor(Visitor visitor);
  List<OrderInformation> findOrderInformationByVisitor(Visitor visitor);
}