package xiaozhuo.info.service.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
//import org.apache.tomcat.jni.OS;

public class ApplyColor2picture {

	public static void main(String[] args) throws IOException {
		shuiYin();
	}

	/**
	 * 给图片打水印(旋转) 给图片写字
	 */
	public static void shuiYin() {

		String srcImgPath = "G:\\javaIconTestPicture\\111.jpg"; // 源图片
		String iconPath = "G:\\javaIconTestPicture\\bd_logo1.png"; // 水印图片
		String targerPath = "G:\\javaIconTestPicture\\test01.jpg"; // 目标路径1  加水印不旋转
		String targerPath2 = "G:\\javaIconTestPicture\\test02.jpg"; // 目标路径2 加水印旋转
		String targerPath3 = "G:\\javaIconTestPicture\\test03.jpg"; // 目标路径3 加文字

		// 给图片添加水印 
		ApplyColor2picture.markImageByIcon(iconPath, srcImgPath, targerPath);
		// 给图片添加水印,水印旋转-45 
		ApplyColor2picture.markImageByIcon(iconPath, srcImgPath, targerPath2, -45);

		printCert2Picture(srcImgPath, targerPath3, "杨子豪");
	}

	/**
	 * 重载方法  不带有图片旋转功能
	 * 
	 * @param iconPath
	 * @param srcImgPath
	 * @param targerPath
	 */
	private static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 *        * 给图片添加水印、可设置水印图片旋转角度        *
	 * 核心方法是Graphics2D的drawImage()和setComposite()      * 即绘图方法和设置上下文方法      *     
	 *  * @param iconPath 水印图片路径        * @param srcImgPath 源图片路径        * @param
	 * targerPath 目标图片路径        * @param degree 水印图片旋转角度        
	 */

	private static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {

		OutputStream os = null; // 输出流

		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

// 得到画笔对象   
// Graphics g= buffImg.getGraphics();   
			Graphics2D g = buffImg.createGraphics();

// 设置对线段的锯齿状边缘处理   
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);

			if (null != degree) {
// 设置水印旋转   
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
//rotate() 方法  第一个参数是将旋转角度转为弧度  
//第二个 第三个 参数是 以哪个点旋转 即旋转中心点相对于图片的坐标
			}
//缩小图片
			BufferedImage imgIconSuoXiao = smallerImge(iconPath, 3);
			ImageIO.write(imgIconSuoXiao, "JPG", new File(iconPath));

//放大图片
			BufferedImage imgIconFangDa = biggerImage(iconPath, 2);
			ImageIO.write(imgIconFangDa, "JPG", new File(iconPath));

// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
			ImageIcon imgIcon = new ImageIcon(iconPath);

// 得到Image对象。   
			Image img = imgIcon.getImage();
			float alpha = 0.5f; // 透明度   0.0是完全透明  1.0是完全不透明
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha)); // 画笔对象的上下文设置
			// 表示水印图片的位置   第四个参数为图像过滤器  BufferedImageOp类对象
			g.drawImage(img, 20, 20, null);
			g.dispose();
			// 释放该图形的上下文及所有资源
			os = new FileOutputStream(targerPath);

			// 生成图片   三个参数分别为原图片的像素缓冲池对象  图片格式  输出流
			ImageIO.write(buffImg, "JPG", os);
			System.out.println("图片完成添加Icon印章。。。。。。");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在图片上写字 核心方法是Graphics2D的drawString()和setFont()方法 即绘制文字和设置字体方法  
	 * 
	 * @param srcImgPath 源图片路径
	 * @param targerPath 目标图片路径
	 */
	public static void printCert2Picture(String srcImgPath, String targerPath, String wenzi) {

		OutputStream os = null;// 输出流
		try {
			// 获取画布
			BufferedImage image = ImageIO.read(new File(srcImgPath));

			// 获取画笔
			Graphics2D pen = image.createGraphics();
			// 设置对线段的锯齿状边缘处理   
			pen.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			pen.drawImage(image.getScaledInstance(image.getWidth(null), image.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			// 绘图环境及绘图工具配置
			pen.setColor(Color.RED);// 设置颜色
			pen.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));// 设置上下文 文字透明度
			pen.setFont(new Font("黑体", Font.BOLD, 25));// 设置字体
			System.out.println((wenzi.length()) / 2 + "......" + image.getWidth(null) / 2);
			pen.drawString(wenzi, image.getWidth(null) / 2, image.getHeight(null) / 2);// 开始绘图
			// 结束处理 收尾工作
			pen.dispose();
			// 通过流将图片输出到目标路径
			os = new FileOutputStream(targerPath);
			ImageIO.write(image, "JPG", os);
			// 输出
			System.out.println("给图片加文字成功");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对图片进行缩小
	 * 
	 * @param originalImage 原始图片
	 * @param times缩小倍数
	 * @return 缩小后的Image
	 */
	public static BufferedImage smallerImge(String srcImgPath, Integer times) {
		try {
			BufferedImage image = ImageIO.read(new File(srcImgPath));
			int width = image.getWidth() / times;
			int height = image.getHeight() / times;
			BufferedImage newImage = new BufferedImage(width, height, image.getType());
			Graphics g = newImage.getGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.dispose();
			System.out.println("图片缩小成功");
			return newImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对图片进行放大
	 * 
	 * @param originalImage 原始图片
	 * @param times         放大倍数
	 * @return 缩小后的Image
	 */
	public static BufferedImage biggerImage(String srcImgPath, Integer times) {
		try {
			BufferedImage image = ImageIO.read(new File(srcImgPath));
			int width = image.getWidth() * times;
			int height = image.getHeight() * times;
			BufferedImage newImage = new BufferedImage(width, height, image.getType());
			Graphics g = newImage.getGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.dispose();
			System.out.println("图片放大成功");
			return newImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
