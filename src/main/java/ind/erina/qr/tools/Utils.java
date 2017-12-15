package ind.erina.qr.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class Utils {
	public static int base32Encode(byte[] out, int outSize, int val, int inSize) {
		final byte b32[] = "0123456789ABCDEFGHIJKLMNOPQRSTU/".getBytes();
		int outByte = inSize * 8 / 5, i, n = 0;
		if (outSize < outByte) {
			return 0;
		}
		for (i = (outByte - 1); i >= 0; i--) {
			out[n++] = b32[(val >> (i * 5)) & 0x1F];
		}
		return outByte;
	}
	public static void writeQrFile(String qrStr) {
		String fileName = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "\\二维码批量.csv";
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(qrStr + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		byte[] out = new byte[4];
		int val = 2920;
		int n = base32Encode(out,4,val,2);
		System.out.println(ByteUtil.toHex(out));
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File com=fsv.getHomeDirectory();    //这便是读取桌面路径的方法了
		System.out.println(com.getPath());
	}
}
