package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Gui.java - clasa defineste interfata grafica a programului
 * Interfata grafica este facuta in asa fel incat daca datele sunt schimbate, ea isi va schimba rapoartele in timp real - !IMPORTANT
 * @author Paul
 * @version 1.0
 * @since 2020
 */

public class Gui extends JFrame {
    private static final long serialVersionUID = 1L;

    JLabel l_bunv;

    JLabel l_pret;
    JSpinner s_pret;

    JLabel l_tip;
    JComboBox c_tip;

    JLabel l_nume;
    JTextField t_nume;

    JLabel l_consum;
    JSpinner s_consum;
    JLabel l_ore;
    JSpinner s_ore;
    JComboBox c_zi_luna;

    JLabel l_descriere;
    JTextField t_descriere;

    JButton first;
    JButton previous;
    JButton next;
    JButton last;
    JButton add;
    JButton delete;

    List<Aparat> arr;
    int pozitie;





    /**
     * Constructorul explicit al clasei Gui
     * @param f
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public Gui(File f, File g) throws IOException, ClassNotFoundException {
        super("Estimarea consumului de energie");
        arr = new ArrayList<>();
        pozitie = 0;

        /**
         * Testarea daca exista un fisier pentru stocarea listei de aparate si creerea unuia daca acesta nu este gasit
         */

        if(!f.exists())
            f.createNewFile();

        boolean empty = f.exists() && f.length() == 0;
        if(empty == false)
            deserializare(f);

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setSize(500,550);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        l_bunv = new JLabel("Cat consuma electrocasnicele tale?");

        l_pret = new JLabel("Pret: ");
        s_pret = new JSpinner(new SpinnerNumberModel(0.6,0.1,100,0.1));

        l_tip = new JLabel("Categorie: ");
        Tip[] tipuriPosibile = Tip.class.getEnumConstants();
        c_tip = new JComboBox(tipuriPosibile);
        c_tip.setSelectedIndex(0);
        c_tip.setPreferredSize(new Dimension(100,20));

        l_nume = new JLabel("Nume aparat: ");
        t_nume = new JTextField(15);

