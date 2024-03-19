package virtual_devices;

public interface CylinderCallback {

    void position1Exited();

    void position1Reached();

    void position2Exited();

    void position2Reached();

    void setCylinder(Cylinder cylinder);
    
}
