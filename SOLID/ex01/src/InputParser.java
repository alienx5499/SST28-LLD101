import java.util.*;

public class InputParser {
  public ParsedInput parse(String raw) {
    Map<String, String> kv = new HashMap<>();

    String[] parts = raw.split(";");
    for (String p : parts) {
      String[] t = p.split("=", 2);
      if (t.length == 2) kv.put(t[0].trim(), t[1].trim());
    }

    String name = kv.get("name");
    if (name == null) name = "";

    String email = kv.get("email");
    if (email == null) email = "";

    String phone = kv.get("phone");
    if (phone == null) phone = "";

    String program = kv.get("program");
    if (program == null) program = "";

    return new ParsedInput(name, email, phone, program);
  }
}
