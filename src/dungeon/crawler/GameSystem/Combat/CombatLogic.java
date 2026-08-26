package dungeon.crawler.GameSystem.Combat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue.ValueType;

import dungeon.crawler.Data.Spells.Spell;
import dungeon.crawler.Data.Spells.SpellNames;
import dungeon.crawler.Data.Spells.SpellRegistry;
import dungeon.crawler.Data.Spells.SpellType;
import dungeon.crawler.GameSystem.Character.Combatant;
import dungeon.crawler.GameSystem.Character.Enemy;
import dungeon.crawler.GameSystem.Character.PartyCharacter;
import dungeon.crawler.GameSystem.GameState.CombatActionState;
import dungeon.crawler.GameSystem.GameState.CombatPhase;
import dungeon.crawler.GameSystem.Inventory.Item;
import dungeon.crawler.GameSystem.Leveling.LevelTable;
import dungeon.crawler.MainGame;
import dungeon.crawler.Menu.CombatEventScreen;
import dungeon.crawler.Observers.CombatLogicObserver;
import dungeon.crawler.Utils.StringUtils;

public class CombatLogic {
    private final CombatStateManager combatState;
    public CombatPhase phase;
    public LinkedList<CombatAction> actionQueue;
    public CombatEventScreen eventScreen;
    public ArrayList<CombatLogicObserver> combatLogicObservers;
    private PartyActionTracker turnTracker;
    private int currentCombatantID;
    public int xpGained;
    private MainGame game;
    private boolean returnFocus;
    private CombatActionHandler actionHandler;

    public CombatLogic(
        CombatEventScreen eventScreen,
        MainGame game,
        PartyActionTracker turnTracker,
        CombatStateManager combatState
    ){
        this.eventScreen = eventScreen;
        this.combatLogicObservers = new ArrayList<CombatLogicObserver>();
        this.actionQueue = new LinkedList<>();
        this.game = game;
        this.combatState = combatState;
        this.xpGained = 0;
        this.currentCombatantID = 0;
        this.returnFocus = false;
        this.turnTracker = turnTracker;
        this.actionHandler = new CombatActionHandler(game.gameState.party, combatState.getCurrentEnemyRoster());
    }
    public void advanceCombat(){
        /* this is run every frame and is for actions that require to wait until messages are done
        being read to run */
        if(eventScreen.isShowingMessage()){
            return ;
        }

        switch(phase) {
            case INTRO:
                notifyOnActionMenuReset();
                advanceState(CombatPhase.ACTIONSELECT);
                break;

            case ACTIONSELECT:
                break;

            case ACTIONSELECT_COMPLETE:
                checkForActionSelectCompletion();
                break;

            case INITIATIVE:
                Gdx.app.log("Combat", "Enemies Decide their action");
                decideEnemyActions();
                sortByInitiative();
                notifyOnActionSelectComplete();
                advanceState(CombatPhase.INITIATIVE_COMPLETE);
                break;

            case INITIATIVE_COMPLETE:
                Gdx.app.log("Combat", "Rolling for Initiative");
                advanceState(CombatPhase.RESOLVE_NEXT_ACTION);
                break;

            case RESOLVE_NEXT_ACTION:
                if (!actionQueue.isEmpty()) {
                    CombatAction nextAction = actionQueue.pop();
                    handleAction(nextAction);

                } else {
                    advanceState(CombatPhase.ACTIONSELECT);
                }
                break;
            case ACTION_COMPLETE:
                notifyOnActionComplete();
                advanceState(CombatPhase.CHECK_CONDITIONS);
                break;
            case CHECK_CONDITIONS:
                checkWinConditions();
                break;
            case LOSS:
                advanceState(CombatPhase.END_LOSS);
                break;
            case VICTORY:
                rewards();
                advanceState(CombatPhase.END_VICTORY);
                break;

            case END_VICTORY:
                notifyOnVictory();
                break;

            case END_LOSS:
                notifyOnLoss();
                break;

            case NEW_ROUND:
                notifyOnActionMenuReset();
                turnTracker.resetTracker();
                advanceState(CombatPhase.ACTIONSELECT);
                break;


        }
    }

    public void advanceState(CombatPhase nextPhase){
        phase =  nextPhase;
        Gdx.app.log("StateCheck", "Current Phase: " + phase);

    }



