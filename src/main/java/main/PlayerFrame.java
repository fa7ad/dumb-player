package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.MatteBorder;

public class PlayerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean maximized = false;
	private boolean playing = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
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
		});

	}

	/**
	 * Create the frame.
	 */
	public PlayerFrame() {
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
				System.out.println(maximized);
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

		JLabel PlayPauseButton = new JLabel("");
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
		sl_contentPane.putConstraint(SpringLayout.SOUTH, PlayPauseButton, -4, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, PlayPauseButton, 4, SpringLayout.WEST, contentPane);
		contentPane.add(PlayPauseButton);

		JSlider PositionSlider = new JSlider(0, 1000);
		PositionSlider.setBorder(null);
		PositionSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (PositionSlider.getValueIsAdjusting()) {
					//long duration = Player.queryDuration(TimeUnit.NANOSECONDS);
					double position = PositionSlider.getValue() / 1000.0;
					//Player.seek((long) (position * duration), TimeUnit.NANOSECONDS);
				}
			}
		});
		PositionSlider.setValue(0);
		PositionSlider.setBackground(Color.DARK_GRAY);
		sl_contentPane.putConstraint(SpringLayout.WEST, PositionSlider, 10, SpringLayout.EAST, PlayPauseButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, PositionSlider, -2, SpringLayout.SOUTH, PlayPauseButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, PositionSlider, -10, SpringLayout.EAST, contentPane);
		contentPane.add(PositionSlider);

	}
}
