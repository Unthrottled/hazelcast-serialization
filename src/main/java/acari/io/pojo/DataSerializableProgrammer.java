package acari.io.pojo;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSerializableProgrammer implements DataSerializable {
    private String name;
    private int age;
    private DataSerializableComputer computer;
    private List<String> languages;

    public DataSerializableProgrammer() {
    }

    public DataSerializableProgrammer(Programmer programmer) {
        this.name = programmer.getName();
        this.age = programmer.getAge();
        this.computer = new DataSerializableComputer(programmer.getComputer());
        this.languages = programmer.getLanguages();
    }

    public String getName() {
        return name;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        int size = languages == null ? -1 : languages.size();
        out.writeInt(size);

        for (int i = 0; i < size; ++i) {
            out.writeUTF(languages.get(i));
        }
        computer.writeData(out);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        name = in.readUTF();
        age = in.readInt();
        int size = in.readInt();
        if (size > -1) {
            languages = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                languages.add(i, in.readUTF());
            }
        }
        computer = new DataSerializableComputer();
        computer.readData(in);
    }
}
