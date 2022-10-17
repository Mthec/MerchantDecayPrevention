//
// Decompiled by Procyon v0.5.36
//

package com.wurmonline.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnection
{
    private static final Logger logger;
    private static final String CLASS_NAME;
    public static final int BUFFER_SIZE = 262136;
    private final ByteBuffer writeBufferTmp;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer_w;
    private ByteBuffer writeBuffer_r;
    public static final long timeOutTime = 300000L;
    public static final long disconTime = 5000L;
    private boolean connected;
    private boolean playerServerConnection;
    private SocketChannel socketChannel;
    private long lastRead;
    private SimpleConnectionListener connectionListener;
    private int toRead;
    private volatile boolean writing;
    private int bytesRead;
    private int totalBytesWritten;
    private int maxBlocksPerIteration;
    private boolean isLoggedIn;
    public int ticksToDisconnect;
    private Socket socket;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    public Random encryptRandom;
    private int remainingEncryptBytes;
    private int encryptByte;
    private int encryptAddByte;
    public Random decryptRandom;
    private int remainingDencryptBytes;
    private int dencryptByte;
    private int decryptAddByte;
    private static final ReentrantReadWriteLock RW_LOCK;
    private boolean callTickWritingFromTick;
    static long maxRead;
    static int maxTotalRead;
    static int maxTotalReadAllowed;
    static int maxReadAllowed;

    SocketConnection(final SocketChannel socketChannel, final boolean enableNagles, final boolean intraServer) throws IOException {
        this.writeBufferTmp = ByteBuffer.allocate(65534);
        this.readBuffer = ByteBuffer.allocate(262136);
        this.writeBuffer_w = null;
        this.writeBuffer_r = null;
        this.playerServerConnection = false;
        this.lastRead = System.currentTimeMillis();
        this.toRead = -1;
        this.maxBlocksPerIteration = 3;
        this.isLoggedIn = true;
        this.ticksToDisconnect = -1;
        this.encryptRandom = new Random(105773331L);
        this.remainingEncryptBytes = 0;
        this.encryptByte = 0;
        this.encryptAddByte = 0;
        this.decryptRandom = new Random(105773331L);
        this.remainingDencryptBytes = 0;
        this.dencryptByte = 0;
        this.decryptAddByte = 0;
        this.callTickWritingFromTick = true;
        (this.socketChannel = socketChannel).configureBlocking(false);
        this.socket = socketChannel.socket();
        this.playerServerConnection = !intraServer;
        if (this.playerServerConnection) {
            this.readBuffer = ByteBuffer.allocate(262136);
            this.writeBuffer_w = ByteBuffer.allocate(32767);
            this.writeBuffer_r = ByteBuffer.allocate(32767);
        }
        else {
            this.readBuffer = ByteBuffer.allocate(262136);
            this.writeBuffer_w = ByteBuffer.allocate(262136);
            this.writeBuffer_r = ByteBuffer.allocate(262136);
        }
        if (!enableNagles) {
            System.out.println("Disabling Nagles");
            socketChannel.socket().setTcpNoDelay(true);
        }
        if (SocketConnection.logger.isLoggable(Level.FINE)) {
            SocketConnection.logger.fine("SocketChannel validOps: " + socketChannel.validOps() + ", isConnected: " + socketChannel.isConnected() + ", isOpen: " + socketChannel.isOpen() + ", isRegistered: " + socketChannel.isRegistered() + ", socket: " + socketChannel.socket());
        }
        this.connected = true;
        this.readBuffer.clear();
        this.readBuffer.limit(2);
        this.writing = false;
        this.writeBuffer_w.clear();
        this.writeBuffer_r.flip();
        this.isLoggedIn = false;
    }

    protected SocketConnection(final String ip, final int port, final boolean enableNagles) throws UnknownHostException, IOException {
        this.writeBufferTmp = ByteBuffer.allocate(65534);
        this.readBuffer = ByteBuffer.allocate(262136);
        this.writeBuffer_w = null;
        this.writeBuffer_r = null;
        this.playerServerConnection = false;
        this.lastRead = System.currentTimeMillis();
        this.toRead = -1;
        this.maxBlocksPerIteration = 3;
        this.isLoggedIn = true;
        this.ticksToDisconnect = -1;
        this.encryptRandom = new Random(105773331L);
        this.remainingEncryptBytes = 0;
        this.encryptByte = 0;
        this.encryptAddByte = 0;
        this.decryptRandom = new Random(105773331L);
        this.remainingDencryptBytes = 0;
        this.dencryptByte = 0;
        this.decryptAddByte = 0;
        this.callTickWritingFromTick = true;
        this.readBuffer = ByteBuffer.allocate(262136);
        this.writeBuffer_w = ByteBuffer.allocate(262136);
        this.writeBuffer_r = ByteBuffer.allocate(262136);
        if (SocketConnection.logger.isLoggable(Level.FINER)) {
            SocketConnection.logger.entering(SocketConnection.CLASS_NAME, "SocketConnection", new Object[] { ip, port });
        }
//        (this.socketChannel = SocketChannel.open()).connect(new InetSocketAddress(ip, port));
        if (!enableNagles) {
            System.out.println("Disabling Nagles");
            this.socketChannel.socket().setTcpNoDelay(true);
        }
        if (SocketConnection.logger.isLoggable(Level.FINE)) {
            SocketConnection.logger.fine("SocketChannel validOps: " + this.socketChannel.validOps() + ", isConnected: " + this.socketChannel.isConnected() + ", isOpen: " + this.socketChannel.isOpen() + ", isRegistered: " + this.socketChannel.isRegistered() + ", socket: " + this.socketChannel.socket());
        }
        this.socketChannel.configureBlocking(false);
        this.connected = true;
        this.readBuffer.clear();
        this.readBuffer.limit(2);
        this.writing = false;
        this.writeBuffer_w.clear();
        this.writeBuffer_r.flip();
    }

    public SocketConnection(final String ip, final int port, final int timeout) throws UnknownHostException, IOException {
        this(ip, port, timeout, true);
    }

    SocketConnection(final String ip, final int port, final int timeout, final boolean enableNagles) throws UnknownHostException, IOException {
        this.writeBufferTmp = ByteBuffer.allocate(65534);
        this.readBuffer = ByteBuffer.allocate(262136);
        this.writeBuffer_w = null;
        this.writeBuffer_r = null;
        this.playerServerConnection = false;
        this.lastRead = System.currentTimeMillis();
        this.toRead = -1;
        this.maxBlocksPerIteration = 3;
        this.isLoggedIn = true;
        this.ticksToDisconnect = -1;
        this.encryptRandom = new Random(105773331L);
        this.remainingEncryptBytes = 0;
        this.encryptByte = 0;
        this.encryptAddByte = 0;
        this.decryptRandom = new Random(105773331L);
        this.remainingDencryptBytes = 0;
        this.dencryptByte = 0;
        this.decryptAddByte = 0;
        this.callTickWritingFromTick = true;
        this.readBuffer = ByteBuffer.allocate(262136);
        this.writeBuffer_w = ByteBuffer.allocate(262136);
        this.writeBuffer_r = ByteBuffer.allocate(262136);
        this.socketChannel = SocketChannel.open();
        this.socketChannel.socket().setSoTimeout(timeout);
//        this.socketChannel.connect(new InetSocketAddress(ip, port));
        if (!enableNagles) {
            System.out.println("Disabling Nagles");
            this.socketChannel.socket().setTcpNoDelay(true);
        }
        if (SocketConnection.logger.isLoggable(Level.FINE)) {
            SocketConnection.logger.fine("SocketChannel validOps: " + this.socketChannel.validOps() + ", isConnected: " + this.socketChannel.isConnected() + ", isOpen: " + this.socketChannel.isOpen() + ", isRegistered: " + this.socketChannel.isRegistered() + ", socket: " + this.socketChannel.socket());
        }
        this.socketChannel.configureBlocking(false);
        this.connected = true;
        this.readBuffer.clear();
        this.readBuffer.limit(2);
        this.writing = false;
        this.writeBuffer_w.clear();
        this.writeBuffer_r.flip();
    }

    public void setMaxBlocksPerIteration(final int aMaxBlocksPerIteration) {
        this.maxBlocksPerIteration = aMaxBlocksPerIteration;
    }

    public String getIp() {
        return "NOTHING";
    }

    public ByteBuffer getBuffer() {
        if (this.writing) {
            throw new IllegalStateException("getBuffer() called twice in a row. You probably forgot to flush()");
        }
        this.writing = true;
        this.writeBufferTmp.clear();
        return this.writeBufferTmp;
    }

    public void clearBuffer() {
        if (this.writing) {
            this.writing = false;
            this.writeBufferTmp.clear();
        }
    }

    public int getUnflushed() {
        return this.writeBuffer_w.position() + this.writeBuffer_r.remaining();
    }

    public void flush() throws IOException {
        this.writing = false;
    }

    public void setConnectionListener(final SimpleConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public boolean isConnected() {
        if (this.playerServerConnection) {
            if (this.isLoggedIn) {
                if (this.lastRead < System.currentTimeMillis() - 300000L) {
                    return false;
                }
            }
            else if (this.lastRead < System.currentTimeMillis() - 5000L) {
                return false;
            }
        }
        return this.connected;
    }

    public void setLogin(final boolean li) {
        if (!this.isLoggedIn && li && this.playerServerConnection) {
            this.writeBuffer_w = ByteBuffer.allocate(786408);
            this.writeBuffer_r = ByteBuffer.allocate(786408);
            this.writeBuffer_w.clear();
            this.writeBuffer_r.flip();
        }
        this.isLoggedIn = li;
    }

    public void disconnect() {
        if (SocketConnection.logger.isLoggable(Level.FINER)) {
            SocketConnection.logger.entering(SocketConnection.CLASS_NAME, "disconnect");
        }
        this.connected = false;
        try {
            if (this.in != null) {
                this.in.close();
            }
            this.in = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.out != null) {
                this.out.close();
            }
            this.out = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            this.socket = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.readBuffer.clear();
        this.writeBuffer_w.clear();
        this.writeBuffer_r.clear();
        this.isLoggedIn = false;
    }

    public void sendShutdown() {

    }

    public void closeChannel() {
        if (SocketConnection.logger.isLoggable(Level.FINER)) {
            SocketConnection.logger.entering(SocketConnection.CLASS_NAME, "closeChannel");
        }
        if (this.socketChannel != null && this.socketChannel.socket() != null) {
            try {
                this.socketChannel.socket().close();
            }
            catch (IOException iox) {
                iox.printStackTrace();
            }
        }
        if (this.socketChannel != null) {
            try {
                this.socketChannel.close();
            }
            catch (IOException iox) {
                iox.printStackTrace();
            }
        }
    }

    public void tick() throws IOException {

    }

    public boolean tickWriting(final long aNanosToWaitForLock) throws IOException {
        return true;
    }

    public void changeProtocol(final long newSeed) {
    }

    private void encrypt(final ByteBuffer bb, final int start, final int end) {
        final byte[] bytes = bb.array();
        for (int i = start; i < end; ++i) {
            if (--this.remainingEncryptBytes < 0) {
                this.remainingEncryptBytes = this.encryptRandom.nextInt(100) + 1;
                this.encryptByte = (byte)this.encryptRandom.nextInt(254);
                this.encryptAddByte = (byte)this.encryptRandom.nextInt(254);
            }
            bytes[i] -= (byte)this.encryptAddByte;
            final byte[] array = bytes;
            final int n = i;
            array[n] ^= (byte)this.encryptByte;
        }
    }

    private void decrypt(final ByteBuffer bb) {
        final byte[] bytes = bb.array();
        final int start = bb.position();
        for (int end = bb.limit(), i = start; i < end; ++i) {
            if (--this.remainingDencryptBytes < 0) {
                this.remainingDencryptBytes = this.decryptRandom.nextInt(100) + 1;
                this.dencryptByte = (byte)this.decryptRandom.nextInt(254);
                this.decryptAddByte = (byte)this.decryptRandom.nextInt(254);
            }
            final byte[] array = bytes;
            final int n = i;
            array[n] ^= (byte)this.dencryptByte;
            bytes[i] += (byte)this.decryptAddByte;
        }
    }

    public void setEncryptSeed(final long seed) {
        this.encryptRandom.setSeed(seed);
        this.remainingEncryptBytes = 0;
    }

    public void setDecryptSeed(final long seed) {
        this.decryptRandom.setSeed(seed);
        this.remainingDencryptBytes = 0;
    }

    public int getSentBytes() {
        return this.totalBytesWritten;
    }

    public int getReadBytes() {
        return this.bytesRead;
    }

    public void clearSentBytes() {
        this.totalBytesWritten = 0;
    }

    public void clearReadBytes() {
        this.bytesRead = 0;
    }

    public boolean isCallTickWritingFromTick() {
        return this.callTickWritingFromTick;
    }

    public void setCallTickWritingFromTick(final boolean newCallTickWritingFromTick) {
        this.callTickWritingFromTick = newCallTickWritingFromTick;
    }

    public boolean isWriting() {
        return this.writing;
    }

    @Override
    public String toString() {
        return "SocketConnection [IrcChannel: " + this.socketChannel + ']';
    }

    static {
        logger = Logger.getLogger(SocketConnection.class.getName());
        CLASS_NAME = SocketConnection.class.getName();
        RW_LOCK = new ReentrantReadWriteLock();
        SocketConnection.maxRead = 0L;
        SocketConnection.maxTotalRead = 0;
        SocketConnection.maxTotalReadAllowed = 20000;
        SocketConnection.maxReadAllowed = 20000;
    }
}
