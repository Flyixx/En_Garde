package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurSauvegarde implements ActionListener {
    InterfaceGraphique inter;

    AdaptateurSauvegarde(InterfaceGraphique f){
        inter = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inter.sauve();
    }
}