    public void handleAction(CombatAction currentAction){
        CombatActionState aState = currentAction.action;
        ArrayList<String> messages;
        String[] messageArray;
        switch(aState){
            // TODO: break ATTACK up into a sub state machine so messages can be displayed
            // and statuses can be updated between the message breaks
            case ATTACK:
                messages = actionHandler.handleAttack(currentAction);
                messageArray = messages.toArray(new String[0]);
                eventScreen.addMessages(messageArray);
                advanceState(CombatPhase.ACTION_COMPLETE);
                break;
            case DEFEND:
                Gdx.app.log("Combat", "Defense Made");
                eventScreen.addMessages(new String[] {"the enemy stares at you dumbfounded"});
                advanceState(CombatPhase.ACTION_COMPLETE);
                break;
            case USE:
                messages = actionHandler.handleItemUse(currentAction);
                messageArray = messages.toArray(new String[0]);
                eventScreen.addMessages(messageArray);
                advanceState(CombatPhase.ACTION_COMPLETE);
                break;

            case CAST:
                messages = actionHandler.handleSpell(currentAction);
                messageArray = messages.toArray(new String[0]);
                eventScreen.addMessages(messageArray);
                advanceState(CombatPhase.ACTION_COMPLETE);
                break;
            default:
                Gdx.app.log("Combat", "ERROR: an action that does not exist");
                messages = actionHandler.handleMiscAction(currentAction);
                messageArray = messages.toArray(new String[0]);
                eventScreen.addMessages(messageArray);
                advanceState(CombatPhase.ACTION_COMPLETE);
        }
    }

    public void addAction(
        int id,
        CombatActionState actionState,
        int targetId
    ) {

        Combatant currentCombatant = game.gameState.party.get(id);
        int initiative = currentCombatant.rollInitiative();

        CombatAction newAction;
        if(actionState == CombatActionState.ATTACK) {
            Combatant target = combatState.getCurrentEnemyRoster().getOrDefault(targetId, null);
            newAction = new CombatAction(
                id,
                initiative,
                currentCombatant,
                actionState,
                target
            );
        } else {
            newAction = new CombatAction(
                id,
                initiative,
                currentCombatant,
                actionState
            );
        }


        this.actionQueue.add(newAction);

        String actorName = ((PartyCharacter)currentCombatant).name;
        String[] flavorText = new String[] {
            StringUtils.format("%s has chosen to %s", actorName, actionState)
        };


        currentCombatantID++;
        returnFocus = true;
        eventScreen.addMessages(flavorText);
        notifyOnEventScreenFocus();
        advanceState(CombatPhase.ACTIONSELECT_COMPLETE);
    }

    public void addCastAction(
        int id,
        CombatActionState actionState,
        int targetId,
        SpellNames spellName
    ) {
        // determine the spells intent and initiative and add it to the action queue
        Combatant currentCombatant = game.gameState.party.get(id);
        PartyCharacter currentParyCharacter = ((PartyCharacter)currentCombatant);
        String actorName = currentParyCharacter.name;

        Spell spell = SpellRegistry.INSTANCE.get(spellName);
        // WILL NEED A SWITCH STATEMENT HERE
        if(spell.getCost() > currentParyCharacter.getMp()){
            String[] flavorText = new String[] {
                StringUtils.format("%s does not have enough spell points", actorName)
            };
            notifyOnEventScreenFocus();
            // DO NOT Advance to next character
            return;
        }
        Combatant target;
        int initiative = currentCombatant.rollInitiative();
        CombatAction newAction;

        if(spell.getType() == SpellType.AOE_DEFENSE || spell.getType() == SpellType.AOE_OFFENSE){
            newAction = new CombatAction(
                id,
                initiative,
                currentCombatant,
                actionState,
                spellName
            );
        } else if(
            spell.getType() == SpellType.SINGLE_OFFENSE
        ) {
            target = combatState.getCurrentEnemyRoster().getOrDefault(targetId, null);
            if(target == null){
                throw new IllegalArgumentException(StringUtils.format("%s is not a valid enemy key", String.valueOf(targetId)));
            }
            newAction = new CombatAction(
                id,
                initiative,
                currentCombatant,
                actionState,
                target,
                spellName
            );
        } else {
            target = game.gameState.party.getOrDefault(targetId, null);
            if(target == null){
                throw new IllegalArgumentException(StringUtils.format("%s is not a valid party key", String.valueOf(targetId)));
            }
            newAction = new CombatAction(
                id,
                initiative,
                currentCombatant,
                actionState,
                target,
                spellName
            );
        }



        this.actionQueue.add(newAction);
        String[] flavorText = new String[] {
            StringUtils.format("%s has chosen to %s %s", actorName, actionState, spell.getName())
        };
        currentCombatantID++;
        returnFocus = true;
        eventScreen.addMessages(flavorText);
        notifyOnEventScreenFocus();
        advanceState(CombatPhase.ACTIONSELECT_COMPLETE);

    }

