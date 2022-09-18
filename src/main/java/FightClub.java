
import java.util.Random;
import java.util.Scanner;

public class FightClub {
    
    public static final Random _seed = new Random();
    static String playerName = "PLAYER";
    static String botName = "BOT";
    
    public static void main(String args[]) {

        Fighter player;
        Fighter bot = new BasicFighter(botName);

        try (Scanner scan = new Scanner(System.in);) {
            
            // Pick loadout
            System.out.print("Pick a perk.. 'poison','heal','stun',or 'crit': ");
            String perk = scan.nextLine();
            System.out.println();
            // ** Factory Pattern **
            switch(perk.toUpperCase()) {
                case "POISON": player = new PoisonFighter(playerName); break;
                case "HEAL": player = new HealFighter(playerName); break;
                case "CRIT": player = new CritFighter(playerName); break;
                case "STUN": player = new StunFighter(playerName); break;
                default : System.out.println("No Perk For You!"); player = new BasicFighter(playerName); break;
            }

            boolean playerTurn = true;
            while (true) {
                boolean actionComplete = false;

                try {
                    if (playerTurn) {
                        System.out.printf("It is the %s's turn. Pick Action: ",player.getName());
                        actionComplete = takeAction(scan.nextInt(), player, bot);
                        System.out.println();
                    } else {
                        System.out.printf("It is the %s's turn.\n", bot.getName());
                        actionComplete = takeAction(_seed.nextInt(3) + 1, bot, player);
                    }
                } catch (FighterDiedException e) {
                    break;
                } catch (FighterStunnedException e) {
                    actionComplete = true;
                }
               
                // Turn finished successfully
                if (actionComplete) {
                    playerTurn = !playerTurn;
                }
            }
        }
    }

    private static boolean takeAction(int actionChoice, Fighter attacker, Fighter defender) throws FighterDiedException, FighterStunnedException {
        if (actionChoice < 1 || actionChoice > 3) {
            System.out.println("Not a real action. Select again!");
            return false;
        }

        attacker.applyStatusEffects();

        switch(actionChoice) {
            // Attack
            case 1: 
                System.out.printf("%s attacked %s.\n", attacker.getName(), defender.getName());
                defender.takeDamage(attacker.attack());
                break;
           // Heal
            case 2:
                attacker.healSelf();
                break;
            // Dodge
            case 3:
                attacker.tryDodgeNextAttack();
                break;
        }
        return true;
    }
}
