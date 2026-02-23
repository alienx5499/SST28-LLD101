import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final InvoiceStore store;
    private final OrderPricer pricer;
    private final BillCalculator billCalc;
    private final InvoiceBuilder builder;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(InvoiceStore store, OrderPricer pricer, BillCalculator billCalc, InvoiceBuilder builder) {
        this.store = store;
        this.pricer = pricer;
        this.billCalc = billCalc;
        this.builder = builder;
    }

    public void addToMenu(MenuItem i) { menu.put(i.id, i); }

    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        PricerResult pr = pricer.compute(menu, lines);
        BillResult br = billCalc.compute(customerType, pr.subtotal, lines.size());
        String raw = builder.build(invId, pr.lines, pr.subtotal, br.taxPct, br.tax, br.discount, br.total);
        String printable = InvoiceFormatter.identityFormat(raw);
        System.out.print(printable);
        store.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + store.countLines(invId) + ")");
    }
}
