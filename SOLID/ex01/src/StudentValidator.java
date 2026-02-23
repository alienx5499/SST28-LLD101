import java.util.*;

public class StudentValidator {
    public List<String> validate(ParsedInput in) {
        List<String> errors = new ArrayList<>();
        if (in.name.isBlank()) errors.add("name is required");
        if (in.email.isBlank() || !in.email.contains("@")) errors.add("email is invalid");
        if (in.phone.isBlank() || !isAllDigits(in.phone)) errors.add("phone is invalid");
        if (!isAllowedProgram(in.program)) errors.add("program is invalid");
        return errors;
    }

    private boolean isAllDigits(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private boolean isAllowedProgram(String program) {
        return program.equals("CSE") || program.equals("AI") || program.equals("SWE");
    }
}
