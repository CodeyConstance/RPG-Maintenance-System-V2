package com.home.rpgapp.controller;

import com.home.rpgapp.model.Character;

public class SessionState {
    private static Character selectedCharacter;

    public static void setSelectedCharacter(Character character) {
        selectedCharacter = character;
    }

    public static Character getSelectedCharacter() {
        return selectedCharacter;
    }
}
