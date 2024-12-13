package dinojump;

public class Utilities {
    public static boolean detectCollision(GameObject a, GameObject b) {
        // Deteksi tabrakan horizontal
        boolean collisionX = a.x < b.x + b.width && a.x + a.width > b.x;
    
        // Deteksi tabrakan vertikal: Doodler harus berada di atas platform
        boolean collisionY = a.y + a.height <= b.y + b.height && a.y + a.height >= b.y;
    
        return collisionX && collisionY;
    }
}
