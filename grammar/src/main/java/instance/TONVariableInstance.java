package instance;

import common.TimeUtils;

import java.util.Timer;

public class TONVariableInstance extends VariableInstance {

    private Timer timer;

    private TONTimerTask tonTimerTask;

    public void startTimer() {

        System.out.println("startTimer() startTimer() startTimer() " + getName());

        if (timer != null) {
            throw new RuntimeException("why?");
        }

        VariableInstance variableInstance = this.getElement("PT");

        // convert to millis
        long millis = TimeUtils.timeToMillis(variableInstance.getValue());

        // String countdownDuration = variableInstance.getValue();
        // String countdownDurationSplit[] = countdownDuration.split(" ");
        // long millis = Long.parseLong(countdownDurationSplit[0]);
        // if (StringUtils.equalsIgnoreCase(countdownDurationSplit[1], "Seconds")) {
        //     millis *= 1000;
        // } else {
        //     throw new RuntimeException(countdownDurationSplit[1] + " Not implented yet!");
        // }

        tonTimerTask = new TONTimerTask();
        tonTimerTask.setTonVariableInstance(this);

        timer = new java.util.Timer();

        timer.schedule(
                tonTimerTask,
                millis);
    }

    public void stopTimer() {

        if (timer == null) {
            return;
        }

        System.out.println("stopTimer() stopTimer() stopTimer() " + getName());

        // set the TON output to false
        VariableInstance variableInstance = getElement("Q");
        variableInstance.setValue("false");

        tonTimerTask = null;

        timer.cancel();
        timer.purge();

        timer = null;
    }

    public void timeout() {

        System.out.println("*************************************");
        System.out.println("Timeout callback called!");
        System.out.println("Timeout callback called!");
        System.out.println("Timeout callback called!");
        System.out.println("*************************************");

        // set the TON output to true
        VariableInstance variableInstance = getElement("Q");
        variableInstance.setValue("true");

        tonTimerTask = null;

        timer.cancel();
        timer.purge();

        timer = null;
    }
}
