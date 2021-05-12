package pro.techdict.bib.bibserver.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<UserAccount, Long> {
    Optional<UserAccount> findByPhone(String phone);
    Optional<UserAccount> findByUserName(String userName);
    Optional<UserAccount> findByEmail(String email);

    @Query("select u from UserAccount u where u.userName like concat('%', :nameLike, '%')")
    List<UserAccount> fetchAllUserByNameLike(@Param("nameLike") String nameLike);
}
