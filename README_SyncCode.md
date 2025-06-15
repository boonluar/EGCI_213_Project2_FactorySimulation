
# How to work together

@author kongphop kayoonvichein 6680081

To keep our code stable and ensure the project runs smoothly, I kindly ask everyone to give their full cooperation and effort. If we all do our part, I believe we can absolutely achieve an A together.

We’ll be sharing a common repository to collaborate. Please only upload the code that you are responsible for — do not overwrite or modify someone else’s work without discussion.

### How does this method work?

Case 1: your file that not exist in the repository, for example, 

    class Warehouse in Warehouse.java, 
    class SupplierThread in SupplierThread.java. 
    
Simply enough just uplaod them in the repository.

Case 2: you needed to edit the existing file, for example

    Mainclass.java

Just change the file name then upload it, for example

    Mainclass_Warehouse.java
    Mainclass_SupplierThread.java
    Mainclass_[Name: Java].java

# How to Use SimulationCoordinator in Project2 Paradigm

The SimulationCoordinator class is created to synchronize activities between the following threads in our simulation, 
"Main thread", 
"SupplierThreads",
"FactoryThreads".

It helps coordinate day-by-day operations so
Suppliers start only after day status is printed.
Factories wait until suppliers finish.
Shipping happens after all production is done.
"All threads wait at the end of the day before starting the next".

### How to Use:

1.) Get Coordinator from Main.java

example in Main.java

    SimulationCoordinator coordinator = new SimulationCoordinator(suppliers, factories);

Then pass it into all thread constructors:

    new Thread(new SupplierThread(..., coordinator), "Supplier-1").start();
    new Thread(new FactoryThread(..., coordinator), "Factory-1").start();

2.) Use It in Your Loop (Supplier or Factory)

    for (int day = 1; day <= totalDays; day++) {
    coordinator.startDayBarrier.await();          // Wait for main to print day
    
    // Do your supplier or factory work here...

    coordinator.suppliersDoneBarrier.await();     // After suppliers put
    coordinator.productionDoneBarrier.await();    // After production
    coordinator.shippingDoneBarrier.await();      // After shipping
    coordinator.endOfDayBarrier.await();          // End of the day
}

### Clean code! jub2

Do not create your own barriers.

Always use the SimulationCoordinator passed to your class.

Fail early if coordinator is null:

    if (coordinator == null) {
    throw new IllegalArgumentException("Coordinator must not be null");
    }







