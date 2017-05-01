package acari.io;

import acari.io.pojo.Computer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Component
public class ComputerRepository {
    private static final List<Computer> COMPUTERS = new ArrayList<>();
    private static final Random ranbo = new Random(9001);


    @PostConstruct
    public void setUp() {
        Computer sandwich = buildComputer(16, "Razer", "Blade", "Stealth");
        Computer bladePro = buildComputer(16, "Razer", "Blade", "Pro");
        Computer dellXps13 = buildComputer(8, "Dell", "XPS", "13");
        Computer macPro = buildComputer(16, "Apple", "Macbook", "Pro");
        Computer macAir = buildComputer(8, "Apple", "Macbook", "Air");
        COMPUTERS.add(sandwich);
        COMPUTERS.add(bladePro);
        COMPUTERS.add(dellXps13);
        COMPUTERS.add(macAir);
        COMPUTERS.add(macPro);
    }

    Computer randomComputer() {
        return COMPUTERS.get(ranbo.nextInt(COMPUTERS.size() + 1));
    }

    private Computer buildComputer(int ram, String make, String model, String subModel) {
        return new Computer(ram, model, model, subModel);
    }

}
