public class EvaluationPipeline {
  // DIP violation: high-level module constructs concretes directly
  private final PlagiarismService plagiarismService;
  private final CodeGradingService codeGradingService;
  private final ReportWritingService reportWritingService;
  private final Rubric rubric;

  public EvaluationPipeline(
      PlagiarismService plagiarismService,
      CodeGradingService codeGradingService,
      ReportWritingService reportWritingService,
      Rubric rubric) {
    this.plagiarismService = plagiarismService;
    this.codeGradingService = codeGradingService;
    this.reportWritingService = reportWritingService;
    this.rubric = rubric;
  }

  public void evaluate(Submission sub) {

    int plag = plagiarismService.check(sub);
    System.out.println("PlagiarismScore=" + plag);

    int code = codeGradingService.grade(sub, rubric);
    System.out.println("CodeScore=" + code);

    String reportName = reportWritingService.write(sub, plag, code);
    System.out.println("Report written: " + reportName);

    int total = plag + code;
    String result = (total >= 90) ? "PASS" : "FAIL";
    System.out.println("FINAL: " + result + " (total=" + total + ")");
  }
}
