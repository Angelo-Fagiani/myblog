package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {

    Optional<Reporting> findByReportingId(ReportingId rId);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.ReportingResponse(" +
            "rep.status," +
            "rep.reportingId.comment.id, " +
            "rep.reporter.username, " +
            "rep.reportingId.comment.author.username, " +
            "rep.reportingId.comment.comment, " +
            "rh.reasonHistoryId.reason.id, " +
            "rep.updatedAt) " +
            "FROM Reporting rep " +
            "INNER JOIN ReasonHistory rh ON rh.reasonHistoryId.reason.id = rep.reason.id " +
            "WHERE ((rh.endDate IS NULL AND rh.reasonHistoryId.startDate <= CURRENT_TIMESTAMP) OR (rep.createdAt BETWEEN rh.reasonHistoryId.startDate AND rh.endDate)) " +
            "ORDER BY rh.severity DESC, rep.updatedAt DESC"
           )
    List<ReportingResponse> getReportings();


}
