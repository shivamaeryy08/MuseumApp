package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.OrderInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
public class TestOrderService {

  @Mock
  private OrderInformationRepository orderInformationRepository;

  @Mock
  private MuseumInformationRepository museumInformationRepository;

  @InjectMocks
  private OrderService orderService;

  private static final Long id = 100L;
  private static final Visitor visitor_test = new Visitor(
    "name",
    "pass"
  );

  private static final int amount_test = 1;
  private static final OrderInformation test_1 = new OrderInformation();
  private static final OrderInformation test_2 = new OrderInformation();

  private static final String NULL_Visitor = "Visitor cannot be null! ";
  private static final String Negative_Order_Amount =
    "Order amount cannot be negative! ";
  private static final String Zero_Order_Amount =
    "Order amount cannot be Zero! ";

  @BeforeEach
  public void setMockOutput() {

      /*Museum information creation */
      Answer<?> returnMuseumInfo = (InvocationOnMock invocation) -> {
        MuseumInformation museumInformation = new MuseumInformation();
                    museumInformation.setVisitorFee (10.50);
                    museumInformation.setMuseumName("Museum"); 
                    museumInformation.setMondayHours("10:00-13:00");
                    museumInformation.setTuesdayHours("10:00-13:00");
                    museumInformation.setWednesdayHours("10:00-13:00");
                    museumInformation.setThursdayHours("10:00-13:00");
                    museumInformation.setFridayHours("10:00-13:00");
                    museumInformation.setSaturdayHours("10:00-13:00");
                    museumInformation.setSundayHours("10:00-13:00");
                    List<MuseumInformation> iterable = new ArrayList<MuseumInformation>();
                    iterable.add(museumInformation);
                    return iterable;
    };
    lenient().when(museumInformationRepository.findAll()).thenAnswer(returnMuseumInfo);
    lenient()
      .when(orderInformationRepository.findOrderInformationByOrderID(anyLong()))
      .thenAnswer((InvocationOnMock invocation) -> {
        if (invocation.getArgument(0).equals(id)) {
          OrderInformation orderInformation = new OrderInformation();
          orderInformation.setOrderID(id);
          return orderInformation;
        } else {
          return null;
        }
      });
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };
    lenient()
      .when(orderInformationRepository.save(any(OrderInformation.class)))
      .thenAnswer(returnParameterAsAnswer);
    lenient()
      .doNothing()
      .when(orderInformationRepository)
      .delete(any(OrderInformation.class));
    lenient()
      .when(
        orderInformationRepository.findOrderInformationByVisitor(
          any(Visitor.class)
        )
      )
      .thenAnswer((InvocationOnMock invocation) -> {
        if (invocation.getArgument(0).equals(visitor_test)) {
          List<OrderInformation> orderList_test = new ArrayList<OrderInformation>();
          test_1.setVisitor(visitor_test);
          test_2.setVisitor(visitor_test);
          orderList_test.add(test_1);
          orderList_test.add(test_2);
          return orderList_test;
        } else {
          return null;
        }
      });
  }

  @Test
  public void testCreateOrderInformation() {
    Visitor visitor = new Visitor();
    visitor.setUsername("username");
    try {
      orderService.createOrder(amount_test, visitor);
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  @Test
  public void testCreateOrderInformationNullParameters() {
    String error = "";
    try {
      orderService.createOrder(amount_test, null);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals(NULL_Visitor, error);
  }

  @Test
  public void testCreateOrderInformationNegativeAmount() {
    String error = "";

    try {
      orderService.createOrder(-1, new Visitor());
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals(Negative_Order_Amount, error);
  }

  @Test
  public void testCreateOrderInformationNillAmount() {
    String error = "";

    try {
      orderService.createOrder(0, new Visitor());
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals(Zero_Order_Amount, error);
  }

  @Test
  public void testGetOrderByID() {
    OrderInformation orderInformation = null;
    try {
      orderInformation = orderService.getOrderByID(id);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(orderInformation);
    assertEquals(id, orderInformation.getOrderID());
  }

  @Test
  public void testGetOrderByInvalidID() {
    String error = "";
    OrderInformation order = null;
    try {
      order = orderService.getOrderByID(Long.MAX_VALUE);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(order);
    assertEquals("An order with the given ID cannot be found!", error);
  }

  @Test
  public void getOrdersByVisitor() {
    List<OrderInformation> orderInformationList = null;

    List<OrderInformation> orderList_assert = new ArrayList<OrderInformation>();
    orderList_assert.add(test_1);
    orderList_assert.add(test_2);

    try {
      orderInformationList = orderService.getOrdersByVisitor(visitor_test);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(visitor_test);
    assertEquals(orderInformationList, orderList_assert);
  }

  @Test
  public void getOrdersByInvalidVisitor() {
    String error = "";
    List<OrderInformation> orderList_assert = null;
    try {
      orderList_assert = orderService.getOrdersByVisitor(null);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(orderList_assert);
    assertEquals("Visitor cannot be null!", error);
  }
}
