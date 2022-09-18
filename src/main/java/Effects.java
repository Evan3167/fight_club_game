// Define new Effects here.
public enum Effects {
    // Default 0 damage, 0 duration
    Poison(0,0),
    Stun(0,0);

    public int damage;
    public int duration;
    public Effects setDamage(int damage) {
        this.damage = damage;
        return this;
    }
    public Effects setDuration(int duration) {
        this.duration = duration;
        return this;
    }
    private Effects(int damage, int duration) {
        this.damage = damage;
        this.duration = duration;
    }
}