package io.acari.pojo;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSerializableProgrammer implements DataSerializable {
    public static final int NULL_LIST = -1;
    private String name;
    private int age;
    private DataSerializableComputer computer;
    private List<String> languages;

    /**
     * No Arguments constructor is needed only if
     * the class does not have one and a constructor
     * with one or more arguments is present.
     * <p>
     * If no constructors are provided the java compiler
     * will automagically put the no args constructor in.
     */
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
        int size = languages == null ? NULL_LIST : languages.size();
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
        if (size > NULL_LIST) {
            languages = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                languages.add(i, in.readUTF());
            }
        }
        computer = new DataSerializableComputer();
        computer.readData(in);
    }
}
