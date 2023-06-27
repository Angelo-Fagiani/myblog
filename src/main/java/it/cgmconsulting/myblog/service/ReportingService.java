package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.entity.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportingService {

    @Autowired ReportingRepository reportingRepository;
    @Autowired UserService userService;

    public void save(Reporting r){
        reportingRepository.save(r);
    }

    public Optional<Reporting> findByReportingId(ReportingId rId){
        return reportingRepository.findByReportingId(rId);
    }

    public List<ReportingResponse> getReportings(){
        return reportingRepository.getReportings();
    }


    public ResponseEntity<?> update(Reporting rep, String newStatus, String reason){
        if(rep.getStatus().equals(ReportingStatus.valueOf(newStatus))) {
            return new ResponseEntity("Status has not been modified", HttpStatus.BAD_REQUEST);
        } else if (rep.getStatus().equals(ReportingStatus.OPEN) && ReportingStatus.valueOf(newStatus).equals(ReportingStatus.IN_PROGRESS)) {
            rep.setStatus(ReportingStatus.valueOf(newStatus));
            rep.setReason(new Reason(reason));
        } else if (rep.getStatus().equals(ReportingStatus.IN_PROGRESS) && !ReportingStatus.valueOf(newStatus).equals(ReportingStatus.OPEN)) {
            if (ReportingStatus.valueOf(newStatus).equals(ReportingStatus.CLOSED_WITH_BAN) || ReportingStatus.valueOf(newStatus).equals(ReportingStatus.PERMABAN)){
                userService.disableUser(rep.getReportingId().getComment().getAuthor().getId());
                rep.getReportingId().getComment().setCensored(true);
            }
            rep.setStatus(ReportingStatus.valueOf(newStatus));
            rep.setReason(new Reason(reason));
        }

        return new ResponseEntity("Reporting has been modified", HttpStatus.OK);

    }
}
