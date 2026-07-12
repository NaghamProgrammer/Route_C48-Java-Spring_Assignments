import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class Main {

    static int[] ids;
    static String[] names;
    static int[] ages;
    static HashSet<String>[] attributes;

    static String[] goalkeeperReqs;
    static String[] defenderReqs;
    static String[] midfielderReqs;
    static String[] strikerReqs;

    static int[] goalkeeperPotential;
    static int[] defenderPotential;
    static int[] midfielderPotential;
    static int[] strikerPotential;
    static boolean[] selected = new boolean[60];





    public static void main(String[] args) {

        ids = new int[60];
        names = new String[60];
        ages = new int[60];
        attributes = new HashSet[60];

        goalkeeperPotential = new int[60];
        defenderPotential = new int[60];
        midfielderPotential = new int[60];
        strikerPotential = new int[60];

        parseRequirements("roleRequirements.txt");
        parsePlayers("players.txt");

        choosePlayers("Goalkeeper" , 1);
        choosePlayers("Defender" , 4);
        choosePlayers("Midfielder" , 4);
        choosePlayers("Striker" , 2);


    }





    //load info from roleRequiremnets file into the program data structures
    public static void parseRequirements(String fileNam){
        try{
            Scanner file = new Scanner(new File("roleRequirements.txt"));

            ArrayList<String> current = null;
            ArrayList<String> gk = new ArrayList<>();
            ArrayList<String> def = new ArrayList<>();
            ArrayList<String> mid = new ArrayList<>();
            ArrayList<String> str = new ArrayList<>();

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.equals("Goalkeeper:")) {
                    current = gk;
                } else if (line.equals("Defender:")) {
                    current = def;
                } else if (line.equals("Midfielder:")) {
                    current = mid;
                } else if (line.equals("Striker:")) {
                    current = str;
                } else if (current != null) {
                    current.add(line);
                }
            }
            file.close();

            goalkeeperReqs = gk.toArray(new String[0]);
            defenderReqs = def.toArray(new String[0]);
            midfielderReqs = mid.toArray(new String[0]);
            strikerReqs = str.toArray(new String[0]);

        }
        catch(FileNotFoundException e){System.out.println("File not found");}
    }



    //load info from players file into the program data structures
    public static void parsePlayers(String fileNam){
        try{
            Scanner file = new Scanner(new File("players.txt"));
            int i = 0;
            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(",");

                ids[i] = Integer.parseInt(tokens[0].trim());
                names[i] = tokens[1].trim();
                ages[i] = Integer.parseInt(tokens[2].trim());

                HashSet<String> playerAttrs = new HashSet<>();
                for (int j = 3; j < tokens.length; j++) {
                    playerAttrs.add(tokens[j].trim());
                }
                attributes[i] = playerAttrs;

                goalkeeperPotential[i] = potentialCalculator(playerAttrs, goalkeeperReqs);
                defenderPotential[i] = potentialCalculator(playerAttrs, defenderReqs);
                midfielderPotential[i] = potentialCalculator(playerAttrs, midfielderReqs);
                strikerPotential[i] = potentialCalculator(playerAttrs, strikerReqs);

                i++;
            }
            file.close();
        }
        catch(FileNotFoundException e){System.out.println("File not found");}
    }


    //this function counts the similar elements between requirements array and player attributes array
    //the method is searching for each element in the role requirements if found in the player attributes increase player's potential for this role by 1
    //its goal is making for each player (GoalKeeper potential, Defender potential, MidFielder potential, Striker potential)
    public static int potentialCalculator(HashSet<String> playerAttributes, String[] roleRequirements){
        int counter = 0;

        for (String reqs : roleRequirements){
            if(playerAttributes.contains(reqs)){counter++;}
        }

        return counter;
    }


    //function to choose players by role and needed number
    //e.g. (GoalKeeper , 1), (Defenders , 4), .....
    //the function basically sorts potentials from potentialCalculator and choose top n and solves tie by choosing the younger
    public static void choosePlayers(String role, int number) {

        int[] potential;

        switch (role) {
            case "Goalkeeper":
                potential = goalkeeperPotential;
                break;
            case "Defender":
                potential = defenderPotential;
                break;
            case "Midfielder":
                potential = midfielderPotential;
                break;
            case "Striker":
                potential = strikerPotential;
                break;
            default:
                System.out.println("Unknown role");
                return;
        }

        System.out.println("== " + role + " ==");

        // select the chose number of players
        for (int k = 0; k < number; k++) {

            int best = -1;

            for (int i = 0; i < ids.length; i++) {

                //if player is already chosen ignore him
                if (selected[i]) {
                    continue;
                }

                // the first player is the best player temporarily
                if (best == -1) {
                    best = i;
                    continue;
                }
                if (potential[i] > potential[best]) {
                    best = i;
                }

                //if equal potential choose younger
                else if (potential[i] == potential[best] &&
                        ages[i] < ages[best]) {
                    best = i;
                }
            }

            selected[best] = true;


            System.out.println( names[best] + " (Age: " + ages[best] + ", Potential: " + potential[best] + ")");
        }

        System.out.println();
    }

}

