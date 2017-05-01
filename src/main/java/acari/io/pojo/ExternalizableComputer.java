package acari.io.pojo;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExternalizableComputer implements Externalizable {
    private String model;
    private String subModel;
    private int ram;
    private String make;

    public ExternalizableComputer() {
    }

    public ExternalizableComputer(Computer computer) {
        this.model = computer.getModel();
        this.subModel = computer.getSubModel();
        this.ram = computer.getRam();
        this.make = computer.getMake();
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeUTF(model);
        objectOutput.writeUTF(subModel);
        objectOutput.writeInt(ram);
        objectOutput.writeUTF(make);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        model = objectInput.readUTF();
        subModel = objectInput.readUTF();
        ram = objectInput.readInt();
        make = objectInput.readUTF();
    }
}
