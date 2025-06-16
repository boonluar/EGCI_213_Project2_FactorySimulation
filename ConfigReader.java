package Project2_6680081;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigReader {
    private int days;
    private int warehouseNum;
    private int freightNum;
    private int freightCapacity;
    private int supplierNum, supplierMin, supplierMax;
    private int factoryNum, factoryMax;
    String path = "src/main/java/Project2_6680081/";

    public ConfigReader(String configPath) {
        readConfigWithRetry(path + configPath);
        printConfig();
    }
    
    //if needed to print in main change variable later
    private void printConfig() {
    System.out.printf("%20s >> %20s Parameters %20s\n", Thread.currentThread().getName(), "=".repeat(20), "=".repeat(20));
    System.out.printf("%20s >> %-18s : %d\n", Thread.currentThread().getName(), "Days of simulation", days);

    // Warehouses
    String[] warehouseNames = new String[warehouseNum];
    for (int i = 0; i < warehouseNum; i++) {warehouseNames[i] = "Warehouse_" + i;}
    String joinedWarehouses = String.join(", ", warehouseNames);
    System.out.printf("%20s >> %-18s : [%s]\n", Thread.currentThread().getName(), "Warehouses", joinedWarehouses);

    // Freights & Freight Capacity
    String[] freightNames = new String[freightNum];
    for (int i = 0; i < freightNum; i++) {freightNames[i] = "Freight_" + i;}
    String joinedFreights = String.join(", ", freightNames);
    System.out.printf("%20s >> %-18s : [%s]\n", Thread.currentThread().getName(), "Freights", joinedFreights);
    System.out.printf("%20s >> %-18s : max = %d\n", Thread.currentThread().getName(), "Freight capacity", freightCapacity);

    // SupplierThreads
    String[] supplierThreads = new String[supplierNum];
    for (int i = 0; i < supplierNum; i++) {supplierThreads[i] = "SupplierThread_" + i;}
    String joinedSuppliers = String.join(", ", supplierThreads);
    System.out.printf("%20s >> %-18s : [%s]\n", Thread.currentThread().getName(), "SupplierThreads", joinedSuppliers);

    // Daily Supply
    System.out.printf("%20s >> %-18s : min = %d, max = %d\n", Thread.currentThread().getName(), "Daily supply", supplierMin, supplierMax);

    // FactoryThreads
    String[] factoryThreads = new String[factoryNum];
    for (int i = 0; i < factoryNum; i++) {factoryThreads[i] = "FactoryThread_" + i;}
    String joinedFactories = String.join(", ", factoryThreads);
    System.out.printf("%20s >> %-18s : [%s]\n", Thread.currentThread().getName(), "FactoryThreads", joinedFactories);

    // Daily Production
    System.out.printf("%20s >> %-18s : max = %d\n", Thread.currentThread().getName(), "Daily production", factoryMax);
}

    private void readConfigWithRetry(String filename) {
    boolean fileOpened = false;

    while (!fileOpened) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            File file = new File(filename);
            if (!file.exists()) throw new FileNotFoundException();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(",");
                String keyword = tokens[0].trim();

                switch (keyword) {
                    case "days":
                        days = Integer.parseInt(tokens[1].trim());
                        break;
                    case "warehouse_num":
                        warehouseNum = Integer.parseInt(tokens[1].trim());
                        break;
                    case "freight_num_max":
                        freightNum = Integer.parseInt(tokens[1].trim());
                        freightCapacity = Integer.parseInt(tokens[2].trim());
                        break;
                    case "supplier_num_min_max":
                        supplierNum = Integer.parseInt(tokens[1].trim());
                        supplierMin = Integer.parseInt(tokens[2].trim());
                        supplierMax = Integer.parseInt(tokens[3].trim());
                        break;
                    case "factory_num_max":
                        factoryNum = Integer.parseInt(tokens[1].trim());
                        factoryMax = Integer.parseInt(tokens[2].trim());
                        break;
                }
            }

            fileOpened = true;

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.err.print("New file name = ");
            Scanner userInput = new Scanner(System.in);
            filename = path + userInput.nextLine().trim(); // Reapply the path here
        }
    }
}

    // Getters
    public int getDays() { return days; }
    public int getWarehouseNum() { return warehouseNum; }
    public int getFreightNum() { return freightNum; }
    public int getFreightCapacity() { return freightCapacity; }
    public int getSupplierNum() { return supplierNum; }
    public int getSupplierMin() { return supplierMin; }
    public int getSupplierMax() { return supplierMax; }
    public int getFactoryNum() { return factoryNum; }
    public int getFactoryMax() { return factoryMax; }
}
