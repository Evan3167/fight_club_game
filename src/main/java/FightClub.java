import java.util.Random;
import java.util.Scanner;

public class FightClub {
    
    public static void main(String args[]) {
        String playerName = "PLAYER";
        String botName = "BOT";

        Fighter player;
        Fighter bot = new BasicFighter(botName);

        boolean playerTurn = true;
        try (Scanner scan = new Scanner(System.in);) {
            
            // Pick loadout
            System.out.print("Pick a perk.. 'poison','heal',or 'crit': ");
            String perk = scan.nextLine();
            System.out.println();
            switch(perk) {
                case "poison": player = new PoisonFighter(playerName); break;
                case "heal": player = new HealFighter(playerName); break;
                case "crit": player = new CritFighter(playerName); break;
                default : System.out.println("Idiot your Basic now."); player = new BasicFighter(playerName); break;
            }

            while (true) {
                boolean actionComplete = false;
                if (playerTurn) {
                    System.out.printf("It is the %s's turn. Pick Action: ",player.name);
                    actionComplete = takeAction(scan.nextInt(), player, bot);
                    System.out.println();
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
            defender.takeDamage(attacker.attack());
            return true;
        } else if (actionChoice == 2) {
            // Heal
            attacker.healSelf(); 
            return true;
        } else if (actionChoice == 3) {
            // Dodge
            attacker.tryDodgeNextAttack();
            return true;
        } else {
            // "Pick a real choice" -- reset action choice
            System.out.println("Not a real action. Select again!");
            return false;
        }
    }

    static abstract class Fighter {
        public String name = null;
        protected int currentHealth = 20;
        protected final int MAX_HEALTH = 20;
        protected final int MIN_HEALTH = 0;

        public boolean isDodging = false;
        protected boolean isAlive = true;

        protected Random seed;

        public Fighter(String name) {
            this.name = name;
            this.seed = new Random();
        }

        public void tryDodgeNextAttack() {
            System.out.printf("%s is attempting to dodge the next attack.\n", this.name);
            this.isDodging = true;
        }

        abstract int attack();
        abstract void takeDamage(int damage);
        abstract void healSelf();
    }

    static class BasicFighter extends Fighter {
        public BasicFighter(String name) {
            super(name);
        }

        public void healSelf() {
            int heal = super.seed.nextInt(6) + 1;
            super.currentHealth = Math.min(super.currentHealth + heal, super.MAX_HEALTH);
            System.out.printf("%s healed for %d.\n", super.name, heal);
        }

        public int attack() {
            return super.seed.nextInt(6) + 1;
        }

        public void takeDamage(int damage) {
            if (isDodging && (super.seed.nextInt(6) + 1) >= damage) {
                System.out.printf("%s dodged the attack!\n", super.name);
            } else {
                super.currentHealth = Math.max(super.currentHealth - damage, super.MIN_HEALTH);
                System.out.printf("%s took %d damage.\n", super.name, damage);
            }
            super.isDodging = false;

            if (super.currentHealth == 0) {
                super.isAlive = false;
            }
        }
    }

    static class PoisonFighter extends BasicFighter {
        public PoisonFighter(String name) {
            super(name);
        }
        // Poison tick 1 dmg for 3 turns if attack lands and deals greater than > 0.
        // But you your attacks deal one less damage 

    }
    static class HealFighter extends BasicFighter {
        public HealFighter(String name) {
            super(name);
        }
        // Heal 2 health everytime you successfully dodge.
        @Override
        public void takeDamage(int damage) {
            if (isDodging && (super.seed.nextInt(6) + 1) >= damage) {
                System.out.printf("%s dodged the attack!\n", super.name);
                System.out.print("Heal Perk: ");
                super.healSelf();
            } else {
                super.currentHealth = Math.max(super.currentHealth - damage, super.MIN_HEALTH);
                System.out.printf("%s took %d damage.\n", super.name, damage);
            }
            super.isDodging = false;

            if (super.currentHealth == 0) {
                super.isAlive = false;
            }
        }
    } 

    static class CritFighter extends BasicFighter {
        private final int CRIT_CHANCE_MODIFIER = 20;
        public CritFighter(String name) {
            super(name);
        }
        // Chance to crit double damage at %20
        @Override
        public int attack() {
            int critChance = super.seed.nextInt(100) + 1;
            int damage = super.seed.nextInt(6) + 1;
            if (critChance <= CRIT_CHANCE_MODIFIER) {
                System.out.println("Crit Perk: Critical Hit!");
                damage *= 2;
            }
            return damage;
        }
    }
}
