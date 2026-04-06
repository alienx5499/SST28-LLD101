import java.nio.charset.StandardCharsets;

public class CsvExporter extends Exporter {
  @Override
  public ExportResult export(ExportRequest req) {
    if (req == null) return new ExportResult("Request cannot be null");
    String body = req.body == null ? "" : req.body.replace("\n", " ").replace(",", " ");
    String csv = "title,body\n" + (req.title == null ? "" : req.title) + "," + body + "\n";
    return new ExportResult("text/csv", csv.getBytes(StandardCharsets.UTF_8));
  }
}
