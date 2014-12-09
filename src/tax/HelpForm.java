/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tax;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

/**
 *
 * @author ninja
 */
public class HelpForm extends javax.swing.JFrame {

    private SwingWorker<Void, Void> videoThread;
//    private SwingWorker<Void, Void> audioThread;
    private static SourceDataLine mLine;
    private long timeStamp;
    private boolean donePlaying = false;
    
    /**
     * Creates new form Help
     */
    public HelpForm() {
        initComponents();
        this.setResizable(false);
//        this.getContentPane().setBackground(Color.decode("0x1F45FC").darker());
        
        try {
            this.setIconImage((BufferedImage) ImageIO.read(Tax.class.getResource("icons/help.png")));
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        videoThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
             // Simulate doing something useful.
             playVideo();

             return null;
            }
            
            @Override
            protected void done() {
                donePlaying = true;
                screen.repaint();
                screen.setIcon(new ImageIcon(Tax.class.getResource("icons/img/no-signal.png")));
            }
        };
        
//        audioThread = new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() throws Exception {
//             // Simulate doing something useful.
//             playSound();
//
//             return null;
//            }
//        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        okBut = new javax.swing.JButton();
        backg = new javax.swing.JPanel();
        screen = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Βοήθεια");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        okBut.setText("ΟΚ");
        okBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButActionPerformed(evt);
            }
        });
        getContentPane().add(okBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 470, 40, -1));

        backg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        backg.setDoubleBuffered(false);
        backg.setEnabled(false);
        backg.setFocusable(false);
        backg.setRequestFocusEnabled(false);
        backg.setLayout(new java.awt.GridLayout(1, 0));

        screen.setOpaque(true);
        backg.add(screen);

        getContentPane().add(backg, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 722, 450));

        setSize(new java.awt.Dimension(748, 530));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void okButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButActionPerformed
        this.dispose();
        videoThread.cancel(true);
//        audioThread.cancel(true);
    }//GEN-LAST:event_okButActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        videoThread.execute();
//        audioThread.execute();
        timeStamp = System.currentTimeMillis();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
