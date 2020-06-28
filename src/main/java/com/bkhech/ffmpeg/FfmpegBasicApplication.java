package com.bkhech.ffmpeg;

import com.bkhech.ffmpeg.vedio.ConvertVedio;
import com.bkhech.ffmpeg.vedio.Range;
import com.bkhech.ffmpeg.vedio.RangeUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class FfmpegBasicApplication implements CommandLineRunner {

	final static int thread = 2 * Runtime.getRuntime().availableProcessors();
	final static ExecutorService executorService = Executors.newFixedThreadPool(thread);

	public static void main(String[] args) {
		SpringApplication.run(FfmpegBasicApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//目标源文件地址
		if (args.length != 1) {
			System.out.println("*************Error: input directory is null*************");
			return;
		}

		String inputDirectory = args[0];
		System.out.println("inputDirectory: " + inputDirectory);

		//输出目录
		File outDirectory = new File("output");
		System.out.println("outDirectory: " + outDirectory.getAbsolutePath());
		if (!outDirectory.exists()) {
			outDirectory.mkdirs();
		}

		//ffmpeg命令目录
		String ffmpegPath = new File("").getAbsolutePath();
		System.out.println("ffmpegPath: " + ffmpegPath);

		//所有文件
		List<String> allFiles = getAllFiles(inputDirectory);
		int size = allFiles.size();
		AtomicInteger totalCounts = new AtomicInteger(size);
		System.out.println("total video counts = " + totalCounts.get());

		System.out.println("~~~~~~~~~~~~~~~~~~~start~~~~~~~~~~~~~~~~~~");

		AtomicInteger errorCounts = new AtomicInteger();

		final CountDownLatch latch = new CountDownLatch(thread);

		List<Range> ranges = RangeUtil.createRangesByFixedThread(thread, size);

		for (Range range : ranges) {
			executorService.execute(() -> {
				for (int j = range.getFrom(); j < range.getTo(); j++) {
					String filePath = allFiles.get(j);
					try {
						ConvertVedio.convertVedio(ffmpegPath,filePath, outDirectory.getAbsolutePath());
						System.out.println("remaining video counts = " + totalCounts.decrementAndGet());
					} catch (Throwable e) {
						errorCounts.incrementAndGet();
						System.out.println("Failure!!! errorMsg:" + e.getMessage() +". " + filePath);
					}
					System.out.println("----------------------------------");
				}

				latch.countDown();
			});
		}


		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("totalCounts: " + allFiles.size());
		System.out.println("errorCounts: " + errorCounts.get());
		System.out.println("~~~~~~~~~~~~~~~~~~~end~~~~~~~~~~~~~~~~~~");
	}

	public static List<String> getAllFiles(String file) {
		List<String> files = new ArrayList<>();
		maybeLoop(new File(file), files);
		return files;
	}

	private static void maybeLoop(File file, List<String> files) {
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].toString());
			} else {
				maybeLoop(tempList[i], files);
			}
		}
	}


}
