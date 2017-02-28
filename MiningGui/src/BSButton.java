import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;

public class BSButton extends JButton {

	private static final long serialVersionUID = 5790478792329430419L;
	private Color hoverBackgroundColor;
	private Color pressedBackgroundColor;

	public BSButton() {
		this(null);
	}

	public BSButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed()) {
			g.setColor(pressedBackgroundColor);
		} else if (getModel().isRollover()) {
			g.setColor(hoverBackgroundColor);
		} else {
			g.setColor(getBackground());
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	@Override
	public void setContentAreaFilled(boolean b) {
	}

	public Color getHoverBackgroundColor() {
		return hoverBackgroundColor;
	}

	public void setHoverBackgroundColor(Color hoverBackgroundColor) {
		this.hoverBackgroundColor = hoverBackgroundColor;
	}

	public Color getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(Color pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}
	
}