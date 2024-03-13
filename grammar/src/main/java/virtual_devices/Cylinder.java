package virtual_devices;

import java.util.Timer;

public class Cylinder {

    private static final int POSITION_1 = 0;

    private static final int POSITION_2 = 100;

    private int position = POSITION_1;

    private CylinderCallback cylinderCallback;

    private Timer timer;

    private CylinderTimerTask cylinderTimerTask;

    private long millis = 5000;

    private boolean hasErrorNeverReachesPosition1;

    private boolean hasErrorNeverReachesPosition2;

    public void moveToPosition1() {

        if (position == POSITION_1) {
            return;
        }

        if (!hasErrorNeverReachesPosition1) {

            cylinderCallback.position2Exited();

            cylinderTimerTask = new CylinderTimerTask();
            cylinderTimerTask.setCylinder(this);

            timer = new java.util.Timer();

            timer.schedule(
                cylinderTimerTask,
                    millis);
        }

    }

    public void moveToPosition2() {

        if (position == POSITION_2) {
            return;
        }

        if (!hasErrorNeverReachesPosition2) {

            cylinderCallback.position1Exited();

            cylinderTimerTask = new CylinderTimerTask();
            cylinderTimerTask.setCylinder(this);

            timer = new java.util.Timer();

            timer.schedule(
                cylinderTimerTask,
                    millis);
        }
        
    }

    public void timeout() {
        if (position == POSITION_1) {
            position = POSITION_2;

            cylinderCallback.position2Reached();
            
        } else {
            position = POSITION_1;

            cylinderCallback.position1Reached();
        }

        cylinderTimerTask = null;

        timer.cancel();
        timer.purge();

        timer = null;
    }

    public CylinderCallback getCylinderCallback() {
        return cylinderCallback;
    }

    public void setCylinderCallback(CylinderCallback cylinderCallback) {
        this.cylinderCallback = cylinderCallback;
    }

    public boolean isHasErrorNeverReachesPosition1() {
        return hasErrorNeverReachesPosition1;
    }

    public void setHasErrorNeverReachesPosition1(boolean hasErrorNeverReachesPosition1) {
        this.hasErrorNeverReachesPosition1 = hasErrorNeverReachesPosition1;
    }

    public boolean isHasErrorNeverReachesPosition2() {
        return hasErrorNeverReachesPosition2;
    }

    public void setHasErrorNeverReachesPosition2(boolean hasErrorNeverReachesPosition2) {
        this.hasErrorNeverReachesPosition2 = hasErrorNeverReachesPosition2;
    }
    
}
