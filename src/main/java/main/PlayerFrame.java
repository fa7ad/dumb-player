package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import lib.SimpleVideoComponent;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.elements.PlayBin;

import com.sun.jna.Platform;

public class PlayerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlayBin playbin;
	private final JFileChooser fileChooser = new JFileChooser();

	public static void initialize(boolean windows) throws Exception {
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		System.setProperty("swing.aatext", "true");
		if (windows) {
			System.setProperty("gstreamer.GstNative.nameFormats", "%s-1.0-0|%s-1.0|%s-0|%s|lib%s|lib%s-0");
		}
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Gst.init();
		try {
			initialize(Platform.isWindows());
			PlayerFrame frame = new PlayerFrame();
			frame.setVisible(true);
			if (args.length > 0) {
				frame.openFile(args[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public PlayerFrame() {
		setTitle("DumbPlayer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 530);

		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.DARK_GRAY);

		setContentPane(contentPane);

		final SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		final String[] audioExts = new String[] { "flac", "aac", "ogg", "mp3", "m4a", "wav" };
		final String[] videoExts = new String[] { "asf", "avi", "3gp", "mp4", "mov", "flv", "mpg", "ts", "mkv", "webm",
				"mxf", "ogg" };
		final ArrayList<String> extensions = new ArrayList<>();
		extensions.addAll(Arrays.asList(audioExts));
		extensions.addAll(Arrays.asList(videoExts));

		final String[] avExts = new String[extensions.size()];
		extensions.toArray(avExts);

		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Video File", videoExts));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Audio File", audioExts));
		fileChooser.setFileFilter(new FileNameExtensionFilter("Audio/Video File", avExts));

		JLabel openFileButton = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.EAST, openFileButton, -8, SpringLayout.EAST, contentPane);
		openFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
		sl_contentPane.putConstraint(SpringLayout.EAST, openFileButton, -4, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, openFileButton, -8, SpringLayout.SOUTH, contentPane);
		contentPane.add(openFileButton);

		final JLabel playPauseButton = new JLabel("");
		sl_contentPane.putConstraint(SpringLayout.WEST, playPauseButton, 4, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, playPauseButton, -6, SpringLayout.SOUTH, contentPane);
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

		final JSlider positionSlider = new JSlider(0, 1000);
		sl_contentPane.putConstraint(SpringLayout.EAST, positionSlider, -6, SpringLayout.WEST, openFileButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, positionSlider, 6, SpringLayout.EAST, playPauseButton);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, positionSlider, -6, SpringLayout.SOUTH, contentPane);
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
		contentPane.add(positionSlider);

		new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playbin == null || positionSlider == null)
					return;
				if (!positionSlider.getValueIsAdjusting() && playbin.isPlaying()) {
					long dur = playbin.queryDuration(TimeUnit.NANOSECONDS);
					long pos = playbin.queryPosition(TimeUnit.NANOSECONDS);
					if (dur > 0) {
						double relPos = (double) pos / dur;
						positionSlider.setValue((int) (relPos * 1000));
					}
					if (dur == pos && dur > 0) {
						playbin.seek(0);
						playbin.stop();
					}
				}
			}
		}).start();

		SimpleVideoComponent videoOutput = new SimpleVideoComponent();
		sl_contentPane.putConstraint(SpringLayout.NORTH, videoOutput, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, videoOutput, -36, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, videoOutput, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, videoOutput, 0, SpringLayout.EAST, contentPane);
		contentPane.add(videoOutput);

		playbin = new PlayBin("GstDumbPlayer");
		playbin.setVideoSink(videoOutput.getElement());

	}

	public void openFile(String file) {
		File f = new File(file);
		playbin.stop();
		playbin.setURI(f.toURI());
		playbin.play();
	}
}
