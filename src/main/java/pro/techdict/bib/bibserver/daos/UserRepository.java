package pro.techdict.bib.bibserver.daos;

import org.springframework.stereotype.Repository;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.Optional;

public interface UserRepository extends BaseRepository<UserAccount, Long> {
    Optional<UserAccount> findByPhone(String phone);
    Optional<UserAccount> findByUserName(String userName);
    Optional<UserAccount> findByEmail(String email);
}
