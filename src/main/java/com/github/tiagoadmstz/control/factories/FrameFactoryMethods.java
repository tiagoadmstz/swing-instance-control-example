package com.github.tiagoadmstz.control.factories;

import com.github.tiagoadmstz.control.frames.FormTwo;

public class FrameFactoryMethods {

    public FormTwo createFormTwo(String text){
        return new FormTwo(text);
    }
    
}
