package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class PlayerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean maximized = false;
	private boolean playing = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		System.setProperty("swing.aatext", "true");
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}


			PlayerFrame frame = new PlayerFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public PlayerFrame() {
		Object self = this;

		setResizable(false); // TODO: remove this
		setTitle("DumbPlayer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(640, 530);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel CloseButton = new JLabel("");
		CloseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				CloseButton.setIcon(new ImageIcon(getClass().getResource("/close-active.png")));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				CloseButton.setIcon(new ImageIcon(getClass().getResource("/close.png")));
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, CloseButton, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, CloseButton, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, CloseButton, 16, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, CloseButton, 16, SpringLayout.WEST, contentPane);
		CloseButton.setToolTipText("Close");
		CloseButton.setIcon(new ImageIcon(getClass().getResource("/close.png")));
		CloseButton.setVerticalAlignment(SwingConstants.TOP);
		CloseButton.setHorizontalAlignment(SwingConstants.LEFT);
		CloseButton.setBackground(Color.DARK_GRAY);
		CloseButton.setSize(16, 16);
		contentPane.add(CloseButton);

		JLabel MaximizeButton = new JLabel("");
		MaximizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				MaximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize-active.png")));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				setExtendedState(maximized ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
				maximized = !maximized;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				MaximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize.png")));
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, MaximizeButton, 0, SpringLayout.NORTH, CloseButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, MaximizeButton, 6, SpringLayout.EAST, CloseButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, MaximizeButton, 38, SpringLayout.WEST, contentPane);
		MaximizeButton.setVerticalAlignment(SwingConstants.TOP);
		MaximizeButton.setToolTipText("Maximize");
		MaximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize.png")));
		MaximizeButton.setHorizontalAlignment(SwingConstants.LEFT);
		MaximizeButton.setBackground(Color.DARK_GRAY);
		contentPane.add(MaximizeButton);

		JSlider PositionSlider = new JSlider(0, 1000);
		PositionSlider.setBorder(null);
		PositionSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (PositionSlider.getValueIsAdjusting()) {
					// long duration = Player.queryDuration(TimeUnit.NANOSECONDS);
					double position = PositionSlider.getValue() / 1000.0;
					// Player.seek((long) (position * duration), TimeUnit.NANOSECONDS);
				}
			}
		});

		JLabel WindowTitle = new JLabel("DumbPlayer");
		sl_contentPane.putConstraint(SpringLayout.NORTH, WindowTitle, 0, SpringLayout.NORTH, CloseButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, WindowTitle, 280, SpringLayout.WEST, contentPane);
		WindowTitle.setFont(UIManager.getFont("defaultFont"));
		WindowTitle.setBackground(Color.DARK_GRAY);
		WindowTitle.setForeground(Color.WHITE);
		WindowTitle.setVerticalAlignment(SwingConstants.TOP);
		WindowTitle.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(WindowTitle);

		JLabel PlayPauseButton = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.WEST, PositionSlider, 10, SpringLayout.EAST, PlayPauseButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, PositionSlider, -2, SpringLayout.SOUTH, PlayPauseButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, PlayPauseButton, 4, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, PlayPauseButton, -4, SpringLayout.SOUTH, contentPane);
		PlayPauseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				playing = !playing;
				PlayPauseButton.setIcon(
						new ImageIcon(getClass().getResource("/play-pause" + (playing ? "-active" : "") + ".png")));
			}
		});
		PlayPauseButton.setVerticalAlignment(SwingConstants.BOTTOM);
		PlayPauseButton.setHorizontalAlignment(SwingConstants.LEFT);
		PlayPauseButton.setIcon(new ImageIcon(getClass().getResource("/play-pause.png")));
		contentPane.add(PlayPauseButton);
		PositionSlider.setValue(0);
		PositionSlider.setBackground(Color.DARK_GRAY);
		sl_contentPane.putConstraint(SpringLayout.EAST, PositionSlider, -10, SpringLayout.EAST, contentPane);
		contentPane.add(PositionSlider);

		JLabel OpenFileButton = new JLabel("");
		OpenFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog((Component) self);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
//					playbin.stop();
//					playbin.setURI(fileChooser.getSelectedFile().toURI());
//					playbin.play();
				}
			}
		});
		OpenFileButton.setToolTipText("Open File");
		OpenFileButton.setIcon(new ImageIcon(getClass().getResource("/open-file.png")));
		sl_contentPane.putConstraint(SpringLayout.EAST, OpenFileButton, 0, SpringLayout.EAST, contentPane);
		contentPane.add(OpenFileButton);

	}
}
