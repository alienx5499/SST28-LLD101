interface PlagiarismService {
    int check(Submission s);
}

public class PlagiarismChecker implements PlagiarismService {
    public int check(Submission s) {
        // fake score: lower is "better", but pipeline adds it anyway (smell)
        return (s.code.contains("class") ? 12 : 40);
    }
}
