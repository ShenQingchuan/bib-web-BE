package pro.techdict.bib.bibserver.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.techdict.bib.bibserver.entities.UserActivity;

public interface UserActivityRepository extends BaseRepository<UserActivity, Long> {

  @Query(value = "select ua from UserActivity ua where ua.creator.userName = :userName")
  Page<UserActivity> getPageableActivities(@Param("userName") String userName, Pageable pageable);

}
