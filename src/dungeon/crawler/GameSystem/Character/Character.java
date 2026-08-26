package dungeon.crawler.GameSystem.Character;

import java.util.ArrayList;

public class Character {
    // TODO: make these private!
    // TODO: is this inhertience even necessary? if not just make Character and PartyCharacter one class
    public String name;
    public int maxHp;
    public int maxMP;
    public int hp;
    public int mp;
    public int initiative;
    public Stance stance;
    public ArrayList<Condition> conditions;

    public boolean isDead;

    public Character(){};
    public Character(
        String name,
        int maxHp,
        int maxMP,
        int hp,
        int mp,
        Stance stance,
        ArrayList<Condition> conditions,
        boolean isDead
    ) {
        this.name = name;
        this.maxHp = maxHp;
        this.maxMP = maxMP;
        this.hp = hp;
        this.mp = mp;
        this.stance = stance;
        this.conditions = conditions;
        this.isDead = isDead;
    }

    public int getMp() {
        return mp;
    }

    public int getHp() {
        return hp;
    }
}
