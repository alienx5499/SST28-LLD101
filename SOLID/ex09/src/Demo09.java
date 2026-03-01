public class Demo09 {
    public static void main(String[] args) {
        System.out.println("=== Evaluation Pipeline ===");
        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");
        Rubric rubric = new Rubric();
        PlagiarismService pc = new PlagiarismChecker();
        CodeGradingService grader = new CodeGrader();
        ReportWritingService writer = new ReportWriter();

        EvaluationPipeline pipeline = new EvaluationPipeline(pc, grader, writer, rubric);
        pipeline.evaluate(sub);
    }
}
