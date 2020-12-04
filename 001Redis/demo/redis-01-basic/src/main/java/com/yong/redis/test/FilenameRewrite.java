package com.yong.redis.test;

import java.io.File;

public class FilenameRewrite {
    public static void main(String[] args) {
        String dir = "/data/softwares/BaiduYunDownload/慕课网课程/一站式学习Redis  从入门到高可用分布式实践";
        String replace = "【瑞客论坛 www.ruike1.com】";
        rewrite(new File(dir), replace);
    }

    private static void rewrite(File dir, String replace) {
        File[] files = dir.listFiles();
        if(files == null) {
            return;
        }
        for (File file : files) {
            if(file.isDirectory()) {
                rewrite(file, replace);
            }
            String oldName = file.getName();
            String newName = oldName.replace(replace, "");
            
            boolean b = file.renameTo(new File(file.getParent() + "/" + newName));
            System.out.println(oldName + " -> " + newName + "  result: " + b);
        }
    }
}
