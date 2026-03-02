import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;
import java.util.*;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        IncidentTicket t = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + t);

        // Demonstrate post-creation mutation through service
        IncidentTicket assigned = service.assign(t, "agent@example.com");
        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\nAfter service updates (new instances): " + escalated);

        // Demonstrate external mutation via leaked list reference
        List<String> externalTags = new ArrayList<>(escalated.getTags());
        externalTags.add("HACKED_FROM_OUTSIDE");
        System.out.println("\nExternal tags list mutated: " + externalTags);
        System.out.println("Ticket tags remain: " + escalated.getTags());

        // Starter compiles; after refactor, you should redesign updates to create new objects instead.
    }
}
