package Project2_6680081;

/*
    @author Kongphop Kayoonvihcien      ID: 6680081
            Wasupon Wiriyakitanan       ID: 6680646
            Phattarada Limsuchaiwat     ID: 6680684
            Kasidit Boonluar            ID: 6680028
            Jonathan Reppert            ID: 6680701
*/

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        String configPath = "config_1.txt";
        
        ConfigReader config = new ConfigReader(configPath);

        int days = config.getDays();
        int warehouseNum = config.getWarehouseNum();
        int freightNum = config.getFreightNum();
        int freightCap = config.getFreightCapacity();
        int supplierNum = config.getSupplierNum();
        int supplierMin = config.getSupplierMin();
        int supplierMax = config.getSupplierMax();
        int factoryNum = config.getFactoryNum();
        int factoryMax = config.getFactoryMax();

        
        // Create shared resources
        List<Warehouse> warehouses = new ArrayList<>();
        for (int i = 0; i < warehouseNum; i++) {
            warehouses.add(new Warehouse("Warehouse_" + (i + 1)));
        }

        List<Freight> freights = new ArrayList<>();
        for (int i = 0; i < freightNum; i++) {
            freights.add(new Freight("Freight_" + (i + 1), freightCap));
        }
        
        SimulationCoordinator coordinator = new SimulationCoordinator(supplierNum, factoryNum);

        // Create and start threads
        List<Thread> allThreads = new ArrayList<>();

        for (int i = 0; i < supplierNum; i++) {
            Thread supplier = new Thread(new SupplierThread(
                warehouses, supplierMin, supplierMax, days, coordinator
            ), "Supplier_" + (i + 1));
            allThreads.add(supplier);
            supplier.start();
        }

        for (int i = 0; i < factoryNum; i++) {
            Thread factory = new Thread(new FactoryThread(
                warehouses, freights, factoryMax, days, coordinator
            ), "Factory_" + (i + 1));
            allThreads.add(factory);
            factory.start();
        }
        

        // Run day-by-day simulation
        for (int day = 1; day <= days; day++) {
            System.out.println("=".repeat(50) + "\nmain >>  Day " + day);
            printWarehouseStatus(warehouses);
            printFreightStatus(freights);

            coordinator.startDayBarrier.await();
            coordinator.suppliersDoneBarrier.await();
            coordinator.productionDoneBarrier.await();
            coordinator.shippingDoneBarrier.await();
            coordinator.endOfDayBarrier.await();
        }

        // Wait for all threads to finish
        for (Thread t : allThreads) t.join();

        // Summary
        List<FactoryThread> factoryRefs = FactoryThread.getInstances();
        factoryRefs.sort((a, b) -> {
            if (b.getTotalCreated() != a.getTotalCreated())
                return Integer.compare(b.getTotalCreated(), a.getTotalCreated());
            return a.getName().compareTo(b.getName());
        });

        System.out.println("\n=== Factory Performance Report ===");
        for (FactoryThread f : factoryRefs) {
            System.out.printf("%s: Created=%d, Shipped=%d, Shipped%%=%.2f%%\n",
                    f.getName(), f.getTotalCreated(), f.getTotalShipped(),
                    f.getShippingRate());
        }
    }

    private static void printWarehouseStatus(List<Warehouse> warehouses) {
        for (Warehouse w : warehouses) {
            System.out.println("Main: " + w.getName() + " has " + w.getMaterialCount() + " materials.");
        }
    }

    private static void printFreightStatus(List<Freight> freights) {
        for (Freight f : freights) {
            f.reset(); // Reset capacity daily
            System.out.println("Main: " + f.getName() + " has " + f.getRemainingCapacity() + " capacity.");
        }
    }
}
    }
}
