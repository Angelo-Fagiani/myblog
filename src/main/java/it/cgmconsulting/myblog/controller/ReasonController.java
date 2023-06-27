package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.ReasonHistory;
import it.cgmconsulting.myblog.payload.request.ReasonRequest;
import it.cgmconsulting.myblog.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("reason")
public class ReasonController {

    @Autowired ReasonService reasonService;

    @PutMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> saveUpdateReason(@RequestBody @Valid ReasonRequest request){
        return new ResponseEntity(reasonService.save(request), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity<List<String>> getNotExpiredResaon(){
        // La chiamata recupera tutte le reason in corso di validità ovvero quelle con endDate settato a null
        return new ResponseEntity(reasonService.getReasonHistoryByEndDateIsNull(), HttpStatus.OK);
    }

    @GetMapping("/valid")
    @PreAuthorize("hasRole('ROLE_READER')")
    public ResponseEntity<?> getNotExpiredResaonHistory(){
        // La chiamata recupera tutte le reason in corso di validità ovvero quelle con endDate settato a null
        List<ReasonHistory> list = reasonService.findByEndDateNull();
        return new ResponseEntity(list, HttpStatus.OK); // va in errore la trasformazione in json in quanto non riesce a recuperare l'id da Reason, essendo la relazione di tipo LAZY
    }
}
