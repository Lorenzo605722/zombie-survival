import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Player {
    String name;
    int hp = 100;
    String weapon;
    List<String> inventory = new ArrayList<>();
    Random r = new Random();
    int zombieUccisi = 0;

    Player(String name) { this.name = name; }

    void chooseWeapon(Scanner s) {
        System.out.println("1. Pistola\n2. Fucile\n3. Machete\n4. Balestra\n5. Coltello\n6. Lancia-razzi\n7. Mazza chiodata");
        int c = s.nextInt(); s.nextLine();
        weapon = switch (c) {
            case 1 -> "Pistola";
            case 2 -> "Fucile";
            case 3 -> "Machete";
            case 4 -> "Balestra";
            case 5 -> "Coltello";
            case 6 -> "Lanciarazzi";
            case 7 -> "Mazza";
            default -> "Mani nude";
        };
    }

    void addItem(String i) { inventory.add(i); }

    void useItem(Scanner s) {
        if (inventory.isEmpty()) {
            System.out.println("Nessun oggetto disponibile.");
            return;
        }
        for (int i = 0; i < inventory.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, inventory.get(i));
        }
        System.out.print("Scegli: ");
        int c = s.nextInt(); s.nextLine();
        String item = inventory.remove(c - 1);
        if (item.equals("Medikit")) {
            hp = Math.min(100, hp + 30);
            System.out.println("Medikit usato. HP: " + hp);
        } else if (item.equals("Granata")) {
            System.out.println("Hai lanciato una granata! Finalmente qualcosa di utile!");
        }
    }

    void attack(Zombie z) {
        String failMsg = "L'attacco fallisce miseramente, come la tua vita.";
        boolean hit = switch (weapon) {
            case "Pistola" -> r.nextInt(100) < 75;
            case "Fucile" -> r.nextInt(100) < 85;
            case "Machete" -> r.nextInt(100) < 50;
            case "Balestra" -> true;
            case "Coltello" -> r.nextInt(100) < 60;
            case "Lanciarazzi" -> true;
            case "Mazza" -> r.nextInt(100) < 70;
            default -> r.nextInt(100) < 30;
        };

        if (!z.canBeKilledWith(weapon)) {
            System.out.println(z.type + " non può essere ucciso con " + weapon + "!");
            return;
        }

        if ((weapon.equals("Balestra") || weapon.equals("Lanciarazzi")) && z.doubleSpawn && !hit) {
            System.out.println("Hai perso tempo a ricaricare boia faus... uno zombie sta pranzando con il tuo cervello vuoto!");
            hp = 0;
            return;
        }

        if (hit) {
            z.hp--;
            if (z.hp <= 0) {
                System.out.println("Zombie eliminato! Un altro passo verso la (in)sanità mentale.");
                zombieUccisi++;
            } else {
                System.out.println("Zombie colpito ma è ancora in piedi!");
                System.out.println("Ti guarda e dice <<Tu stai cercando di inculare me?>>");
            }
        } else {
            System.out.println(failMsg);
        }
    }


    void takeDamage(int dmg) {
        hp -= dmg;
        System.out.println("Hai subito " + dmg + " danni. HP: " + hp);
    }

    boolean isAlive() { return hp > 0; }
}
