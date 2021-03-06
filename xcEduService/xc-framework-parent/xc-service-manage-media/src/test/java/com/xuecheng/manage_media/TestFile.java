package com.xuecheng.manage_media;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hewei
 * @date 2019/11/7 - 0:46
 */
public class TestFile {

    //测试文件分块
    @Test
    public void testChunk() throws IOException {
        //源文件
        File sourceFile = new File("E:\\liumeiticeshi\\lucene.avi");
        //块文件目录
        String chunkFileFolder = "E:\\liumeiticeshi\\chunks\\";
        //先定义块文件大小
        long chunkFileSize = 1 * 1024 * 1024;
        //块数
        long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
        //创建读文件对象
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkFileNum; i++) {
            //块文件
            File chunFile = new File(chunkFileFolder+i);
            //创建向块文件的写对象
            RandomAccessFile raf_write = new RandomAccessFile(chunFile, "rw");
            int len = 0;
            while ((len = raf_read.read(b)) != -1){
                raf_write.write(b,0,len);
                //如果块文件的大小达到1M，开始写下一块
                if(chunFile.length()>=chunkFileSize){
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();
    }

    //测试文件合并
    @Test
    public void testMergeFile() throws IOException{
        //块文件目录(提前建立好chunks目录)
        String chunkFileFolderPath = "E:\\liumeiticeshi\\chunks\\";
        //块文件目录对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        //将块文件排序，按名称升序
        Arrays.sort(files, (File o1, File o2)->{
            return Integer.parseInt(o1.getName())-Integer.parseInt(o2.getName());
        });
        //List<File> fileList = Arrays.asList(files);
        /*Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName())-Integer.parseInt(o2.getName());
            }
        });*/
        /*Collections.sort(fileList, (File o1, File o2)->{
            return Integer.parseInt(o1.getName())-Integer.parseInt(o2.getName());
        });*/
        //合并文件
        File mereFile = new File("E:\\liumeiticeshi\\lucene_merge.avi");
        //创建新文件
        mereFile.createNewFile();
        //创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mereFile, "rw");
        byte[] b = new byte[1024];
        for (File file : files) {
            //创建一个读快文件的对象
            RandomAccessFile raf_read = new RandomAccessFile(file, "r");
            int len = 0;
            while((len = raf_read.read(b)) != -1){
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();

    }

}
