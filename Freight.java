package Project2_6680081;

public class Freight {
    //private final int id;
    private final String name;
    private final int max;
    private int loaded = 0;

    public Freight (String name, int maxCapacity) {
        //this.id = id;
        this.name = name;
        this.max = maxCapacity;
    }
    public synchronized int shipProducts (int amount) {
        int room = max - loaded;
        int toShip = Math.min(room, amount);
        loaded += toShip;
        return toShip;
    }
    public synchronized void reset ()             { loaded = 0; }
    public synchronized int getRemainingCapacity ()       { return max - loaded; }
    public String getName()                       { return name; }
    //public int getId()                            { return id; }
}
