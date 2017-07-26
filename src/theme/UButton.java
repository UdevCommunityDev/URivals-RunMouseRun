package theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author programmer
 */
public class UButton extends JButton{

    public Color bg = Theme.DEFAULT;
    public Color hoverBg = null;
    public Color borderColor = Color.BLACK;
    public ArrayList<Component> validationComponents = new ArrayList<Component>();

    public UButton(String text) {
        super(text);

        this.setText(text);
        this.setBgColor(this.bg);
        this.setBorder(new CompoundBorder(BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(Theme.LABELED_MARGIN, 0, Theme.LABELED_MARGIN, 0)));

        this.setPreferredSize(new Dimension(
                Theme.BTN_DEFAULT_WIDTH, Theme.BTN_DEFAULT_HEIGHT)
        );
        this.setMinimumSize(new Dimension(
                Theme.BTN_DEFAULT_WIDTH, Theme.BTN_DEFAULT_HEIGHT)
        );
        this.setMaximumSize(new Dimension(
                120, Theme.BTN_DEFAULT_HEIGHT)
        );
        this.setForeground(Theme.BTN_DEFAULT_TEXT_COLOR);

        UButton self = this;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                self.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                self.setBackground(bg);
            }
        });
    }

    public void setBgColor(Color background) {
        this.bg = background;
        this.hoverBg = calcHoverBgColor(bg);
        this.borderColor = calcHoverBgColor(bg);
        setBackground(bg);
        setBorder(BorderFactory.createLineBorder(borderColor));
    }

    public Color calcHoverBgColor(Color color) {
        return new Color(color.getRed() - Theme.HOVER_PLUS,color.getGreen() - Theme.HOVER_PLUS,color.getBlue() - Theme.HOVER_PLUS);
    }

    public Color calcBorderColor(Color color) {
        return new Color(color.getRed() - Theme.BORDER_PLUS,color.getGreen() - Theme.BORDER_PLUS,color.getBlue() - Theme.BORDER_PLUS);
    }

    public void addValidation(Component component) {
        validationComponents.add(component);
    }

    public ArrayList<Component> getValidations() {
        return validationComponents;
    }

    public void clearValidations() {
        validationComponents.clear();
    }
}
