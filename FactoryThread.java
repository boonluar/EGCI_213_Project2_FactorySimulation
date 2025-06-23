package Project2_6680081;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FactoryThread extends Thread {
    private final int id;
    private final int maxProduction;
    private final int numDays;
    private final Warehouse[] warehouses;
    private final Freight[] freights;
    private final CyclicBarrier[] barriers; 
    private int totalProduced = 0;
    private int totalShipped = 0;
    private int unshipped = 0;

    private final Random random = new Random();

    public FactoryThread(int id, int maxProduction, int numDays,
                         Warehouse[] warehouses, Freight[] freights,
                         CyclicBarrier[] barriers) {
        this.id = id;
        this.maxProduction = maxProduction;
        this.numDays = numDays;
        this.warehouses = warehouses;
        this.freights = freights;
        this.barriers = barriers;
    }

    @Override
    public void run() {
        for (int day = 1; day <= numDays; day++) {
            try {
                
                barriers[0].await();

                barriers[1].await();

                int producedToday = tryProduce(day);
                totalProduced += producedToday;

                barriers[2].await(); 

                int totalToShip = producedToday + unshipped;
                System.out.printf("Factory %d >> Day %d >> Total to ship = %d\n", id, day, totalToShip);
                barriers[3].await();

                int shipped = tryShip(day, totalToShip);
                totalShipped += shipped;
                unshipped = totalToShip - shipped;

                barriers[4].await(); 

                System.out.printf("Factory %d >> Day %d >> Unshipped left = %d\n", id, day, unshipped);
                barriers[5].await();

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        double percentShipped = (totalProduced == 0) ? 0 : (100.0 * totalShipped / totalProduced);
        System.out.printf("Factory %d >> Shipped %.2f%% of produced products\n", id, percentShipped);
    }

    private int tryProduce(int day) {
        Warehouse wh = warehouses[random.nextInt(warehouses.length)];
        int got = wh.getMaterials(maxProduction);
        System.out.printf("Factory %d >> Day %d >> Got %d materials from Warehouse %d\n", id, day, got, wh.getId());
        return got;
    }

    private int tryShip(int day, int amountToShip) {
        Freight freight = freights[random.nextInt(freights.length)];
        int shipped = freight.shipProducts(amountToShip);
        System.out.printf("Factory %d >> Day %d >> Shipped %d to Freight %d\n", id, day, shipped, freight.getId());
        return shipped;
    }
    
    class Warehouse {
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

    class Freight {
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
}