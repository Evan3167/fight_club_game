
public class HealFighter extends BasicFighter {
    public HealFighter(String name) {
        super(name);
    }
    // heal-self everytime you successfully dodge.
    @Override
    public void dodgeMechanic() {
        System.out.print("Heal Perk: ");
        super.healSelf();
    }
} 