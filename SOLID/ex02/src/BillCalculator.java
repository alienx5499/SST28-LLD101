public class BillCalculator {
  public BillResult compute(String customerType, double subtotal, int lineCount) {
    double taxPct = TaxRules.taxPercent(customerType);
    double tax = subtotal * (taxPct / 100.0);
    double discount = DiscountRules.discountAmount(customerType, subtotal, lineCount);
    double total = subtotal + tax - discount;
    return new BillResult(taxPct, tax, discount, total);
  }
}
