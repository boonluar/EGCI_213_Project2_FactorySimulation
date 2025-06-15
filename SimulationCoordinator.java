package Project2_6680081;

import java.util.concurrent.CyclicBarrier;

public class SimulationCoordinator {
    public final CyclicBarrier startDayBarrier;
    public final CyclicBarrier suppliersDoneBarrier;
    public final CyclicBarrier productionDoneBarrier;
    public final CyclicBarrier shippingDoneBarrier;
    public final CyclicBarrier endOfDayBarrier;

    public SimulationCoordinator(int suppliers, int factories) {
        int totalThreads = suppliers + factories;

        // +1 for main thread
        startDayBarrier = new CyclicBarrier(totalThreads + 1);
        suppliersDoneBarrier = new CyclicBarrier(totalThreads + 1);
        productionDoneBarrier = new CyclicBarrier(totalThreads + 1);
        shippingDoneBarrier = new CyclicBarrier(totalThreads + 1);
        endOfDayBarrier = new CyclicBarrier(totalThreads + 1);
    }
}
