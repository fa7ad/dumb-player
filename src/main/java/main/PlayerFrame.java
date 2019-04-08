package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import lib.SimpleVideoComponent;
import lib.ComponentMover;
import lib.ComponentResizer;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.elements.PlayBin;

import com.sun.jna.Platform;

public class PlayerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean maximized = false;
	private PlayBin playbin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		System.setProperty("swing.aatext", "true");
		if (Platform.isWindows()) {
			System.setProperty("gstreamer.GstNative.nameFormats", "%s-1.0-0|%s-1.0|%s-0|%s|lib%s|lib%s-0");
		}
		Gst.init();

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
		setTitle("DumbPlayer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(640, 530);

		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		final JLabel closeButton = new JLabel("");
		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(new ImageIcon(getClass().getResource("/close-active.png")));
			}

			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(new ImageIcon(getClass().getResource("/close.png")));
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, closeButton, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, closeButton, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, closeButton, 16, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, closeButton, 16, SpringLayout.WEST, contentPane);
		closeButton.setToolTipText("Close");
		closeButton.setIcon(new ImageIcon(getClass().getResource("/close.png")));
		closeButton.setVerticalAlignment(SwingConstants.TOP);
		closeButton.setHorizontalAlignment(SwingConstants.LEFT);
		closeButton.setBackground(Color.DARK_GRAY);
		closeButton.setSize(16, 16);
		contentPane.add(closeButton);

		final JLabel maximizeButton = new JLabel("");
		maximizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				maximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize-active.png")));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				setExtendedState(maximized ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
				maximized = !maximized;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				maximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize.png")));
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, maximizeButton, 0, SpringLayout.NORTH, closeButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, maximizeButton, 6, SpringLayout.EAST, closeButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, maximizeButton, 38, SpringLayout.WEST, contentPane);
		maximizeButton.setVerticalAlignment(SwingConstants.TOP);
		maximizeButton.setToolTipText("Maximize");
		maximizeButton.setIcon(new ImageIcon(getClass().getResource("/maximize.png")));
		maximizeButton.setHorizontalAlignment(SwingConstants.LEFT);
		maximizeButton.setBackground(Color.DARK_GRAY);
		contentPane.add(maximizeButton);

		JLabel openFileButton = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.EAST, openFileButton, 0, SpringLayout.EAST, contentPane);
		openFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(contentPane);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					playbin.stop();
					playbin.setURI(fileChooser.getSelectedFile().toURI());
					playbin.play();
				}
			}
		});

		openFileButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), "open");
		openFileButton.getActionMap().put("open", new AbstractAction("open") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(contentPane);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					playbin.stop();
					playbin.setURI(fileChooser.getSelectedFile().toURI());
					playbin.play();
				}
			}
		});

		openFileButton.setToolTipText("Open File (Ctrl + O)");
		openFileButton.setIcon(new ImageIcon(getClass().getResource("/open-file.png")));
		sl_contentPane.putConstraint(SpringLayout.EAST, openFileButton, 0, SpringLayout.EAST, contentPane);
		contentPane.add(openFileButton);

		JLabel windowTitle = new JLabel("Dumb Player");
		windowTitle.setForeground(Color.WHITE);
		windowTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sl_contentPane.putConstraint(SpringLayout.NORTH, windowTitle, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, windowTitle, 6, SpringLayout.EAST, maximizeButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, windowTitle, 16, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, windowTitle, -6, SpringLayout.WEST, openFileButton);
		contentPane.add(windowTitle);

		final JSlider positionSlider = new JSlider(0, 1000);
		positionSlider.setBorder(null);
		positionSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (positionSlider.getValueIsAdjusting()) {
					long duration = playbin.queryDuration(TimeUnit.NANOSECONDS);
					double position = positionSlider.getValue() / 1000.0;
					playbin.seek((long) (position * duration), TimeUnit.NANOSECONDS);
				}
			}
		});
		positionSlider.setValue(0);
		positionSlider.setBackground(Color.DARK_GRAY);
		sl_contentPane.putConstraint(SpringLayout.EAST, positionSlider, -10, SpringLayout.EAST, contentPane);
		contentPane.add(positionSlider);

		new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!positionSlider.getValueIsAdjusting() && playbin.isPlaying()) {
					long dur = playbin.queryDuration(TimeUnit.NANOSECONDS);
					long pos = playbin.queryPosition(TimeUnit.NANOSECONDS);
					if (dur > 0) {
						double relPos = (double) pos / dur;
						positionSlider.setValue((int) (relPos * 1000));
					}
				}
			}
		}).start();

		final JLabel playPauseButton = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.WEST, positionSlider, 10, SpringLayout.EAST, playPauseButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, positionSlider, -2, SpringLayout.SOUTH, playPauseButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, playPauseButton, 4, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, playPauseButton, -4, SpringLayout.SOUTH, contentPane);
		playPauseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean playing = playbin.isPlaying();

				if (playing) {
					playbin.pause();
				} else {
					playbin.play();
				}

				playPauseButton.setIcon(
						new ImageIcon(getClass().getResource("/play-pause" + (playing ? "-active" : "") + ".png")));
			}
		});

		playPauseButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
				"playpause");
		playPauseButton.getActionMap().put("playpause", new AbstractAction("playpause") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				boolean playing = playbin.isPlaying();

				if (playing) {
					playbin.pause();
				} else {
					playbin.play();
				}

				playPauseButton.setIcon(
						new ImageIcon(getClass().getResource("/play-pause" + (playing ? "-active" : "") + ".png")));

			}
		});

		playPauseButton.setVerticalAlignment(SwingConstants.BOTTOM);
		playPauseButton.setHorizontalAlignment(SwingConstants.LEFT);
		playPauseButton.setIcon(new ImageIcon(getClass().getResource("/play-pause.png")));
		playPauseButton.setToolTipText("Play/Pause (SPACE)");
		contentPane.add(playPauseButton);

		SimpleVideoComponent videoOutput = new SimpleVideoComponent();
		sl_contentPane.putConstraint(SpringLayout.NORTH, videoOutput, 6, SpringLayout.SOUTH, closeButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, videoOutput, 0, SpringLayout.WEST, closeButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, videoOutput, -6, SpringLayout.NORTH, positionSlider);
		sl_contentPane.putConstraint(SpringLayout.EAST, videoOutput, 0, SpringLayout.EAST, openFileButton);
		contentPane.add(videoOutput);

		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this);
		ComponentMover cm = new ComponentMover(this, windowTitle);
		cm.setAutoLayout(true);

		playbin = new PlayBin("GstDumbPlayer");
		playbin.setVideoSink(videoOutput.getElement());

	}
}
