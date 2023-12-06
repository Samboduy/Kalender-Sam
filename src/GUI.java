//alla paket som behövs
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.*;

import static javax.swing.UIManager.get;

public class GUI extends JFrame implements ActionListener {
    //AJ variabler sorterade efter java regler
    ArrayList<LocalDate> sevenDates = new ArrayList<>();
    HashMap<LocalDate,String> memory = new HashMap<>();
        //AJ tydligare namn på variabel om vad som sparas, fältinfo verkar sparas här mot rätt dag
    ArrayList<JPanel> headerList = new ArrayList<>();
    ArrayList<JTextArea> textContent = new ArrayList<>();
    ArrayList<JPanel> panelList = new ArrayList<>();
    ArrayList<JButton> buttonWeekList = new ArrayList<>();
    ArrayList<JButton> buttonList = new ArrayList<>();
    JPanel footer;//tillagt AJ
    Font fontDayMonth = (new Font("Serif", Font.PLAIN, 11));//tillagt AJ
    GridLayout gridLayout = new GridLayout(2, 1);//tillagt AJ
    Border panelBorder = BorderFactory.createLineBorder(Color.GRAY);//tillagt AJ
    Color white = (Color.white); //tillagt AJ
    JTextField [] fieldList = {new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField("")};
    JFrame calender = new JFrame();
    /*Privata egenskaper samt datum, listor med paneler, labels, knappar, text-fields, textareas*/
    private LocalDate useDate;
    private LocalDate today = LocalDate.now();
    private boolean on=false;
    private LocalDate fakeMonday =
            LocalDate.of(2023,11,6);

    public LocalDate getUseDate() {

        return this.useDate;
    }

    public void setUseDate(LocalDate newUseDate){

        this.useDate = newUseDate;
    }

    public boolean getOn(){

        return this.on;
    }

    public void setOn(boolean newOn){

        this.on=newOn;
    }

    public LocalDate getFakeMonday(){

        return this.fakeMonday;
    }

    public LocalDate getToday() {
        return today;
    }

    // AJ Används inte, kan plockas bort
    /*public void setFakeMonday(LocalDate newFakeMonday){

        this.fakeMonday=newFakeMonday;
    }*/
    public void setToday(LocalDate newToday) {
        this.today = newToday;
    }

    /*Start metoden ger jFrame calendar en titel, vilken storlek, att den ska stänga sig, ställer in layouten
    och kallar på generator constructor*/
        //AJ istället för att skriva det så här i en klumptext hade jag satt kortare kommentarer inne i koden
    public void start() {

        calender.setTitle("Calender"); //titel på kalender
        calender.setSize(800, 600); //sätter storlek
        calender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calender.setLayout(new GridLayout(1, 9, 10, 0));

        generator(LocalDate.now()); //hämtar in dagens datum
        calender.setVisible(true);
    }

