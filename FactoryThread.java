package Project2_6680081;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FactoryThread extends Thread {
    private final int id;
    private final int maxProduction;
    private final int numDays;
    //private static final List<FactoryThread> instances = new ArrayList<>();
    private final Warehouse[] warehouses;
    private final Freight[] freights;
    private final CyclicBarrier[] barriers; 
    private int totalCreated = 0;
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
        //instances.add(this);
    }

    @Override
    public void run() {
        for (int day = 1; day <= numDays; day++) {
            try {
                
                barriers[0].await();

                barriers[1].await();

                int producedToday = tryProduce(day);
                totalCreated += producedToday;

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

        double percentShipped = (totalCreated == 0) ? 0 : (100.0 * totalShipped / totalCreated);
        System.out.printf("Factory %d >> Shipped %.2f%% of produced products\n", id, percentShipped);
    }

    private int tryProduce(int day) {
        Warehouse wh = warehouses[random.nextInt(warehouses.length)];
        int got = wh.getMaterials(maxProduction);
        System.out.printf("Factory %d >> Day %d >> Got %d materials from Warehouse %d\n", id, day, got, wh.getId());  //missing id creation in main?
        return got;
    }

    private int tryShip(int day, int amountToShip) {
        Freight freight = freights[random.nextInt(freights.length)];
        int shipped = freight.shipProducts(amountToShip);
        System.out.printf("Factory %d >> Day %d >> Shipped %d to Freight %d\n", id, day, shipped, freight.getId());  //missing id creation in main?
        return shipped;
    }
    /*
    public static List<FactoryThread> getInstances() {
        return instances;
    }*/
    
    public int getTotalCreated() {
        return totalCreated;
    }

    public int getTotalShipped() {
        return totalShipped;
    }

    public int getUnshipped() {
        return unshipped;
    }

    public double getShippingRate() {
        return (totalCreated == 0) ? 0 : (100.0 * totalShipped / totalCreated);
    }

}