package net.caiban.util;

// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 2003-6-23 11:15:30
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ZipUtil.java


import java.io.*;
import java.util.zip.*;

public class ZipUtil
{

    public static void unzip(String sourcefiles[], String baseDir)
        throws IOException
    {
        if(sourcefiles == null)
            return;
        ZipInputStream zis = null;
        FileInputStream fis = null;
        CheckedInputStream cis = null;
        byte buf[] = new byte[1024];
        FileOutputStream out = null;
        try
        {
            File base = new File(baseDir);
            for(int i = 0; i < sourcefiles.length; i++)
            {
                fis = new FileInputStream(sourcefiles[i]);
                cis = new CheckedInputStream(fis, new Adler32());
                zis = new ZipInputStream(cis);
                for(ZipEntry entry = null; (entry = zis.getNextEntry()) != null;)
                {
                    File f = new File(base, entry.getName());
                    if(entry.isDirectory())
                    {
                        if(f.mkdirs())
                            throw new IllegalArgumentException("Can't make dir:" + f);
                    } else
                    {
                        if(!f.getParentFile().exists())
                            f.getParentFile().mkdirs();
                        out = new FileOutputStream(f);
                        for(int n = 0; (n = zis.read(buf)) != -1;)
                            out.write(buf, 0, n);

                        out.close();
                    }
                }

            }

        }
        finally
        {
            try
            {
                if(out != null)
                    out.close();
            }
            catch(Exception exception1) { }
            try
            {
                if(zis != null)
                    zis.close();
                if(cis != null)
                    cis.close();
                fis.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private static void increasedZip(ZipOutputStream zos, byte buf[], String baseDir, String f)
        throws IOException
    {
        File file = new File(baseDir + "/" + f);
        if(!file.exists())
            return;
        if(file.isDirectory())
        {
            String children[] = file.list();
            if(children != null)
            {
                for(int i = 0; i < children.length; i++)
                    increasedZip(zos, buf, baseDir, f + "/" + children[i]);

            }
        } else
        {
            ZipEntry zipentry = new ZipEntry(f);
            zos.putNextEntry(zipentry);
            FileInputStream fis = new FileInputStream(file);
            int j;
            while((j = fis.read(buf)) > -1) 
                zos.write(buf, 0, j);
            fis.close();
            zos.flush();
        }
    }

    public static void zip(String outFileName, String baseDir, String sourcefiles[])
    {
        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        CheckedOutputStream cos = null;
        byte buf[] = new byte[1024];
        try
        {
            fos = new FileOutputStream(outFileName);
            cos = new CheckedOutputStream(fos, new Adler32());
            zos = new ZipOutputStream(cos);
            for(int i = 0; i < sourcefiles.length; i++)
                increasedZip(zos, buf, baseDir, sourcefiles[i]);

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(zos != null)
                    zos.close();
                if(cos != null)
                    cos.close();
                fos.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public ZipUtil()
    {
    }
}