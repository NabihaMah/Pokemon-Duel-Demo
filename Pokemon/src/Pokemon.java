import java.util.Arrays;

//deals with individual pokemon
class Pokemon {
    private int hp; // the Pokemon's hp
    private final String name; // the Pokemon's name
    private final String type; // the Pokemon's type
    private final String resistance; // the Pokemon's  resistance
    private final String weakness; // the Pokemon's weakness
    private final int attackNum; // the number of attacks the Pokemon has
    Attack[] attacks; // the Pokemon's attacks
    private int energy = 50; // the Pokemon's energy
    boolean stunned = false; // if the Pokemon is stunned
    boolean disabled = false; // if the pokemon is disabled

    //
    public Pokemon(String block) {
        String[] stats = block.split(",");

        name = stats[0];
        hp = Integer.parseInt(stats[1]);
        type = stats[2];
        resistance = stats[3];
        weakness = stats[4];
        attackNum = Integer.parseInt(stats[5]);

        Attack[] attacks = new Attack[attackNum]; //  array of the Pokemon's attacks
        String[] attack = new String[attackNum]; // Strings of attacks
        for (int i = 0; i < attackNum; i++) { // adds an attack to the String attacks
            attack[i] = stats[i * 4 + 6] + "," + stats[i * 4 + 7] + "," + stats[i * 4 + 8] + "," + stats[i * 4 + 9];
        }

        for (int i = 0; i < attackNum; i++){ // adds each attack to the Attack array
            Attack att = new Attack(attack[i]);
            attacks[i] = att;
    }
        this.attacks = attacks;
    }

    // gets the Pokemon's hp
    public int getHP() {
        return hp;
    }

    // Checks if the Pokemon is awake
    public boolean awake(){
        return hp > 0;
    }

    // Recharges the Pokemon
    public void recharge() {
        energy += 10;
        if (energy > 50) {
            energy = 50;
        }
    }

    // heals Pokemon at the end of the battle
    public void endHeal() {
    hp += 20;
    }

    // gets the Pokemon's energy
    public int getEnergy(){
        return energy;
    }

    // carries out the Pokemon's attack, takes the Pokemon being attacked and attack number
    public void attack(Pokemon enemy, int i){
            Attack att = attacks[i];
            if (att.specialAttack() == 1) { // if the pokemon was stunned, it becomes stunned
                enemy.stunned = true;
            } else if (att.specialAttack() == 2) { // if the pokemon was disabled, it becomes disabled
                enemy.disabled = true;
            }
            if (disabled){ // if the Pokemon is disabled, it does 10 less damage
                att.damage += 10;
            }
            if(enemy.resistance.equals(type)){
                att.damage /= 2;
            }
            else if (enemy.weakness.equals(type)){
                att.damage *= 2;
            }
            enemy.hp -= att.damage;
            energy -= att.energyCost; // the energy cost is subtracted from the energy
        if(enemy.hp<0){
            enemy.hp =0;
        }
        if (hp<0){
            hp=0;
        }
    }

    // Checks if there is enough energy to carry out an attack
    public int attackAvailable(){
        int i = 0; // umber of possible attacks available
        for (int k = 0; k < attackNum; k++) {
            Attack att = attacks[k];
            if (energy > att.getEnergyCost()) {
                i++;
            }
        }
        return i;
    }

    // prints attacks to pick from
    public void attackSelect(){
        for (int k = 0; k < attackNum; k++) {
            Attack att = attacks[k];
            if (energy > att.getEnergyCost()) {
                System.out.print(k + ":  ");
                System.out.println(attacks[k]); // prints possible attacks
            }
        }
    }


    public String toString() {
        return name + " " + hp + " " + type + " " + resistance + " " + weakness + " " + attackNum + " " + Arrays.toString(attacks);
    }
}



