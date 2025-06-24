package Project2_6680081;

public class Warehouse {
    //private final int id;
    private final String name;
    private int balance = 0;
    private int totalAdded = 0;
    public Warehouse (String name)               { this.name = name; }
    public synchronized void put (int amount)    { balance += amount; 
        totalAdded += amount;}
    public synchronized int getMaterials (int amount) {
        int taken = Math.min (amount, balance);
        balance -= taken;
        return taken;
    }
    public synchronized int getMaterialCount()         { return balance; } 
    public synchronized int getTotalAdded()      { return totalAdded; }
    public String getName()                      { return name; } 
    //public int getId()                           { return id; }
    
}
