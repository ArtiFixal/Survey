package survey.GUI;

import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Dialog indicating completion status of a task.
 * 
 * @author ArtiFixal
 */
public class CompleteStatusDialog extends JFrame {
	/**
	 * Number of tasks.
	 */
    public int maxTasks;
	
	/**
	 * Task which is actually running.
	 */
    private int currentTask;

    public CompleteStatusDialog(String title,int maxTasks){
        super(title);
        setLocationRelativeTo(null);
        this.maxTasks = maxTasks;
        currentTask = 0;
        initDialog();
    }

    public CompleteStatusDialog(String title,int maxTasks, Component parrent){
        super(title);
        setLocationRelativeTo(parrent);
        this.maxTasks = maxTasks;
        currentTask = 0;
        initDialog();
    }
	
    public void increaseCompletness()
    {
        currentTask++;
        updateTaskLabelText();
        progress.setValue(currentTask);
        if(currentTask!=maxTasks)
			repaint();   
        else
            dispose();
    }
    
    private void initDialog()
    {
        setAlwaysOnTop(true);
        setSize(320, 120);
        setResizable(false);
        currentTaskText=new JLabel("Completed (0/"+maxTasks+")");
        progress=new JProgressBar(currentTask, maxTasks);
        progress.setStringPainted(true);
        GroupLayout layout=new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(8)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(currentTaskText)
                        .addComponent(progress)
                )
                .addGap(8)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(12)
                .addComponent(currentTaskText)
                .addGap(8)
                .addComponent(progress)
                .addGap(12)
        );
		setVisible(true);
    }
    
    private void updateTaskLabelText()
    {
        currentTaskText.setText("Completed ("+currentTask+"/"+maxTasks+")");
    }
    
    private JProgressBar progress;
    private JLabel currentTaskText;
}
