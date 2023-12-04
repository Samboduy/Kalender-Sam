//alla paket som behövs
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.*;

public class GUI extends JFrame implements ActionListener {

    /*Privata egenskaper samt datum,listor med paneler, labels, knappar, textfields, textareas*/
    private LocalDate useDate;
    private LocalDate today = LocalDate.now();
    ArrayList<LocalDate> sevenDates = new ArrayList<>();
     private boolean on=false;
    private LocalDate fakeMonday =
            LocalDate.of(2023,11,6);
    public LocalDate getUseDate() {

        return this.useDate;
    }
    public boolean getOn(){

        return this.on;
    }
    public void setOn(boolean newOn){

        this.on=newOn;
    }
    public void setUseDate(LocalDate newUseDate){

        this.useDate = newUseDate;
    }
    public LocalDate getFakeMonday(){

        return this.fakeMonday;
    }
    public void setFakeMonday(LocalDate newFakeMonday){

        this.fakeMonday=newFakeMonday;
    }
    public void setToday(LocalDate newToday) {
        this.today = newToday;
    }
    public LocalDate getToday() {
        return today;
    }

    HashMap<LocalDate,String> memory = new HashMap<>();

    ArrayList<JPanel> headerList = new ArrayList<>();

    ArrayList<JTextArea> textContent = new ArrayList<>();
    ArrayList<JPanel> panelList = new ArrayList<>();




    ArrayList<JButton> buttonWeekList = new ArrayList<>();
    ArrayList<JButton> buttonList = new ArrayList<>();

    JTextField [] fieldList = {new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField(""),
            new JTextField("")};


    JFrame calender = new JFrame();

    /*Start metoden ger jFrame calendern en titel, vilken storlek, att den ska stänga sig, ställer in layouten
    och kallar på generator konstruktorn*/
    public void start() {

        calender.setTitle("Calender");
        calender.setSize(800, 600);
        calender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calender.setLayout(new GridLayout(1, 9, 10, 0));

        generator(LocalDate.now());
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
            /*skapar ny tom Jlabels*/
            JLabel weekName = new JLabel();
            JLabel dayMonth = new JLabel();

            /*sparar vilken veckodag det är i label weekName genom sevendates listan
            * sammma sak med dayMonth förutom att den sparar dag och månad*/
            weekName.setText(String.valueOf(sevenDates.get(i).getDayOfWeek()));
            dayMonth.setText((sevenDates.get(i).getDayOfMonth()) + " "
                    + (sevenDates.get(i).getMonth()) );
            /*ändrar fontet på dayMonth labeln*/
            dayMonth.setFont(new Font("Serif", Font.PLAIN, 11));

            /*tar fram en panel och ställer in layouten,ger panelen en grå border samt ändrar bakgrundsfärg */
            panelList.get(i).setLayout(new BorderLayout());
            panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelList.get(i).setBackground(Color.white);

            /*tar fram en panel från headerList och ändrar des layout,
            bakgrundsfärg och läger till weekName label och daymonth labeln*/
            headerList.get(i).setLayout(new GridLayout(2, 1));
            headerList.get(i).setBackground(Color.white);
            headerList.get(i).add(weekName);
            headerList.get(i).add(dayMonth);

            /*skappar en ny Jpanel och ändrar des layout,
            backgrundsfärg och lägger till en textField och en knapp deras listor*/
            JPanel footer = new JPanel();
            footer.setLayout(new GridLayout(2,1));
            footer.setBackground(Color.white);
            footer.add(fieldList[i]);
            footer.add(buttonList.get(i));

            /*marker ändrar backgrundsfärg*/
            marker(i);

            /*Panelen lägger till en textArea från dess lista,
             lägger till en header och en footer från deras listor samt bestämmer layouten.
              PanelListan läggs sen till i calendern, calender uppdaterar sig genom setVisivle true */
            panelList.get(i).add(textContent.get(i));
            writeMemory(sevenDates.get(i),i);
            panelList.get(i).add(headerList.get(i), BorderLayout.NORTH);
            panelList.get(i).add(footer,BorderLayout.SOUTH);


            calender.add(panelList.get(i));
            calender.setVisible(true);
        }

        /*lägger till en knapp till calendern som ändrar till nästa vecka, on blir true och ändras aldrig till false igen.
        * on används för att kolla om programet redan har körts en gång*/
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
        //for loopen skriver in sju datum i sevenDates listan genom att använda usedate så länge j inte är lika med 7
        for (int j = 0; j!=7; j++){

            sevenDates.add(getUseDate());
            setUseDate(getUseDate().plusDays(1));
        }
    }
    /*rensar listor så att de kan användas igen och tar bort varje panel från calendern*/
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
     ger knappar actionlisteners om boolean on inte är true*/
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
    /*marker metoden markerar textArean och headerLabeln med rosa färg om det är dagens datum, resten får vit färg*/
    public void marker(int i){
        if(getToday().equals(sevenDates.get(i))){
            textContent.get(i).setBackground(Color.pink);
            headerList.get(i).setBackground(Color.pink);
        }
        else{
            textContent.get(i).setBackground(Color.white);

            headerList.get(i).setBackground(Color.white);

        }
    }
    /*update metoden kollar om datumet som sparat i today är dagens datum,
    är det inte det så ändras today till dagens datum och sen kallar den på generator metoden*/
    public void update(){
        if (!getToday().equals(LocalDate.now())){
            setToday(LocalDate.now());
            generator(getToday());
        }
    }
    /*addMemory konstruktorn tar emot ett datum och en String. Datumet och String kommer från knappen man har tryckt på.
    * memory är hashmap som gör datumet till en nyckel och sparar Stringen*/
    public void addMemory(LocalDate key, String text) {
        memory.put(key,text);
    }
    /*writeMemory konstruktorn tar emot ett datum från genrator och en Integer från konstruktorn och
    kollar om memory hashmapen har en nyckel som stämmer överäns.
    Om det stämmer så tar textcontent fram rätt textArea med hjälp av Integern
    och sen srkivs allting som fanns i den nyckeln in i rätt textArea*/
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
     Det man skrev skickas till addMemor metoden samt vilket datum. calendern uppdateras med setVisible.
     Update metoden ser till så att det blir rätt dag*/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonList.get(0)) {
            update();
            String content = " " + fieldList[0].getText();
            textContent.get(0).append(content +"\n");

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
        }
        /*buttonWeekList ändrar på useDate till måndags datum minus en vecka,
         rensar textContent listan vilken innehåller textArean och kallar på generator konstruktorn */
        else if (e.getSource() == buttonWeekList.get(0)) {

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
}
