package virtual_devices;

import java.util.TimerTask;

public class CylinderTimerTask extends TimerTask {

    private Cylinder cylinder;

    @Override
    public void run() {
        System.out.println("TON timeout!");
        cylinder.timeout();
    }

    public Cylinder getCylinder() {
        return cylinder;
    }

    public void setCylinder(Cylinder cylinder) {
        this.cylinder = cylinder;
    }

}
