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
    public class Warehouse {
    private final int id;
    private int stock;

    public Warehouse(int id) {
        this.id = id;
        this.stock = 0;
    }

    public synchronized void addMaterials(int amount) {
        stock += amount;
    }

    public synchronized int getMaterials(int maxAmount) {
        int taken = Math.min(maxAmount, stock);
        stock -= taken;
        return taken;
    }

    public int getId() {
        return id;
    }
}
    public class Freight {
    private final int id;
    private final int maxCapacityPerDay;

    public Freight(int id, int maxCapacityPerDay) {
        this.id = id;
        this.maxCapacityPerDay = maxCapacityPerDay;
    }

    public synchronized int shipProducts(int amount) {
        int shipped = Math.min(amount, maxCapacityPerDay);
        return shipped;
    }

    public int getId() {
        return id;
    }
}
}