package org.bal.quote.server.repository;

import org.bal.quote.annotation.Traced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Integer> {

    @Traced(serviceName = "quote-find-by-id")
    Optional<QuoteEntity> findById(Integer id);

    @Traced(serviceName = "quote-find-all")
    List<QuoteEntity> findAll();


}
