import java.util.*;

public class OrderPricer {
  public PricerResult compute(Map<String, MenuItem> menu, List<OrderLine> orderLines) {
    double sub = 0.0;

    List<LineDisplay> list = new ArrayList<>();
    for (OrderLine l : orderLines) {
      MenuItem item = menu.get(l.itemId);
      double lineTotal = item.price * l.qty;
      sub += lineTotal;
      list.add(new LineDisplay(item.name, l.qty, lineTotal));
    }

    return new PricerResult(sub, list);
  }
}