    public void generator(LocalDate useDate){
        /*
        Tar in ett datum och sparar det, kollar om
        */
        setUseDate(useDate);

        clearLists();

        create();

        findMonday();
        /*lägger till en knapp för att ändra till föra veckan*/
        calender.add(buttonWeekList.get(0));


        /*kör så länge useDate är lika med måndag och i inte är lika med 7*/
        for (int i=0; getUseDate().getDayOfWeek()
                .equals(getFakeMonday().getDayOfWeek()) && i!=7; i++){
            /*skapar ny tom JLabels*/
            JLabel weekName = new JLabel();
            JLabel dayMonth = new JLabel();

            /*sparar vilken veckodag det är i label weekName genom seven-dates listan
            * samma sak med dayMonth förutom att den sparar dag och månad*/
            weekName.setText(String.valueOf(sevenDates.get(i).getDayOfWeek()));
            dayMonth.setText((sevenDates.get(i).getDayOfMonth()) + " "
                    + (sevenDates.get(i).getMonth()) );
            /*ändrar font på dayMonth label*/
            dayMonth.setFont(fontDayMonth);

            /*tar fram en panel och ställer in layouten, ger panelen en grå border samt ändrar bakgrundsfärg */
            panelList.get(i).setLayout(new BorderLayout());
            panelList.get(i).setBorder(panelBorder);
            panelList.get(i).setBackground(white);

            /*tar fram en panel från headerList och ändrar des layout,
            bakgrundsfärg och läger till weekName label och dayMonth label*/
            headerList.get(i).setLayout(gridLayout);
            headerList.get(i).setBackground(white);
            headerList.get(i).add(weekName);
            headerList.get(i).add(dayMonth);

            /*Skapar en ny JPanel och ändrar des layout,
            backgrundsfärg och lägger till en textField och en knapp deras listor*/
            footer = new JPanel();
            footerDesign();
            footer.add(fieldList[i]);
            footer.add(buttonList.get(i));

            /*marker ändrar backgrundsfärg*/
            marker(i);

            /*Panelen lägger till en textArea från dess lista,
             lägger till en header och en footer från deras listor samt bestämmer layouten.
              PanelListan läggs sen till i calendar, calender uppdaterar sig genom setVisible true */
            panelList.get(i).add(textContent.get(i));
            writeMemory(sevenDates.get(i),i);
            panelList.get(i).add(headerList.get(i), BorderLayout.NORTH);
            panelList.get(i).add(footer,BorderLayout.SOUTH);


            calender.add(panelList.get(i));
            calender.setVisible(true);
        }

        /*Lägger till en knapp till calendar som ändrar till nästa vecka, on blir true och ändras aldrig till false igen.
        * On används för att kolla om programmet redan har körts en gång*/
        calender.add(buttonWeekList.get(1));
        setOn(true);
        calender.setVisible(true);
        update();

    }
    /*findMonday metoden använder sig av en while loop som sätts igång så länge useDate inte är måndag,
    * ändrar useDate till useDate minus en dag*/
    public void findMonday(){
        while (!getUseDate().getDayOfWeek()
                .equals(getFakeMonday().getDayOfWeek())){

            setUseDate(getUseDate().minusDays(1));
        }
        //for loopen skriver in sju datum i sevenDates listan genom att använda useDate så länge j inte är lika med 7
        for (int j = 0; j!=7; j++){

            sevenDates.add(getUseDate());
            setUseDate(getUseDate().plusDays(1));
        }
    }
    /*rensar listor så att de kan användas igen och tar bort varje panel från calendar*/
    public void clearLists(){
        if (getOn()){
            for (int i = 0; i!=7; i++){
                calender.remove(panelList.get(i));
                /*calender.remove(headerList.get(i));*/
                headerList.clear();
                sevenDates.clear();

            }
        }
        panelList.clear();
    }

    /*Metod som skapar nya tomma textAreas, paneler samt knappar med namn.
     ger knappar actionListeners om boolean on inte är true*/

    public void create(){

        for (int i = 0; i != 7; i++) {
            textContent.add(new JTextArea());
            textContent.get(i).setEditable(false);
            textContent.get(i).setLineWrap(true);

            headerList.add(new JPanel());
            panelList.add(new JPanel());
            buttonList.add(new JButton("create"));
        }
        buttonWeekList.add(new JButton("<"));
        buttonWeekList.add(new JButton(">"));
        if (!getOn()) {
            addListeners();
        }
    }

    /*marker metoden markerar textArean och header label med rosa färg om det är dagens datum, resten får vit färg*/
    public void marker(int i){
        if(getToday().equals(sevenDates.get(i))){
            textContent.get(i).setBackground(Color.pink);
            headerList.get(i).setBackground(Color.pink);
        }
        /*else {
            textContent.get(i).setBackground(Color.white);

            headerList.get(i).setBackground(Color.white);
                           AJ Onödigt att sätta detta vitt eftersom det redan är det sen tidigare i koden
        }*/
    }

    /*Update metoden kollar om datumet som sparat i today är dagens datum,
    är det inte det så ändras today till dagens datum och sen kallar den på generator metoden*/
    public void update(){
        if (!getToday().equals(LocalDate.now())){
            setToday(LocalDate.now());
            generator(getToday());
        }
    }

