package acari.io.pojo;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ExternalizableProgrammer implements Externalizable {
    private static final long serialVersionUID = 6757860161913660513L;
    private String name;
    private int age;
    private ExternalizableComputer computer;
    private List<String> languages;

    public ExternalizableProgrammer() {
    }

    public ExternalizableProgrammer(Programmer programmer) {
        this.name = programmer.getName();
        this.age = programmer.getAge();
        this.computer = new ExternalizableComputer(programmer.getComputer());
        this.languages = programmer.getLanguages();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        int size = languages == null ? -1 : languages.size();
        out.writeInt(size);

        for (int i = 0; i < size; ++i) {
            out.writeUTF(languages.get(i));
        }
        computer.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
        age = in.readInt();
        int size = in.readInt();
        if (size > -1) {
            languages = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                languages.add(i, in.readUTF());
            }
        }
        computer = new ExternalizableComputer();
        computer.readExternal(in);
    }

    public String getName() {
        return name;
    }
}
