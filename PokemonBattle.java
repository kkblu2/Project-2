import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Move {
    private String name;
    private int power;
    private double accuracy;
    private String effect;

    public Move(String name, int power, double accuracy, String effect) {
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.effect = effect;
    }

    public void applyEffect(Pokemon target) {
        if (effect != null && Math.random() < 0.3) {
            target.setStatus(effect);
        }
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public double getAccuracy() {
        return accuracy;
    }
}

class Pokemon {
    private String name;
    private int maxHp;
    private int hp;
    private int attackPower;
    private int defense;
    private int speed;
    private List<Move> moves;
    private String status;
    private boolean isDefending;

    public Pokemon(String name, int attackPower, int defense, int speed) {
        this.name = name;
        this.maxHp = 100;
        this.hp = 100;
        this.attackPower = attackPower;
        this.defense = defense;
        this.speed = speed;
        this.moves = new ArrayList<>();
        this.status = null;
        this.isDefending = false;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void attack(Pokemon opponent, Move move) {
        if (Math.random() < move.getAccuracy()) {
            int damage = (attackPower * move.getPower()) / opponent.defense;
            if (opponent.isDefending) {
                damage /= 2;
                System.out.println(opponent.name + " defended and took reduced damage!");
            }
            opponent.takeDamage(damage);
            System.out.println(name + " used " + move.getName() + "!");
            System.out.println(opponent.name + " took " + damage + " damage!");
            move.applyEffect(opponent);
        } else {
            System.out.println(name + "'s attack missed!");
        }
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp == 0) {
            System.out.println(name + " fainted!");
        }
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
        System.out.println(name + " healed " + amount + " HP!");
    }

    public void defend() {
        isDefending = true;
        System.out.println(name + " is defending!");
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
    }
}

class Trainer {
    private String name;
    private Pokemon pokemon;

    public Trainer(String name, Pokemon pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }

    public String getName() {
        return name;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
}

public class PokemonBattle {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void battle(Trainer trainer1, Trainer trainer2) {
        System.out.println("Battle between " + trainer1.getName() + " and " + trainer2.getName() + " begins!");

        while (trainer1.getPokemon().getHp() > 0 && trainer2.getPokemon().getHp() > 0) {
            System.out.println("\n" + trainer1.getName() + "'s " + trainer1.getPokemon().getName() + ": "
                    + trainer1.getPokemon().getHp() + " HP");
            System.out.println(trainer2.getName() + "'s " + trainer2.getPokemon().getName() + ": "
                    + trainer2.getPokemon().getHp() + " HP");

            System.out.print(trainer1.getName() + ", press 'f' to fight, 'h' to heal, or 'd' to defend: ");
            String action = scanner.nextLine().toLowerCase();

            trainer1.getPokemon().setDefending(false);

            switch (action) {
                case "f":
                    Move move = trainer1.getPokemon().getMoves()
                            .get(random.nextInt(trainer1.getPokemon().getMoves().size()));
                    trainer1.getPokemon().attack(trainer2.getPokemon(), move);
                    break;
                case "h":
                    trainer1.getPokemon().heal(20);
                    break;
                case "d":
                    trainer1.getPokemon().defend();
                    break;
                default:
                    System.out.println("Invalid action. Skipping turn.");
            }

            if (trainer2.getPokemon().getHp() > 0) {
                trainer2.getPokemon().setDefending(false);
                Move rivalMove = trainer2.getPokemon().getMoves()
                        .get(random.nextInt(trainer2.getPokemon().getMoves().size()));
                trainer2.getPokemon().attack(trainer1.getPokemon(), rivalMove);
            }

            // Apply status effects
            if ("Poison".equals(trainer1.getPokemon().getStatus())) {
                trainer1.getPokemon().takeDamage(5);
                System.out.println(trainer1.getPokemon().getName() + " is hurt by poison!");
            }
            if ("Poison".equals(trainer2.getPokemon().getStatus())) {
                trainer2.getPokemon().takeDamage(5);
                System.out.println(trainer2.getPokemon().getName() + " is hurt by poison!");
            }
        }

        if (trainer1.getPokemon().getHp() == 0) {
            System.out.println(trainer2.getName() + " wins!");
        } else {
            System.out.println(trainer1.getName() + " wins!");
        }
        System.out.println("Game Over");
    }

    public static void main(String[] args) {
        Move tackle = new Move("Tackle", 40, 1.0, null);
        Move ember = new Move("Ember", 40, 1.0, "Burn");
        Move waterGun = new Move("Water Gun", 40, 1.0, null);
        Move thundershock = new Move("Thundershock", 40, 1.0, "Paralyze");
        Move poisonSting = new Move("Poison Sting", 15, 1.0, "Poison");

        Pokemon charmander = new Pokemon("Charmander", 52, 43, 65);
        charmander.addMove(tackle);
        charmander.addMove(ember);

        Pokemon squirtle = new Pokemon("Squirtle", 48, 65, 43);
        squirtle.addMove(tackle);
        squirtle.addMove(waterGun);

        Pokemon pikachu = new Pokemon("Pikachu", 55, 40, 90);
        pikachu.addMove(tackle);
        pikachu.addMove(thundershock);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", 49, 49, 45);
        bulbasaur.addMove(tackle);
        bulbasaur.addMove(poisonSting);

        List<Pokemon> availablePokemon = List.of(charmander, squirtle, pikachu, bulbasaur);

        System.out.println("Welcome to Pokémon Battle!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        System.out.println("Choose your Pokémon:");
        for (int i = 0; i < availablePokemon.size(); i++) {
            System.out.println((i + 1) + ". " + availablePokemon.get(i).getName());
        }

        System.out.print("Enter the number of your choice: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        Pokemon playerPokemon = (choice >= 0 && choice < availablePokemon.size()) ? availablePokemon.get(choice)
                : pikachu;

        if (choice < 0 || choice >= availablePokemon.size()) {
            System.out.println("Invalid choice. Default to Pikachu.");
        }

        Trainer player = new Trainer(playerName, playerPokemon);
        Trainer rival = new Trainer("Rival", availablePokemon.get(random.nextInt(availablePokemon.size())));

        battle(player, rival);
    }
}