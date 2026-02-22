import java.util.*;

public class PricerResult {
    public final double subtotal;
    public final List<LineDisplay> lines;

    public PricerResult(double subtotal, List<LineDisplay> lines) {
        this.subtotal = subtotal;
        this.lines = lines;
    }
}
