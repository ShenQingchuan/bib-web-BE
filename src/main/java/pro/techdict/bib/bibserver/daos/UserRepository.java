package pro.techdict.bib.bibserver.daos;

import org.springframework.data.repository.CrudRepository;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUid(Long uid);
    Optional<UserAccount> findByPhone(String phone);
    Optional<UserAccount> findByUserName(String userName);
    Optional<UserAccount> findByEmail(String email);
}