    public void addItemAction(
        int id,
        CombatActionState actionState,
        int targetId,
        Item item
    ){
        Combatant currentCombatant = game.gameState.party.get(id);
        Combatant target = game.gameState.party.get(targetId);
        int initiative = currentCombatant.rollInitiative();

        CombatAction newAction = new CombatAction(
            id,
            initiative,
            currentCombatant,
            actionState,
            target,
            item
        );
        this.actionQueue.add(newAction);
        String[] flavorText = new String[] {
            StringUtils.format("%s has chosen to use %s", currentCombatant.getName(), item.name)
        };
        currentCombatantID++;
        returnFocus = true;
        eventScreen.addMessages(flavorText);
        notifyOnEventScreenFocus();
        advanceState(CombatPhase.ACTIONSELECT_COMPLETE);
    }


    public void checkForActionSelectCompletion(){
        if(turnTracker.nextEligibleCombatant()){
            advanceState(CombatPhase.ACTIONSELECT);
            // send signal to send focus back to action menu without resetting currentCombatantID
            notifyOnCombatMenuFocus();
        } else {
            playerActionSelectComplete();
        }
    }

    public void checkWinConditions(){
        // TODO: Break up this logic
        // remove any actions from dead combatants
        actionQueue.removeIf(action -> !action.combatant.canAttack());
        // Check for total party wipe
        boolean isAnyoneAlive = false;
        for(PartyCharacter partyMember: this.game.gameState.party.values()){
            if(!partyMember.isDead){
                isAnyoneAlive = true;
            }
        }
        if(!isAnyoneAlive){
            eventScreen.addMessages(new String[] {"All adventurers have died!"});
            advanceState(CombatPhase.LOSS);
            return;
        }
        // add XP
        for(Enemy enemy: this.combatState.getCurrentEnemyRoster().values()){
            if(enemy.isDead){
                this.xpGained += enemy.earnedXP;
            }
        }
        // Check for dead enemies and remove from board
        // TODO: Terrible. Do not remove from game state, move to combat state instead
        this.combatState.getCurrentEnemyRoster().values().removeIf(enemy -> enemy.checkDeath());
        // check for total enemy wipe
        if(this.combatState.getCurrentEnemyRoster().isEmpty()){
            eventScreen.addMessages(new String[] {"All enemies have been vanquished!"});

            eventScreen.addMessages(new String[] {
                StringUtils.format("You have gained %s experience points from the fight", String.valueOf(xpGained))
            });

            advanceState(CombatPhase.VICTORY);
            return;
        }
        //else
        if (actionQueue.isEmpty()) {
            advanceState(CombatPhase.NEW_ROUND);
        } else {
            advanceState(CombatPhase.RESOLVE_NEXT_ACTION);
        }
    }

    public void rewards(){
                // TODO: bad
        // this.game.gameState.player.xp = this.game.gameState.player.xp + xpGained;
        // int xpForEach = xpGained / this.game.gameState.party.size();
        int xpForEach = (int) Math.ceil((double) xpGained / this.game.gameState.party.size());

        this.game.gameState.party.entrySet().stream().forEach(partyEntry -> {
            PartyCharacter partyMember = partyEntry.getValue();
            partyMember.xp = partyMember.xp + xpForEach;

            int nextLevel = partyMember.level + 1;
            if(partyMember.xp >= LevelTable.getRequiredXp(nextLevel)){
                ArrayList<String> messages = partyMember.LevelUp(nextLevel);
                eventScreen.addMessages(messages.toArray(new String[0]));
            }
        });

        Random roll = new Random();
        int addGold = roll.nextInt(100) + 1;
        this.game.gameState.gold = this.game.gameState.gold + addGold;
        eventScreen.addMessages(new String[] {
                StringUtils.format(
                    "You earn %s gold from this fight",
                    String.valueOf(addGold)
                )
            }
        );



    }

    public void addListener(CombatLogicObserver listener){
        combatLogicObservers.add(listener);
    }


    public void notifyOnActionMenuReset(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onActionMenuReset();
        }
    }

    public void notifyOnCombatMenuFocus(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onActionMenuFocus();
        }
    }

    public void notifyOnEventScreenFocus(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onEventScreenFocus();
        }
    }


    public void notifyOnActionSelectComplete(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onActionSelectComplete();
        }
    }

    public void notifyOnActionComplete(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onActionComplete();
        }
    }

    public void notifyOnLoss(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onLoss();
        }
    }

    public void notifyOnVictory(){
        for(CombatLogicObserver listener: combatLogicObservers){
            listener.onVictory();
        }
    }

    private void decideEnemyActions(){
        for (int enemyID: this.combatState.getCurrentEnemyRoster().keySet()){
            actionQueue.add(EnemyAttackLogic.decideAction(
                this.combatState.getCurrentEnemyRoster().get(enemyID),
                enemyID,
                this.game.gameState
            ));
        }
    }

    public void playerActionSelectComplete(){
        advanceState(CombatPhase.INITIATIVE);
    }


    private void sortByInitiative(){
        actionQueue.sort((a, b) -> Integer.compare(b.iniative, a.iniative));
    }


}
