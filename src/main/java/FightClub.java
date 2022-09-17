import java.util.Random;
import java.util.Scanner;

public class FightClub {
    
    public static void main(String args[]) {
        // Pick loadout
        Fighter player = new Fighter("player");
        Fighter bot = new Fighter("bot");

        boolean playerTurn = true;
        try (Scanner scan = new Scanner(System.in);) {
            while (true) {
                IFighter currentFighter = playerTurn ? player : bot;
                currentFighter.refreshLoadOut();

                boolean actionComplete = false;
                if (playerTurn) {
                    System.out.printf("It is the %s's turn. Pick Action: \n",player.name);
                    actionComplete = takeAction(scan.nextInt(), player, bot);
                } else {
                    System.out.printf("It is the %s's turn.\n", bot.name);
                    Random rando = new Random();
                    actionComplete = takeAction(rando.nextInt(3) + 1, bot, player);
                }

                // Turn finished successfully
                if (actionComplete) {
                    playerTurn = !playerTurn;
                }

                // Check if someone died and end game
                if (!player.isAlive) {
                    System.out.printf("%s died! %s wins!\n", player.name, bot.name);
                    break;
                } else if (!bot.isAlive) {
                    System.out.printf("%s died! %s wins!\n", bot.name, player.name);
                    break;
                }
            }
        }
    }

    private static boolean takeAction(int actionChoice, Fighter attacker, Fighter defender) {
        if (actionChoice == 1) {
            // Attack
            System.out.printf("%s attacked %s.\n", attacker.name, defender.name);
            defender.takeDamage(attacker.loadOut.attack);
            return true;
        } else if (actionChoice == 2) {
            // Heal
            attacker.healSelf(attacker.loadOut.heal); 
            System.out.printf("%s healed for %d.\n", attacker.name, attacker.loadOut.heal);
            return true;
        } else if (actionChoice == 3) {
            // Dodge
            System.out.printf("%s is attempting to dodge the next attack.\n", attacker.name);
            attacker.tryDodgeNextAttack(defender.loadOut.attack);
            return true;
        } else {
            // "Pick a real choice" -- reset action choice
            return false;
        }
    }

    static class Fighter implements IFighter {
        public String name = null;
        private int currentHealth = 20;
        private final int MAX_HEALTH = 20;
        private final int MIN_HEALTH = 0;

        public boolean isDodging = false;
        private boolean isAlive = true;
        public LoadOut loadOut = null;
        Perk perk = null;

        public Fighter(String name) {
            this.name = name;
            refreshLoadOut();
        }

        public void tryDodgeNextAttack(int opponentsAttack) {
            if (this.loadOut.dodge >= opponentsAttack) {
                this.isDodging = true;
            }
        }

        public void healSelf(int heal) {
            this.currentHealth = Math.min(currentHealth + heal, MAX_HEALTH);
        }

        public void takeDamage(int damage) {
            if (!isDodging) {
                this.currentHealth = Math.max(this.currentHealth - damage, MIN_HEALTH);
                System.out.printf("%s took %d damage.\n", this.name, damage);
            } else {
                System.out.printf("%s dodged the attack!\n", this.name);
                this.isDodging = false;
            }

            if (this.currentHealth == 0) {
                this.isAlive = false;
            }
        }

        public void refreshLoadOut() {
            // System.out.printf("%s loadout refreshed.\n",this.name);
            this.loadOut = new LoadOut();
        }
    }

    static class LoadOut {
        public int attack = 0;
        public int heal = 0;
        public int dodge = 0;

        public LoadOut() {
            Random rando = new Random();
            this.attack = rando.nextInt(6) + 1;
            this.heal = rando.nextInt(6) + 1;
            this.dodge = rando.nextInt(6) + 1;
        }

        @Override
        public String toString() {
            return "LoadOut [attack=" + attack + ", dodge=" + dodge + ", heal=" + heal + "]";
        }
    }

    /* Not implemeneted yet */
    class Perk {
    }

    interface IFighter {
        public void refreshLoadOut();
    }
}
