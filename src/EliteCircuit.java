
import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class EliteCircuit {

    // SYSTEM SETTINGS
    static final int MAX_TEAMS = 8;
    static final int PLAYERS_PER_TEAM = 5;
    static final int MAX_MATCHES = 100;

    // INPUT AND RANDOM GENERATOR
    static Scanner input = new Scanner(System.in);
    static Random random = new Random();

    // TEAM ARRAYS
    static String[] teamID = new String[MAX_TEAMS];
    static String[] teamName = new String[MAX_TEAMS];
    static String[][] playerNames =
            new String[MAX_TEAMS][PLAYERS_PER_TEAM];

    // TEAM STATISTICS
    static int[] wins = new int[MAX_TEAMS];
    static int[] losses = new int[MAX_TEAMS];
    static int[] points = new int[MAX_TEAMS];
    static int[] totalKills = new int[MAX_TEAMS];
    static int[] totalDeaths = new int[MAX_TEAMS];

    // AVAILABLE MAPS
    static String[] maps = {
        "Raid",
        "Nuketown",
        "Summit",
        "Firing Range",
        "Standoff"
    };

    // MATCH HISTORY
    // 0 = Team A
    // 1 = Team B
    // 2 = Round 1 Map
    // 3 = Team A Round 1 Kills
    // 4 = Team B Round 1 Kills
    // 5 = Round 2 Map
    // 6 = Team A Round 2 Kills
    // 7 = Team B Round 2 Kills
    // 8 = Round 3 Map
    // 9 = Team A Round 3 Kills
    // 10 = Team B Round 3 Kills
    // 11 = Winning Team
    // 12 = Rounds Played

    static int[][] matchHistory =
            new int[MAX_MATCHES][13];

    // SYSTEM COUNTERS
    static int teamCount = 0;
    static int matchCount = 0;

    // MAIN METHOD
    public static void main(String[] args) {

        loadData();

        int choice;

        do {
            displayMenu();

            choice = getIntInput("Enter your choice: ", 1, 9);

            switch (choice) {

                case 1:
                    registerTeam();
                    break;

                case 2:
                    manageRoster();
                    break;

                case 3:
                    displayTeams();
                    break;

                case 4:
                    searchTeam();
                    break;

                case 5:
                    simulateMatch();
                    break;

                case 6:
                    displayMatchHistory();
                    break;

                case 7:
                    displayStandings();
                    break;

                case 8:
                    displayStatistics();
                    break;

                case 9:
                    saveData();
                    System.out.println("\nExiting OPERATION: ELITE CIRCUIT...");
                    break;
            }

            if (choice != 9) {
                System.out.println("\nPress Enter to continue...");
                input.nextLine();
            }

        } while (choice != 9);
    }

    // DISPLAY MAIN MENU
    public static void displayMenu() {

        System.out.println("\n========================================");
        System.out.println("       OPERATION: ELITE CIRCUIT");
        System.out.println("       FPS ESPORTS TOURNAMENT SYSTEM");
        System.out.println("========================================");

        System.out.println("[1] Register New Squad");
        System.out.println("[2] Manage Player Roster");
        System.out.println("[3] View Registered Squads");
        System.out.println("[4] Search Squad");
        System.out.println("[5] Launch Match Simulation");
        System.out.println("[6] View Match History");
        System.out.println("[7] Tournament Standings");
        System.out.println("[8] Tournament Statistics");
        System.out.println("[9] Save and Exit");

        System.out.println("========================================");
    }

    // GET VALID INTEGER INPUT
    public static int getIntInput(String message, int min, int max) {

        int number;

        while (true) {

            System.out.print(message);
            String value = input.nextLine().trim();

            try {
                number = Integer.parseInt(value);

                if (number >= min && number <= max) {
                    return number;
                }

                System.out.println(
                    "Invalid input! Enter a number from " +
                    min + " to " + max + "."
                );

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    // GET NON-EMPTY TEXT INPUT
    public static String getTextInput(String message) {

        String value;

        while (true) {

            System.out.print(message);
            value = input.nextLine().trim();

            if (!value.isEmpty()) {
                return value.replace("|", "/");
            }

            System.out.println("Input cannot be empty!");
        }
    }

    // FIND TEAM USING ID OR NAME
    public static int findTeam(String search) {

        for (int i = 0; i < teamCount; i++) {

            if (teamID[i].equalsIgnoreCase(search) ||
                teamName[i].equalsIgnoreCase(search)) {

                return i;
            }
        }

        return -1;
    }

    // REGISTER NEW SQUAD
    public static void registerTeam() {

        if (teamCount >= MAX_TEAMS) {
            System.out.println("\nMaximum squad limit reached!");
            return;
        }

        System.out.println("\n========== REGISTER NEW SQUAD ==========");

        String name;

        while (true) {

            name = getTextInput("Enter Squad Name: ");

            boolean duplicate = false;

            for (int i = 0; i < teamCount; i++) {

                if (teamName[i].equalsIgnoreCase(name)) {
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                break;
            }

            System.out.println("Squad name already exists!");
        }

        teamID[teamCount] =
                String.format("TEC%03d", teamCount + 1);

        teamName[teamCount] = name;

        System.out.println("\nEnter the 5 Player Names:");

        for (int i = 0; i < PLAYERS_PER_TEAM; i++) {

            playerNames[teamCount][i] =
                    getTextInput("Player " + (i + 1) + ": ");
        }

        wins[teamCount] = 0;
        losses[teamCount] = 0;
        points[teamCount] = 0;
        totalKills[teamCount] = 0;
        totalDeaths[teamCount] = 0;

        System.out.println("\n========================================");
        System.out.println("       SQUAD REGISTERED SUCCESSFULLY!");
        System.out.println("========================================");

        System.out.println("Team ID: " + teamID[teamCount]);
        System.out.println("Squad Name: " + teamName[teamCount]);

        for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
            System.out.println(
                (i + 1) + ". " + playerNames[teamCount][i]
            );
        }

        teamCount++;

        saveData();
    }

    // MANAGE PLAYER ROSTER
    public static void manageRoster() {

        System.out.println("\n========== MANAGE PLAYER ROSTER ==========");

        if (teamCount == 0) {
            System.out.println("No squads registered yet!");
            return;
        }

        for (int i = 0; i < teamCount; i++) {
            System.out.println(teamID[i] + " - " + teamName[i]);
        }

        String searchID = getTextInput("\nEnter Team ID: ");

        int teamIndex = findTeam(searchID);

        if (teamIndex == -1) {
            System.out.println("\nTeam ID not found!");
            return;
        }

        System.out.println("\nSquad: " + teamName[teamIndex]);

        for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
            System.out.println(
                (i + 1) + ". " + playerNames[teamIndex][i]
            );
        }

        int playerNumber = getIntInput(
            "\nEnter player number to replace (1-5): ", 1, 5
        );

        String oldName = playerNames[teamIndex][playerNumber - 1];

        String newName = getTextInput("Enter new player name: ");

        playerNames[teamIndex][playerNumber - 1] = newName;

        System.out.println("\n========================================");
        System.out.println("       PLAYER UPDATED SUCCESSFULLY!");
        System.out.println("========================================");

        System.out.println("Squad: " + teamName[teamIndex]);
        System.out.println("Previous Player: " + oldName);
        System.out.println("New Player: " + newName);

        saveData();
    }

    // DISPLAY REGISTERED SQUADS
    public static void displayTeams() {

        System.out.println("\n========== REGISTERED SQUADS ==========");

        if (teamCount == 0) {
            System.out.println("No squads registered yet!");
            return;
        }

        for (int i = 0; i < teamCount; i++) {

            System.out.println("\nTeam ID: " + teamID[i]);
            System.out.println("Squad Name: " + teamName[i]);

            System.out.println("Players:");

            for (int j = 0; j < PLAYERS_PER_TEAM; j++) {
                System.out.println(
                    (j + 1) + ". " + playerNames[i][j]
                );
            }

            System.out.println("Wins: " + wins[i]);
            System.out.println("Losses: " + losses[i]);
            System.out.println("Points: " + points[i]);
            System.out.println("Kill Difference: " + getScoreDifference(i));

            System.out.println("----------------------------------------");
        }
    }

    // SEARCH SQUAD
    public static void searchTeam() {

        System.out.println("\n========== SEARCH SQUAD ==========");

        if (teamCount == 0) {
            System.out.println("No squads registered yet!");
            return;
        }

        String search = getTextInput("Enter Team ID or Squad Name: ");

        int teamIndex = findTeam(search);

        if (teamIndex == -1) {
            System.out.println("\nNo matching squad found!");
            return;
        }

        System.out.println("\n========== SQUAD FOUND ==========");
        System.out.println("Team ID: " + teamID[teamIndex]);
        System.out.println("Squad Name: " + teamName[teamIndex]);

        System.out.println("\nPlayers:");

        for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
            System.out.println(
                (i + 1) + ". " + playerNames[teamIndex][i]
            );
        }

        System.out.println("\nWins: " + wins[teamIndex]);
        System.out.println("Losses: " + losses[teamIndex]);
        System.out.println("Points: " + points[teamIndex]);
        System.out.println("Kill Difference: " + getScoreDifference(teamIndex));

        System.out.println("=================================");
    }

    // GENERATE RANDOM KILL SCORE
    public static int generateScore() {
        return random.nextInt(101);
    }

    // SELECT RANDOM MAP
    public static int selectRandomMap() {
        return random.nextInt(maps.length);
    }

    // LAUNCH MATCH SIMULATION
    public static void simulateMatch() {

        System.out.println("\n========== MATCH SIMULATION ==========");

        if (teamCount < 2) {
            System.out.println("At least 2 squads are required!");
            return;
        }

        if (matchCount >= MAX_MATCHES) {
            System.out.println("Maximum match history limit reached!");
            return;
        }

        System.out.println("\nREGISTERED SQUADS:");

        for (int i = 0; i < teamCount; i++) {
            System.out.println(
                "[" + (i + 1) + "] " + teamName[i] +
                " (" + teamID[i] + ")"
            );
        }

        int teamA = getIntInput(
            "\nSelect Team A: ", 1, teamCount
        ) - 1;

        int teamB;

        while (true) {

            teamB = getIntInput(
                "Select Team B: ", 1, teamCount
            ) - 1;

            if (teamA != teamB) {
                break;
            }

            System.out.println("A squad cannot play against itself!");
        }

        int[] record = new int[13];

        for (int i = 0; i < 13; i++) {
            record[i] = -1;
        }

        record[0] = teamA;
        record[1] = teamB;

        int winsA = 0;
        int winsB = 0;
        int killsA = 0;
        int killsB = 0;

        System.out.println("\n========================================");
        System.out.println("             MATCH START!");
        System.out.println("========================================");

        for (int round = 1; round <= 3; round++) {

            if (winsA == 2 || winsB == 2) {
                break;
            }

            int mapIndex = selectRandomMap();

            int scoreA;
            int scoreB;

            // REPEAT IF ROUND SCORES ARE TIED
            do {
                scoreA = generateScore();
                scoreB = generateScore();
            } while (scoreA == scoreB);

            killsA += scoreA;
            killsB += scoreB;

            System.out.println("\n========== ROUND " + round + " ==========");
            System.out.println("Map: " + maps[mapIndex]);

            System.out.println(
                teamName[teamA] + ": " + scoreA + " kills"
            );

            System.out.println(
                teamName[teamB] + ": " + scoreB + " kills"
            );

            if (scoreA > scoreB) {

                winsA++;

                System.out.println(
                    "\nROUND WINNER: " + teamName[teamA]
                );

            } else {

                winsB++;

                System.out.println(
                    "\nROUND WINNER: " + teamName[teamB]
                );
            }

            int mapPosition = 2 + ((round - 1) * 3);

            record[mapPosition] = mapIndex;
            record[mapPosition + 1] = scoreA;
            record[mapPosition + 2] = scoreB;

            System.out.println(
                "SERIES SCORE: " + winsA + " - " + winsB
            );
        }

        // DETERMINE MATCH WINNER
        int winner;

        if (winsA == 2) {
            winner = teamA;
        } else {
            winner = teamB;
        }

        record[11] = winner;
        record[12] = winsA + winsB;

        matchHistory[matchCount] = record;
        matchCount++;

        updateStandings(teamA, teamB, winner, killsA, killsB);

        System.out.println("\n========================================");
        System.out.println("             MATCH COMPLETE");
        System.out.println("========================================");

        System.out.println(
            "FINAL SERIES SCORE: " + winsA + " - " + winsB
        );

        System.out.println("WINNER: " + teamName[winner]);
        System.out.println("Tournament Points Earned: 3");

        System.out.println("Total Kills - " + teamName[teamA] +
                ": " + killsA);

        System.out.println("Total Kills - " + teamName[teamB] +
                ": " + killsB);

        System.out.println("========================================");

        saveData();
    }

    // UPDATE TOURNAMENT STANDINGS
    public static void updateStandings(
            int teamA, int teamB, int winner,
            int killsA, int killsB) {

        if (winner == teamA) {

            wins[teamA]++;
            losses[teamB]++;
            points[teamA] += 3;

        } else {

            wins[teamB]++;
            losses[teamA]++;
            points[teamB] += 3;
        }

        totalKills[teamA] += killsA;
        totalDeaths[teamA] += killsB;

        totalKills[teamB] += killsB;
        totalDeaths[teamB] += killsA;
    }

    // CALCULATE KILL DIFFERENCE
    public static int getScoreDifference(int teamIndex) {
        return totalKills[teamIndex] - totalDeaths[teamIndex];
    }

    // DISPLAY MATCH HISTORY
    public static void displayMatchHistory() {

        System.out.println("\n========== MATCH HISTORY ==========");

        if (matchCount == 0) {
            System.out.println("No matches have been played yet!");
            return;
        }

        for (int i = 0; i < matchCount; i++) {

            int[] record = matchHistory[i];

            int teamA = record[0];
            int teamB = record[1];

            System.out.println("\nMATCH #" + (i + 1));
            System.out.println(
                teamName[teamA] + " VS " + teamName[teamB]
            );

            for (int round = 0; round < record[12]; round++) {

                int position = 2 + (round * 3);

                System.out.println("\nRound " + (round + 1));
                System.out.println("Map: " + maps[record[position]]);

                System.out.println(
                    teamName[teamA] + ": " + record[position + 1]
                );

                System.out.println(
                    teamName[teamB] + ": " + record[position + 2]
                );
            }

            System.out.println("\nWinner: " + teamName[record[11]]);
            System.out.println("----------------------------------------");
        }
    }

    // DISPLAY TOURNAMENT STANDINGS
    public static void displayStandings() {

        System.out.println("\n========== TOURNAMENT STANDINGS ==========");

        if (teamCount == 0) {
            System.out.println("No squads registered yet!");
            return;
        }

        // STORE TEAM INDEXES FOR SORTING
        int[] order = new int[teamCount];

        for (int i = 0; i < teamCount; i++) {
            order[i] = i;
        }

        // SORT BY POINTS, WINS, AND KILL DIFFERENCE
        for (int i = 0; i < teamCount - 1; i++) {

            for (int j = 0; j < teamCount - i - 1; j++) {

                int first = order[j];
                int second = order[j + 1];

                boolean swap = false;

                if (points[first] < points[second]) {
                    swap = true;

                } else if (points[first] == points[second] &&
                           wins[first] < wins[second]) {
                    swap = true;

                } else if (points[first] == points[second] &&
                           wins[first] == wins[second] &&
                           getScoreDifference(first) <
                           getScoreDifference(second)) {
                    swap = true;
                }

                if (swap) {

                    int temp = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = temp;
                }
            }
        }

        System.out.printf(
            "%-5s %-20s %-6s %-7s %-8s %-8s%n",
            "RANK", "SQUAD", "WINS", "LOSSES", "POINTS", "K/D DIFF"
        );

        System.out.println(
            "----------------------------------------------------------------"
        );

        for (int i = 0; i < teamCount; i++) {

            int index = order[i];

            System.out.printf(
                "%-5d %-20s %-6d %-7d %-8d %-8d%n",
                i + 1,
                teamName[index],
                wins[index],
                losses[index],
                points[index],
                getScoreDifference(index)
            );
        }
    }

    // DISPLAY TOURNAMENT STATISTICS
    public static void displayStatistics() {

        System.out.println("\n========== TOURNAMENT STATISTICS ==========");

        System.out.println("Total Registered Squads: " + teamCount);
        System.out.println("Total Matches Played: " + matchCount);

        int totalRounds = 0;
        int totalMatchKills = 0;

        for (int i = 0; i < matchCount; i++) {

            totalRounds += matchHistory[i][12];

            for (int round = 0; round < matchHistory[i][12]; round++) {

                int position = 3 + (round * 3);

                totalMatchKills += matchHistory[i][position];
                totalMatchKills += matchHistory[i][position + 1];
            }
        }

        System.out.println("Total Rounds Played: " + totalRounds);
        System.out.println("Total Kills Recorded: " + totalMatchKills);

        if (teamCount == 0) {
            System.out.println("No squad statistics available.");
            return;
        }

        int topTeam = 0;

        for (int i = 1; i < teamCount; i++) {

            if (wins[i] > wins[topTeam] ||
                (wins[i] == wins[topTeam] &&
                 getScoreDifference(i) > getScoreDifference(topTeam))) {

                topTeam = i;
            }
        }

        System.out.println("\n========== TOP PERFORMING SQUAD ==========");
        System.out.println("Squad: " + teamName[topTeam]);
        System.out.println("Wins: " + wins[topTeam]);
        System.out.println("Losses: " + losses[topTeam]);
        System.out.println("Points: " + points[topTeam]);
        System.out.println("Total Kills: " + totalKills[topTeam]);
        System.out.println("Total Deaths: " + totalDeaths[topTeam]);
        System.out.println("Kill Difference: " + getScoreDifference(topTeam));

        System.out.println("==========================================");
    }

    // LOAD SAVED DATA
    public static void loadData() {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        // LOAD TEAMS
        File teamFile = new File("data/teams.txt");

        if (teamFile.exists()) {

            try (BufferedReader reader =
                    new BufferedReader(new FileReader(teamFile))) {

                String line;

                while ((line = reader.readLine()) != null &&
                       teamCount < MAX_TEAMS) {

                    String[] data = line.split("\\|", -1);

                    if (data.length != 7) {
                        continue;
                    }

                    teamID[teamCount] = data[0];
                    teamName[teamCount] = data[1];

                    for (int i = 0; i < PLAYERS_PER_TEAM; i++) {
                        playerNames[teamCount][i] = data[i + 2];
                    }

                    teamCount++;
                }

            } catch (IOException e) {
                System.out.println("Error loading team data.");
            }
        }

        // LOAD MATCH HISTORY
        File matchFile = new File("data/matches.txt");

        if (matchFile.exists()) {

            try (BufferedReader reader =
                    new BufferedReader(new FileReader(matchFile))) {

                String line;

                while ((line = reader.readLine()) != null &&
                       matchCount < MAX_MATCHES) {

                    String[] data = line.split(",", -1);

                    if (data.length != 13) {
                        continue;
                    }

                    int[] record = new int[13];
                    boolean valid = true;

                    try {

                        for (int i = 0; i < 13; i++) {
                            record[i] = Integer.parseInt(data[i]);
                        }

                    } catch (NumberFormatException e) {
                        valid = false;
                    }

                    if (!valid) {
                        continue;
                    }

                    if (record[0] < 0 || record[0] >= teamCount ||
                        record[1] < 0 || record[1] >= teamCount ||
                        record[11] < 0 || record[11] >= teamCount ||
                        record[12] < 2 || record[12] > 3) {
                        continue;
                    }

                    boolean validRounds = true;

                    for (int round = 0; round < record[12]; round++) {

                        int position = 2 + (round * 3);

                        if (record[position] < 0 ||
                            record[position] >= maps.length ||
                            record[position + 1] < 0 ||
                            record[position + 2] < 0) {

                            validRounds = false;
                            break;
                        }
                    }

                    if (!validRounds) {
                        continue;
                    }

                    matchHistory[matchCount] = record;
                    matchCount++;
                }

            } catch (IOException e) {
                System.out.println("Error loading match history.");
            }
        }

        // REBUILD TEAM STATISTICS FROM MATCH HISTORY
        for (int i = 0; i < teamCount; i++) {
            wins[i] = 0;
            losses[i] = 0;
            points[i] = 0;
            totalKills[i] = 0;
            totalDeaths[i] = 0;
        }

        for (int i = 0; i < matchCount; i++) {

            int[] record = matchHistory[i];

            int teamA = record[0];
            int teamB = record[1];

            int killsA = 0;
            int killsB = 0;

            for (int round = 0; round < record[12]; round++) {

                int position = 3 + (round * 3);

                killsA += record[position];
                killsB += record[position + 1];
            }

            updateStandings(
                teamA, teamB, record[11], killsA, killsB
            );
        }

        if (teamCount > 0 || matchCount > 0) {
            System.out.println("\nSaved tournament data loaded successfully!");
            System.out.println("Registered Squads: " + teamCount);
            System.out.println("Matches Loaded: " + matchCount);
        }
    }

    // SAVE DATA TO TEXT FILES
    public static void saveData() {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        // SAVE TEAM INFORMATION
        try (PrintWriter writer =
                new PrintWriter(new FileWriter("data/teams.txt"))) {

            for (int i = 0; i < teamCount; i++) {

                writer.print(teamID[i] + "|");
                writer.print(teamName[i]);

                for (int j = 0; j < PLAYERS_PER_TEAM; j++) {
                    writer.print("|" + playerNames[i][j]);
                }

                writer.println();
            }

        } catch (IOException e) {
            System.out.println("Error saving team data.");
        }

        // SAVE MATCH HISTORY
        try (PrintWriter writer =
                new PrintWriter(new FileWriter("data/matches.txt"))) {

            for (int i = 0; i < matchCount; i++) {

                for (int j = 0; j < 13; j++) {

                    writer.print(matchHistory[i][j]);

                    if (j < 12) {
                        writer.print(",");
                    }
                }

                writer.println();
            }

        } catch (IOException e) {
            System.out.println("Error saving match history.");
        }
    }
}
