package ind.erina.qr.tools;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

public class ByteUtil {
	private static String digits = "0123456789abcdef";

	/**
	 * 将byte数组变为可显示的字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String toHex(byte[] data) {

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i != data.length; i++) {
			int v = data[i] & 0xff;

			buf.append(digits.charAt(v >> 4));
			buf.append(digits.charAt(v & 0xf));
		}

		return buf.toString().toUpperCase();
	}

	/**
	 * 将byte数组变为可显示的字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2String(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length + b.length - 1);
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toHexString(b[i]));
			if (i < b.length - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	public static byte[] fromHex(String hex) {
		if (hex.length() % 2 > 0)
			hex = "0" + hex;
		ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
		for (int i = 0; i < hex.length(); i++) {
			String hexStr = hex.charAt(i) + "";
			i++;
			hexStr += hex.charAt(i);
			byte b = (byte) Integer.parseInt(hexStr, 16);
			// System.out.println(hexStr+"="+b);
			bf.put(b);
		}
		return bf.array();
	}

	public static long crc32(String src, String checksum) {
		CRC32 crc = new CRC32();
		crc.update(src.getBytes());
		Long checksum2 = crc.getValue();
		BigInteger bi = new BigInteger(ByteUtil.fromHex(checksum));
		return bi.longValue() - checksum2;
	}

	public static String crc(String src) {
		CRC32 crc = new CRC32();
		crc.update(src.getBytes());
		Long checksum = crc.getValue();
		System.out.println("checksum:" + checksum);
		return Long.toHexString(checksum);
	}

	public static byte[] reverse(byte[] src) {
		ByteBuffer bb = ByteBuffer.allocate(src.length);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(src);
		bb.slice();
		return ByteUtil.fromHex(Long.toHexString(bb.getLong(0)));
	}
	public static byte[] littleEndian(byte[] src){
		ByteBuffer bb = ByteBuffer.allocate(src.length);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(src);
		bb.slice();
		return bb.array();
	}
	public static byte[] bigEndian(byte[] src){
		ByteBuffer bb = ByteBuffer.allocate(src.length);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.put(src);
		bb.slice();
		return bb.array();
	}
}
