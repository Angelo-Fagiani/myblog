package it.cgmconsulting.myblog.repository;

import java.util.Optional;
import java.util.Set;

import it.cgmconsulting.myblog.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority , Long>{

	Optional<Authority> findByAuthorityName(String authorityName);

	boolean existsByAuthorityName(String authorityName);

	Set<Authority> findByIdIn(Set<Long> ids);

	Set<Authority> findByAuthorityNameIn(Set<String> authorities); // select * from authority where authority_name IN (string1, stringa2, etc..)




}
