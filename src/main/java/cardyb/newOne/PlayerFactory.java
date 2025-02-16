package cardyb.newOne;

import cardyb.newOne.models.ActionEntity;
import cardyb.newOne.models.CardEntity;
import cardyb.newOne.models.PlayerEntity;

import java.util.*;

public class PlayerFactory {

    public static PlayerEntity createStandardPlayer() {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("pot", 540);
        attributes.put("tokens", 5);
        attributes.put("Special-Abilties", new String[]{"no something", "extra something", "hat"});

        List<ActionEntity> defaultTurnActions = new ArrayList<>();
        defaultTurnActions.add(new ActionEntity("PICK_UP_A_CARD"));
        defaultTurnActions.add(new ActionEntity("PICK_UP_A_CARD"));
        defaultTurnActions.add(new ActionEntity("PICK_UP_A_CARD"));
        defaultTurnActions.add(new ActionEntity("PICK_UP_A_CARD"));
        defaultTurnActions.add(new ActionEntity("PLAY_A_CARD"));
        defaultTurnActions.add(new ActionEntity("DISCARD_A_CARD"));
        defaultTurnActions.add(new ActionEntity("END_TURN"));

        List<ActionEntity> nowActions = new ArrayList<>();
        List<List<ActionEntity>> nextActions = new ArrayList<>();

        List<CardEntity> discardPile = new ArrayList<>();
        List<CardEntity> deck = new ArrayList<>();

        String id = UUID.randomUUID().toString();

        return new PlayerEntity(id, defaultTurnActions, nowActions, nextActions, discardPile, deck, attributes);
    }


    public static PlayerEntity createPlayerWithNowActions() {
        PlayerEntity player = createStandardPlayer();

        List<ActionEntity> nowActions = new ArrayList<>();
        nowActions.add(new ActionEntity("PICK_UP_A_CARD"));
        nowActions.add(new ActionEntity("PICK_UP_A_CARD"));
        nowActions.add(new ActionEntity("PICK_UP_A_CARD"));
        nowActions.add(new ActionEntity("PICK_UP_A_CARD"));
        nowActions.add(new ActionEntity("PLAY_A_CARD"));
        nowActions.add(new ActionEntity("DISCARD_A_CARD"));
        nowActions.add(new ActionEntity("END_TURN"));
        player.setNowActions(nowActions);
        return player;
    }



}
