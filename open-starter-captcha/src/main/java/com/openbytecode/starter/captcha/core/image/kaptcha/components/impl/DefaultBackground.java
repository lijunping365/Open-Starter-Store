package com.openbytecode.starter.captcha.core.image.kaptcha.components.impl;

import com.openbytecode.starter.captcha.core.image.kaptcha.components.BackgroundProducer;
import com.openbytecode.starter.captcha.properties.CaptchaProperties;
import com.openbytecode.starter.captcha.properties.ImageCodeProperties;
import com.openbytecode.starter.captcha.utils.KaptchaConfigHelper;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Default implementation of {@link BackgroundProducer}, adds a gradient
 * background to an image. The gradient color is diagonal and made of Color From
 * (top left) and Color To (bottom right).
 *
 * Copyright https://github.com/penggle/kaptcha
 */
public class DefaultBackground implements BackgroundProducer {

	private final CaptchaProperties captchaProperties;

	public DefaultBackground(CaptchaProperties captchaProperties) {
		this.captchaProperties = captchaProperties;
	}

	/**
	 * @param baseImage the base image
	 * @return an image with a gradient background added to the base image.
	 */
	public BufferedImage addBackground(BufferedImage baseImage) {
		final ImageCodeProperties imageProps = captchaProperties.getImage();
		Color colorFrom = KaptchaConfigHelper.getColor(imageProps.getBackgroundClearFrom(), Color.LIGHT_GRAY);
		Color colorTo = KaptchaConfigHelper.getColor(imageProps.getBackgroundClearTo(), Color.WHITE);

		int width = baseImage.getWidth();
		int height = baseImage.getHeight();

		// create an opaque image
		BufferedImage imageWithBackground = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graph = (Graphics2D) imageWithBackground.getGraphics();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		hints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY));
		hints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));

		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));

		graph.setRenderingHints(hints);

		GradientPaint paint = new GradientPaint(0, 0, colorFrom, width, height,
				colorTo);
		graph.setPaint(paint);
		graph.fill(new Rectangle2D.Double(0, 0, width, height));

		// draw the transparent image over the background
		graph.drawImage(baseImage, 0, 0, null);

		return imageWithBackground;
	}
}
