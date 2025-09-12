//package com.baeldung.springai.memory.iplinfo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ipl/schedule")
//public class ScheduleController {
//
//    private final IplTools scheduleService;
//
//    @Autowired
//    public ScheduleController(IplTools scheduleService) {
//        this.scheduleService = scheduleService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Match>> getAllMatches() {
//        return ResponseEntity.ok(scheduleService.getAllMatches());
//    }
//
//    @GetMapping("/match/{matchNumber}")
//    public ResponseEntity<Match> getMatchByNumber(@PathVariable int matchNumber) {
//        Match match = scheduleService.getMatchByNumber(matchNumber);
//        if (match != null) {
//            return ResponseEntity.ok(match);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/team/{teamName}")
//    public ResponseEntity<List<Match>> getMatchesByTeam(@PathVariable String teamName) {
//        List<Match> matches = scheduleService.getMatchesByTeam(teamName);
//        return ResponseEntity.ok(matches);
//    }
//
//    @GetMapping("/date/{date}")
//    public ResponseEntity<List<Match>> getMatchesByDate(
//            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
//        List<Match> matches = scheduleService.getMatchesByDate(date);
//        return ResponseEntity.ok(matches);
//    }
//
//    @GetMapping("/date-range")
//    public ResponseEntity<List<Match>> getMatchesByDateRange(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
//        List<Match> matches = scheduleService.getMatchesByDateRange(startDate, endDate);
//        return ResponseEntity.ok(matches);
//    }
//
//    @GetMapping("/venue/{venue}")
//    public ResponseEntity<List<Match>> getMatchesByVenue(@PathVariable String venue) {
//        List<Match> matches = scheduleService.getMatchesByVenue(venue);
//        return ResponseEntity.ok(matches);
//    }
//}