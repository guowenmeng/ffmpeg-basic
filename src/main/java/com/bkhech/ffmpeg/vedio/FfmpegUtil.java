package com.bkhech.ffmpeg.vedio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FfmpegUtil {

    /**
     * 转化为h264
     * @param ffmpegPath
     * @param inputPath
     * @param outputPath
     * @return
     * @throws Exception
     */
    public static Boolean ffmpeg(String ffmpegPath, String inputPath, String outputPath) throws Exception{
        if (!checkfile(inputPath)) {
            throw new Exception("不是文件");
        }

        if (checkContentType(inputPath)) {
            List<String> command = getFfmpegCommand(ffmpegPath, inputPath, outputPath);
            if (null != command && command.size() > 0) {
                return process(command, inputPath);
            }
        }
        return false;
    }

    /**
     * 检查文件的合法性
     * @param path
     * @return
     */
    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * 检查视频的格式
     * @param inputPath 源文件
     *                  ffmpeg能解析的格式：（mpg，wmv，3gp，mp4，mov，avi，flv，asx，asf等）
     * @return
     */
    private static boolean checkContentType(String inputPath) throws Exception {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1).toLowerCase();
        if (type.equals("avi")) {
            return true;
        } else if (type.equals("mpg")) {
            return true;
        } else if (type.equals("wmv")) {
            return true;
        } else if (type.equals("3gp")) {
            return true;
        } else if (type.equals("mov")) {
            return true;
        } else if (type.equals("mp4")) {
            return true;
        } else if (type.equals("asx")){
            return true;
        } else if (type.equals("asf")) {
            return true;
        } else if (type.equals("flv")) {
            return true;
        }
        throw new Exception("文件格式不支持");
    }

    /**
     * 解析
     * @param command
     * @param inputPath
     * @throws Exception
     */
    private static boolean process(List<String> command, String inputPath) throws Exception{
        Process videoProcess = null;
        try {
            if (null == command || command.size() == 0) {
                return false;
            }
            videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
            new PrintStream(videoProcess.getErrorStream()).start();
            new PrintStream(videoProcess.getInputStream()).start();
            int exitcode = videoProcess.waitFor();
            if (exitcode == 0) {
                //删除源文件
                new File(inputPath).delete();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("convert failed",e);
        } finally {
            if (videoProcess != null) {
                videoProcess.destroy();
            }
        }

    }

    /**
     * 根据文件类型设置ffmpeg命令
     * @param oldfilepath
     * @param outputPath
     * @return
     */
    private static List<String> getFfmpegCommand(String ffmpegPath, String oldfilepath, String outputPath) {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath + "\\ffmpeg");
        command.add("-i");
        command.add(oldfilepath);
        //解决 Too many packets buffered for output stream 0:1
        command.add("-max_muxing_queue_size");
        command.add("1024");
        command.add("-c:v");
        command.add("libx264");
        //height not divisible by 2 (240x135)
        //指定宽度，高度 等比缩放，-2 表示缩放为偶数
//        command.add("-vf");
//        command.add("scale=340:-2");

        command.add("-c:a");
        command.add("copy");
        command.add(outputPath);
        return command;
    }

}
