package info.daviot.gui.component;

import info.daviot.util.validation.FailedValidationException;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTextArea;


public class ErrorMessageDialog extends InformationDialog
{
	private JButton detailsButton;
	private	JScrollPane detailsScrollPane;
	
    public ErrorMessageDialog(Frame frame, String title, String message, Throwable exception)
    {
        super(frame, title);
        init(message, exception);
    }

    public ErrorMessageDialog(Dialog dialog, String title, String message, Throwable exception)
    {
        super(dialog, title);
        init(message, exception);
    }

	public ErrorMessageDialog(Frame frame, String title, String message)
	{
		this(frame, title, message, null);
	}

	public ErrorMessageDialog(Dialog dialog, String title, String message)
	{
		this(dialog, title, message, null);
	}
	
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if (visible)
			detailsButton.requestFocus();
	}
	
	public void init(String message, Throwable exception)
	{
		Box topBox=Box.createVerticalBox();
		JTextArea messageTextArea=new JTextArea(80,5);
		messageTextArea.setLineWrap(true);
		messageTextArea.setText(message);
		messageTextArea.setEditable(false);
		//+System.getProperty("line.separator")+exception);
		JScrollPane messageScrollPane=new JScrollPane(messageTextArea);
		messageScrollPane.setPreferredSize(new Dimension(800,100));
		JTextArea detailsTextArea=new JTextArea(80,20);
		detailsTextArea.setLineWrap(true);
		detailsTextArea.setEditable(false);		
		detailsTextArea.setText(throwableToString(exception));
		detailsScrollPane=new JScrollPane(detailsTextArea);	
		detailsScrollPane.setPreferredSize(new Dimension(800,500))	;
		detailsScrollPane.setVisible(false);
		detailsButton=new JButton("Details>>");
		detailsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (detailsScrollPane.isVisible())
				{
					detailsScrollPane.setVisible(false);
					detailsButton.setText("Details>>");
					pack();
				}
				else
				{
					detailsScrollPane.scrollToBeginning();
					detailsScrollPane.setVisible(true);
					detailsButton.setText("Details<<");
					pack();
				}
			}
		}
		);
		topBox.add(messageScrollPane);
		topBox.add(Box.createVerticalStrut(10));
		topBox.add(detailsButton);
		getInsidePanel().setLayout(new BorderLayout());
		getInsidePanel().add(topBox, BorderLayout.NORTH);
		getInsidePanel().add(detailsScrollPane, BorderLayout.CENTER);		
		pack();
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}			
		});
		detailsButton.setNextFocusableComponent(closeButton);
		closeButton.setNextFocusableComponent(detailsButton);
	}

	private String throwableToString(Throwable exception)
	{
		if (exception==null)
			return "";
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		printWriter.flush();
		return stringWriter.toString();
	}
}

