import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
  @Override
  public ExportResult export(ExportRequest req) {
    if (req == null) return new ExportResult("Request cannot be null");
    if (req.body != null && req.body.length() > 20) {
      return new ExportResult("PDF cannot handle content > 20 chars");
    }
    String fakePdf = "PDF(" + req.title + "):" + (req.body == null ? "" : req.body);
    return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
  }
}
