package ind.erina.qr;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import ind.erina.qr.bo.QrBo;
import ind.erina.qr.tools.MatrixToImageWriter;

@SuppressWarnings("serial")
public class QrMain extends JFrame {
	public static void main(String[] args) {
		new QrMain();
	}
	
	JLabel lbImg;

	JLabel lbDeviceType = new JLabel("设备类型", 4);
	JTextField txDeviceType = new JTextField();

	JLabel lbCodeType = new JLabel("串码类型", 4);
	JComboBox<String> cbCodeType = new JComboBox<>(new String[] { "SN", "SE" });

	JLabel lbTypeId = new JLabel("类型标识码", 4);
	JTextField txTypeId = new JTextField();

	JLabel lbPtDate = new JLabel("生产日期", 4);
	JTextField txPtDate = new JTextField();

	JLabel lbNo = new JLabel("编号", 4);
	JTextField txStart = new JTextField();
	JTextField txEnd = new JTextField();

	JLabel lbSoleCode = new JLabel("SE唯一码", 4);
	JTextField txSoleCode = new JTextField();

	JTextField txQr;

	public QrMain() {
		super("VISION QR");
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setDefaultCloseOperation(3);
		this.setLayout(null);
		this.setSize(475, 250);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		int y = 10;
		lbImg = new JLabel("");
		lbImg.setBounds(260, y, 200, 200);
		this.add(lbImg);

		this.lbDeviceType.setBounds(10, y, 65, 20);
		this.add(lbDeviceType);
		LimitedDocument limitDocument = new LimitedDocument(8);
		limitDocument.setAllowChar("0123456789ABCDEFabcdef");
		this.txDeviceType.setDocument(limitDocument);
		this.txDeviceType.setBounds(80, y, 170, 20);
		this.add(txDeviceType);

		y += 23;

		this.lbCodeType.setBounds(10, y, 65, 20);
		this.add(lbCodeType);
		cbCodeType.setBounds(80, y, 170, 20);
		cbCodeType.addActionListener(e -> {
			System.out.println(this.cbCodeType.getSelectedItem().toString());
		});
		this.add(cbCodeType);

		y += 23;

		lbTypeId.setBounds(10, y, 65, 20);
		this.add(lbTypeId);
		limitDocument = new LimitedDocument(4);
		limitDocument.setAllowChar("0123456789ABCDEFabcdef");
		txTypeId.setDocument(limitDocument);
		txTypeId.setBounds(80, y, 170, 20);
		add(txTypeId);

		y += 23;

		lbPtDate.setBounds(10, y, 65, 20);
		this.add(lbPtDate);
		limitDocument = new LimitedDocument(8);
		limitDocument.setAllowChar("0123456789");
		txPtDate.setDocument(limitDocument);
		txPtDate.setBounds(80, y, 170, 20);
		add(txPtDate);

		y += 23;
		lbNo.setBounds(10, y, 65, 20);
		this.add(lbNo);
		limitDocument = new LimitedDocument(5);
		limitDocument.setAllowChar("0123456789");
		txStart.setDocument(limitDocument);
		txStart.setBounds(80, y, 80, 20);
		txStart.setToolTipText("起始编号或单个编号");
		add(txStart);
		limitDocument = new LimitedDocument(5);
		limitDocument.setAllowChar("0123456789");
		txEnd.setDocument(limitDocument);
		txEnd.setBounds(165, y, 80, 20);
		txEnd.setToolTipText("结束编号");
		add(txEnd);

		y += 23;
		lbSoleCode.setBounds(10, y, 65, 20);
		this.add(lbSoleCode);
		limitDocument = new LimitedDocument();
		limitDocument.setAllowChar("0123456789ABCDEFabcdef");
		txSoleCode.setDocument(limitDocument);
		txSoleCode.setBounds(80, y, 170, 20);
		add(txSoleCode);
		
		y += 23;
		JLabel lbQr = new JLabel("QR",4);
		lbQr.setBounds(10, y, 65, 20);
		add(lbQr);
		txQr = new JTextField();
		txQr.setBounds(80, y, 170, 20);
		this.add(txQr);
		
		y += 42;

		JButton batchBtn = new JButton("批量生成");
		batchBtn.setBounds(10, y, 115, 20);
		batchBtn.addActionListener(e -> {
			QrBo o = new QrBo();
			o.deviceType = this.txDeviceType.getText();
			o.codeType = this.cbCodeType.getSelectedItem().toString();
			o.typeIdentifier = this.txTypeId.getText();
			o.ptDate = this.txPtDate.getText();
			o.start = StringUtils.isBlank(this.txStart.getText()) ? 0 : Integer.parseInt(this.txStart.getText());
			o.end = StringUtils.isBlank(this.txEnd.getText()) ? 0 : Integer.parseInt(this.txEnd.getText());
			if(o.start>o.end) {
				JOptionPane.showMessageDialog(null, "请调整编号范围");
				txEnd.requestFocus();
				return;
			}
			o.soleCode = this.txSoleCode.getText();
			try {
				o.batchGen();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		batchBtn.setToolTipText("在桌面找csv");
		add(batchBtn);

		JButton singleBtn = new JButton("生成单个");
		singleBtn.setBounds(135, y, 115, 20);
		singleBtn.addActionListener(e -> {
			QrBo o = new QrBo();
			o.deviceType = this.txDeviceType.getText();
			o.codeType = this.cbCodeType.getSelectedItem().toString();
			o.typeIdentifier = this.txTypeId.getText();
			o.ptDate = this.txPtDate.getText();
			o.start = StringUtils.isBlank(this.txStart.getText()) ? 0 : Integer.parseInt(this.txStart.getText());
			o.soleCode = this.txSoleCode.getText();
			try {
				String ctn = o.gen();
				this.txQr.setText(ctn);
				
				Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
				sysClip.setContents(new StringSelection(ctn), null);
				
				int width = 300;
				int height = 300;
				String format = "png";
				Hashtable hints = new Hashtable();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				BitMatrix bitMatrix = new MultiFormatWriter().encode(ctn, BarcodeFormat.QR_CODE, width, height, hints);
				
				if (lbImg != null) {
					BufferedImage image = new BufferedImage(width, height, 1);
					for (int x = 0; x < width; ++x) {
						for (int j = 0; j < height; ++j) {
							image.setRGB(x, j, (bitMatrix.get(x, j)) ? -16777216 : -1);
						}
					}
					ImageIcon icon = new ImageIcon(image);
					icon.setImage(icon.getImage().getScaledInstance(200, 200, 1));
					lbImg.setIcon(icon);
				}
				File outputFile = new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath()
						+ File.separator + o.deviceType+o.ptDate+o.start+ ".png");
				MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});
		singleBtn.setToolTipText("在桌面找png；文本已复制到剪贴板");
		add(singleBtn);
		setVisible(true);
	}
}

class LimitedDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int maxLength = -1;
	private String allowCharAsString = null;

	public LimitedDocument() {
	}

	public LimitedDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	public void insertString(int offset, String str, AttributeSet attrSet) throws BadLocationException {
		if (str == null) {
			return;
		}
		if ((this.allowCharAsString != null) && (str.length() == 1) && (this.allowCharAsString.indexOf(str) == -1)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}

		char[] charVal = str.toCharArray();
		String strOldValue = getText(0, getLength());
		char[] tmp = strOldValue.toCharArray();
		if ((this.maxLength != -1) && (tmp.length + charVal.length > this.maxLength)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		super.insertString(offset, str.toUpperCase(), attrSet);
	}

	public void setAllowChar(String str) {
		this.allowCharAsString = str;
	}
}