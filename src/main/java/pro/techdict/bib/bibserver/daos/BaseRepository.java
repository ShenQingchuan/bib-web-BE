package pro.techdict.bib.bibserver.daos;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import pro.techdict.bib.bibserver.entities.BaseEntity;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<
    T extends BaseEntity<ID>,
    ID extends Serializable
    > extends PagingAndSortingRepository<T, ID> {

  @Override
  @Transactional(readOnly = true)
  @NotNull
  @Query("select e from #{#entityName} e where e.id = ?1 and e.entityStatus = 0")
  Optional<T> findById(@NotNull ID id);

  @Override
  @Transactional(readOnly = true)
  @Query("select case when count(e) > 0 then true else false end from #{#entityName} e where e.id = ?1 and e.entityStatus = 0")
  boolean existsById(@NotNull ID id);

  @Override
  @Transactional(readOnly = true)
  @NotNull
  @Query("select e from #{#entityName} e where e.entityStatus = 0")
  Iterable<T> findAll();

  @Override
  @Transactional(readOnly = true)
  @NotNull
  @Query("select e from #{#entityName} e where e.id = ?1 and e.entityStatus = 0")
  Iterable<T> findAllById(@NotNull Iterable<ID> iterable);

  @Override
  @Transactional(readOnly = true)
  @Query("select count(e) from #{#entityName} e where e.entityStatus = 0")
  long count();

  @Transactional
  @Modifying
  @Query("update #{#entityName} e set e.entityStatus = 1 where e.id = ?1")
  void logicDeleteById(@NotNull ID id);

  @Transactional
  default void logicDelete(@NotNull T t) {
    logicDeleteById(t.getId());
  }

  @Transactional
  default void logicDeleteAll(@NotNull Iterable<? extends T> iterable) {
    iterable.forEach(entity -> logicDeleteById(entity.getId()));
  }

  @Transactional
  @Modifying
  @Query("update #{#entityName} e set e.entityStatus = 1")
  void logicDeleteAll();
}
