interface ReportWritingService {
    String write(Submission s, int plag, int code);
}

public class ReportWriter implements ReportWritingService {
    public String write(Submission s, int plag, int code) {
        // writes to a pretend file name
        return "report-" + s.roll + ".txt";
    }
}
