package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.OrderInformationDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.OrderInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.service.MuseumInformationService;
import ca.mcgill.ecse321.MuseumManagementSystem.service.OrderService;
import ca.mcgill.ecse321.MuseumManagementSystem.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    MuseumInformationService museumInformationService;


    @GetMapping(value = {"/orders", "/orders/"})
    public ResponseEntity<?> getAllOrders() {
        List<OrderInformation> orders = orderService.getAllOrders();
        return new ResponseEntity<>(convertToDto(orders), HttpStatus.OK);
    }

    @GetMapping(value = {"/{username}/orders", "/{username}/orders/"})
    public ResponseEntity<?> getOrderInformationByVisitor(@PathVariable("username") String username) {
        try {
           Visitor visitor = userService.getVisitorByUsername(username);
           List<OrderInformation> orderInformation = orderService.getOrdersByVisitor(visitor);
           return new ResponseEntity<>(convertToDto(orderInformation), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }    
    }

    @GetMapping(value = {"/orders/{orderID}", "/orders/{orderID}/"})
    public ResponseEntity<?> getOrderByOrderID(@PathVariable(name = "orderID") Long orderID) {
        try {
            OrderInformation orderInformation = orderService.getOrderByID(orderID);
            OrderInformationDto orderDto = convertToDto(orderInformation);
            return new ResponseEntity<>(orderDto, HttpStatus.OK); 
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    @PostMapping(value = {"/order", "/order/"})
    public ResponseEntity<?> createOrderInformation(@RequestBody OrderInformationDto request) {
        try {

            Visitor visitor = userService.getVisitorByUsername(request.getVisitor().getUsername());
            OrderInformation orderInformation = orderService.createOrder(request.getAmount(), visitor);
            OrderInformationDto orderInformationDto = convertToDto(orderInformation);
            return new ResponseEntity<>(orderInformationDto, HttpStatus.CREATED); 

        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* --------------------------------------- Helper Methods ---------------------------------- */
    
    private OrderInformationDto convertToDto(OrderInformation orderInformation) {
        if (orderInformation == null) {
            return null;
        }
        return new OrderInformationDto(orderInformation.getOrderID(), orderInformation.getOrderDate(), (Integer) orderInformation.getAmount(), (Double) orderInformation.getTotalPrice(), convertToDto(orderInformation.getVisitor()));
    }

    private VisitorDto convertToDto(Visitor visitor) {
        if (visitor == null ) {
            return null;
        }
        return new VisitorDto(visitor.getUsername(), visitor.getPassword());
    }

    private ArrayList<OrderInformationDto> convertToDto(List<OrderInformation> orders) {
        ArrayList<OrderInformationDto> orderDtos = new ArrayList<OrderInformationDto>();
        for (OrderInformation order : orders) {
            orderDtos.add(convertToDto(order));
        }
        return orderDtos;
    }
}
