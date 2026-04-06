public class Demo10 {
  public static void main(String[] args) {
    System.out.println("=== Transport Booking ===");
    TripRequest req =
        new TripRequest("23BCS1010", new GeoPoint(12.97, 77.59), new GeoPoint(12.93, 77.62));
    DistanceCalculator dist = new DefaultDistanceCalculator();
    DriverAllocator alloc = new DefaultDriverAllocator();
    PaymentGateway pay = new DefaultPaymentGateway();
    FareCalculator fareCalc = new DefaultFareCalculator();
    TransportBookingService svc = new TransportBookingService(dist, alloc, pay, fareCalc);
    svc.book(req);
  }
}
