package com.bkhech.ffmpeg.vedio;

public class ConvertVedio {

    public static void convertVedio(String ffmpegPath, String inputPath, String outputPath) throws Exception {
        String outputFilePath = getoutputPathFile(inputPath, outputPath);
        System.out.println("outputFilePath = " + outputFilePath);
        FfmpegUtil.ffmpeg(ffmpegPath, inputPath, outputFilePath);
    }

    /**
     * 获取输出文件名
     * @param inputPath
     * @param outputPath
     * @return
     */
    private static String getoutputPathFile(String inputPath, String outputPath) {
        return outputPath + inputPath.substring(inputPath.lastIndexOf("\\"));
    }
}
