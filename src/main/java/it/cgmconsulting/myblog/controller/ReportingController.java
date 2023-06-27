package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.payload.request.ReportingRequest;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import it.cgmconsulting.myblog.security.CurrentUser;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.CommentService;
import it.cgmconsulting.myblog.service.ReasonService;
import it.cgmconsulting.myblog.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("reporting")
public class ReportingController {

    @Autowired ReportingService reportingService;
    @Autowired CommentService commentService;
    @Autowired ReasonService reasonService;

    @PutMapping
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity<?> save(@RequestBody @Valid ReportingRequest request, @CurrentUser UserPrincipal userPrincipal){

        // recuperare il Comment
        Optional<Comment> c = commentService.findById(request.getCommentId());
        //verificare che commento non sia stato già segnalato
        Optional<Reporting> r = reportingService.findByReportingId(new ReportingId(c.get()));
        if (r.isPresent())
            return new ResponseEntity<>("Il commento è già stato segnalato", HttpStatus.BAD_REQUEST);
        //verifica che l'utente segnalante non sia lo stesso autore del commento
        if(c.get().getAuthor().getId() == userPrincipal.getId())
            return new ResponseEntity<String>("You cannot send a report of this comment", HttpStatus.FORBIDDEN);
        // recupera reason
        String reason = request.getReason().trim().toUpperCase();
        Reason rs = reasonService.getValidReason(reason);
        if(rs == null)
            return new ResponseEntity<String>("Reason not found", HttpStatus.NOT_FOUND);
        //istanziare un Reporting e salvarlo
        Reporting reporting = new Reporting(new ReportingId(c.get()), rs, new User(userPrincipal.getId()));
        reportingService.save(reporting);

        return new ResponseEntity<String>("Report Send", HttpStatus.CREATED);

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> getReportings(){

        List<ReportingResponse> list = reportingService.getReportings();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}/{newStatus}/{reason}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Transactional
    public ResponseEntity<?> update(@PathVariable long commentId, @PathVariable String newStatus, @PathVariable String reason){
        // il moderatore può aggiornare una segnalazione andandone a modificare lo status e/o reason
        // CAMBIO STATUS in quest'ordine : DA open a in_progress a chiuso (3 stati) -> non si può tornare indietro
        // Qualora la segnalazione venga chiusa con PERMABAN o CLOSED_WITH_BAN, l'utente va disabilitato ed il commento censurato.
        Optional<Reporting> rep = reportingService.findByReportingId(new ReportingId(new Comment(commentId)));
        if(rep.isEmpty())
            return new ResponseEntity("Reporting not found", HttpStatus.NOT_FOUND);

       return  reportingService.update(rep.get(), newStatus, reason);

    }

}


