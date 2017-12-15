package ind.erina.qr.bo;

import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang.time.DateUtils;

import ind.erina.qr.tools.ByteUtil;
import ind.erina.qr.tools.Utils;

public class QrBo {
	private StringBuilder finalString;
	public String deviceType;
	public String codeType;
	public String typeIdentifier;
	public String soleCode;
	public String ptDate;
	public int start;
	public int end;

	public QrBo() {
	}

	public String gen() throws Exception {
		this.finalString = new StringBuilder();
		this.finalString.append("\"");
		this.finalString.append(deviceType);
		this.finalString.append(codeType);
		switch (codeType) {
		case "SN":
			this.finalString.append("0C");
			byte[] ti = ByteUtil.fromHex(this.typeIdentifier);
			int val = ti[1];
			val |= ti[0] << 8;
			byte[] out = new byte[3];
			Utils.base32Encode(out, 3, val, 2);
			// System.out.println(ByteUtil.toHex(out));
			this.finalString.append(ByteUtil.toHex(out));
			long d1 = DateUtils.parseDate("20100101", new String[] { "yyyyMMdd" }).getTime();
			long d2 = DateUtils.parseDate(this.ptDate, new String[] { "yyyyMMdd" }).getTime();
			long dif = (d2 - d1) / (1000 * 3600 * 24);
			out = new byte[4];
			// System.out.println((int) dif);
			Utils.base32Encode(out, 4, (int) dif, 2);
			// System.out.println(ByteUtil.toHex(out));
			this.finalString.append(ByteUtil.toHex(out));
			this.finalString.append(String.format("%010X", this.start));
			break;
		case "SE":
			this.finalString.append(String.format("%02X", soleCode.length()));
			this.finalString.append(soleCode);
			break;
		}
		this.finalString.append("\"");
		return this.finalString.toString().toUpperCase();
	}

	public void batchGen() throws Exception {
		if(start>end) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		String fileName = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + File.separator
				+ "二维码批量.csv";
		FileWriter writer = new FileWriter(fileName, true);
		for (int i = start; i <= end; i++) {
			sb.append(String.format("%010X", i)).append(",").append(gen()).append(System.getProperty("line.separator"));
			if (i % 1000 == 0) {
				writer.write(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		writer.write(sb.toString());
		writer.close();
	}

	public static void main(String[] args) throws Exception {
		QrBo bo = new QrBo();
		bo.deviceType = "6D01010E";
		bo.codeType = "SN";
		bo.typeIdentifier = "0E01";
		bo.ptDate = "20171212";
		bo.start = 30;
		String qr = bo.gen();
		System.out.println(qr);
//		bo.batchGen();
	}
}
