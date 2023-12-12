//package com.fastshop.helper;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//
//import com.fastshop.helper.MatrixToImageWriter;
//import com.fastshop.model.MyOrderDetail;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//public class QRCodeGenerator {
//
//	public static byte[] generateQRCode(MyOrderDetail myOrderDetail) {
//		try {
//			// Create QR code content
//			String content = "Product Name: " + myOrderDetail.getTotalqty() + "\n"
//					+ myOrderDetail.getMyOrder().getUser().getEmail();
//
//			// Add other fields as needed
//
//			// Set up QR code hints
//			Map<EncodeHintType, Object> hints = new HashMap<>();
//			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//			// Create QR code matrix
//			QRCodeWriter qrCodeWriter = new QRCodeWriter();
//			BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);
//
//			// Create QR code image
//			BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//			// Convert image to byte array
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(qrCodeImage, "png", baos);
//			return baos.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}