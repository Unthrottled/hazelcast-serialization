package io.acari.pojo;

import java.io.Serializable;

public class Computer implements Serializable {
    private static final long serialVersionUID = -2198928914280590576L;
    private final String model;
    private final String subModel;
    private final int ram;
    private final String make;

    public Computer(int ram, String make, String model, String subModel) {
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
