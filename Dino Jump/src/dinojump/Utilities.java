package dinojump;

public class Utilities {
    public static boolean detectCollision(GameObject a, GameObject b) {
        return 
            a.x < b.x + b.width &&
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y;
    }
}
