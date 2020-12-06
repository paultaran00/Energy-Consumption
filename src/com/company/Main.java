package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main.java - Implementeaza "Estimarea consumului de energie"
 */

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File f = new File("Aparate");
        File g = new File("consumfZLA");
        Gui obj = new Gui(f, g);
        obj.setVisible(true);
        obj.setLocationRelativeTo(null);

    }
}
