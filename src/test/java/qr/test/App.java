package qr.test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import ind.erina.qr.QrMain;
import ind.erina.qr.tools.MatrixToImageWriter;

public class App {

	public static void main(String[] args)throws Exception {
		QrMain q = new QrMain();
	}
	public static void main2(String[] args)throws Exception {
		String s= "\"6D01010ESN0C33473132514D0000030\"";
		makeQR(s,"c:/Users/Juliana/","QRQRQR");
		
	}

	public static void makeQR(String s, String filePath, String mac) throws WriterException, IOException {
		int width = 500;
		int height = 500;
		String format = "png";

		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

		BitMatrix bitMatrix = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, width, height, hints);

		File outputFile = new File(filePath + File.separator + mac + ".png");
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
	}
}