//        
    }//GEN-LAST:event_formWindowGainedFocus

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backg;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JButton okBut;
    private javax.swing.JLabel screen;
    // End of variables declaration//GEN-END:variables

    
    private void playVideo() {
//    if (args.length <= 0)
//      throw new IllegalArgumentException("must pass in a filename" +
//      		" as the first argument");

        String filename = "etc/help.mp4";

        // Let's make sure that we can actually convert video pixel formats.
        if (!IVideoResampler.isSupported(
            IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
          throw new RuntimeException("you must install the GPL version" +
                    " of Xuggler (with IVideoResampler support) for " +
                    "this demo to work");

        // Create a Xuggler container object
        IContainer container = IContainer.make();

        // Open up the container
        if (container.open(filename, IContainer.Type.READ, null) < 0)
          throw new IllegalArgumentException("could not open file: " + filename);

        // query how many streams the call to open found
        int numStreams = container.getNumStreams();

        // and iterate through the streams to find the first video stream
        int videoStreamId = -1;
        IStreamCoder videoCoder = null;
        for(int i = 0; i < numStreams; i++)
        {
          // Find the stream object
          IStream stream = container.getStream(i);
          // Get the pre-configured decoder that can decode this stream;
          IStreamCoder coder = stream.getStreamCoder();

          if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
          {
            videoStreamId = i;
            videoCoder = coder;
            break;
          }
        }
        if (videoStreamId == -1)
          throw new RuntimeException("could not find video stream in container: "
              +filename);

        /*
         * Now we have found the video stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        if (videoCoder.open() < 0)
          throw new RuntimeException("could not open video decoder for container: "
              +filename);

        IVideoResampler resampler = null;
        if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
        {
          // if this stream is not in BGR24, we're going to need to
          // convert it.  The VideoResampler does that for us.
          resampler = IVideoResampler.make(videoCoder.getWidth(), 
              videoCoder.getHeight(), IPixelFormat.Type.BGR24,
              videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
          if (resampler == null)
            throw new RuntimeException("could not create color space " +
                            "resampler for: " + filename);
        }

        /*
         * Now, we start walking through the container looking at each packet.
         */
        IPacket packet = IPacket.make();
        long firstTimestampInStream = Global.NO_PTS;
        long systemClockStartTime = 0;
        while(container.readNextPacket(packet) >= 0)
        {
          /*
           * Now we have a packet, let's see if it belongs to our video stream
           */
          if (packet.getStreamIndex() == videoStreamId)
          {
            /*
             * We allocate a new picture to get the data out of Xuggler
             */
            IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                videoCoder.getWidth(), videoCoder.getHeight());

            int offset = 0;
            while(offset < packet.getSize())
            {
              /*
               * Now, we decode the video, checking for any errors.
               * 
               */
              int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
              if (bytesDecoded < 0)
                throw new RuntimeException("got error decoding video in: "
                    + filename);
              offset += bytesDecoded;

              /*
               * Some decoders will consume data in a packet, but will not be able to construct
               * a full video picture yet.  Therefore you should always check if you
               * got a complete picture from the decoder
               */
              if (picture.isComplete())
              {
                IVideoPicture newPic = picture;
                /*
                 * If the resampler is not null, that means we didn't get the
                 * video in BGR24 format and
                 * need to convert it into BGR24 format.
                 */
                if (resampler != null)
                {
                  // we must resample
                  newPic = IVideoPicture.make(resampler.getOutputPixelFormat(),
                      picture.getWidth(), picture.getHeight());
                  if (resampler.resample(newPic, picture) < 0)
                    throw new RuntimeException("could not resample video from: "
                        + filename);
                }
                if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
                  throw new RuntimeException("could not decode video" +
                            " as BGR 24 bit data in: " + filename);

                /**
                 * We could just display the images as quickly as we decode them,
                 * but it turns out we can decode a lot faster than you think.
                 * 
                 * So instead, the following code does a poor-man's version of
                 * trying to match up the frame-rate requested for each
                 * IVideoPicture with the system clock time on your computer.
                 * 
                 * Remember that all Xuggler IAudioSamples and IVideoPicture objects
                 * always give timestamps in Microseconds, relative to the first
                 * decoded item. If instead you used the packet timestamps, they can
                 * be in different units depending on your IContainer, and IStream
                 * and things can get hairy quickly.
                 */
                if (firstTimestampInStream == Global.NO_PTS)
                {
                  // This is our first time through
                  firstTimestampInStream = picture.getTimeStamp();
                  // get the starting clock time so we can hold up frames
                  // until the right time.
                  systemClockStartTime = System.currentTimeMillis();
                } else {
                  long systemClockCurrentTime = System.currentTimeMillis();
                  long millisecondsClockTimeSinceStartofVideo =
                    systemClockCurrentTime - systemClockStartTime;
                  // compute how long for this frame since the first frame in the
                  // stream.
                  // remember that IVideoPicture and IAudioSamples timestamps are
                  // always in MICROSECONDS,
                  // so we divide by 1000 to get milliseconds.
                  long millisecondsStreamTimeSinceStartOfVideo =
                    (picture.getTimeStamp() - firstTimestampInStream)/1000;
                  final long millisecondsTolerance = 50; // and we give ourselfs 50 ms of tolerance
                  final long millisecondsToSleep = 
                    (millisecondsStreamTimeSinceStartOfVideo -
                      (millisecondsClockTimeSinceStartofVideo +
                          millisecondsTolerance));
                  if (millisecondsToSleep > 0)
                  {
                    try
                    {
                      Thread.sleep(millisecondsToSleep);
                    }
                    catch (InterruptedException e)
                    {
                      // we might get this when the user closes the dialog box, so
                      // just return from the method.
                      System.out.println("video thread killed");
                      return;
                    }
                  }
                }

                // And finally, convert the BGR24 to an Java buffered image
                final BufferedImage javaImage = Utils.videoPictureToImage(newPic);

                // and display it on the Java Swing window4
                screen.getGraphics().drawImage(javaImage, 0, 0, null);
    //            screen.repaint();

                if ((System.currentTimeMillis() - timeStamp) > 1000) {
                    timeStamp = System.currentTimeMillis();
                    System.gc();
    //                System.out.println("memory cleaned");
                }
              }
            }
          }
          else
          {
            /*
             * This packet isn't part of our video stream, so we just
             * silently drop it.
             */
            do {} while(false);
          }

        }
        /*
         * Technically since we're exiting anyway, these will be cleaned up by 
         * the garbage collector... but because we're nice people and want
         * to be invited places for Christmas, we're going to show how to clean up.
         */
        if (videoCoder != null)
        {
          videoCoder.close();
          videoCoder = null;
        }
        if (container !=null)
        {
          container.close();
          container = null;
        }
  }
    
    
    private void playSound() {
//    if (args.length <= 0)
//      throw new IllegalArgumentException("must pass in a filename as the first argument");
    
        String filename = "etc/help.mp4";

        // Create a Xuggler container object
        IContainer container = IContainer.make();

        // Open up the container
        if (container.open(filename, IContainer.Type.READ, null) < 0)
          throw new IllegalArgumentException("could not open file: " + filename);

        // query how many streams the call to open found
        int numStreams = container.getNumStreams();

        // and iterate through the streams to find the first audio stream
        int audioStreamId = -1;
        IStreamCoder audioCoder = null;
        for(int i = 0; i < numStreams; i++)
        {
          // Find the stream object
          IStream stream = container.getStream(i);
          // Get the pre-configured decoder that can decode this stream;
          IStreamCoder coder = stream.getStreamCoder();

          if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
          {
            audioStreamId = i;
            audioCoder = coder;
            break;
          }
        }
        if (audioStreamId == -1)
          throw new RuntimeException("could not find audio stream in container: "+filename);

        /*
         * Now we have found the audio stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        if (audioCoder.open() < 0)
          throw new RuntimeException("could not open audio decoder for container: "+filename);

        /*
         * And once we have that, we ask the Java Sound System to get itself ready.
         */
        openJavaSound(audioCoder);

        /*
         * Now, we start walking through the container looking at each packet.
         */
        IPacket packet = IPacket.make();
        while(container.readNextPacket(packet) >= 0)
        {

          if (Thread.interrupted()) {
              System.out.println("audio thread killed");
              return;
          }
          /*
           * Now we have a packet, let's see if it belongs to our audio stream
           */
          if (packet.getStreamIndex() == audioStreamId)
          {
            /*
             * We allocate a set of samples with the same number of channels as the
             * coder tells us is in this buffer.
             * 
             * We also pass in a buffer size (1024 in our example), although Xuggler
             * will probably allocate more space than just the 1024 (it's not important why).
             */
            IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());

            /*
             * A packet can actually contain multiple sets of samples (or frames of samples
             * in audio-decoding speak).  So, we may need to call decode audio multiple
             * times at different offsets in the packet's data.  We capture that here.
             */
            int offset = 0;

            /*
             * Keep going until we've processed all data
             */
            while(offset < packet.getSize())
            {
              int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
              if (bytesDecoded < 0)
                throw new RuntimeException("got error decoding audio in: " + filename);
              offset += bytesDecoded;
              /*
               * Some decoder will consume data in a packet, but will not be able to construct
               * a full set of samples yet.  Therefore you should always check if you
               * got a complete set of samples from the decoder
               */
              if (samples.isComplete())
              {
                playJavaSound(samples);
              }
            }
          }
          else
          {
            /*
             * This packet isn't part of our audio stream, so we just silently drop it.
             */
            do {} while(false);
          }

        }
        /*
         * Technically since we're exiting anyway, these will be cleaned up by 
         * the garbage collector... but because we're nice people and want
         * to be invited places for Christmas, we're going to show how to clean up.
         */
        closeJavaSound();

        if (audioCoder != null)
        {
          audioCoder.close();
          audioCoder = null;
        }
        if (container !=null)
        {
          container.close();
          container = null;
        }
  }
    
    private static void openJavaSound(IStreamCoder aAudioCoder) {
        AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(),
            (int)IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()),
            aAudioCoder.getChannels(),
            true, /* xuggler defaults to signed 16 bit samples */
            false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try
        {
          mLine = (SourceDataLine) AudioSystem.getLine(info);
          /**
           * if that succeeded, try opening the line.
           */
          mLine.open(audioFormat);
          /**
           * And if that succeed, start the line.
           */
          mLine.start();
        }
        catch (LineUnavailableException e)
        {
          throw new RuntimeException("could not open audio line");
        }
  }

  private static void playJavaSound(IAudioSamples aSamples) {
        /**
         * We're just going to dump all the samples into the line.
         */
        byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
        mLine.write(rawBytes, 0, aSamples.getSize());
  }

  private static void closeJavaSound() {
        if (mLine != null)
        {
          /*
           * Wait for the line to finish playing
           */
          mLine.drain();
          /*
           * Close the line.
           */
          mLine.close();
          mLine=null;
    }
  }
}