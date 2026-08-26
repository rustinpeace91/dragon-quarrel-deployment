package dungeon.crawler.GameSystem.Magic;

import dungeon.crawler.Data.Spells.Spell;
import dungeon.crawler.Data.Spells.SpellNames;

import java.util.ArrayList;

public class MagicSystem {
    public ArrayList<SpellNames> availableSpells;

    public MagicSystem(){
        this.availableSpells = new ArrayList();
    }

    public void addSpell(SpellNames spell){
        this.availableSpells.add(spell);
    }

    public void setSpells(ArrayList<SpellNames> spells){
        this.availableSpells = spells;
    }

}
