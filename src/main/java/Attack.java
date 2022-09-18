import java.util.ArrayList;
import java.util.List;

// Attacks can have mutliple effects.
public abstract class Attack {

    public List<Effects> effects = new ArrayList<>();
    protected boolean computed = false;
    protected int damage = 0;
    public abstract int computeDamage();
    
    public int getDamage() {
        if (!computed) {
            damage = computeDamage();
            computed = true;
        }
        return damage;
    }

    protected int defaultDamage() {
        return FightClub._seed.nextInt(6) + 1;
    }
}