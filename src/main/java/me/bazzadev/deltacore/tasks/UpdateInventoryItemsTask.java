package me.bazzadev.deltacore.tasks;

import me.bazzadev.deltacore.managers.StaffGUIManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateInventoryItemsTask extends BukkitRunnable {

    private final StaffGUIManager staffGUIManager;

    public UpdateInventoryItemsTask(StaffGUIManager staffGUIManager) {
        this.staffGUIManager = staffGUIManager;
    }

    @Override
    public void run() {

        if (staffGUIManager.getMainGUI().getViewers().size() < 1) {
            staffGUIManager.setUpdate(false);
            // System.out.println("cancelled task");
            cancel();
        } else if (staffGUIManager.getDoHardRefresh()) {
            staffGUIManager.hardRefresh();
            // System.out.println("refreshed");
        } else {
            staffGUIManager.refresh();
        }

    }
}
