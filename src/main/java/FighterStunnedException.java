public class FighterStunnedException extends Exception {

    public FighterStunnedException(String name) {
        System.out.printf("%s is stunned and skipped their turn!\n", name);
    }
}