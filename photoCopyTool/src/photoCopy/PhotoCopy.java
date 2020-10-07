package photoCopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class PhotoCopy {
	final static String SEPARATOR = "file.separator";
	final static String AIM_DIR = "temporaryFiles";
	static String OLD_PATH = "/Users/zhuyuan/myworks/temporaryFiles";
	static String NEW_PATH = "/Users/zhuyuan/myworks/t";
	static long totalFiles = 0;

	public static void test(String path) { // path为目录

		File file = new File(path);
		File[] files = file.listFiles(); // 遍历该目录所有文件和文件夹对象
		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				test(files[i].toString()); // 递归操作，逐一遍历各文件夹内的文件
			} else {
				if (!files[i].isDirectory())
					System.out.println(files[i]); // 只打印文件，不打印文件夹
			}
		}
	}

	public static void findAimPath(String path, boolean isInAimPath) { // path为目录
		File file = new File(path);
		if (file != null) {
			File[] files = file.listFiles(); // 遍历该目录所有文件和文件夹对象
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					boolean isInPath = isInAimPath;
					File f = files[i];
					if (f.isDirectory()) {
						if (AIM_DIR.equals(f.getName())) {
							isInPath = true;
						}

						findAimPath(f.toString(), isInPath); // 递归操作，逐一遍历各文件夹内的文件
					} else {
						if (!f.isDirectory() && isInAimPath) {
							System.out.println("file:" + f);
							totalFiles++;
						}
					}
				}
			}
		}
	}

	public static void copyFolder(String oldPath, String newPath) {

		try {
			boolean f = (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹

			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile() && !temp.getName().equals(".DS_Store")) {
					String fileNameStr = newPath + File.separator + (temp.getName()).toString();
					// System.out.println(fileNameStr);
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(fileNameStr);
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
					totalFiles++;
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("Copy photo wrong!" + e.getCause());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		OLD_PATH = scanner.nextLine();
		NEW_PATH = scanner.nextLine();
		Long t = System.currentTimeMillis();
		int loopCount = 2;
		for (int i = 0; i < loopCount; i++) {
			copyFolder(OLD_PATH, NEW_PATH);
		}

		System.out.println((System.currentTimeMillis() - t) / 1000 + "s");
		System.out.println("the total files:" + totalFiles);
		System.out.println((System.currentTimeMillis() - t) + "ms");
	}
}