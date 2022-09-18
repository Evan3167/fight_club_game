
public class FighterDiedException extends Exception {
    
    public FighterDiedException(String loserName) {
        String winnerName = loserName.equals(FightClub.playerName) ? FightClub.botName : FightClub.playerName;
        System.out.printf("%s died! %s wins!\n", loserName, winnerName);
    }
}