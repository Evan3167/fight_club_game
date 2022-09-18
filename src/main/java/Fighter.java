
public interface Fighter {
    public String getName();
    public boolean isAlive();
    public void tryDodgeNextAttack();
    public void healSelf();
    public Attack attack();
    public void takeDamage(Attack attack) throws FighterDiedException;
    public void applyStatusEffects() throws FighterDiedException, FighterStunnedException;
}