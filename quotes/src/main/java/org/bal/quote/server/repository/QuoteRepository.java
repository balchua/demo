package org.bal.quote.server.repository;

import org.springframework.cloud.sleuth.SpanName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Integer> {

    @SpanName("quote-find-by-id")
    Optional<QuoteEntity> findById(Integer id);

    @SpanName("quote-find-all")
    List<QuoteEntity> findAll();


}
