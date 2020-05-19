package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEazyExcel {
    public static void main(String[] args) {
        //1.设置写入文件夹地址和excel名称
       /* String filename="D:\\write.xlsx";
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());*/

       //实现Excel的读操作
        String filename="D:\\write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    //创建方法返回List集合
    private static List<DemoData> getData(){
        List<DemoData> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data=new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
