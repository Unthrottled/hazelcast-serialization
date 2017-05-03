package io.acari.pojo;

import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class IdentifiedDataSerializableProgrammer extends DataSerializableProgrammer implements IdentifiedDataSerializable {

    public static final int FACTORY_ID = 9000;
    public static final int OBJECT_ID = 9001;

    public IdentifiedDataSerializableProgrammer() {
        super();
    }

    public IdentifiedDataSerializableProgrammer(Programmer programmer) {
        super(programmer);
    }

    @Override
    public int getFactoryId() {
        return FACTORY_ID;
    }

    @Override
    public int getId() {
        return OBJECT_ID;
    }
}
