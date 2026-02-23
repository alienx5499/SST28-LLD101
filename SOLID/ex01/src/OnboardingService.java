import java.util.*;

public class OnboardingService {
    private final StudentStore store;
    private final InputParser parser;
    private final StudentValidator validator;
    private final OnboardingPrinter printer;

    public OnboardingService(StudentStore store, InputParser parser, StudentValidator validator, OnboardingPrinter printer) {
        this.store = store;
        this.parser = parser;
        this.validator = validator;
        this.printer = printer;
    }

    public void registerFromRawInput(String raw) {
        printer.printInput(raw);
        ParsedInput parsed = parser.parse(raw);
        
        List<String> errors = validator.validate(parsed);
        if (!errors.isEmpty()) {
            printer.printValidationErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(store.count());
        StudentRecord rec = new StudentRecord(id, parsed.name, parsed.email, parsed.phone, parsed.program);
        store.save(rec);
        printer.printConfirmation(id, store.count(), rec);
    }
}
