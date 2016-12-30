package game24;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author 
 * zerochen NameDialog is a class to get the input from users By using
 * this class, the program can get the names of players, the puzzle and
 * the guess answer
 *
 */

public class InputDialog extends JDialog {
    JTextField nameField;
    JButton okButton;

    /*
     * construct NameDialog to create a dialog for user to enter a name when
     * clicking the button "OK", go to the next step title is the title of the
     * dialog guidance is the hint for user's input
     */
    public InputDialog(JFrame frame, String title, String guidance) {
        super(frame, title, true);

        setLayout(new BorderLayout());

        // use the box container for "PAGE_Axis"
        Box centerBox = new Box(BoxLayout.PAGE_AXIS);

        // create a label on the top of the dialog
        JLabel label = new JLabel(guidance);
        centerBox.add(label);

        // create a text field
        nameField = new JTextField(50);
        centerBox.add(nameField);

        add(centerBox, BorderLayout.CENTER);

        // create a button
        // click to continue
        JPanel bottomPan = new JPanel(new FlowLayout());
        okButton = new JButton("  OK  ");

        // Anonymous Inner Class of ActionListener
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        bottomPan.add(okButton);
        add(bottomPan, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
}
