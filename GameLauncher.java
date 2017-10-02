package team.groupfour.risk.gamelauncher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import team.groupfour.risk.gameConfiguration.ConfigureGame;

/**
 * This class is used to create a window for game launcher
 * 
 * @author Jingyu Lu
 *
 */
public class GameLauncher {

	public static void main(String[] args) {
		// create a window
		JFrame myFrame=new JFrame();
		//create button
		JButton mapEditorBtn=new JButton("Map Editor");
		JButton gameConfigurationBtn= new JButton("Start Game");
		
		//layout
		myFrame.setLayout(null);
		//position and size
		myFrame.setBounds(100, 100, 560, 280);
		//title
		myFrame.setTitle("Risk Launcher");
		//cannot adjust window
		myFrame.setResizable(false);
		//visible
		myFrame.setVisible(true);
		//close window
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//button configuration
		mapEditorBtn.setBounds(100, 110, 100, 50);
		mapEditorBtn.setVisible(true);
		gameConfigurationBtn.setBounds(330, 110, 100, 50);
		gameConfigurationBtn.setVisible(true);
		//add button on frame
		myFrame.add(mapEditorBtn);
		myFrame.add(gameConfigurationBtn);
		
		//button event
		mapEditorBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("地图编辑器");
			}
		});
		
		gameConfigurationBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("配置游戏并开始");
				//create an object for game configuration file
				ConfigureGame cfgRisk=new ConfigureGame();
				cfgRisk.gameconfiguration();
				cfgRisk.startGame();
			}
		});
		
	}
}
