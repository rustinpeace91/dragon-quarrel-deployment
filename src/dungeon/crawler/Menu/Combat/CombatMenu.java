package dungeon.crawler.Menu.Combat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.Combat.CombatAction;
import dungeon.crawler.GameSystem.Combat.CombatStateManager;
import dungeon.crawler.GameSystem.Combat.CombatUtils;
import dungeon.crawler.GameSystem.Combat.PartyActionTracker;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.GameState.GameState;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.Menu.BaseLinearMenu;
import dungeon.crawler.Menu.Combat.Action.ActionSubMenu;
import dungeon.crawler.Menu.Combat.Inventory.ItemSelectMenu;
import dungeon.crawler.Menu.Combat.Magic.SpellSelectMenu;
import dungeon.crawler.Observers.ActionSelectObserver;

public class CombatMenu extends BaseLinearMenu {
    private final List<ActionSelectObserver> actionSelectObservers = new ArrayList<>();
    private final CombatStateManager combatState;
    private GameState gameState;
    private PartyActionTracker turnTracker;
    private boolean readingMessages;

    private Actor currentActor;

    public CombatMenu (
        Skin skin,
        GameState gameState,
        CombatStateManager combatState,
        PartyActionTracker turnTracker
    ) {
        super(skin);
        this.gameState = gameState;
        this.turnTracker = turnTracker;
        this.combatState = combatState;
        setToggleable(false);
        this.initializeVisualMenu();
        // list of map IDS of all alive combatants

    }

    private void initializeVisualMenu(){
        this.clearChildren();
        this.initializeArrow();
        this.defaults().size(170f, 60f).pad(5f);
        this.addButton("Attack", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                BaseLinearMenu nextMenu = new AttackSubMenu(
                    skin,
                    gameState,
                    combatState
                );
                setSubMenu(nextMenu);
                openSubMenu(nextMenu);
            }
        });

        this.addButton("Action", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                int currentId = turnTracker.getCurrentCombatantID();
                PartyCharacter currentCombatant = gameState.party.get(currentId);

                BaseLinearMenu nextMenu = new ActionSubMenu(
                    skin,
                    gameState,
                    currentCombatant
                );
                setSubMenu(nextMenu);
                openSubMenu(nextMenu);
            }
        });

        this.addButton("Magic", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                int currentId = turnTracker.getCurrentCombatantID();
                PartyCharacter currentCombatant = gameState.party.get(currentId);
                if (currentCombatant.charClass.isMagicUser()){
                    ArrayList<SpellNames> spellList = currentCombatant.charClass.getMagicSystem().availableSpells;
                    BaseLinearMenu nextMenu = new SpellSelectMenu(
                        skin,
                        gameState,
                        currentCombatant,
                        spellList,
                        combatState
                    );
                    setSubMenu(nextMenu);
                    openSubMenu(nextMenu);
//                    handleCastAction(CombatActionState.CAST, 1, SpellNames.FIREBALL);
                }
            }
        });

        this.addButton("Inventory", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                int currentId = turnTracker.getCurrentCombatantID();
                PartyCharacter currentCombatant = gameState.party.get(currentId);
                /*

                */
                // TODO: check inventory UP HERE before menu is spawned
                ArrayList<Item> availableItems = currentCombatant.inventory.getInventoryList();
                if(!availableItems.isEmpty()){
                    BaseLinearMenu nextMenu = new ItemSelectMenu(
                        skin,
                        gameState,
                        currentCombatant,
                        availableItems
//                        spellList
                    );
                    setSubMenu(nextMenu);
                    openSubMenu(nextMenu);
                }


//                    handleCastAction(CombatActionState.CAST, 1, SpellNames.FIREBALL);
            }
        });

        this.addButton("Defend", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.log("Fight", "fuuuck u");
            }
        });

        this.addButton("Status", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.log("Fight", "fuuuck u");
            }
        });

        this.defaults().pad(2);

        this.pack();
        this.addFocusListeners();
    }
    private void handleAction(CombatActionState state, int targetId){
        int currentId = turnTracker.getCurrentCombatantID();
        notifyActionSelect(currentId, state, targetId);
//        if (currentCombatantID > 0){
//            Gdx.app.log("Combat", "yeah");
//        }
//        // TODO: maybe just check if there is a next int?
//        if (currentCombatantID < partyIDs.size()) {
//            int currentId = partyIDs.get(currentCombatantID);
//            notifyActionSelect(currentId, state, targetId);
//            currentCombatantID++;
//        } else {
//            Gdx.app.log("Combat", "Combatant ID index exceeds party size!!!!");
//        }

    }

    public void handleCastAction(SpellNames spellName, int targetId){
        int currentId = turnTracker.getCurrentCombatantID();
        notifyActionSelect(currentId, CombatActionState.CAST, targetId, spellName);
    }

    public void handleItemAction(Item item, int targetId){
        int currentId = turnTracker.getCurrentCombatantID();
        notifyActionSelect(currentId, CombatActionState.USE, targetId, item);
    }

    public void notifyActionSelect(int combatantId, CombatActionState actionState, int targetId){
        for (ActionSelectObserver observer : actionSelectObservers) {
            observer.onActionSelect(combatantId, actionState, targetId);
        }
    }

    public void notifyActionSelect(int combatantId, CombatActionState actionState, int targetId, SpellNames spellName){
        for (ActionSelectObserver observer : actionSelectObservers) {
            observer.onActionSelect(combatantId, actionState, targetId, spellName);
        }
    }

    public void notifyActionSelect(int combatantId, CombatActionState actionState, int targetId, Item item){
        for (ActionSelectObserver observer : actionSelectObservers) {
            observer.onActionSelect(combatantId, actionState, targetId, item);
        }
    }

    public void addActionSelectObserver(ActionSelectObserver observer) {
        actionSelectObservers.add(observer);
    }



    public void handleAttackSelection(int id){
        handleAction(CombatActionState.ATTACK, id);
    }

    public void handleActionSelection(CombatActionState action){
        handleAction(action, -1);
    }

    public void resetMenu(){
        this.buttonList = populateButtonList();
        resetMenuSelection();
    }

    public void initializeMenu(){
        this.buttonList = populateButtonList();
        resetMenuSelection();
    }

    public void setActive(boolean value){
        // simimalar to refreshAndSetActive except we always want to keep this menu visible
        if(value){
            this.buttonList = populateButtonList();
            resetMenuSelection();
        } else {
            getStage().setKeyboardFocus(null);
        }
    }



    public void setReadingMessages(boolean readingMessages) {
        this.readingMessages = readingMessages;
    }

}
