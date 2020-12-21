public class BufferIO {
    public static int writeBufferByte(byte[] buffer, int index, byte value) {
        buffer[index++] = (byte) (value & 0xff);
        return index;
    }

    public static int writeBufferBoolean(byte[] buffer, int index, boolean value) {
        if (value)
            buffer[index++] = 1;
        else
            buffer[index++] = 0;
        return index;
    }

    public static int writeBufferShort(byte[] buffer, int index, short value) {
        buffer[index++] = (byte) (value & 0xff);
        buffer[index++] = (byte) (value >> 8 & 0xff);
        return index;
    }

    public static int writeBufferInt(byte[] buffer, int index, int value) {
        buffer[index++] = (byte) (value & 0xff);
        buffer[index++] = (byte) (value >> 8 & 0xff);
        buffer[index++] = (byte) (value >> 16 & 0xff);
        buffer[index++] = (byte) (value >> 24 & 0xff);
        return index;
    }

    public static int writeBufferLong(byte[] buffer, int index, long value) {
        buffer[index++] = (byte) (value & 0xff);
        buffer[index++] = (byte) (value >> 8 & 0xff);
        buffer[index++] = (byte) (value >> 16 & 0xff);
        buffer[index++] = (byte) (value >> 24 & 0xff);
        buffer[index++] = (byte) (value >> 32 & 0xff);
        buffer[index++] = (byte) (value >> 40 & 0xff);
        buffer[index++] = (byte) (value >> 48 & 0xff);
        buffer[index++] = (byte) (value >> 56 & 0xff);
        return index;
    }

    public static int writeBufferChar(byte[] buffer, int index, char value) {
        buffer[index++] = (byte) (value & 0xff);
        // buffer[index++] = (byte) (value >> 8 & 0xff);
        return index;
    }

    public static int writeBufferFloat(byte[] buffer, int index, float value) {
        int ivalue = Float.floatToRawIntBits(value);
        buffer[index++] = (byte) (ivalue & 0xff);
        buffer[index++] = (byte) (ivalue >> 8 & 0xff);
        buffer[index++] = (byte) (ivalue >> 16 & 0xff);
        buffer[index++] = (byte) (ivalue >> 24 & 0xff);
        return index;
    }

    public static int writeBufferDouble(byte[] buffer, int index, double value) {
        long lvalue = Double.doubleToRawLongBits(value);
        buffer[index++] = (byte) (lvalue & 0xff);
        buffer[index++] = (byte) (lvalue >> 8 & 0xff);
        buffer[index++] = (byte) (lvalue >> 16 & 0xff);
        buffer[index++] = (byte) (lvalue >> 24 & 0xff);
        buffer[index++] = (byte) (lvalue >> 32 & 0xff);
        buffer[index++] = (byte) (lvalue >> 40 & 0xff);
        buffer[index++] = (byte) (lvalue >> 48 & 0xff);
        buffer[index++] = (byte) (lvalue >> 56 & 0xff);
        return index;
    }

    public static int writeBufferString(byte[] buffer, int index, String value) {
        for (int i = 0; i < value.length(); i++) {
            index = writeBufferChar(buffer, index, value.charAt(i));
        }
        index = writeBufferChar(buffer, index, '\0');
        return index;
    }

    public static byte readBufferByte(byte[] buffer, int index) {
        return buffer[index];
    }

    public static boolean readBufferBoolean(byte[] buffer, int index) {
        return buffer[index] == 1;
    }

    public static short readBufferShort(byte[] buffer, int index) {
        short ret = 0;
        ret |= (short) buffer[index];
        ret |= ((short) buffer[index + 1]) << 8;
        return ret;
    }

    public static int readBufferInt(byte[] buffer, int index) {
        int ret = 0;
        ret |= (int) buffer[index];
        ret |= ((int) buffer[index + 1]) << 8;
        ret |= ((int) buffer[index + 2]) << 16;
        ret |= ((int) buffer[index + 3]) << 24;
        return ret;
    }

    public static long readBufferLong(byte[] buffer, int index) {
        long ret = 0;
        ret |= (long) buffer[index];
        ret |= ((long) buffer[index + 1]) << 8;
        ret |= ((long) buffer[index + 2]) << 16;
        ret |= ((long) buffer[index + 3]) << 24;
        ret |= ((long) buffer[index + 4]) << 32;
        ret |= ((long) buffer[index + 5]) << 40;
        ret |= ((long) buffer[index + 6]) << 48;
        ret |= ((long) buffer[index + 7]) << 56;
        return ret;
    }

    public static char readBufferChar(byte[] buffer, int index) {
        char ret = 0;
        ret = (char) buffer[index];
        return ret;
    }

    public static float readBufferFloat(byte[] buffer, int index) {
        int ivalue = readBufferInt(buffer, index);
        float value = Float.intBitsToFloat(ivalue);
        return value;
    }

    public static double readBufferDouble(byte[] buffer, int index) {
        long ivalue = readBufferInt(buffer, index);
        double value = Double.longBitsToDouble(ivalue);
        return value;
    }

    public static String readBufferString(byte[] buffer, int index) {
        String ret = new String();
        for (int i = 0; buffer[index + i] != '\0'; i++) {
            ret += (char) buffer[index + i];
        }
        return ret;
    }
}
