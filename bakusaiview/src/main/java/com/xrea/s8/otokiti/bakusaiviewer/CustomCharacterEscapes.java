package com.xrea.s8.otokiti.bakusaiviewer;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

public class CustomCharacterEscapes extends CharacterEscapes {

	private static final long serialVersionUID = 7736657591529310603L;
	private final int[] asciiEscapes;

    public CustomCharacterEscapes() {
        int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        esc['"']  = CharacterEscapes.ESCAPE_STANDARD;
        esc['\''] = CharacterEscapes.ESCAPE_STANDARD;
        esc['/']  = CharacterEscapes.ESCAPE_STANDARD;
        esc['\n'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['>'] = CharacterEscapes.ESCAPE_STANDARD;
        esc['<'] = CharacterEscapes.ESCAPE_STANDARD;
        asciiEscapes = esc;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return null;
    }
}
