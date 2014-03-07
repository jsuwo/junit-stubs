import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OrderProcessorTest {

  public class WarehouseInStockStub implements Warehouse {

    public boolean fill(Order order) {
      return true;
    }

  }

  public class WarehouseOutOfStockStub implements Warehouse {
    
    public boolean fill(Order order) {
      return false;
    }

  }

  public class MailerServiceStub implements MailerService {
  
    private List<Message> messages = new ArrayList<Message>();
  
    public void send(Message msg) {
      messages.add(msg);
    }
  
    public int numberSent() {
      return messages.size();
    }

  }        

  private MailerServiceStub mailer;
  private Warehouse inStockWarehouse;
  private Warehouse outOfStockWarehouse;
  private Order order;

  @Before
  public void setup() {
    mailer = new MailerServiceStub();
    inStockWarehouse = new WarehouseInStockStub();
    outOfStockWarehouse = new WarehouseOutOfStockStub();
    order = new Order("jeff@example.com", "Xbox One");
  }

  @Test
  public void testProcessOrderSendsEmailToCustomerUponFillingOrder() throws Exception {
    OrderProcessor processor = new OrderProcessor(inStockWarehouse, mailer);
    processor.processOrder(order);
    assertEquals(1, mailer.numberSent());
  }

  @Test(expected=Exception.class)
  public void testProcessOrderThrowsExceptionIfWarehouseOutOfStock() throws Exception {
    OrderProcessor processor = new OrderProcessor(outOfStockWarehouse, mailer);
    processor.processOrder(order);
  }

}
