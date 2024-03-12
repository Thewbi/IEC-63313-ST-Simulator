package instance;

import java.util.TimerTask;

public class TONTimerTask extends TimerTask {

    private TONVariableInstance tonVariableInstance;

    @Override
    public void run() {
        System.out.println("TON timeout!");
        tonVariableInstance.timeout();
    }

    public TONVariableInstance getTonVariableInstance() {
        return tonVariableInstance;
    }

    public void setTonVariableInstance(TONVariableInstance tonVariableInstance) {
        this.tonVariableInstance = tonVariableInstance;
    }
}
