import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class RoundlIcon implements Icon {
	Color color;

	public RoundlIcon(Color c) {
		color = c;
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color);
		g.fillOval(x, y, getIconWidth(), getIconHeight());
	}

}
