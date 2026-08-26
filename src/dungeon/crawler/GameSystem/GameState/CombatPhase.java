package dungeon.crawler.GameSystem.GameState;

public enum CombatPhase {
    INTRO,
    ACTIONSELECT,
    ACTIONSELECT_COMPLETE,
    INITIATIVE,
    INITIATIVE_COMPLETE,
    RESOLVE_NEXT_ACTION,
    ACTION_COMPLETE,
    CHECK_CONDITIONS,
    LOSS,
    VICTORY,
    END_VICTORY,
    END_LOSS,
    NEW_ROUND
    
}


/*

ACTIONSELECT
INITIATIVE
COMBATROUND


array messages
combatPhase state
initiative Map.of(int, Combatant)

function combatLoop(){
    if messages(
        diplsayMessages()
    ) else {
        case INITIATIVE{
            rollInitiative()
        }
        case ACTIONSELECT {
            // draw action menu 
            ActionMenu()
        }
        case COMBATROUND {
            // check iniative counter
            // pull Combatant object
            if player controlled {
                check action
                perform action
            } else {
                attack random character in party
                for now, write enemy logic later
            }
            updateCharacterStats()
            addMessages()
        }
        case EVALUATE {
            checkforCombatTermination()
        }
        case END {
            addXPandGold()
            queueMessages()
        }
    }
}

Attack(attacker, defender){
    
}

Magic(attacker, target){
    // check currently equipped spell
    // spells are equipped like weapons
    // create attack damage
    // substract spell points
    Attack(attacker, defender)
}

Action{
    action: enum
    target: id
}

*/