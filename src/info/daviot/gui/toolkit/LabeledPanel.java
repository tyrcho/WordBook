package info.daviot.gui.toolkit;


public class LabeledPanel extends Panel {
	private String title;
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;    

	public LabeledPanel(String title) {
		super();
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title=title;
		toolkit.initLabeledPanel(this);
	}
}

