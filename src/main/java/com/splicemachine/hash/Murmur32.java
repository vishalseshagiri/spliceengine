package com.splicemachine.hash;

import java.nio.ByteBuffer;

/**
 * @author Scott Fines
 * Created on: 11/2/13
 */
public class Murmur32 implements Hash32{
    private static final int c1 = 0xcc9e2d51;
    private static final int c2 = 0x1b873593;

    private final int seed;

    Murmur32(int seed) {
        this.seed = seed;
    }

    @Override
    public int hash(String elem) {
        assert elem!=null: "Cannot hash a null element!";
        int h = seed;
        int length = elem.length();
        int pos = 0;
        int visited =0;
        char[] chars = elem.toCharArray();
        while(length-pos>=4){
            /*
             * Since a char has two bytes, we create one int by packing together two chars
             */
            int k1 = littleEndianInt(chars,pos);
            h = mutate(h,k1);
            pos+=2;
            visited+=4;
        }
        h = updatePartial(chars,length,pos,h,visited);

        return finalize(h);
    }

    @Override
    public int hash(byte[] bytes, int offset, int length) {
        int pos = offset;
        int visited=0;
        int h = seed;
        while(length-pos>=4){
            int k1 = littleEndianInt(bytes, pos);
            h =mutate(h,k1);
            pos+=4;
            visited+=4;
        }
        h = updatePartial(bytes, length, pos,h,visited);
        return finalize(h);
    }

    @Override
    public int hash(long item) {
        long littleEndian = Long.reverseBytes(item);
        int h = seed;
        int k1 = (int)littleEndian;
        h = mutate(h,k1);
        k1 = (int)(littleEndian>>>32);
        h = mutate(h,k1);

        h ^= 8;
        return finalize(h);
    }

    @Override
    public int hash(int element) {
        int h = seed;
        h = mutate(h,element)^8;
        return finalize(h);
    }

    @Override
    public int hash(short element) {
        return hash((int)element);
    }

    @Override
    public int hash(ByteBuffer bytes) {
        int length = bytes.remaining();
        int h = seed;
        byte[] block = new byte[4];
        int bytesVisited=0;
        while(bytes.remaining()>=4){
            bytes.get(block);
            int k1 = littleEndianInt(block, 0);
            h =mutate(h, k1);
            bytesVisited+=4;
        }
        bytes.get(block,0,length-bytesVisited);
        h = updatePartial(block,length,0,h,bytesVisited);
        return finalize(h);
    }

    private static int littleEndianInt(CharSequence bytes, int offset) {
        char b0 = bytes.charAt(offset);
        char b1 = bytes.charAt(offset+1);
        char b2 = bytes.charAt(offset+2);
        char b3 = bytes.charAt(offset+3);
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }


    private static int littleEndianInt(char[] bytes, int offset) {
        char b0 = bytes[offset];
        char b1 = bytes[offset+1];
        char b2 = bytes[offset+2];
        char b3 = bytes[offset+3];
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }
    private static int littleEndianInt(byte[] bytes, int offset) {
        byte b0 = bytes[offset];
        byte b1 = bytes[offset+1];
        byte b2 = bytes[offset+2];
        byte b3 = bytes[offset+3];
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }

    private int updatePartial(CharSequence bytes, int length, int pos, int h,int bytesVisited) {
        int k1 = 0;
        switch(length-bytesVisited){
            case 3:
                k1 ^= (bytes.charAt(pos+2) & 0xFF)<<16;
                bytesVisited++;
            case 2:
                k1 ^= ((bytes.charAt(pos+1) & 0xFF) <<8);
                bytesVisited++;
            case 1:
                k1 ^= bytes.charAt(pos) & 0xFF;
                bytesVisited++;
            default:
                k1 *= c1;
                k1 = Integer.rotateLeft(k1,15);
                k1 *= c2;
                h ^=k1;
        }

        h ^= bytesVisited;
        return h;
    }

 private int updatePartial(char[] bytes, int length, int pos, int h,int bytesVisited) {
        int k1 = 0;
        switch(length-bytesVisited){
            case 3:
                k1 ^= (bytes[pos+2] & 0xFF)<<16;
                bytesVisited++;
            case 2:
                k1 ^= ((bytes[pos+1] & 0xFF) <<8);
                bytesVisited++;
            case 1:
                k1 ^= bytes[pos] & 0xFF;
                bytesVisited++;
            default:
                k1 *= c1;
                k1 = Integer.rotateLeft(k1,15);
                k1 *= c2;
                h ^=k1;
        }

        h ^= bytesVisited;
        return h;
    }

    private int updatePartial(byte[] bytes, int length, int pos, int h,int bytesVisited) {
        int k1 = 0;
        switch(length-bytesVisited){
            case 3:
                k1 ^= (bytes[pos+2] & 0xFF)<<16;
                bytesVisited++;
            case 2:
                k1 ^= ((bytes[pos+1] & 0xFF) <<8);
                bytesVisited++;
            case 1:
                k1 ^= bytes[pos] & 0xFF;
                bytesVisited++;
            default:
                k1 *= c1;
                k1 = Integer.rotateLeft(k1,15);
                k1 *= c2;
                h ^=k1;
        }

        h ^= bytesVisited;
        return h;
    }

    private int finalize(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

    private int mutate(int h1, int k1) {
        k1 *=c1;
        k1 = Integer.rotateLeft(k1,15);
        k1 *=c2;

        h1^=k1;
        h1 = Integer.rotateLeft(h1,13);
        h1 = h1 *5 + 0xe6546b64;
        return h1;
    }

    public static void main(String...args) throws Exception{
        for(int i=0;i<100;i++){
            ByteBuffer bb = ByteBuffer.allocate(8);
            bb.asLongBuffer().put(i);
            byte[] bytes = bb.array();
            int byteVersion = new Murmur32(0).hash(bytes,0,bytes.length);
            int bbVersion = new Murmur32(0).hash(bb);
            int longVersion = new Murmur32(0).hash(i);
            if(byteVersion!=bbVersion)
                System.out.printf("Disagreement among bytes! Byte: %d, Buffer:%d, long:%d%n",
                        byteVersion,bbVersion,longVersion);
            else if(longVersion!=byteVersion)
                System.out.printf("Disagreement with long! Byte: %d, Buffer:%d, long:%d%n",
                        byteVersion,bbVersion,longVersion);
        }
    }


}
