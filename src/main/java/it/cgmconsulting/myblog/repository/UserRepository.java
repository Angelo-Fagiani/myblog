package it.cgmconsulting.myblog.repository;

import java.util.List;
import java.util.Optional;

import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.response.UserMe;
import it.cgmconsulting.myblog.payload.response.XlsAuthorResponse;
import it.cgmconsulting.myblog.payload.response.XlsReaderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// METODI DERIVATI: restituiscono SOLO entità o collection di entità, oppure primitivi/wrapper

	Optional<User> findByEmail(String email);
	Optional<User> findByIdAndEnabledTrue(long id);
	Optional<User> findByConfirmCode(String confirmCode);

	Optional<User> findByUsernameOrEmail(String username, String email);

	//Optional<User> findByUsernameAndDobBetweenAndEnabledTrue(String username, LocalDate start, LocalDate end);
	// select * from user where username='pippo' AND (dob between '2000-01-01' and '2022-12-31') and enabled=1

	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndEnabledTrue(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	// JPQL : Java Persistent Query Language
	@Query(value="SELECT new it.cgmconsulting.myblog.payload.response.UserMe(" +
			"u.id, " +
			"u.username, " +
			"u.email, " +
			"avatar" +
			") " +
			"FROM User u " +
			"LEFT JOIN Avatar avatar ON u.avatar.id = avatar.id " +
			"WHERE u.id = :id"
	)
	UserMe getMe(@Param("id") long id);

	@Modifying
	@Transactional
	@Query(value="UPDATE user SET enabled=false, updated_at=CURRENT_TIMESTAMP WHERE id = :userId", nativeQuery = true)
	void disableUser(@Param("userId") long userId);

	@Query(value="SELECT rh.severity " +
			"FROM reporting rep " +
			"INNER JOIN reason_history rh ON rep.reason_id = rh.reason_id " +
			"INNER JOIN comment c ON c.id = rep.comment_id " +
			"INNER JOIN user u ON u.id = c.author " +
			"WHERE ((u.updated_at BETWEEN rh.start_date AND rh.end_date) OR (rh.end_date IS NULL)) " +
			"AND u.id = :userId", nativeQuery = true)
	int getSeverity(@Param("userId") long userId);

	@Query(value="SELECT new it.cgmconsulting.myblog.payload.response.XlsAuthorResponse("
			+ "u.id, "
			+ "u.username, "
			+ "(SELECT COUNT(p.id) from Post p WHERE u=p.author) AS writtenPosts, "
			+ "(SELECT COALESCE(ROUND(AVG(r.rate),2), 0) FROM Rating r WHERE r.ratingId.post.author.id=u.id) AS avg "
			+ ") FROM User u "
			+ "INNER JOIN u.authorities a ON a.authorityName='ROLE_EDITOR' "
	)
	List<XlsAuthorResponse> getXlsAuthorResponse();

	@Query(value="SELECT new it.cgmconsulting.myblog.payload.response.XlsReaderResponse(" +
			"u.id, " +
			"u.username, " +
			"(SELECT COUNT(c.id) from Comment c WHERE u=c.author) AS writtenComments, " +
			"(SELECT COUNT(r) FROM Reporting r WHERE r.reportingId.comment.author.id = u.id " +
				"AND r.status " +
				"IN (it.cgmconsulting.myblog.entity.ReportingStatus.PERMABAN, it.cgmconsulting.myblog.entity.ReportingStatus.CLOSED_WITH_BAN)) AS reportingsWithBan," +
			"u.enabled" +
			") FROM User u " +
			"INNER JOIN u.authorities a ON a.authorityName='ROLE_READER' "
	)
	List<XlsReaderResponse> getXlsReaderResponse();

}