package Project2_6680081;

public class Warehouse {
    private final String name;
    private int balance = 0;
    public Warehouse (String name)               { this.name = name; }
    public synchronized void put (int amount)    { balance += amount; }
    public synchronized int get (int amount) {
        int taken = Math.min (amount, balance);
        balance -= taken;
        return taken;
    }
    public synchronized int getBalance()         { return balance; } 
    public String getName()                      { return name; } 
}
