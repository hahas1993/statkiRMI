import javax.swing.JFrame;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class GUI extends JPanel{

    private static StartWindow s;
    private static JButton[][] PlayerGrid;
    private static JButton[][] ComGrid;
    private static JTextArea textArea;
    private static JButton zatwierdz;
    private static Checkbox playerTurn;
    private static Checkbox comTurn;
    private static JScrollPane scrollPane;
    private static ActionListener al;
    private static boolean shipsSet=false;
    private static boolean saveClicked = false;
    
    public GUI() throws InterruptedException {
                s = new StartWindow();
                while(s.isSaveClick()==false){Thread.sleep(100);}
                JFrame f=s.getStartWindow();
                createFrame(f);     
    }

    
    private static void createFrame(JFrame f) {  
        f.setContentPane(new GUI(10, 10));
        f.pack();
        f.setVisible(true);
    }
       
   private GUI(int width, int length) {
        Border playerBorder = BorderFactory.createTitledBorder("Twoje statki");
        Border comBorder = BorderFactory.createTitledBorder("Statki przeciwnika");
        JPanel player = new JPanel();
        JPanel com = new JPanel();
        CheckboxGroup cbg = new CheckboxGroup();
        zatwierdz = new JButton("Zatwierdź");
        playerTurn = new Checkbox("Gracz",cbg,false);
        comTurn = new Checkbox("Przeciwnik",cbg,false);
        playerTurn.setEnabled(false);
        comTurn.setEnabled(false);
        zatwierdz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                msgAreaSetText("Zatwierdz wcisniety.\n");
                saveClicked = true;      
                zatwierdz.setEnabled(false);
            }
            });

        player.setBorder(playerBorder);// set border round player PlayerGrid 
        player.setLayout(new GridLayout(10, 10));
        com.setBorder(comBorder);// set border round com PlayerGrid        
        com.setLayout(new GridLayout(10, 10));
        PlayerGrid = new JButton[width][length]; //allocate the size of PlayerGrid
        ComGrid = new JButton[width][length]; //allocate the size of ComGrid
        createActionListener();
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                PlayerGrid[x][y] = new JButton(); //creates new button    
                player.add(PlayerGrid[x][y]); //adds button to PlayerGrid
                PlayerGrid[x][y].setBackground(Color.WHITE);//sets PlayerGrid background colour
                PlayerGrid[x][y].setPreferredSize(new Dimension(35, 35));//sets each PlayerGrid buttons dimensions    
                PlayerGrid[x][y].addActionListener(getActionListener());
                PlayerGrid[x][y].setName(x+""+y);
                ComGrid[x][y] = new JButton(); //creates new button
                com.add(ComGrid[x][y]); //adds button to PlayerGrid
                ComGrid[x][y].setBackground(Color.LIGHT_GRAY);//sets PlayerGrid background colour
                ComGrid[x][y].setPreferredSize(new Dimension(35, 35));//sets each PlayerGrid buttons di
                ComGrid[x][y].setName(x+""+y);
                ComGrid[x][y].addActionListener(
                        getActionListener());
            }
        }
        JPanel container = new JPanel();
        JPanel third = new JPanel();
        JPanel bottomRight = new JPanel();
        JPanel bottomLeft = new JPanel();
        bottomRight.setLayout(new BoxLayout(bottomRight, BoxLayout.Y_AXIS));
        third.setBorder(BorderFactory.createTitledBorder("Statki"));// set border round com PlayerGrid  
        textArea = new JTextArea(10, 68);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        bottomLeft.add(scrollPane);
        bottomRight.add(zatwierdz);
        bottomRight.add(playerTurn);
        bottomRight.add(comTurn);
        third.add(bottomLeft);
        third.add(bottomRight);
        container.add(player);
        container.add(com);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(container);
        this.add(third);
    }
   
   private void createActionListener(){
       al= new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b =(JButton)e.getSource();
                buttonClicked(b);
            }
        };
 }
   // Ustawiaie statków, oraz strzały w plansze przeciwnika
    private void buttonClicked(JButton b){
        int x=b.getName().charAt(0);
        int y=b.getName().charAt(1);
        if(shipsSet==true){
            try {
                //Gra - plansza przeciwnika
                if(b.getBackground()==Color.LIGHT_GRAY){
                     Client.hitField(x,y);
                }
         
            } catch (RemoteException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            //Rozstawianie statkow - twoja plansza
            if(b.getBackground()==Color.WHITE){
                  b.setBackground(Color.GREEN);          
            }
        }      
    }
    
    
    private ActionListener getActionListener(){
            return al;
    }

    public void msgAreaSetText(String text) {
        textArea.setText(textArea.getText()+text);
    }
    /**
     * @return the shipsSet
     */
    public boolean isShipsSet() {
        return shipsSet;
    }
    
    public boolean isFlag(){
        return s.isSaveClick();
    }
    
    public String getPlayerName(){
        return s.getPlayerName();
    }
    
    //Zaznaczanie, ze spudlowales
    public void setHitFieldCom(int x, int y){
        getComGrid()[x][y].setBackground(Color.BLUE);
    }
    
   //Zaznaczenie ze trafiles
    public void setMisHitFieldCom(int x, int y){
        getComGrid()[x][y].setBackground(Color.GRAY);
    }
    
     //Zaznaczanie na Twoim polu, że przeciwnik spudłował
    public void setMisHitFieldPlayer(int x, int y){
         getComGrid()[x][y].setBackground(Color.GRAY);
    }
    
     //Zaznaczanie na Twoim polu, że przeciwnik trafił w statek
    public void setHitFieldPlayer(int x, int y){
        getComGrid()[x][y].setBackground(Color.BLUE);
    }
    /**
     * @return the saveClicked
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    /**
     * @return the PlayerGrid
     */
    public JButton[][] getPlayerGrid() {
        return PlayerGrid;
    }
    public JButton[][] getComGrid() {
        return ComGrid;
    }
    /**
     * @param shipsSet the shipsSet to set
     */
    public void setShipsSet(boolean shipsSet) {
        this.shipsSet = shipsSet;
    }
    
    public void setPlayerTurn(){
        playerTurn.setState(true);
    }
    
    public void setComTurn(){
        comTurn.setState(true);
    }
}
