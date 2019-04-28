# dumb-player
A small GStreamer based video player created using Java (Swing).

[![DumbPlayer](https://i.imgur.com/Q3B7efP.png)](https://github.com/fa7ad/dumb-player/)

## About the project

**Requirements**:

* Java/JDK (1.6/6+)
* Eclipse (comes with Maven built-in)
* *Maven*

**Runtime Dependencies**:

* Gstreamer (1.12*)  
  Any 1.10+ version will work on Linux. Windows needs [this specific version](https://gstreamer.freedesktop.org/data/pkg/windows/1.12.0/gstreamer-1.0-x86_64-1.12.0.msi). Other future releases may work this is the one I found working in my tests.

**Recommended Utilities**:  
* [WindowBuilder](https://www.eclipse.org/windowbuilder/download.php) for Eclipse from Eclipse Marketplace.

## Usage

* **Option 1**: Run pre-compiled JAR
  1. Install JRE
  2. Install Gstreamer
  3. **WINDOWS Only**: Add gstreamer to PATH
  4. Run it:
    * From CLI: `java -jar DumbPlayer-1.0.0-RC3.jar some-video.mp4`
    * Or just execute the JAR file and click the Open file button or Press <kbd>Ctrl</kbd>+<kbd>O</kbd>
* **Option 2**: Compile it yourself
  1. Follow instructions 1 through 3 from **Option 1**
  2. Clone this Repo
  3. Open the project in Eclipse
  4. Click Build and Run
  
## LICENSE
GNU General Public License 3.0
