public class BillResult {
  public final double taxPct;
  public final double tax;
  public final double discount;
  public final double total;

  public BillResult(double taxPct, double tax, double discount, double total) {
    this.taxPct = taxPct;
    this.tax = tax;
    this.discount = discount;
    this.total = total;
  }
}
