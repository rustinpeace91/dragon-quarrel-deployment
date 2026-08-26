package dungeon.crawler.Utils;

import java.util.Map;

import dungeon.crawler.GameSystem.Character.PartyCharacter;

public class PartyUtils {

    public static PartyCharacter returnPartyMemberByName(
        Map<Integer, PartyCharacter> party, String name
    ) {
        for (Map.Entry<Integer, PartyCharacter> partyMember : party.entrySet()) {
            String s = "yeah";
            if (partyMember.getValue().name.equals(name)) {
                return partyMember.getValue();
            }
        }
        return null;
    }

    public static void resurrectDeadPartyMembers(Map<Integer, PartyCharacter> party){
        for (Map.Entry<Integer, PartyCharacter> partyMember : party.entrySet()) {
            if(partyMember.getValue().isDead){
                partyMember.getValue().resurrect();
            }
        }
    }

    public static void cureAllAilments(Map<Integer, PartyCharacter> party){
        for (Map.Entry<Integer, PartyCharacter> partyMember : party.entrySet()) {
            partyMember.getValue().removeAllStatuses();
        }
    }



}
