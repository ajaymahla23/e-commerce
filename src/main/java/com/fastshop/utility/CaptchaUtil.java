package com.fastshop.utility;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;

public class CaptchaUtil {

	// Creating Captcha Object
	public static Captcha createCaptcha(Integer width, Integer height) {
		Color bg = new Color(255, 255, 0);
		Color bg2 = new Color(0, 255, 255);
		return new Captcha.Builder(width, height).addBackground(new GradiatedBackgroundProducer(bg, bg2))
				.addText(new DefaultTextProducer(), new DefaultWordRenderer()).addNoise(new CurvedLineNoiseProducer())
				.addBorder().build();
	}

	// Converting to binary String
	public static String encodeCaptcha(Captcha captcha) {
		String image = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "jpg", bos);
			byte[] byteArray = Base64.getEncoder().encode(bos.toByteArray());
			image = new String(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

}
