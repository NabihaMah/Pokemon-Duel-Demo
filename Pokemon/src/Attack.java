// deals with each attack
class Attack {
    String attackName; // name of the attack
    int energyCost; // energy cost for the attack
    int damage; // damage delivered for the attack
    String special; // special action for the attack

    // Constructor
    public Attack(String block){
            String[] stats = block.split(",");
            attackName = stats[0];
            energyCost = Integer.parseInt(stats[1]);
            damage = Integer.parseInt(stats[2]);
            special = stats[3];
    }

    // Checks for a special attack
    public int specialAttack(){
        if (special.equals("stun")){
            if (stun()) {
                return 1;
            }
        }
        else if (special.equals("wild card")){
            wildCard();
        }
        else if (special.equals("wild storm")){
            wildStorm();
        }
        else if (special.equals("disable")){
            disable();
            return 2;
        }
        else if (special.equals("recharge")){
            recharge();
        }
        return 0;
    }

    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)) + low;
    }

    // performs stun - a special attack
    public boolean stun(){
        int chance = randint(0,1); // 50% chance of occurance
        return chance == 0;
    }

    // gets energy cost
    public int getEnergyCost(){
        return energyCost;
    }

    // performs wild card - a special attack
    public void wildCard(){ // 50% chance of occurance
        int chance = randint(0,1);
      if (chance == 0){
       damage = 0;
      }
    }

    // performs wild storm - a special attack
    public void wildStorm(){ // 50% chance of occurance
        int i = randint(0,1);
        if (i==0){
            damage=0;
        }
    }

    // disables pokemon
    public void disable(){
    }

    // recharges the Pokemon - a special attack
    public void recharge(){
        energyCost -= 20;
    }

    public String toString(){
       return attackName + " energy cost: " + energyCost + " damage: " + damage + " " + special;
    }


}

