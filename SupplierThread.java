package Project2_6680081;

import java.util.*;
import java.io.*;

/*  for (int i = 0; i < supplierNum; i++) {
            Thread supplier = new Thread(new SupplierThread(
                warehouses, supplierMin, supplierMax, days, coordinator
            ), "Supplier_" + (i + 1));
            allThreads.add(supplier);
            supplier.start();
        }

*/

public class SupplierThread extends Thread {
    private final List<Warehouse> warehouse;
    private final int supplierMin;
    private final int supplierMax;
    private final int totalDays;
    private final SimulationCoordinator coordinator;
    private final Random random;


    public SupplierThread(List<Warehouse> warehouse, int supplierMin, int supplierMax, int days, SimulationCoordinator coordinator) { 
        this.warehouse = warehouse;
        this.supplierMin = supplierMin;
        this.supplierMax = supplierMax;
        this.totalDays = totalDays;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (coordinator == null) {
            throw new IllegalArgumentException("Coordinator must not be null");
            }
        for (int day = 0; day < SimulationCoordinator.days; day++) {
            try {
                // Step 3.1: Wait until day header and warehouse/freight info is printed
                coordinator.dailyStartBarrier.await();

                // Step 3.2: Generate and put random amount of material into a random warehouse
                int amount = supplierMin + random.nextInt(supplierMax - supplierMin + 1);
                int WarehouseNumber = random.nextInt(SimulationCoordinator.warehouses.size()); //random warehouse, no repeat of same warehouse
                SimulationCoordinator.warehouses.get(warehouseNumber).put(amount); //select the warehouse, add the amount

                //System.out.println(getName() + "  >>  put " + amount + " materials    Warehouse_" + warehouseNumber + " balance = %-5d",); //unused output

                // Wait for all SupplierThreads to finish before letting FactoryThreads proceed
                coordinator.supplierDoneBarrier.await();

                // Wait for all FactoryThreads to finish their day too before next cycle
                coordinator.factoryDoneBarrier.await();

            } catch (Exception e) {
                System.out.println(getName() + ": Error - " + e.getMessage());
            }
        }
    }
}
