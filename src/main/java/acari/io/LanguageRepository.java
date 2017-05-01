package acari.io;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class LanguageRepository {
    private final List<String> LANGUAGES = Lists.newLinkedList(
            Lists.newArrayList("Java", "Groovy", "Go", "Clojure", "Lisp", "Ruby",
                    "Javascript", "C++", "C", "C#", "Perl", "Cobol", "R", "Matlab"));
    private final Random ranbo = new Random(9002);

    List<String> randomLanguages() {
        return LANGUAGES.stream().filter(s -> ranbo.nextBoolean()).collect(Collectors.toList());
    }

}
