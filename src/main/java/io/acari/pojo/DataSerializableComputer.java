package io.acari.pojo;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class DataSerializableComputer implements DataSerializable {
    private String model;
    private String subModel;
    private int ram;
    private String make;

    /**
     * No Arguments constructor is needed only if
     * the class does not have one and a constructor
     * with one or more arguments is present.
     * <p>
     * If no constructors are provided the java compiler
     * will automagically put the no args constructor in.
     */
    public DataSerializableComputer() {
    }

    public DataSerializableComputer(Computer computer) {
        this.model = computer.getModel();
        this.subModel = computer.getSubModel();
        this.ram = computer.getRam();
        this.make = computer.getMake();
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(model);
        out.writeUTF(subModel);
        out.writeInt(ram);
        out.writeUTF(make);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        model = in.readUTF();
        subModel = in.readUTF();
        ram = in.readInt();
        make = in.readUTF();
    }
}
