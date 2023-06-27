package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonHistory;
import it.cgmconsulting.myblog.entity.ReasonHistoryId;
import it.cgmconsulting.myblog.payload.request.ReasonRequest;
import it.cgmconsulting.myblog.repository.ReasonHistoryRepository;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReasonService {

    @Autowired ReasonRepository reasonRepository;
    @Autowired ReasonHistoryRepository reasonHistoryRepository;

    @Transactional
    public String save(ReasonRequest request){
        // inserimento nuova reason:
        // a) scrivere record su tabella reason
        // b) scrivere record su tabella reason_history

        // aggiornamento di una reason:
        // a) scrivere un nuovo record su reason_history
        // b) settare end_date dell'ultimo record inserito (relativamente alla reason cercata) col giorno prima della start_date

        String msg = "";
        ReasonHistory rh = reasonHistoryRepository.getReasonHistoryByReason(request.getReason());
        if(rh == null){ // inserimento nuova reason
            Reason r = reasonRepository.save(new Reason(request.getReason()));
            save(new ReasonHistory(new ReasonHistoryId(r, request.getStartDate()), request.getSeverity()));
            msg = "New reason added";
        } else { // aggiornamento della reason trovata
            if(rh.getSeverity() != request.getSeverity()) {
                if((rh.getEndDate() != null && LocalDate.from(request.getStartDate()).isAfter(rh.getEndDate())) ||
                        (rh.getEndDate() == null && LocalDate.from(request.getStartDate()).isAfter(rh.getReasonHistoryId().getStartDate()))) {
                    rh.setEndDate(LocalDate.from(request.getStartDate()).minus(1, ChronoUnit.DAYS));
                    save(new ReasonHistory(new ReasonHistoryId(rh.getReasonHistoryId().getReason(), request.getStartDate()), request.getSeverity()));
                    msg = "Reason " + request.getReason() + " has been updated";
                } else {
                    msg = "Invalid start date";
                }
            } else {
                msg = "Same severity: nothing to update";
            }

        }
        return msg;
    }

    public void save(ReasonHistory rh){
        reasonHistoryRepository.save(rh);
    }

    public List<String> getReasonHistoryByEndDateIsNull(){
        return reasonHistoryRepository.getReasonHistoryByEndDateIsNull();
    }

    public List<ReasonHistory> findByEndDateNull(){
        return reasonHistoryRepository.findByEndDateNull();
    }

    public Reason getValidReason(String reason){
        return reasonHistoryRepository.getValidReason(reason);
    }
}
