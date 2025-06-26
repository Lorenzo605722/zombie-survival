import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Game {
    Scanner scanner = new Scanner(System.in);
    Player player;
    Random random = new Random();
    int round = 1;
    final int MAX_ROUND = 12;
    boolean reachedBunker = false;

    List<Location> allLocations = List.of(
            new Location("Supermercato abbandonato", 0.6, false, "Non avevo mai visto il supermercato così buio... gli scaffali vuoti raccontano una storia di fuga e disperazione."),
            new Location("Boscaglia", 0.3, true, "Una volta arrivato nella boscaglia, il silenzio si fece pesante. Il vento tra gli alberi sembrava sussurrare antichi avvertimenti."),
            new Location("Centro città", 0.8, false, "Le strade vuote rimbombano dei miei passi... ma lì in fondo vedo un supermercato."),
            new Location("Palude", 0.7, false, "I tuoi piedi affondano nel fango. La nebbia è spessa. Qualcosa si muove nell'acqua stagnante..."),
            new Location("Casa abbandonata", 0.5, false, "Il pavimento scricchiola sotto i tuoi passi, ogni ombra sembra osservarti."),
            new Location("Ospedale", 0.8, false, "I corridoi puzzano di disinfettante stantio e morte."),
            new Location("CNA di Bologna", 0.4, false, "Un tempo si parlava di imprese, ora solo sopravvivenza."),
            new Location("Zoo", 0.6, false, "Le gabbie sono rotte. Urla strane si confondono tra animali e mostri."),
            new Location("Scuola Superiore Aldini", 0.2, true, "Una porta di metallo. Un codice. Silenzio. Poi... si apre. Hai trovato il rifugio.")
    );

    List<String> loot = List.of("occhiali da sole", "cappello", "un pupazzo", "un preservativo usato", "un cucchiaio", "una penna", "una sciarpa", "un telecomando", "un libro");

    public void start() throws InterruptedException {
        print("BENVENUTO in ZOMBIE SURVIVAL: Fuga verso una nuova VITA!\n" +
                "Ti trovi a BOLOGNA,\nil tuo scopo è quello di trovare un luogo che possa darti " +
                "sicurezza, rifugio e sostentamento.\nTrova il bunker, e sopravvivi 12 round.\n" +
                "Riuscirà una persona con un QI basso come il tuo a trovare una via di fuga? ", 80);

        System.out.print("Inserisci il tuo sciocco nome sopravvissuto/a: ");
        player = new Player(scanner.nextLine());

        print("Scegli la tua arma iniziale prima che mi incazzi:", 30);
        player.chooseWeapon(scanner);
        player.addItem("Medikit");
        player.addItem("Granata");

        while (round <= MAX_ROUND && player.isAlive() && !reachedBunker) {
            System.out.printf("\n--- ROUND %d ---%n", round);
            playRound();
            round++;
        }

        if (reachedBunker)
            print("🎉 Sei arrivato al bunker! Sei salvo! (purtroppo)", 40);
        else if (player.isAlive())
            print("✅ Hai sopravvissuto, ma la fine è ancora lontana...", 30);
        else
            print("💀 Sei stato sopraffatto dagli zombie... scemo/a", 40);

        // --- RESOCONTO FINALE ---
        System.out.println("\n--- RESOCONTO FINALE ---");
        System.out.println("Nome: " + player.name);
        System.out.println("HP finali: " + player.hp);
        System.out.println("Arma utilizzata: " + player.weapon);
        System.out.println("Zombie uccisi: " + player.zombieUccisi);
        System.out.println("Oggetti trovati: " +
                (player.inventory.isEmpty() ? "Nessuno" : String.join(", ", player.inventory)));
    }


    void playRound() throws InterruptedException {
        List<Location> options = new ArrayList<>(allLocations);
        Collections.shuffle(options);
        options = options.subList(0, 3);

        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, options.get(i).name);
        }

        int choice;
        do {
            System.out.print("Scegli una destinazione (1-3): ");
            choice = scanner.nextInt();
            scanner.nextLine();
        } while (choice < 1 || choice > 3);

        Location loc = options.get(choice - 1);
        print(loc.description, 35);

        if (loc.name.equals("Bunker")) {
            reachedBunker = true;
            return;
        }

        if (random.nextDouble() < loc.danger) {
            int zombieCount = random.nextDouble() < 0.3 ? 2 : 1;
            for (int i = 0; i < zombieCount && player.isAlive(); i++) {
                Zombie z = Zombie.random();
                print(z.narrative, 35);
                combat(z);
            }
        } else {
            print("Nessun segno di vita... per ora.", 30);
        }

        if (random.nextDouble() < 0.4) {
            String find = loot.get(random.nextInt(loot.size()));
            print("Hai trovato " + find + ".", 30);
            player.addItem(find);
        }
    }

    void combat(Zombie z) {
        while (z.hp > 0 && player.hp > 0) {
            System.out.println("\n1. Attacca\n2. Usa oggetto\n3. Fuggi");
            int c = scanner.nextInt(); scanner.nextLine();
            switch (c) {
                case 1 -> player.attack(z);
                case 2 -> player.useItem(scanner);
                case 3 -> {
                    if (random.nextBoolean()) {
                        print("Sei fuggito con successo!", 20); return;
                    } else print("Non sei riuscito a fuggire!", 20);
                }
            }
            if (z.hp > 0) player.takeDamage(z.damage);
        }
    }

    void print(String s, int delay) {
        for (char c : s.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(delay); } catch (Exception e) {}
        }
        System.out.println();
    }
}