

public class CritFighter extends BasicFighter {

    private final int CRIT_CHANCE_MODIFIER = 20;
    public CritFighter(String name) {
        super(name);
    }
    // Chance to crit double damage at %20
    @Override
    public Attack attack() {
        return new Attack() {
            public int computeDamage() {
                int dmg = this.defaultDamage();
                int critChance = FightClub._seed.nextInt(100) + 1;
                if (critChance <= CRIT_CHANCE_MODIFIER) {
                    System.out.println("Crit Perk: Critical Hit!");
                    dmg *= 2;
                }
                return dmg;
            }
        };
    }
}