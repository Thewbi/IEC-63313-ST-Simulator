package model;

/**
 * https://www.fernhillsoftware.com/help/iec-61131/sequential-function-chart/sfc-step.html
 */
public enum ActionQualifier {

    /* N Non-Stored The action runs while the step is active. */
    N,

    /*
     * L Time Limited The action runs until either of these events occurs first:
     * The timeout completes.
     * The step is deactivated.
     */
    L,

    /*
     * D Time Delayed The action starts after a time delay. The action stops when
     * the step is deactivated. If the step is deactivated before the time delay
     * completes the action does not run.
     */
    D,

    /*
     * P Pulse The action is run once when the step is activated. The action is run
     * again when the step is deactivated.
     */
    P,

    /* P1 Pulse (Rising Edge) The action is run once when the step is activated. */
    P1,

    /*
     * P0 Pulse (Falling Edge) The action is run once when the step is deactivated.
     */
    P0,

    /*
     * S Stored The action runs until a Reset (R) turns off the action. The action
     * continues to run after the step is deactivated.
     */
    S,

    /*
     * SL Stored and Time Limited The action runs until either of these events
     * occurs first:
     * The timeout completes.
     * The action is reset.
     */
    SL,

    /*
     * SD Stored and Time Delayed The action starts after a time delay even if the
     * step is not active. The action is stopped by a Reset (R).
     */
    SD,

    /*
     * DS Delayed and Stored The action starts after a time delay provided the step
     * is still active. The action is stopped by a Reset (R).
     */
    DS,

    /*
     * R Reset Turns off any of these stored actions: S - Stored, SD - Stored and
     * time delayed, DS - Delayed and stored, SL - Stored and time limited
     */
    R

}
