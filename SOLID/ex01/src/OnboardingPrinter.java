import java.util.*;

public class OnboardingPrinter {
  public void printInput(String raw) {
    System.out.println("INPUT: " + raw);
  }

  public void printValidationErrors(List<String> errors) {
    System.out.println("ERROR: cannot register");
    for (String e : errors) System.out.println("- " + e);
  }

  public void printConfirmation(String id, int total, StudentRecord rec) {
    System.out.println("OK: created student " + id);
    System.out.println("Saved. Total students: " + total);
    System.out.println("CONFIRMATION:");
    System.out.println(rec);
  }
}
