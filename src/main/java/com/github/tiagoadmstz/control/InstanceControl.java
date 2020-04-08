package com.github.tiagoadmstz.control;

import com.github.tiagoadmstz.control.factories.FrameFactory;
import com.github.tiagoadmstz.control.frames.FormOne;

public class InstanceControl {

    public static void main(String[] args) {
        FrameFactory.getInstance(FormOne.class, true, true);
    }

}
