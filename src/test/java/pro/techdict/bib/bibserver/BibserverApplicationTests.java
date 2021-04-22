package pro.techdict.bib.bibserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.techdict.bib.bibserver.daos.UserRepository;

@SpringBootTest
class BibserverApplicationTests {

    @Autowired
    UserRepository userRepository;

}
