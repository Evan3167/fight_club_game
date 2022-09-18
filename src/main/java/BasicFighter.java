import java.util.HashMap;
import java.util.Map;

public class BasicFighter implements Fighter {

    private String name = null;
    protected int currentHealth = 20;
    protected final int MAX_HEALTH = 20;
    protected final int MIN_HEALTH = 0;

    private Map<Effects, Map<String,Integer>> statuses = new HashMap<>();

    protected boolean isDodging = false;
    protected boolean isAlive = true;

    public BasicFighter(String name) {
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isAlive() {
        return this.isAlive;
    }

    // Define anomymous functions for Attack interface for BasicFighter
    public Attack attack() {
        return new Attack() {
            public int computeDamage() {
                return this.defaultDamage();
            }
        };
    }

    private final void removeHealth(int damage) {
        this.currentHealth = Math.max(this.currentHealth - damage, this.MIN_HEALTH);
        System.out.printf("%s took %d damage.\n", this.name, damage);
    }
    private final void removeHealthByEffect(int damage, Effects damageType) {
        if (damage != 0) {
            this.currentHealth = Math.max(this.currentHealth - damage, this.MIN_HEALTH);
            System.out.printf("%s took %d %s damage.\n", this.name, damage, damageType);
        }
    }
    public final void healSelf() {
        int heal = FightClub._seed.nextInt(6) + 1;
        this.currentHealth = Math.min(this.currentHealth + heal, this.MAX_HEALTH);
        System.out.printf("%s healed for %d.\n", this.name, heal);
    }

    public final void tryDodgeNextAttack() {
        System.out.printf("%s is attempting to dodge the next attack.\n", this.name);
        this.isDodging = true;
    }

    protected void dodgeMechanic() {
        // Placeholder for other classes to use.
    }

    // FINAL applied to method to indicate it cannot be overwritten.
    public final void takeDamage(Attack attack) throws FighterDiedException {
        if (isDodging && (FightClub._seed.nextInt(6) + 1) >= attack.getDamage()) {
            System.out.printf("%s dodged the attack!\n", this.name);
            dodgeMechanic();
        } else {
            removeHealth(attack.getDamage());
            // Damage must be greater than 0 to apply status effects
            if (attack.getDamage() > 0) {
                System.out.println(attack.effects.toString());
                 // Go through negative effects attached to the attack and store them in status effects
                for (Effects effect : attack.effects) {
                    // Reset Effect status
                    if (statuses.containsKey(effect)) {
                        Map<String,Integer> eft = statuses.get(effect);
                        eft.compute("duration", (k,v) -> v = effect.duration);
                        eft.compute("damage", (k,v) -> v = effect.damage);
                    } 
                    // Apply new Effect status
                    else {
                        Map<String,Integer> eft = new HashMap<>();
                        eft.put("duration",effect.duration);
                        eft.put("damage",effect.damage);
                        statuses.put(effect, eft);
                    }
                }
            }
           
        }
        this.isDodging = false;

        if (this.currentHealth == 0) {
            throw new FighterDiedException(this.name);
        }
    }

    /* 
     *  Go through active status effects. Define status effect behavior here.
     */
    public final void applyStatusEffects() throws FighterDiedException, FighterStunnedException {
        // Remove all statuses where the duration expired.
        statuses.entrySet().removeIf(e -> e.getValue().get("duration") == 0);

        for (Map.Entry<Effects, Map<String,Integer>> eft : statuses.entrySet()) {
            eft.getValue().replace("duration", eft.getValue().get("duration")-1);
            removeHealthByEffect(eft.getValue().get("damage"), eft.getKey());
        }

        if (statuses.containsKey(Effects.Stun)) {
            throw new FighterStunnedException(this.name);
        }

        if (this.currentHealth == 0) {
            throw new FighterDiedException(this.name);
        }
    }
}
