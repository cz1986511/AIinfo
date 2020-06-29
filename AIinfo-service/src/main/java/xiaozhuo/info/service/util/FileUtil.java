package xiaozhuo.info.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Chen
 */

public class FileUtil {
	
	private static final int BUFFER_SIZE = 65536;

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		File file = new File("d:\\result.zip");
		if (!file.exists()) {
			System.out.println("file not exist");
		}
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				System.out.println("解压" + entry.getName());
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = "d:\\" + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					File targetFile = new File("d:\\" + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if (!targetFile.getParentFile().exists()) {
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();
					// 将压缩文件内容写入到这个文件中
					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len;
					byte[] buf = new byte[BUFFER_SIZE];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流顺序，先打开的后关闭
					fos.close();
					is.close();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("解压完成,耗时:" + (end - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
