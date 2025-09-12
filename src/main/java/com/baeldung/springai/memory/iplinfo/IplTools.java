package com.baeldung.springai.memory.iplinfo;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IplTools {

    private List<Match> matches;

    @PostConstruct
    public void init() {
        // Load the IPL 2025 schedule data
        loadIPLSchedule();
    }

    private void loadIPLSchedule() {
        matches = new ArrayList<>();

        // Initialize the schedule with the data from the PDF
        // First part of the schedule
        addMatch(1, 1, "22-03-2025", "Sat", "19:30", "Kolkata Knight Riders", "Royal Challengers Bengaluru", "Kolkata");
        addMatch(2, 2, "23-03-2025", "Sun", "15:30", "Sunrisers Hyderabad", "Rajasthan Royals", "Hyderabad");
        addMatch(3, 2, "23-03-2025", "Sun", "19:30", "Chennai Super Kings", "Mumbai Indians", "Chennai");
        addMatch(4, 3, "24-03-2025", "Mon", "19:30", "Delhi Capitals", "Lucknow Super Giants", "Visakhapatnam");
        addMatch(5, 4, "25-03-2025", "Tue", "19:30", "Gujarat Titans", "Punjab Kings", "Ahmedabad");
        addMatch(6, 5, "26-03-2025", "Wed", "19:30", "Rajasthan Royals", "Kolkata Knight Riders", "Guwahati");
        addMatch(7, 6, "27-03-2025", "Thu", "19:30", "Sunrisers Hyderabad", "Lucknow Super Giants", "Hyderabad");
        addMatch(8, 7, "28-03-2025", "Fri", "19:30", "Chennai Super Kings", "Royal Challengers Bengaluru", "Chennai");
        addMatch(9, 8, "29-03-2025", "Sat", "19:30", "Gujarat Titans", "Mumbai Indians", "Ahmedabad");
        addMatch(10, 9, "30-03-2025", "Sun", "15:30", "Delhi Capitals", "Sunrisers Hyderabad", "Visakhapatnam");
        addMatch(11, 9, "30-03-2025", "Sun", "19:30", "Rajasthan Royals", "Chennai Super Kings", "Guwahati");
        addMatch(12, 10, "31-03-2025", "Mon", "19:30", "Mumbai Indians", "Kolkata Knight Riders", "Mumbai");
        addMatch(13, 11, "01-04-2025", "Tue", "19:30", "Lucknow Super Giants", "Punjab Kings", "Lucknow");
        addMatch(14, 12, "02-04-2025", "Wed", "19:30", "Royal Challengers Bengaluru", "Gujarat Titans", "Bengaluru");
        addMatch(15, 13, "03-04-2025", "Thu", "19:30", "Kolkata Knight Riders", "Sunrisers Hyderabad", "Kolkata");
        addMatch(16, 14, "04-04-2025", "Fri", "19:30", "Lucknow Super Giants", "Mumbai Indians", "Lucknow");
        addMatch(17, 15, "05-04-2025", "Sat", "15:30", "Chennai Super Kings", "Delhi Capitals", "Chennai");
        addMatch(18, 15, "05-04-2025", "Sat", "19:30", "Punjab Kings", "Rajasthan Royals", "New Chandigarh");
        addMatch(19, 16, "06-04-2025", "Sun", "15:30", "Kolkata Knight Riders", "Lucknow Super Giants", "Kolkata");
        addMatch(20, 16, "06-04-2025", "Sun", "19:30", "Sunrisers Hyderabad", "Gujarat Titans", "Hyderabad");
        addMatch(21, 17, "07-04-2025", "Mon", "19:30", "Mumbai Indians", "Royal Challengers Bengaluru", "Mumbai");
        addMatch(22, 18, "08-04-2025", "Tue", "19:30", "Punjab Kings", "Chennai Super Kings", "New Chandigarh");
        addMatch(23, 19, "09-04-2025", "Wed", "19:30", "Gujarat Titans", "Rajasthan Royals", "Ahmedabad");
        addMatch(24, 20, "10-04-2025", "Thu", "19:30", "Royal Challengers Bengaluru", "Delhi Capitals", "Bengaluru");
        addMatch(25, 21, "11-04-2025", "Fri", "19:30", "Chennai Super Kings", "Kolkata Knight Riders", "Chennai");
        addMatch(26, 22, "12-04-2025", "Sat", "15:30", "Lucknow Super Giants", "Gujarat Titans", "Lucknow");
        addMatch(27, 22, "12-04-2025", "Sat", "19:30", "Sunrisers Hyderabad", "Punjab Kings", "Hyderabad");
        addMatch(28, 23, "13-04-2025", "Sun", "15:30", "Rajasthan Royals", "Royal Challengers Bengaluru", "Jaipur");
        addMatch(29, 23, "13-04-2025", "Sun", "19:30", "Delhi Capitals", "Mumbai Indians", "Delhi");
        addMatch(30, 24, "14-04-2025", "Mon", "19:30", "Lucknow Super Giants", "Chennai Super Kings", "Lucknow");
        addMatch(31, 25, "15-04-2025", "Tue", "19:30", "Punjab Kings", "Kolkata Knight Riders", "New Chandigarh");
        addMatch(32, 26, "16-04-2025", "Wed", "19:30", "Delhi Capitals", "Rajasthan Royals", "Delhi");
        addMatch(33, 27, "17-04-2025", "Thu", "19:30", "Mumbai Indians", "Sunrisers Hyderabad", "Mumbai");
        addMatch(34, 28, "18-04-2025", "Fri", "19:30", "Royal Challengers Bengaluru", "Punjab Kings", "Bengaluru");
        addMatch(35, 29, "19-04-2025", "Sat", "15:30", "Gujarat Titans", "Delhi Capitals", "Ahmedabad");
        addMatch(36, 29, "19-04-2025", "Sat", "19:30", "Rajasthan Royals", "Lucknow Super Giants", "Jaipur");
        addMatch(37, 30, "20-04-2025", "Sun", "19:30", "Punjab Kings", "Mumbai Indians", "New Chandigarh");

        // Second part of the schedule
        addMatch(38, 30, "20-04-2025", "Sun", "19:30", "Mumbai Indians", "Rajasthan Royals", "Mumbai");
        addMatch(39, 31, "21-04-2025", "Mon", "19:30", "Kolkata Knight Riders", "Sunrisers Hyderabad", "Kolkata");
        addMatch(40, 32, "22-04-2025", "Tue", "19:30", "Lucknow Super Giants", "Punjab Kings", "Lucknow");
        addMatch(41, 33, "23-04-2025", "Wed", "19:30", "Sunrisers Hyderabad", "Lucknow Super Giants", "Hyderabad");
        addMatch(42, 34, "24-04-2025", "Thu", "19:30", "Royal Challengers Bengaluru", "Royal Challengers Bengaluru", "Bengaluru");
        addMatch(43, 35, "25-04-2025", "Fri", "19:30", "Chennai Super Kings", "Gujarat Titans", "Chennai");
        addMatch(44, 36, "26-04-2025", "Sat", "19:30", "Punjab Kings", "Kolkata Knight Riders", "Kolkata");
        addMatch(45, 37, "27-04-2025", "Sun", "15:30", "Mumbai Indians", "Punjab Kings", "Mumbai");
        addMatch(46, 37, "27-04-2025", "Sun", "19:30", "Delhi Capitals", "Mumbai Indians", "Delhi");
        addMatch(47, 38, "28-04-2025", "Mon", "19:30", "Rajasthan Royals", "Sunrisers Hyderabad", "Jaipur");
        addMatch(48, 39, "29-04-2025", "Tue", "19:30", "Delhi Capitals", "Chennai Super Kings", "Delhi");
        addMatch(49, 40, "30-04-2025", "Wed", "19:30", "Chennai Super Kings", "Rajasthan Royals", "Chennai");
        addMatch(50, 41, "01-05-2025", "Thu", "19:30", "Rajasthan Royals", "Lucknow Super Giants", "Jaipur");
        addMatch(51, 42, "02-05-2025", "Fri", "19:30", "Gujarat Titans", "Delhi Capitals", "Ahmedabad");
        addMatch(52, 43, "03-05-2025", "Sat", "19:30", "Royal Challengers Bengaluru", "Gujarat Titans", "Bengaluru");
        addMatch(53, 44, "04-05-2025", "Sun", "15:30", "Kolkata Knight Riders", "Chennai Super Kings", "Kolkata");
        addMatch(54, 44, "04-05-2025", "Sun", "19:30", "Punjab Kings", "Delhi Capitals", "Dharamsala");
        addMatch(55, 45, "05-05-2025", "Mon", "19:30", "Sunrisers Hyderabad", "Royal Challengers Bengaluru", "Hyderabad");
        addMatch(56, 46, "06-05-2025", "Tue", "19:30", "Mumbai Indians", "Kolkata Knight Riders", "Mumbai");
        addMatch(57, 47, "07-05-2025", "Wed", "19:30", "Kolkata Knight Riders", "Mumbai Indians", "Kolkata");
        addMatch(58, 48, "08-05-2025", "Thu", "19:30", "Punjab Kings", "Gujarat Titans", "Dharamsala");
        addMatch(59, 49, "09-05-2025", "Fri", "19:30", "Lucknow Super Giants", "Rajasthan Royals", "Lucknow");
        addMatch(60, 50, "10-05-2025", "Sat", "19:30", "Sunrisers Hyderabad", "Kolkata Knight Riders", "Hyderabad");
        addMatch(61, 51, "11-05-2025", "Sun", "15:30", "Punjab Kings", "Lucknow Super Giants", "Dharamsala");
        addMatch(62, 51, "11-05-2025", "Sun", "19:30", "Delhi Capitals", "Delhi Capitals", "Delhi");
        addMatch(63, 52, "12-05-2025", "Mon", "19:30", "Chennai Super Kings", "Punjab Kings", "Chennai");
        addMatch(64, 53, "13-05-2025", "Tue", "19:30", "Royal Challengers Bengaluru", "Kolkata Knight Riders", "Bengaluru");
        addMatch(65, 54, "14-05-2025", "Wed", "19:30", "Gujarat Titans", "Chennai Super Kings", "Ahmedabad");
        addMatch(66, 55, "15-05-2025", "Thu", "19:30", "Mumbai Indians", "Sunrisers Hyderabad", "Mumbai");
        addMatch(67, 56, "16-05-2025", "Fri", "19:30", "Rajasthan Royals", "Delhi Capitals", "Jaipur");
        addMatch(68, 57, "17-05-2025", "Sat", "19:30", "Royal Challengers Bengaluru", "Punjab Kings", "Bengaluru");
        addMatch(69, 58, "18-05-2025", "Sun", "15:30", "Gujarat Titans", "Kolkata Knight Riders", "Ahmedabad");
        addMatch(70, 58, "18-05-2025", "Sun", "19:30", "Lucknow Super Giants", "Sunrisers Hyderabad", "Lucknow");

        // Playoff matches
        addMatch(71, 60, "20-05-2025", "Tue", "19:30", "TBD", "TBD", "Hyderabad", "Qualifier 1");
        addMatch(72, 61, "21-05-2025", "Wed", "19:30", "TBD", "TBD", "Hyderabad", "Eliminator");
        addMatch(73, 63, "23-05-2025", "Fri", "19:30", "TBD", "TBD", "Kolkata", "Qualifier 2");
        addMatch(74, 65, "25-05-2025", "Sun", "19:30", "TBD", "TBD", "Kolkata", "Final");

    }

    private void addMatch(int matchNumber, int matchDay, String dateStr, String day, String timeStr,
                          String homeTeam, String awayTeam, String venue) {
        addMatch(matchNumber, matchDay, dateStr, day, timeStr, homeTeam, awayTeam, venue, "Regular");
    }

    private void addMatch(int matchNumber, int matchDay, String dateStr, String day, String timeStr,
                          String homeTeam, String awayTeam, String venue, String matchType) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));

        Match match = new Match(matchNumber, matchDay, date, day, time, homeTeam, awayTeam, venue, matchType);
        matches.add(match);
    }

    // Get all matches
    @Tool(description = "Get all matches for IPL 2025")
    public List<Match> getAllMatches() {
        return matches;
    }

    // Get matches by team (either home or away)
    @Tool(description = "Get matches by team (either home or away)")
    public List<Match> getMatchesByTeam(String teamName) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().equalsIgnoreCase(teamName) ||
                        match.getAwayTeam().equalsIgnoreCase(teamName))
                .collect(Collectors.toList());
    }

    // Get matches by date
    @Tool(description = "Get matches by date : format yyyy-MM-dd")
    public List<Match> getMatchesByDate(LocalDate date) {
        return matches.stream()
                .filter(match -> match.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Get matches by date range
    @Tool(description = "Get matches by date range : format yyyy-MM-dd")
    public List<Match> getMatchesByDateRange(LocalDate startDate, LocalDate endDate) {
        return matches.stream()
                .filter(match -> (match.getDate().isEqual(startDate) || match.getDate().isAfter(startDate)) &&
                        (match.getDate().isEqual(endDate) || match.getDate().isBefore(endDate)))
                .collect(Collectors.toList());
    }

    // Get matches by venue
    @Tool(description = "Get matches by venue")
    public List<Match> getMatchesByVenue(String venue) {
        return matches.stream()
                .filter(match -> match.getVenue().equalsIgnoreCase(venue))
                .collect(Collectors.toList());
    }

    // Get match by match number
    @Tool(description = "Get match by match number")
    public Match getMatchByNumber(int matchNumber) {
        return matches.stream()
                .filter(match -> match.getMatchNumber() == matchNumber)
                .findFirst()
                .orElse(null);
    }
}