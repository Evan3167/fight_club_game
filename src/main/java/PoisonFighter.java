
public class PoisonFighter extends BasicFighter {
    public PoisonFighter(String name) {
        super(name);
    }
    // Poison tick 1 dmg for 3 turns if attack lands and deals greater than > 0.
    // But you your attacks deal one less damage 
    @Override
    public Attack attack() {
        return new Attack() {
            public int computeDamage() {
                Effects effect = Effects.Poison.setDamage(1);
                effect = effect.setDuration(3);
                effects.add(Effects.Poison);
                // -1 attack applied
                return this.defaultDamage()-1;
            }
        };
    }
}