    /*addMemory constructor tar emot ett datum och en String. Datumet och String kommer från knappen man har tryckt på.
    * Memory är hashmap som gör datumet till en nyckel och sparar Stringen*/

    //addMemory används inte, kan plockas bort
    /*public void addMemory(LocalDate key, String text) {
        memory.put(key,text);
    }*/

    /*writeMemory constructor tar emot ett datum från generator och en Integer från constructor och
    kollar om memory hashmap har en nyckel som stämmer överens.
    Om det stämmer så tar textContent fram rätt textArea med hjälp av Integer
    och sen skrivs allting som fanns i den nyckeln in i rätt textArea*/
    public void writeMemory(LocalDate key, int textArea){

        if (getOn()&& memory.containsKey(key)){
            textContent.get(textArea).append(memory.get(key));
        }
    }

    /*addListeners lägger till ActionListeners till alla knappar*/
    public void addListeners() {
        for (int h = 0; h!=7; h++){
            buttonList.get(h).addActionListener(this);
        }
           buttonWeekList.get(0).addActionListener(this);
           buttonWeekList.get(1).addActionListener(this);
    }

    /*Om man trycker på en create knapp så sparas texten man skrev in i en textField i en String,
     sen tar den fram rätt textArea och skriver ut texten i en ny rad.
     Det man skrev skickas till addMemory metoden samt vilket datum. Calendar uppdateras med setVisible.
     Update metoden ser till så att det blir rätt dag*/
    @Override
    public void actionPerformed(ActionEvent e) { //sätta switch case eller for loop här istället för if sats

        for (int i= 0; i<buttonList.size(); i++){
            if (e.getSource() == buttonList.get(i)){
                update();
                String content = " " + fieldList[i].getText();
                textContent.get(i).append(content +"\n");
                calender.setVisible(true);
            }
        }
        /*if (e.getSource() == buttonList.get(0)) {
            update();
            String content = " " + fieldList[0].getText();
            addMemory(sevenDates.get(0),textContent.get(0).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(1)) {
            update();
            String content = " " + fieldList[1].getText();
            textContent.get(1).append(content +"\n");

            addMemory(sevenDates.get(1),textContent.get(1).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(2)) {
            update();
            String content = " " + fieldList[2].getText();
            textContent.get(2).append(content +"\n");

            addMemory(sevenDates.get(2),textContent.get(2).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(3)) {
            update();
            String content = " " + fieldList[3].getText();
            textContent.get(3).append(content +"\n");

            addMemory(sevenDates.get(3),textContent.get(3).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(4)) {
            update();
            String content = " " + fieldList[4].getText();
            textContent.get(4).append(content +"\n");

            addMemory(sevenDates.get(4),textContent.get(4).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(5)) {
            update();
            String content = " " + fieldList[5].getText();
            textContent.get(5).append(content +"\n");

            addMemory(sevenDates.get(5),textContent.get(5).getText());
            calender.setVisible(true);
        }
        else if (e.getSource() == buttonList.get(6)) {
            update();
            String content = " " + fieldList[6].getText();
            textContent.get(6).append(content +"\n");

            addMemory(sevenDates.get(6),textContent.get(6).getText());

            calender.setVisible(true);
        }*/

        /*buttonWeekList ändrar på useDate till måndags datum minus en vecka,
         rensar textContent listan vilken innehåller textArean och kallar på generator constructor */
        if (e.getSource() == buttonWeekList.get(0)) {
            setUseDate(sevenDates.get(0));
            setUseDate(getUseDate().minusWeeks(1));
            textContent.clear();
            generator(getUseDate());
        }
        else if (e.getSource() == buttonWeekList.get(1)) {
            setUseDate(sevenDates.get(0));
            setUseDate(getUseDate().plusWeeks(1));
            textContent.clear();
            generator(getUseDate());
        }

    }
    public void footerDesign(){
        footer.setLayout(gridLayout);
        footer.setBackground(Color.white);
    }
}
