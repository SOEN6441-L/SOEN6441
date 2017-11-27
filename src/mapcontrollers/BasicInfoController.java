package mapcontrollers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import mapmodels.RiskMapModel;
import mapviews.BasicInfoView;

/**
 * Class acting as the BasicInfoView's controller, 
 * to define action performed according to different users' action.
 */
public class BasicInfoController implements ActionListener {
	private RiskMapModel curMap;
	private BasicInfoView curView;
	
	/**
	 * Method to add a model to this controller.
	 * @param m model
	 */
	public void addModel(RiskMapModel m){
		this.curMap = m;
	}

	/**
	 * Method to add a view to this controller.
	 * @param v view
	 */
	public void addView(BasicInfoView v){
		this.curView = v;
	}	
	
	/**
	 * Method to define action performed according to different users' action.
	 * @param e the action event of user.
	 */	
	public void actionPerformed(ActionEvent e) {
		String buttonName = e.getActionCommand();
		switch (buttonName){
		case "Proceed":
			proceedHandler();
			break;				
		case "Cancel":
			cancelHandler();
			break;			
		}
	}	
	
	/**
	 * Method to respond user choosing proceed button, which will continues to next step.
	 */
	private void proceedHandler() { 
		if (curView.getAuthor()==null||curView.getAuthor().trim().isEmpty()){
			JOptionPane.showMessageDialog(null,"Parameter author can't be empty.");
			curView.setAuthor("anonymous");
			return;
		}
		if (curView.getImage()==null||curView.getImage().trim().isEmpty()){
			JOptionPane.showMessageDialog(null,"Parameter image can't be empty.");
			curView.setImage("none");
			return;
		}
		curMap.setAuthor(curView.getAuthor().trim());
		curMap.setWarn((curView.getWarnYes())?"yes":"no");
		curMap.setWrap((curView.getWrapYes())?"yes":"no");
		curMap.setImage(curView.getImage().trim());
		curMap.setScroll((curView.getScrollHor())?"horizontal":(curView.getScrollVer())?"vertical":"none");
		curView.setState(1);
		curView.setVisible(false);
	}
	
	/**
	 * Method to respond user cancel the process
	 */
	private void cancelHandler() { 
		curView.setState(0);
		curView.setVisible(false);
	}	
	
}
