public class StunFighter extends BasicFighter {
    private final int STUN_CHANCE_MODIFIER = 20;

    public StunFighter(String name) {
        super(name);
    }

    @Override
    public Attack attack() {
        return new Attack() {
            public int computeDamage() {
                int stunChance = FightClub._seed.nextInt(100) + 1;
                if (stunChance <= STUN_CHANCE_MODIFIER) {
                    Effects stun = Effects.Stun.setDamage(0);
                    stun.setDuration(1);
                    effects.add(Effects.Stun);
                }
                return this.defaultDamage();
            }
        };
    }
}
