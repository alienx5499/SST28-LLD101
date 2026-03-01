public interface ClubAdminTools {
    // Fat interface (ISP violation)
}

interface FinanceTools extends ClubAdminTools {
    void addIncome(double amt, String note);
    void addExpense(double amt, String note);
}

interface MinutesTools extends ClubAdminTools {
    void addMinutes(String text);
}

interface EventTools extends ClubAdminTools {
    void createEvent(String name, double budget);
    int getEventsCount();
}
