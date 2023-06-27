package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, String> {
}
