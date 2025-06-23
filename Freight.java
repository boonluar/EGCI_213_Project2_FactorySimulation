package Project2_6680081;

public class Freight {
    private final String name;
    private final int max;
    private int loaded = 0;

    public Freight (String name, int maxCapacity) {
        this.name = name;
        this.max = maxCapacity;
    }
    public synchronized int ship (int amount) {
        int room = max - loaded;
        int toShip = Math.min(room, amount);
        loaded += toShip;
        return toShip;
    }
    public synchronized void reset ()             { loaded = 0; }
    public synchronized int getRemaining ()       { return max - loaded; }
    public String getName()                       { return name; }
}