        l_consum = new JLabel("Consum: ");
        s_consum = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));

        l_ore = new JLabel("Timp estimat: ");
        s_ore = new JSpinner(new SpinnerNumberModel(1,1,999,1));
        String[] ziluna = {"zi", "luna"};
        c_zi_luna = new JComboBox(ziluna);
        c_zi_luna.setPreferredSize(new Dimension(60,20));

        l_descriere = new JLabel("Descriere: ");
        t_descriere = new JTextField(35);

        /**
         *Partea de sus a interfetei unde utilizatorul poate alege pretul KW/luna si tipul monedei folosite
         */

        JPanel top1 = new JPanel(new BorderLayout());
            top1.setPreferredSize(new Dimension(480, 60));

            top1.add(l_bunv, BorderLayout.WEST);

            JPanel pret_curetn = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pret_curetn.add(l_pret);
                pret_curetn.add(s_pret);
                TipMonede[] tipuriMonede = TipMonede.class.getEnumConstants();
                JComboBox c_monede = new JComboBox(tipuriMonede);
                c_monede.setSelectedIndex(0);
                c_monede.setPreferredSize(new Dimension(60,20));
                pret_curetn.add(c_monede);
                pret_curetn.add(new JLabel("KWh"));
            top1.add(pret_curetn,BorderLayout.EAST);

        this.add(top1);

        /**
         * A doua parte a interfetei unde utilizatorul poate alege tipul aparatului si numele acestuia
         */

        JPanel top2 = new JPanel(new BorderLayout());
            top2.setPreferredSize(new Dimension(480, 40));

            JPanel tip = new JPanel(new FlowLayout(FlowLayout.LEFT));
                tip.add(l_tip);
                tip.add(c_tip);
            top2.add(tip,BorderLayout.WEST);

            JPanel nume = new JPanel(new FlowLayout(FlowLayout.LEFT));
                nume.add(l_nume);
                nume.add(t_nume);
            top2.add(nume,BorderLayout.EAST);

        this.add(top2);

        /**
         * A treia parte a interfetei unde utilizatorul poate alege consumul in wati pe ora, timpul estimat iar daca timpul estimat este pe zi sau pe luna
         */

        JPanel top3 = new JPanel(new BorderLayout());
            top3.setPreferredSize(new Dimension(480, 70));
            
            JPanel consum = new JPanel(new FlowLayout(FlowLayout.LEFT));
                consum.add(l_consum);
                consum.add(s_consum);
                consum.add(new JLabel("W/h"));

            top3.add(consum, BorderLayout.WEST);

            JPanel timp = new JPanel(new FlowLayout(FlowLayout.LEFT));
                timp.add(l_ore);
                timp.add(s_ore);
                timp.add(new JLabel("ora/ore pe "));
                timp.add(c_zi_luna);

            top3.add(timp,BorderLayout.EAST);

            JPanel descriere = new JPanel(new FlowLayout(FlowLayout.LEFT));
                descriere.setPreferredSize(new Dimension(480, 25));
                descriere.add(l_descriere);
                descriere.add(t_descriere);

            top3.add(descriere,BorderLayout.SOUTH);

        this.add(top3);

        JPanel spatiu = new JPanel(new BorderLayout());
            spatiu.setPreferredSize(new Dimension(480,20));
        this.add(spatiu);

        /**
         * Butoanele de inainte respectiv inapoi pentru a naviga prin lista de aparate deja inserate
         */

        JPanel top4 = new JPanel(new BorderLayout());
            top4.setPreferredSize(new Dimension(475, 25));
            previous = new JButton(" Inapoi  ");
            next = new JButton("Inainte");
            top4.add(previous,BorderLayout.WEST);
            top4.add(next,BorderLayout.EAST);

        this.add(top4);

        /**
         * Butoanele de Adaugare si Stergere respectiv Primul si Ultimul pentru a naviga direct la primul sau ultimul element
         */

        JPanel top5 = new JPanel(new BorderLayout());
            top5.setPreferredSize(new Dimension(475, 31));
            add = new JButton("Adauga");
            top5.add(add, BorderLayout.WEST);

            JPanel firstlast = new JPanel(new FlowLayout());
                firstlast.setPreferredSize(new Dimension(60, 20));
                first = new JButton("Primul");
                last = new JButton("Ultimul");
                firstlast.add(first);
                firstlast.add(last);

            top5.add(firstlast, BorderLayout.CENTER);

            delete = new JButton("Sterge");
            top5.add(delete, BorderLayout.EAST);

        this.add(top5);





        JPanel spatiuu = new JPanel(new BorderLayout());
            spatiuu.setPreferredSize(new Dimension(480,20));
        this.add(spatiuu);





        JTextField wConsumatiZi = new JTextField(5);
        JTextField pretZi = new JTextField(6);

        JTextField wConsumatiLuna = new JTextField(5);
        JTextField pretLuna = new JTextField(6);

        JTextField wConsumatiAn = new JTextField(5);
        JTextField pretAn = new JTextField(6);









        JTextField monedaZ = new JTextField(4);
        String z = String.valueOf(c_monede.getSelectedItem());
        monedaZ.setText(z);
        JTextField monedaL = new JTextField(4);
        String u = String.valueOf(c_monede.getSelectedItem());
        monedaL.setText(u);
        JTextField monedaA = new JTextField(4);
        String a = String.valueOf(c_monede.getSelectedItem());
        monedaA.setText(a);

        /**
         * Afisarea consumului total pe zi, impreuna cu pretul pentru si moneda
         */

        JPanel zi = new JPanel(new FlowLayout(FlowLayout.LEFT));
            zi.setPreferredSize(new Dimension(480, 30));
            zi.add(new JLabel("Toate aparatele consuma"));
            zi.add(wConsumatiZi);
            zi.add(new JLabel("W/zi, costul fiind de"));
            zi.add(pretZi);
            zi.add(monedaZ);

        this.add(zi, BorderLayout.WEST);

        /**
         * Afisarea consumului total pe luna, impreuna cu pretul si moneda
         */

        JPanel luna = new JPanel(new FlowLayout(FlowLayout.LEFT));
            luna.setPreferredSize(new Dimension(480, 30));
            luna.add(new JLabel("Toate aparatele consuma"));
            luna.add(wConsumatiLuna);
            luna.add(new JLabel("W/luna, costul fiind de"));
            luna.add(pretLuna);
            luna.add(monedaL);
        this.add(luna, BorderLayout.WEST);

        /**
         * Afisarea consumului total pe an, impreuna cu pretul si moneda
         */

        JPanel an = new JPanel(new FlowLayout(FlowLayout.LEFT));
            an.setPreferredSize(new Dimension(480, 30));
            an.add(new JLabel("Toate aparatele consuma"));
            an.add(wConsumatiAn);
            an.add(new JLabel("W/an, costul fiind de"));
            an.add(pretAn);
            an.add(monedaA);
        this.add(an, BorderLayout.WEST);


        JComboBox cat = new JComboBox(tipuriPosibile);
        JTextField consumCat = new JTextField(7);

        consumCat.setText(String.valueOf(consumCategorie((Tip) cat.getSelectedItem())));

        /**
         * Afisarea consumului pentru fiecare categorie in parte, ea fiind aleasa de catre utilizator
         */

        JPanel categorie = new JPanel(new FlowLayout(FlowLayout.LEFT));
            categorie.add(new JLabel("Categoria "));
            categorie.add(cat);
            categorie.add(new JLabel(" are consumul de "));
            categorie.add(consumCat);
            categorie.add(new JLabel("W/h"));
        this.add(categorie);

        JTextField ceaMaiCategorie = new JTextField(10);

        /**
         * Categoria cu cel mai mare consum
         */

        JPanel ceaMaiCat = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ceaMaiCat.add(new JLabel("Categoria "));
            ceaMaiCat.add(ceaMaiCategorie);
            ceaMaiCat.add(new JLabel(" are consumul cel mai mare de curent electric"));
        this.add(ceaMaiCat);


        /**
         * ActionListener pentru schimbarea monedei din partea de sus a programului, astfel ca aceasta sa fie schimbata live si in partea de jos
         */

        c_monede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String z = String.valueOf(c_monede.getSelectedItem());
                monedaZ.setText(z);
                String u = String.valueOf(c_monede.getSelectedItem());
                monedaL.setText(u);
                String a = String.valueOf(c_monede.getSelectedItem());
                monedaA.setText(a);
            }
        });

        /**
         * Adaugarea unui nou aparat in lista
         * Refresh-uri pentru consum, pret si categorii astfel incat ele sa fie la curent cu elementul nou adaugat
         */

        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                arr.add(new Aparat((Tip) c_tip.getSelectedItem(),t_nume.getText(), (Integer) s_consum.getValue(),
                        (Integer) s_ore.getValue(), String.valueOf(c_zi_luna.getSelectedItem()), t_descriere.getText()));
                pozitie = arr.size() - 1;
                c_tip.setSelectedIndex(0);
                t_nume.setText(null);
                t_descriere.setText(null);
                s_consum.setValue(1);
                s_ore.setValue(1);

                wConsumatiZi.setText(String.valueOf(consumwZi(arr)));
                wConsumatiLuna.setText(String.valueOf(consumwLuna(arr)));
                wConsumatiAn.setText(String.valueOf(consumwAn(arr)));


                pretZi.setText(String.format("%.2f", consumwZi(arr)/1000 * (double) s_pret.getValue()));
                pretLuna.setText(String.format("%.2f", consumwLuna(arr)/1000 * (double) s_pret.getValue()));
                pretAn.setText(String.format("%.2f", consumwAn(arr)/1000 * (double) s_pret.getValue()));

                ceaMaiCategorie.setText(ceaMaiCategorie());


                consumCat.setText(String.valueOf(consumCategorie((Tip) cat.getSelectedItem())));

            }
        });


        /**
         * Stergerea unui element din lista
         * Refresh-uri pentru consum, pret si categorii astfel incat ele sa fie la curent cu lista fara elementul sters
         */

        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(arr.isEmpty())
                    t_nume.setText("Lista este goala!");
                else {
                    arr.remove(pozitie);
                    pozitie--;
                    if (pozitie >=0) {
                        c_tip.setSelectedItem(arr.get(pozitie).getTip());
                        t_nume.setText(arr.get(pozitie).getNume());
                        s_consum.setValue(arr.get(pozitie).getConsum());
                        s_ore.setValue(arr.get(pozitie).getOre());
                        c_zi_luna.setSelectedItem(arr.get(pozitie).getZi_luna());
                        t_descriere.setText(arr.get(pozitie).getDescriere());
                    }
                    else{
                        c_tip.setSelectedIndex(0);
                        t_nume.setText(null);
                        t_descriere.setText(null);
                        s_consum.setValue(1);
                        s_ore.setValue(1);
                    }

                    wConsumatiZi.setText(String.valueOf(consumwZi(arr)));
                    wConsumatiLuna.setText(String.valueOf(consumwLuna(arr)));
                    wConsumatiAn.setText(String.valueOf(consumwAn(arr)));


                    pretZi.setText(String.format("%.2f", consumwZi(arr)/1000 * (double) s_pret.getValue()));
                    pretLuna.setText(String.format("%.2f", consumwLuna(arr)/1000 * (double) s_pret.getValue()));
                    pretAn.setText(String.format("%.2f", consumwAn(arr)/1000 * (double) s_pret.getValue()));

                    ceaMaiCategorie.setText(ceaMaiCategorie());


                    consumCat.setText(String.valueOf(consumCategorie((Tip) cat.getSelectedItem())));

                }
            }
        });

        /**
         * Afisarea primului element din lista la apasarea butonului "Primul"
         */

        first.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(arr.isEmpty())
                    t_nume.setText("Lista este goala!");
                else {
                    c_tip.setSelectedItem(arr.get(0).getTip());
                    t_nume.setText(arr.get(0).getNume());
                    s_consum.setValue(arr.get(0).getConsum());
                    s_ore.setValue(arr.get(0).getOre());
                    c_zi_luna.setSelectedItem(arr.get(0).getZi_luna());
                    t_descriere.setText(arr.get(0).getDescriere());

                    pozitie = 0;
                }
            }
        });


        /**
         * Afisarea ultimului element din lista la apasarea butonului "Ultimul"
         */

        last.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(arr.isEmpty())
                    t_nume.setText("Lista este goala!");
                else {
                    pozitie = arr.size() - 1;

                    c_tip.setSelectedItem(arr.get(pozitie).getTip());
                    t_nume.setText(arr.get(pozitie).getNume());
                    s_consum.setValue(arr.get(pozitie).getConsum());
                    s_ore.setValue(arr.get(pozitie).getOre());
                    c_zi_luna.setSelectedItem(arr.get(pozitie).getZi_luna());
                    t_descriere.setText(arr.get(pozitie).getDescriere());

                }
            }
        });

        /**
         * Navigarea prin lista (inapoi)
         */

        previous.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(arr.isEmpty())
                    t_nume.setText("Lista este goala!");
                else {

                    if(pozitie == 0)
                        pozitie = arr.size();

                    pozitie -= 1;

                    c_tip.setSelectedItem(arr.get(pozitie).getTip());
                    t_nume.setText(arr.get(pozitie).getNume());
                    s_consum.setValue(arr.get(pozitie).getConsum());
                    s_ore.setValue(arr.get(pozitie).getOre());
                    c_zi_luna.setSelectedItem(arr.get(pozitie).getZi_luna());
                    t_descriere.setText(arr.get(pozitie).getDescriere());

                }
            }
        });

        /**
         * Navigarea prin lista (inainte)
         */

        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(arr.isEmpty())
                    t_nume.setText("Lista este goala!");
                else {

                    if(pozitie == arr.size() - 1)
                        pozitie = -1;

                    pozitie += 1;

                    c_tip.setSelectedItem(arr.get(pozitie).getTip());
                    t_nume.setText(arr.get(pozitie).getNume());
                    s_consum.setValue(arr.get(pozitie).getConsum());
                    s_ore.setValue(arr.get(pozitie).getOre());
                    c_zi_luna.setSelectedItem(arr.get(pozitie).getZi_luna());
                    t_descriere.setText(arr.get(pozitie).getDescriere());

                }
            }
        });

        /**
         * Refresh-ul pretului de la cele 3 rapoarte la schimbarea pretului KW/luna (cel de sus)
         */

        s_pret.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pretZi.setText(String.format("%.2f", consumwZi(arr)/1000 * (double) s_pret.getValue()));
                pretLuna.setText(String.format("%.2f", consumwLuna(arr)/1000 * (double) s_pret.getValue()));
                pretAn.setText(String.format("%.2f", consumwAn(arr)/1000 * (double) s_pret.getValue()));
            }
        });

        /**
         * Calcularea consumului atunci cand categoria este schimbata
         */

        cat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumCat.setText(String.valueOf(consumCategorie((Tip) cat.getSelectedItem())));
            }
        });


        /**
         * WindowListener atunci cand programul este pornit pentru a initializa valorile rapoartelor
         */

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                if (!arr.isEmpty()) {
                    c_tip.setSelectedItem(arr.get(0).getTip());
                    t_nume.setText(arr.get(0).getNume());
                    s_consum.setValue(arr.get(0).getConsum());
                    s_ore.setValue(arr.get(0).getOre());
                    c_zi_luna.setSelectedItem(arr.get(0).getZi_luna());
                    t_descriere.setText(arr.get(0).getDescriere());
                }


                wConsumatiZi.setText(String.valueOf(consumwZi(arr)));
                wConsumatiLuna.setText(String.valueOf(consumwLuna(arr)));
                wConsumatiAn.setText(String.valueOf(consumwAn(arr)));

                ceaMaiCategorie.setText(ceaMaiCategorie());



                pretZi.setText(String.format("%.2f", consumwZi(arr)/1000 * (double) s_pret.getValue()));
                pretLuna.setText(String.format("%.2f", consumwLuna(arr)/1000 * (double) s_pret.getValue()));
                pretAn.setText(String.format("%.2f", consumwAn(arr)/1000 * (double) s_pret.getValue()));
            }
        });

        /**
         * WindowsListener atunci cand programul este inchis pentru a salva modificarile in fisier
         */

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                try {
                    serializare(f);
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }
        });


    }

    /**
     * Functia care calculeaza consumul pe zi a tuturor aparatelor din lista
     * @return int
     */

    int consumwZi(List<Aparat> t){
        int c = 0;

        for (Aparat i: t){
            if (i.getZi_luna().equals("zi")){
                c = c + i.getConsum() * i.getOre();
            }
            if (i.getZi_luna().equals("luna")){
                c = (int) (c + Math.ceil(i.getConsum()*i.getOre()/31));
            }
        }
//        System.out.println(c);
        return c;
    }

    /**
     * Functia care calculeaza consumul pe luna a tuturor aparatelor din lista
     * @return int
     */

    int consumwLuna(List<Aparat> t){
        int c = 0;
        for (Aparat i: t){
            if (i.getZi_luna().equals("luna")){
                c = c + i.getConsum()*i.getOre();
            }
            if (i.getZi_luna().equals("zi")){
                c = c + i.getConsum()*i.getOre()*31;
            }
        }
//        System.out.println(c);
        return c;
    }

    /**
     * Functia care calculeaza consumul pe an a tuturor aparatelor din lista
     * @return int
     */

    int consumwAn(List<Aparat> t){
        int c = 0;
        for (Aparat i: t){
            if (i.getZi_luna().equals("luna")){
                c = c + i.getConsum()*i.getOre()*12;
            }
            if (i.getZi_luna().equals("zi")){
                c = c + i.getConsum()*i.getOre()*365;
            }
        }
//        System.out.println(c);
        return c;
    }

    /**
     * Functia care calculeaza consumul pentru categoria data ca parametru
     * @return int
     */

    int consumCategorie(Tip a){
        int c = 0;
        for (Aparat i:arr){
            if (i.getTip() == a){
                c = c + i.getConsum();
            }
        }
        return c;
    }

    /**
     * Functia care calculeaza care categorie are consumul cel mai mare
     * @return String
     */

    String ceaMaiCategorie(){
        int x = -1;
        for( Tip i: Tip.values()){
            if (x < consumCategorie(i)){
                x = consumCategorie(i);
            }
        }
        for( Tip i: Tip.values()){
            if (x == consumCategorie(i)){
                return String.valueOf(i);
            }
        }
        return "N/A";
    }

    /**
     * Serializare
     * @param f
     * @throws IOException
     */

    void serializare(File f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(arr);
        oos.close();
        fos.close();
    }

    /**
     * Deserializare
     * @param f
     * @throws IOException
     * @throws ClassNotFoundException
     */

    void deserializare(File f) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);

        List<?> l = (List<?>) ois.readObject();

        for(Object o: l)
            arr.add((Aparat) o);

        ois.close();
        fis.close();
    }
}
