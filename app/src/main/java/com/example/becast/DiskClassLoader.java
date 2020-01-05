package com.example.becast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DiskClassLoader extends ClassLoader {
    private String path;
    DiskClassLoader(String path){
        this.path=path;
    }

    @Override
    protected Class<?> findClass(String name)throws ClassNotFoundException{
        Class clazz;
        byte[] classData=loaderClassData(name);
        if(classData==null){
            throw new ClassNotFoundException();
        }
        else{
            clazz=defineClass(name,classData,0,classData.length);
        }
        return clazz;
    }

    private byte[] loaderClassData(String name){
        System.out.print(name);
        String fileName=getFileName(name);
        System.out.print(fileName);
        File file=new File(path,fileName);
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try{
            in=new FileInputStream(file);
            out=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=0;
            while((length=in.read(buffer))!=-1){
                out.write(buffer,0,length);
            }
            return out.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(in!=null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                if(out!=null){
                    out.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getFileName(String name){
        int index=name.lastIndexOf('.');
        if(index==-1){
            return name+".class";
        }
        else{
            return name.substring(index+1)+".class";
        }
    }
}
