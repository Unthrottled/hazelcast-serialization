package acari.io.pojo;

import java.io.Serializable;

public class Computer implements Serializable {
    private final String model;
    private final String subModel;
    private final int ram;
    private final String make;

    public Computer(String model, String subModel, int ram, String make) {
        this.model = model;
        this.subModel = subModel;
        this.ram = ram;
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public String getSubModel() {
        return subModel;
    }

    public int getRam() {
        return ram;
    }

    public String getMake() {
        return make;
    }
}
