import java.util.Random;

class Zombie {
    String type;
    int damage;
    int hp;
    String narrative;
    boolean doubleSpawn = false;

    Zombie(String t, int d, int h, String n) {
        type = t; damage = d; hp = h; narrative = n;
    }

    static Zombie random() {
        Random r = new Random();
        switch (r.nextInt(6)) {
            case 0: return new Zombie("Zombie normale", 10, 1, "Un gemito. Poi un'ombra barcollante si avvicina...");
            case 1: return new Zombie("Zombie ciccione", 15, 3, "Passi rimbombanti... uno zombie ciccione ingordo appare! Ti vuole mangiare sta palla di lardo");
            case 2: return new Zombie("Zombie palestrato", 20, 2, "Un colosso marcio di muscoli sbuca: è uno zombie palestrato che ha appena preso il trembolone!");
            case 3: return new Zombie("Zombie omosessuale", 10, 1, "Capelli perfetti, corsetta letale... zombie omosessuale in arrivo!");
            case 4: return new Zombie("Zombie femminista", 20, 1, "Cartello di protesta appeso al collo, urla fastidiose, ma nessun piatto pronto in tavola ... arriva lo zombie femminista!");
            default: Zombie z = new Zombie("Zombie doppio", 15, 2, "Zombie avanzano! SONO IN DUE STI BASTARDI! Preparati Zio Porc!"); z.doubleSpawn = true; return z;
        }
    }

    boolean canBeKilledWith(String w) {
        return switch (type) {
            case "Zombie palestrato" -> w.equals("Fucile") || w.equals("Pistola") || w.equals("Lanciarazzi");
            case "Zombie femminista" -> w.equals("Fucile") || w.equals("Pistola") || w.equals("Lanciarazzi");
            default -> true;
        };
    }
}
