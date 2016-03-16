import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Klis
 */
public class StartWindow  extends JPanel{
    
    private JFrame startWindow;
    private static JTextField tf;
    private String playerName;
    private static boolean saveClick=false; 
    
        public StartWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createFrame();
            }
        });
    }
    private void createFrame() {
        startWindow = new JFrame("Statki");
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setLocationRelativeTo(null);
        startWindow.getContentPane().add(new StartWindow(200,50));
        startWindow.setSize(100, 100);
        startWindow.pack();
        startWindow.setVisible(true);
    }
    
    public StartWindow(int width, int lenght){
        JButton save = new JButton();
        JPanel pan = new JPanel();
        pan.setPreferredSize(new Dimension(200,50));
        tf=new JTextField();
        tf.setText("nick");
        tf.setPreferredSize(new Dimension(150, 20));
        pan.setBorder(BorderFactory.createTitledBorder("Nazwa użytkownika"));   
        pan.add(tf);
        save.setText("OK");
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new BorderLayout());
        save.setPreferredSize(new Dimension(50, 30));
        this.add(pan,BorderLayout.PAGE_START);
        this.add(save,BorderLayout.CENTER);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                 if(!tf.getText().equals("nick") && !tf.getText().isEmpty()){
                   System.out.println(tf.getText());
                    playerName=tf.getText();
                    setSaveClicked(true);
                }
                else{
                    System.out.println("Wprowadz nazwe uzytkownika");
                };
            }
        }); 
   }
    
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return the saveClicked
     */
    public boolean isSaveClick() {
        return saveClick;
    }

    /**
     * @param saveClicked the saveClicked to set
     */
    public void setSaveClicked(boolean saveClick) {
        this.saveClick = saveClick;
    }

    /**
     * @return the startWindow
     */
    public JFrame getStartWindow() {
        return startWindow;
    }
}